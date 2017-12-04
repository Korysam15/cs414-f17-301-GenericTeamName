BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p5.main"

MAIN_CLASS="ServerMain"

PORT=$1

[ -z $PORT ] && PORT=5491 && echo "Using default port $PORT."

PASSWORD_FILE=$2

[ -z $PASSWORD_FILE ] && PASSWORD_FILE="passwords.txt" && echo "Using default password file: $PASSWORD_FILE"


cd $BIN
clear
java -cp . $PACKAGE.$MAIN_CLASS $PORT $PASSWORD_FILE
