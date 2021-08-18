## run_example.sh
# CAUTION
# Run this script within its folder. Otherwise the paths might be wrong!
#####################################
# chmod +x run_example.sh.sh
# ./run_example.sh.sh
#

trap "exit" INT

#####################

number_of_runs=1 #30
time_bound=120 #sec = 10min
step_size_eval=30

declare -a subjects=(
"AddLoop"
)

declare -a classpaths=(
"./bin-instr/" # "AddLoop"
)

declare -a drivers=(
"FuzzDriver" # "AddLoop"
)

# Check array sizes
if [[ ${#subjects[@]} != ${#classpaths[@]} ]]
then
  echo "[Error in script] the array sizes of subjects and classpaths do not match!. Abort!"
  exit 1
fi

run_counter=0
total_number_subjects=${#subjects[@]}
total_number_experiments=$(( $total_number_subjects * $(($number_of_runs * 1)))) #1 because of just hybrid

if [ "$(uname)" == "Darwin" ]; then
  echo "set DYLD_LIBRARY_PATH to /Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib"
  export DYLD_LIBRARY_PATH=/Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  echo "set LD_LIBRARY_PATH to /Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib"
  export LD_LIBRARY_PATH=/Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib
else
  echo "OS not supported!"
  exit 1
fi

echo
echo "Run complete evaluation..."

cd ../HyDiff

# Run just hybrid analysis
for (( i=0; i<=$(( $total_number_subjects - 1 )); i++ ))
do
  cd ./${subjects[i]}/

  for j in `seq 1 $number_of_runs`
  do
    run_counter=$(( $run_counter + 1 ))
    echo "[$run_counter/$total_number_experiments] Run hybrid analysis for ${subjects[i]}, round $j .."

    mkdir ./hydiff-out-$j/

    cd ./fuzzing/

    # Start Kelinci server
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci ${drivers[i]} @@ > ../hydiff-out-$j/server-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 nohup /Users/yannic/repositories/hydiff/tool/fuzzing/afl-differential/afl-fuzz -i in_dir -o ../hydiff-out-$j -S afl -t 999999999 /Users/yannic/repositories/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface @@ > ../hydiff-out-$j/afl-log.txt &
    afl_pid=$!

    cd ../symexe/

    # Start SPF
    nohup java -Xmx6144m -cp "/Users/yannic/repositories/hydiff/tool/symbolicexecution/badger-differential/build/*:/Users/yannic/repositories/hydiff/tool/symbolicexecution/badger-differential/lib/*:/Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/build/*:/Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib/*:/Users/yannic/repositories/hydiff/tool/symbolicexecution/jpf-core/build/*" edu.cmu.sv.badger.app.BadgerRunner config_hybrid $j > ../hydiff-out-$j/spf-log.txt &
    spf_pid=$!

    # Wait for timebound
    sleep $time_bound

    # Stop SPF, AFL and Kelinci server
    kill $spf_pid
    kill $afl_pid
    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10

  done
done
