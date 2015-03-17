#!/bin/bash

BODY=$( cat $2 | sed -n "/Results :/, /Tests run/ p" )
FAILURES=$( echo $BODY | sed -n "/Results :/, /Tests run/ p"  | grep "Tests run:" | sed -n -e 's/^.*Failures: \([0-9]*\), .*/\1/p' )
ERRORS=$( echo $BODY | sed -n "/Results :/, /Tests run/ p"  | grep "Tests run:" | sed -n -e 's/^.*Errors: \([0-9]*\), .*/\1/p' )

if [ \( $FAILURES -gt 0 \) -o \( $ERRORS -gt 0 \) ] 
then 
	TITLE=$( echo $BODY | sed  's/(mobi\.boilr\.libdynticker\.exchanges\.\([a-zA-Z0-9]*\)ExchangeTest)/\n(\1)\n/g; s/\n [a-zA-Z0-9 :.,()/]*//g; s/^[a-zA-Z0-9 :]*\n//g; s/(\([a-zA-Z0-9]*\))/\1/g; s/\n/, /g' )
	BODY=$( echo "$BODY" | sed ':a;N;$!ba;s/\n/\\n/g')
	CRED=$(cat $1)
	curl -u "$CRED" -H "Content-Type: application/json" -d \
	'{
		"title":"'"$TITLE"' failed automatic tests", 
		"body":"'"$BODY"'",
		"labels" : [
			"bug"
		]
	}'\
	https://api.github.com/repos/andrefbsantos/libdynticker/issues
fi