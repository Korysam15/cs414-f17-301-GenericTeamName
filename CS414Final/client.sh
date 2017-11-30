BIN="bin/"

LOG_LEVEL=$1

PACKAGE="edu.colostate.cs.cs414.p5.main"

MAIN_CLASS="PlayerMain"

ADDR="localhost"

PORT=5491

cd $BIN
clear
java -cp . $PACKAGE.$MAIN_CLASS $ADDR $PORT $LOG_LEVEL
