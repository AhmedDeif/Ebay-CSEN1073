#/bin/bash
rm -f bin/*.class
javac -cp "./libs/*.jar" -d bin src/**.java
