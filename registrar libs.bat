@echo off

call mvn install:install-file -Dfile=ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.1 -Dpackaging=jar
call mvn install:install-file -Dfile=RXTXcomm.jar -DgroupId=org.rxtx -DartifactId=rxtxcomm -Dversion=0.0.1 -Dpackaging=jar
call mvn install:install-file -Dfile=babylon-theme-3.0.1.jar -DgroupId=org.primefaces -DartifactId=babylon-theme -Dversion=3.0.1 -Dpackaging=jar
