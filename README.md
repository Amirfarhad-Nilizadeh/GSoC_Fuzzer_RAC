# GSoC-2021 Project

Author: Amirfarhad Nilizadeh (<af.nilizadeh@knights.ucf.edu>)

Mentor: Corina Pasareanu (<corina.pasareanu@west.cmu.edu>) Yannic Noller (<yannic.noller@acm.org>) and Pavel Parízek (<parizek@d3s.mff.cuni.cz>).

Project Description: Security and Semantic bugs exist in software systems, and discovering them is time-consuming, complicated, and challenging. Several static and dynamic techniques are presented for discovering bugs. However, finding a bug is still a challenging topic. In this work, we investigate developing a prototype tool that uses the benefits of using the lightweight specification, fuzzing, and symbolic execution for discovering security and semantic bugs in an arbitrary Java program. This proposal aims to extend Badger, meaning both SPF and Kelinci, with the ability of handling both pre and postconditions using the runtime assertion checker of OpenJML with the lightweight specification.

Requirements:

Java 8 in a Linux system. Kelinci, Badger, OpenJML and SPF all work with Java 8.
