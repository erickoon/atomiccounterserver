#!/bin/bash

numOfReq=10000
numOfCReq=50

ab -p index.html -c $numOfCReq -n $numOfReq http://localhost:8000/
finalCtr=`curl -XPOST http://localhost:8000/`

echo ""
echo "Sent $numOfReq requests total with $numOfCReq requests concurrently."

if [ "$finalCtr" == "$numOfReq" ]; then
		echo "Test passed.  Final counter is $finalCtr"
else
		echo "Test failed.  Final counter is $finalCtr"
fi

exit 0
