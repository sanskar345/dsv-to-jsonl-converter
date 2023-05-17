-- For creating jar file with dependencies refer to this

    https://stackoverflow.com/questions/1729054/including-dependencies-in-a-jar-with-maven/1729094#1729094

    Command To Create A Jar file with all the dependencies :-
    mvn assembly:assembly -DdescriptorId=jar-with-dependencies

    Note -
    The jar file reads the input file from the current directory and creates the output file in the cusrrent directory.

    The Jar file is present in the target folder with name - dsv-to-jsonl-1.0-SNAPSHOT-jar-with-dependencies

    To run it use command :- java -jar dsv-to-jsonl-1.0-SNAPSHOT-jar-with-dependencies.jar
