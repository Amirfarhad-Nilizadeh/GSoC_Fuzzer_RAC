Command line used to find this crash:

/home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/afl-differential/afl-fuzz -i in_dir -o ../hydiff-out -S afl -t 999999999 /home/amirfarhad/Desktop/ProgramAnalysis/Tools/hydiff/tool/fuzzing/kelinci-differential/fuzzerside/interface -port 7001 @@

If you can't reproduce a bug outside of afl-fuzz, be sure to set the same
memory limit. The limit used for this fuzzing session was 50.0 MB.

Need a tool to minimize test cases before investigating the crashes or sending
them to a vendor? Check out the afl-tmin that comes with the fuzzer!

Found any cool bugs in open-source tools using afl-fuzz? If yes, please drop
me a mail at <lcamtuf@coredump.cx> once the issues are fixed - I'd love to
add your finds to the gallery at:

  http://lcamtuf.coredump.cx/afl/

Thanks :-)
