export  LD_LIBRARY_PATH=/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib
cd symexe

java -Xmx6144m -cp "/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/badger-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/build/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-symbc-differential/lib/*:/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/symbolicexecution/jpf-core/build/*" edu.cmu.sv.badger.app.BadgerRunner config_hybrid > ../hydiff-out/spf-log.txt &
