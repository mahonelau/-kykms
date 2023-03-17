#!/bin/bash

port=8080

pid=$(netstat -nlp | grep :$port | awk '{print $7}' | awk -F"/" '{ print $1 }');

if [  -n  "$pid"  ];  then
    kill  -9  $pid;
	echo "KM service stopped."
fi

nohup java -jar ./jeecg-boot-module-system-2.4.5.jar catalina.out 2>&1 &
echo "KM service start completed."