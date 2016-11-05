#! /bin/bash


USAGE="scf_copy.sh <src_dir> [<service-name>*]"
DES_DIR="/opt/scf"


if [ $# -lt 1 ];then
	echo "$USAGE"
	exit 1
fi

SRC_DIR=$1
charactor=`echo $SRC_DIR|grep -o '.$'`
if [ $charactor == "/" ];then
	tmp=$SRC_DIR
	SRC_DIR=`echo $tmp|sed 's#\(.*\)\/#\1#g'`
fi

echo $SRC_DIR
#copy scf/conf diractory 
if [ ! -d $DES_DIR ];then
	mkdir -p $DES_DIR
fi
cp -r $SRC_DIR/conf $DES_DIR

#copy service in deploy directory
src_deploy_dir=$SRC_DIR/service/deploy
des_deploy_dir=$DES_DIR/service/deploy

if [ ! -d $des_deploy_dir ];then
	mkdir -p $des_deploy_dir
fi

if [ $# -lt 2 ]; then
	for src_file in $src_deploy_dir/*; do
		if [ -d $src_file ];then
			echo -e "$src_file\t$des_deploy_dir"
			cp -r $src_file $des_deploy_dir
		fi
	done
else
	shift
	while [ $# -gt 0 ];
	do
		param=$1
		src_file=$src_deploy_dir/$param
		echo $src_file
		if [ -d $src_file ];then
			cp -r $src_file $des_deploy_dir
		fi
		shift
	done
fi


#copy bin/ *.sh to /opt/scf/bin
src_bin_dir=$SRC_DIR/bin
des_bin_dir=$DES_DIR/bin

if [ ! -d $des_bin_dir ]; then
	mkdir -p $des_bin_dir
fi

for src_file in $src_bin_dir/*; do
	src_file_name=`echo $src_file|sed 's#.*/##g'`
	case $src_file_name in
		startup.sh )
		;;
		restart.sh )
		;;
		shutdown.sh )
		;;
		jstat.sh )
		;;
		jstack.sh )
		;;
		scfcopy.sh )
		;;
		reboot.sh )
		;;
		*.sh )
		echo $src_file $des_bin_dir
		cp $src_file $des_bin_dir
		chmod +x "$des_bin_dir/$src_file_name"
		;;
	esac
done


#append jvm parameter to service config
echo ------------------------------------------------

	for config_file in $des_deploy_dir/*; do
			service_name=`echo $config_file|sed 's#.*/##g'`
		for sh_file in $des_bin_dir/*; do
			file_name=`echo $sh_file|sed 's#.*/##g'`
			sh_file_name=`echo $file_name|awk -F'_' '{print $1}'`
			if [ "$sh_file_name" == "$service_name" ];then
				xms=`cat $sh_file|grep "VM_XMS"|awk -F'=' '{print $2}'`
				xmx=`cat $sh_file|grep "VM_XMX"|awk -F'=' '{print $2}'`
				xmx=`cat $sh_file|grep "VM_XMN"|awk -F'=' '{print $2}'`
				break;
			else
				continue;
			fi
		done

		echo $service_name

		if [[ $xms == "" ]];then
			value_xms=2g
		else
			value_xms=$xms
		fi

		if [[ $xmx == "" ]];then
			value_xmx=2g
		else
			value_xmx=$xmx
		fi

		if [[ $xmn == "" ]];then
			value_xmn=1g
		else
			value_xmn=$xmn
		fi

#chech the existents of jvm parameter
		flag=`cat $config_file/scf_config.xml | grep 'scf.server.vm.xms'` 
		if [ "$flag" != "" ];then
			continue
		fi
#add jvm parameter
		sed -i "s#</configuration>#<!-- java opts -->\n<property>\n<name>scf.server.vm.xms</name>\n<value>$value_xms</value>\n</property>\n\n<property>\n<name>scf.server.vm.xmx</name>\n<value>$value_xmx</value>\n</property>\n\n<property>\n<name>scf.server.vm.xmn</name>\n<value>$value_xmn</value>\n</property>\n\n</configuration>#g; s///g" $config_file/scf_config.xml
	done

#change config.xml content
	for config_file in $des_deploy_dir/*;
	do
		service_name=`echo $config_file|sed 's#.*/##g'`
		sed -i "N;/\n.*scf.encoding/!P;D; s///g" $config_file/scf_config.xml
		sed -i "/scf.encoding/ ,+2d; s///g" $config_file/scf_config.xml
		sed -i "/service encoding/d; s///g" $config_file/scf_config.xml

		sed -i "N;/\n.*scf.server.tcp.listenIP/!P;D; s///g" $config_file/scf_config.xml
		sed -i "/scf.server.tcp.listenIP/ ,+2d; s///g" $config_file/scf_config.xml
		sed -i "/socket server listent ip/d; s///g" $config_file/scf_config.xml
	
		sed -i "N;/\n.*scf.server.telnet.listenIP/!P;D; s///g" $config_file/scf_config.xml
		sed -i "/scf.server.telnet.listenIP/, +2d; s///g" $config_file/scf_config.xml

		sed -i "/param name=\"File\"/s#\(.*\)value=\(.*\)#\1value=\"../log/$service_name/$service_name.log\" \/>#g; s///g" $config_file/scf_log4j.xml
	done

