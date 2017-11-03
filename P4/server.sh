BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.client_server.server"

MAIN_CLASS="Server"

PORT=5491

PASSWORD_FILE="passwords.txt"


cd $BIN

java -cp . $PACKAGE.$MAIN_CLASS $PORT $PASSWORD_FILE
