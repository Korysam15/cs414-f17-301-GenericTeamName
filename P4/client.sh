BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.main"

MAIN_CLASS="PlayerMain"

ADDR="localhost"

PORT=5491


cd $BIN

java -cp . $PACKAGE.$MAIN_CLASS $ADDR $PORT
