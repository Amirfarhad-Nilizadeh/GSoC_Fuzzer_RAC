cd fuzzing

AFL_SKIP_CPUFREQ=1 AFL_I_DONT_CARE_ABOUT_MISSING_CRASHES=1 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz -i in_dir -o ../hydiff-out -S afl -t 999999999 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface -port 7001 @@