#!/bin/bash

SPARK_DIR="../spark"

#compile source code
mvn clean install

#start spark master
$SPARK_DIR/sbin/start-master.sh -h localhost -p 7077

#start spark slave
$SPARK_DIR/sbin/start-slave.sh spark://localhost:7077

cd target
zip -d sparkFpgaApp-1.0-SNAPSHOT.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
cd ..

#submit to spark
$SPARK_DIR/bin/spark-submit --master spark://localhost:7077 --class com.sparkFpgaApp.scala.app ./target/sparkFpgaApp-1.0-SNAPSHOT.jar
