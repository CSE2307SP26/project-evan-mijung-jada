#!/bin/bash

# create bin folder if not exists
mkdir -p bin

# compile all Java files
javac -d bin src/main/*.java

# run the app
java -cp bin main.MainMenu