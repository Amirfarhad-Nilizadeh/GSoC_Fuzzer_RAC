## prepare_example.sh
#####################################
# chmod +x prepare_example.sh
# ./prepare_example.sh
#

trap "exit" INT

declare -a subjects=(
"AbsoluteDriver"
)

declare -a classpaths=(
"." # "example"
)

declare -a instr_skip_classes=(
"empty" # "example"
)

# Check array sizes
if [[ ${#subjects[@]} != ${#classpaths[@]} ]]
then
echo "[Error in script] the array sizes of subjects and classpaths do not match!. Abort!"
exit 1
fi
if [[ ${#subjects[@]} != ${#instr_skip_classes[@]} ]]
then
echo "[Error in script] the array sizes of subjects and instr_skip_classes do not match!. Abort!"
exit 1
fi

run_counter=0
total_number_subjects=${#subjects[@]}
echo

#cd ../AddLoop

for (( i=0; i<=$(( $total_number_subjects - 1 )); i++ ))
do
  run_counter=$(( $run_counter + 1 ))
  echo "[$run_counter/$total_number_subjects] Prepare ${subjects[i]}.."

  cd ./${subjects[i]}/fuzzing

  # Generate fuzzing bytecode
  rm -rf bin
  mkdir bin
  cd src
  javac -cp ${classpaths[i]}:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/instrumentor/build/libs/kelinci.jar *.java -d ../bin
  cd ..

  # Instrument fuzzing bytecode
  rm -rf bin-instr
  java -cp ${classpaths[i]}:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/instrumentor/build/libs/kelinci.jar edu.cmu.sv.kelinci.instrumentor.Instrumentor -mode REGRESSION -i bin -o bin-instr -skipmain -export-cfgdir cfg -skipclass ${instr_skip_classes[i]}

  cd ../symexe

  # Generate symexe bytecode
  rm -rf bin
  mkdir bin
  cd src
  javac -g -cp ${classpaths[i]}:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/build/* *.java -d ../bin
  cd ../../../
  echo

done

echo ""
