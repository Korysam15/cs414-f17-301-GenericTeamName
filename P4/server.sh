BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.main"

MAIN_CLASS="ServerMain"

PORT=5491

PASSWORD_FILE="passwords.txt"


cd $BIN

java -cp . $PACKAGE.$MAIN_CLASS $PORT $PASSWORD_FILE
