BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.main"

MAIN_CLASS="PlayerMain"

ADDR="76.120.120.62"

PORT=54350


cd $BIN
clear
java -cp . $PACKAGE.$MAIN_CLASS $ADDR $PORT
