#!/bin/sh
APP_HOME=.
CLASSPATH=.
for i in "$APP_HOME"/lib/*.jar
do
 CLASSPATH="$CLASSPATH":"$i"
done

export CLASSPATH=.:$CLASSPATH
echo ${CLASSPATH}
java -Xms512m -Xmx1024m com.unbank.QuartzStartup
exit
