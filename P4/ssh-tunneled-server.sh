BIN="bin/"

PACKAGE="edu.colostate.cs.cs414.p4.main"

MAIN_CLASS="PlayerMain"

ADDR="localhost"

USER="pflagert"

PORT_A=54350

PORT_B=5491

ERRORS=0

help_message() {
	n="\n"
	nn="\n\n"
	t="  "
	tt="$t$t"
	ttt="$tt$t"
	tttt="$ttt$t"
	nnt="$nn$t"
	nntt="$nn$tt"
	ntt="$n$tt"
	nttt="$n$ttt"
	ntttt="$n$tttt"
	echo -e "Usage: $0 [Options...]$nnt"															\
																									\
																									\
				"Options [(-h,--help),(-a,--address),(-p,--port), (-u,--user)].. Args $nntt"		\
																									\
																									\
					"[-h | --help]: displays this message. $nntt"									\
																									\
																									\
					"[-a | --address]: sets the address to forward to.$nttt"						\
							"Example 1: $0 -a denver.cs.colostate.edu$ntttt"						\
							"Example 2: $0 --address lincoln.cs.colostate.edu$nntt"					\
																									\
																									\
					"[-p | --port]: sets the port to forward to.$nttt"								\
							"Example 1: $0 -p 4000$ntttt"											\
							"Example 2: $0 --port 5491$nntt" 										\
																									\
																									\
					"[-u | --user]: sets the user on the target address.$nttt"						\
							"Example 1: $0 --address lincoln.cs.colostate.edu --user pflagert$ntttt"\
							"Example 2: $0 -apu lincoln.cs.colostate.edu 5491 pflagert$nntt"		\
																									\
																									\
				"If no options are given:$nttt"														\
					"Address is $ADDR.$nttt"														\
					"Port is $PORT.$nttt"															\
					"User is $USER.$nn"
	
}

inc_error() {
	let ERRORS=ERRORS+1
}

print_settings() {
	echo -e "Address is: $ADDR\nPort is: $PORT_A\nUser is: $USER"
}

main() {
	print_settings
	PID=-1
	if [[ "$ADDR" != "localhost" ]]; then
		echo -n "Tunneling $PORT_A on $ADDR to $PORT_B on localhost: PID=";
		ssh  -nNT -C -R "$ADDR:$PORT_A:localhost:$PORT_B" "$USER@$ADDR" &
		PID=$!
		echo $PID
		sleep 3
	fi
	./server.sh
	if [[ "$PID" != "-1" ]]; then
		if ps aux | grep ssh | tr -s ' ' ' ' | cut -d ' ' -f2 | grep $PID &>/dev/null; then			
			echo -ne "\nWould you like to kill the tunneled connection?(Y/n): ";
			read response
			response=$(echo $response | tr '/a-z/' '/A-Z/')
			if [[ "$response" == "Y" ]] || [[ "$response" == "YES" ]]; then
				kill -9 $PID
			else 
				echo "You can always kill this connection later with 'kill -9 $PID'";
			fi
		fi
	fi
}

# check command line arguments

# check for globed options
pattern="[hapu]"
declare -a options;
declare -a args;
for ((i=1; i <= $#; i++)); do
	opt=${@:$i:1}
	if echo $opt | grep -- "^--.*" &>/dev/null; then
		options+=($opt)
	elif echo $opt | grep -- "^-$pattern$" &>/dev/null; then
		options+=($opt)
	elif echo $opt | grep -- "^-$pattern$pattern$" &>/dev/null; then
		opt1="-${opt:1:1}"
		opt2="-${opt:2:1}"
		options+=("$opt1")
		options+=("$opt2")
	elif echo $opt | grep -- "^-$pattern$pattern$pattern$" &>/dev/null; then
		opt1="-${opt:1:1}"
		opt2="-${opt:2:1}"
		opt3="-${opt:3:1}"
		options+=("$opt1")
		options+=("$opt2")
		options+=("$opt3")	
	elif echo $opt | grep -- "^-$pattern$pattern$pattern$pattern$" &>/dev/null; then
		opt1="-${opt:1:1}"
		opt2="-${opt:2:1}"
		opt3="-${opt:3:1}"
		opt4="-${opt:4:1}"
		options+=("$opt1")
		options+=("$opt2")
		options+=("$opt3")
		options+=("$opt4")	
	elif echo $opt | grep -- "^-.*" &>/dev/null; then
		options+=($opt) 
	else
		args+=("$opt")	
	fi
	
done

set -- "${options[@]}"
#echo "$@"
#echo "${args[@]}"

# check for help option
for opt in "$@"; do
    if [[ "$opt" == "--help" ]] || [[ "$opt" == "-h" ]]; then
		help_message
		exit 0
	fi
done

i=0
# now perform options 
while [ $# -gt 0 ]; do
	case "$1" in
		-a | --address)
			ADDR=${args[$i]}
			;;
		-p | --port)
			PORT_A=${args[$i]}
			;;
		-u | --user)
			USER=${args[$i]}
			;;
		#-h | --help) Already Checked
		* ) # anything else is an error
			inc_error
			echo "Unkown option: '$1'"
			echo "Run: '$0 --help' for more information"
			exit 1;
			
	esac
	if [[ "$ERRORS" != "0" ]]; then 
		exit 1;
	fi
	shift
	let i=i+1
done

if [[ "$ERRORS" != "0" ]]; then
	exit 1;
else
	main
fi

