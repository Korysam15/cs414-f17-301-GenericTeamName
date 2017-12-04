#!/bin/bash

# clear the console
clear 

# allow first argument to be the Address of the host
ADDR=$1

# check if ADDR is set if not set to localhost
[ -z $ADDR ] && ADDR="localhost" && echo "Using default host: $ADDR"

# allow second command line argumnet to be the address
PORT=$2

# check if port is set if not set to 5491
[ -z $PORT ] && PORT=5491 && echo "Using default port $PORT."

# allow the third command line argument to be the log level
LOG_LEVEL=$3
[ -z $LOG_LEVEL ] && LOG_LEVEL="OFF" && echo "Using default log level: $LOG_LEVEL"

# directory where the .class file may exists
BIN="bin"

# package the main class belongs too
PACKAGE="edu.colostate.cs.cs414.p5.main"

# main class
MAIN_CLASS="PlayerMain"

# the actual location of the class file
CLASS_FILE=$BIN/edu/colostate/cs/cs414/p5/main/$MAIN_CLASS.class

# build file
BUILD_FILE="dist-build.xml"
   
# directory where jars will exists
BUILD_DIR="dist"
   
# name of the jar file
JAR="Player.jar"

# runs the player directly from the .class file
runFromClassFile() {
   cd $BIN
   java -cp . $PACKAGE.$MAIN_CLASS $ADDR $PORT $LOG_LEVEL
}

# attempts to build the project (if not already built)
buildProject() {
   # check if already built
   if [ -f $BUILD_DIR/$JAR ]; then
      echo "Found jar file";
      return 0;
   fi

   # check if the build file exists
   if [ -f $BUILD_FILE ]; then
      echo "Found ant build file!"
      if ant -buildfile $BUILD_FILE &>/dev/null; then
         echo "BUILD SUCCESSFUL!"
         return 0
      else
         echo "Build failed."
         return 1
      fi
   else
      echo "Failed to find $BUILD_FILE."
      return 1
   fi
}

# runs the player from the jar file
runFromJarFile() {
   cd $BUILD_DIR
   java -jar $JAR $ADDR $PORT $LOG_LEVEL
}

# last resort to run the project
lastResort() {
  mkdir $BIN
  echo "Attempting build project from javac"
  if find . -name "*.java" | grep -v ".*Test.java" | xargs javac -d $(pwd)/$BIN -cp $(pwd)/lib/mysql-connector-java-5.1.44-bin.jar;
  then
      runFromClassFile
  else
      echo "Failed to build project..."
      echo "You can try to build the project by opening it in Eclipse."
  fi
}

# first try to run the player from the .class file under the bin directory
# check if the .class file exists
if [ -f $CLASS_FILE ]; then
   echo "Found main class!"
   runFromClassFile
# attempt to build the project from the ant file and run the jar
elif buildProject; then
   runFromJarFile
# attempt sketchy javac operation to build the project
else
   lastResort
fi
