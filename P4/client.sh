BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.user"

MAIN_CLASS="Player"

ADDR="localhost"

PORT=5491


cd $BIN

java -cp . $PACKAGE.$MAIN_CLASS $ADDR $PORT
