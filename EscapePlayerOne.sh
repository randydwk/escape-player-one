#!/bin/bash
echo 'Starting Game ...'
javac -cp .:lib.jar src/escapegame/*.java -d bin
java -cp bin escapegame.EscapeGame
