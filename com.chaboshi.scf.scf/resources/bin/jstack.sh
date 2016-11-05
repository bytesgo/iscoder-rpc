#!/bin/sh

##################################################################################
# Date:2011-12-05
# Author:haoxb
# Describe
#     Print stack info
#     jstack.sh [<server-name | pid>] [<print count>] [<print url>]
#     <server-name>:this server name
#     <pid>:this server pid
#     <print-count>:stack info print count
#     <print-url>:stack info print url  ex:/opt/scf_v3.4/log
#               if url is ex so print real url is /opt/scf_v3.4/log/jt$PID_$COUNT 
##################################################################################

USAGE="Usage: jstack.sh [<server-name | pid>] [<print-count>] [<print-url>]"

#if no args specified, show usage
if [ $# -ne 3 ]; then
  echo $USAGE
  exit 1
fi

PID=$1
COUNT=$2
FILE_URL=$3

case x$PID in
x[0-9]*)
;;
*)
PID=`ps -ef|grep -v grep|grep =$PID |sed -n 1p |awk '{print $2}'`
;;
esac

case x$COUNT in
x[0-9]*)
;;
*)
echo "please input COUNT num!"
exit 1
;;
esac

if [ "$PID" = "" ];then
    echo "Not this server or PID!"
    exit 1
fi

for((i=0;i<$COUNT;i++));do
echo "jstack $PID > $FILE_URL/jt$PID"_"$i"
jstack $PID > $FILE_URL/jt$PID"_"$i
done

