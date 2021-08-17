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
time_bound=12 #sec = 10min
step_size_eval=30

declare -a subjects=(
"AbsoluteDriver"
)

declare -a classpaths=(
"./bin-instr/" 
)

declare -a drivers=(
"FuzzDriver" 
)

declare -a fuzz_dist_targets=(
"Example2.calculate(II)I:761,Example2.calculate(II)I:769" 
)

declare -a num_dist_targets=(
"2" # "AddLoop"
)

# Check array sizes
if [[ ${#subjects[@]} != ${#classpaths[@]} ]]
then
  echo "[Error in script] the array sizes of subjects and classpaths do not match!. Abort!"
  exit 1
fi
if [[ ${#subjects[@]} != ${#fuzz_dist_targets[@]} ]]
then
  echo "[Error in script] the array sizes of subjects and fuzz_dist_targets do not match!. Abort!"
  exit 1
fi
if [[ ${#subjects[@]} != ${#num_dist_targets[@]} ]]
then
  echo "[Error in script] the array sizes of subjects and num_dist_targets do not match!. Abort!"
  exit 1
fi

run_counter=0
total_number_subjects=${#subjects[@]}
total_number_experiments=$(( $total_number_subjects * $(($number_of_runs * 3)))) #3 because of fuzzing+symexe+hybrid

if [ "$(uname)" == "Darwin" ]; then
  echo "set DYLD_LIBRARY_PATH to ../../../../tool/symbolicexecution/jpf-symbc-differential/lib"
  export DYLD_LIBRARY_PATH=/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  echo "set LD_LIBRARY_PATH to ../../../../tool/symbolicexecution/jpf-symbc-differential/lib"
  export LD_LIBRARY_PATH=/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib
else
  echo "OS not supported!"
  exit 1
fi

echo
echo "Run complete evaluation..."

#cd ../HyDiff

# Run just fuzzing
for (( i=0; i<=$(( $total_number_subjects - 1 )); i++ ))
do
  cd ./${subjects[i]}/fuzzing/
  for j in `seq 1 $number_of_runs`
  do
    run_counter=$(( $run_counter + 1 ))
    echo "[$run_counter/$total_number_experiments] Run fuzzing analysis for ${subjects[i]}, round $j .."

    mkdir ../fuzzer-out-$j/

    # Start Kelinci server
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci -dist-target ${fuzz_dist_targets[i]} ${drivers[i]} @@ > ../fuzzer-out-$j/server-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 nohup /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz -i in_dir -o ../fuzzer-out-$j -c regression -S afl -t 999999999 -r ${num_dist_targets[i]} /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface -t ${num_dist_targets[i]} @@ > ../fuzzer-out-$j/afl-log.txt &
    afl_pid=$!

    # Wait for timebound
    sleep $time_bound

    # Stop AFL and Kelinci server
    kill $afl_pid
    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10
  done
  cd ../../

  # Evaluate run
  #python3 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/experiments/scripts/evaluate_regression_fuzz.py ${subjects[i]}/fuzzer-out- $number_of_runs $time_bound $step_size_eval

done

# Run just symexe
for (( i=0; i<=$(( $total_number_subjects - 1 )); i++ ))
do
  cd ./${subjects[i]}/

  for j in `seq 1 $number_of_runs`
  do

    run_counter=$(( $run_counter + 1 ))
    echo "[$run_counter/$total_number_experiments] Run symexe analysis for ${subjects[i]}, round $j .."

    mkdir ./symexe-out-$j/

    cd ./symexe/

    # Start SPF
    nohup java -Xmx6144m -cp "/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-core/build/*" edu.cmu.sv.badger.app.BadgerRunner config_symexe $j > ../symexe-out-$j/spf-log.txt &
    spf_pid=$!

    # Wait for timebound
    sleep $time_bound

    # Stop SPF
    kill $spf_pid

    # Assess.
    cd ../fuzzing

    # Start Kelinci server
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci -dist-target ${fuzz_dist_targets[i]} ${drivers[i]} @@ > ../symexe-out-$j/server-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_IMPORT_FIRST=1 AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz-import -i in_dir -o ../symexe-out-$j -c regression -S afl -t 999999999 -r ${num_dist_targets[i]} /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface -t ${num_dist_targets[i]} @@ > ../symexe-out-$j/afl-log.txt

    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10

    cd ../
  done

  cd ../

  # Evaluate run
  #python3 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/experiments/scripts/evaluate_regression_symexe.py ${subjects[i]}/symexe-out- $number_of_runs $time_bound $step_size_eval

done

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
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci -dist-target ${fuzz_dist_targets[i]} ${drivers[i]} @@ > ../hydiff-out-$j/server-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 nohup /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz -i in_dir -o ../hydiff-out-$j -c regression -S afl -t 999999999 -r ${num_dist_targets[i]} /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface -t ${num_dist_targets[i]} @@ > ../hydiff-out-$j/afl-log.txt &
    afl_pid=$!

    cd ../symexe/

    # Start SPF
    nohup java -Xmx6144m -cp "/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-core/build/*" edu.cmu.sv.badger.app.BadgerRunner config_hybrid $j > ../hydiff-out-$j/spf-log.txt &
    spf_pid=$!

    # Wait for timebound
    sleep $time_bound

    # Stop SPF, AFL and Kelinci server
    kill $spf_pid
    kill $afl_pid
    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10


    ####
    # Assess both runs, i.e. combine them in one instance afl-spf.
    cd ../fuzzing

    # Start Kelinci server
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci -dist-target ${fuzz_dist_targets[i]} FuzzDriver @@ > ../hydiff-out-$j/server-afl+spf-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_IMPORT_FIRST=1 AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz-import -i in_dir -o ../hydiff-out-$j -c regression -S afl-spf -t 999999999 -r ${num_dist_targets[i]} /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface @@ > ../hydiff-out-$j/afl+spf-log.txt
    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10

    ####
    # Assess only spf run to determine later the times for the first odiff.

    # Start Kelinci server
    nohup java -cp ${classpaths[i]} edu.cmu.sv.kelinci.Kelinci -dist-target ${fuzz_dist_targets[i]} FuzzDriver @@ > ../hydiff-out-$j/server-spf-log.txt &
    server_pid=$!
    sleep 5 # Wait a little bit to ensure that server is started

    # Start modified AFL
    AFL_IMPORT_FIRST=1 AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz-import-spf -i in_dir -o ../hydiff-out-$j -c regression -S spf-replay -t 999999999 -r ${num_dist_targets[i]} /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface @@ > ../hydiff-out-$j/spf-replay-log.txt

    kill $server_pid

    # Wait a little bit to make sure that processes are killed
    sleep 10

    cd ../
  done

  cd ../

  # Evaluate run
  #python3 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/experiments/scripts/evaluate_regression_hydiff.py ${subjects[i]}/hydiff-out- $number_of_runs $time_bound $step_size_eval

done
