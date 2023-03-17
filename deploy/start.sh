#!/bin/sh
#chkconfig:2345 80 90
#decription:autostart

nohup java -jar ./jeecg-boot-module-system-2.4.5.jar catalina.out 2>&1 &
echo "KM service start completed."