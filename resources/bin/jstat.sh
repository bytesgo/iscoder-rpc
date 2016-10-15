#!/bin/sh

##################################################################################
# Date:2011-12-05
# Author:haoxb
# Describe
#     Print heap info.
#     jstak.sh [-<option>] [<server-name | pid>] [<interval-time> ms] [<count>]
#     <option>: -gc | -class | -gccause | -gcutil | -gcpermcapacity | -gccapacity 
#              -gcnew | -gcnewcapacity | -gcold | -gcoldcapacity 
#              -compiler | -printcompilation
#              default -gc
#     <server-name>:this server name
#     <pid>:this server process id
#     <interval-time>: Sampling interval The following forms are allowed:<n>['ms']
#                   Where <n> is an integer and the suffix specifies the units as milliseconds('ms') . 
#                   The default units are 'ms'.
##################################################################################

USAGE="Usage: jstak.sh [-<option>] [<server-name | pid>] [<interval-time> ms] [<count>]"

# if no args specified, show usage
#if [ $# -ne 1 ]; then
#  echo $USAGE
#  exit 1
#fi


#process ID
OPTION=$1
PID=""
#time interval util:ms
TIME=""
COUNT=""

if [ "$OPTION" ];then
    if [ ${OPTION:0:1} = "-" ];then
        if [ ${OPTION:1:2} != "h" ];then
            PID=$2
            TIME=$3
            COUNT=$4
        else
            PID=$1
            TIME=$2
            COUNT=$3
        fi
    else
        OPTION="-gc"
        PID=$1
        TIME=$2
        COUNT=$3
    fi
else
    echo $USAGE
    exit 1   
fi

if [ $PID == "-h" ]; then
    echo "Usage:jstak.sh -h|server-name|pid"
    echo "    jstak.sh [-<option>] [<server-name | pid>] [<interval-time> ms] [<count>]"
    echo "Definitions:"
    echo "    <option>: -gc | -class | -gccause | -gcutil | -gcpermcapacity | -gccapacity 
              -gcnew | -gcnewcapacity | -gcold | -gcoldcapacity 
              -compiler | -printcompilation
              default -gc"
    echo "    <server-name>:this server name"
    echo "    <pid>:this server process id"
    echo "    <interval-time>: Sampling interval The following forms are allowed:
                   <n>['ms']
                   Where <n> is an integer and the suffix specifies 
                   the units as milliseconds('ms') . The default units are 'ms'."
    exit 1
fi

case x$PID in
x[0-9]*)
;;
*)
PID=`ps -ef|grep -v grep|grep =$PID |sed -n 1p |awk '{print $2}'`
;;
esac

if [ "$PID" = "" ];then
    echo "Not this server or PID!"
    exit 1
fi

if [ "$TIME" ]; then
    case x$TIME in
    x[0-9]*)
      ;;
    *)
      echo "please input time num!"
      exit 1
      ;;
    esac
fi

if [ "$COUNT" ]; then
    case x$COUNT in
    x[0-9]*)
      ;;
    *)
      echo "please input count num!"
      exit 1
      ;;
    esac
fi


jstat $OPTION $PID $TIME $COUNT

#jstat -gc
#jstat -gcold
#jstat -gcnew
#jstat -gcutil
#jstat -class
#jstat -gccapacity
#jstat -gccause
#jstat -gcnewcapacity
#jstat -gcoldcapacity
#jstat -gcpermcapacity
#jstat -compiler  #JIT
#jstat -printcompilation #JIT
