#!/bin/sh

### ====================================================================== ###
##                                                                          ##
##                       SCF server bootstrap script                        ##
##                                                                          ##
### ====================================================================== ###


USAGE="Usage: startup.sh <service-name> [<other-scf-config>]"
SYSTEM_PROPERTY=""

# if no args specified, show usage
if [ $# -lt 1 ]; then
  echo $USAGE
  exit 1
fi


# get arguments
SERVICE_NAME=$1
OTHER_SCF_CONFIG=""
for((i=2; i<=$#; i++)); do
  OTHER_SCF_CONFIG=$OTHER_SCF_CONFIG" "${!i}
done


# check tools.jar
if [ ! -f "$JAVA_HOME"/lib/tools.jar ]; then
  echo "Can't find tools.jar in JAVA_HOME"
  echo "Need a JDK to run javac"
  exit 1
fi


# check service is run
javacount=`ps -ef|grep java|grep "sn:$SERVICE_NAME" |wc -l`
#echo "javacount:"$javacount
if [ $javacount -ge 1 ] ; then
  echo "warning: has a [$SERVICE_NAME] is running, please check......................................"
  exit 1
fi



# get path
DIR=`dirname "$0"`
DIR=`cd "$bin"; pwd`
PROGNAME=`basename $0`
ROOT_PATH="$DIR"/..
DEPLOY_PATH="$ROOT_PATH"/service/deploy
PID_PATH="$ROOT_PATH"/tmp/pid



# java opts
if [ "$VM_XMS" = "" ]; then
  VM_XMS=1g
fi

if [ "$VM_XMX" = "" ]; then
  VM_XMX=1g
fi

if [ "$VM_XMN" = "" ]; then
  VM_XMN=256m
fi

JAVA_OPTS="-Xms$VM_XMS -Xmx$VM_XMX -Xmn$VM_XMN -Xss1024K -XX:PermSize=256m -XX:MaxPermSize=512m -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=65536 -XX:MaxTenuringThreshold=0 -XX:CMSInitiatingOccupancyFraction=80"



# class path
CLASS_PATH=.:"$JAVA_HOME"/lib/tools.jar

for jar in $ROOT_PATH/lib/*.jar; do
  CLASS_PATH=$CLASS_PATH:$jar
done



# main class
MAIN_CLASS=com.bj58.spat.scf.server.bootstrap.Main


java $JAVA_OPTS -classpath $CLASS_PATH -Duser.dir=$DIR $SYSTEM_PROPERTY $MAIN_CLASS $OTHER_SCF_CONFIG -Dscf.service.name=$SERVICE_NAME &

echo pid:$!

echo $! > "$PID_PATH"/"$SERVICE_NAME"

