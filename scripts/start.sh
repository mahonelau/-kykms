#!/bin/bash

nohup java -jar jeecg-boot-module-system-2.4.5.jar catalina.out 2>&1 &
echo "KM service start completed."