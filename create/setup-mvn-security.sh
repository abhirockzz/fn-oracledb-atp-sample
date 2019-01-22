#!/bin/bash

master_password=$1
oraclecom_userid=$2
oraclecom_password=$3
# Encrypt the master password and put it into settings-security.xml
enc_master_password=`mvn --encrypt-master-password ${master_password}`
sed -e "s#MASTER_PASSWORD#${enc_master_password}#" < settings-security.template > settings-security.xml

# Encrypt the user password and put it into settings.xml
cwd=`pwd`
enc_oraclecom_password=`mvn -Dsettings.security=${cwd}/settings-security.xml --encrypt-password ${oraclecom_password}`
echo
sed -e "s#USER_ID#${oraclecom_userid}#" -e "s#USER_PASSWORD#${enc_oraclecom_password}#" < settings.template > settings.xml

echo "Maven settings.xml and settings-security.xml created."
