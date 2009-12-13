set BOT_HOME=..
rem set DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n
rem set JMX=-Dcom.sun.management.jmxremote
%JAVA_HOME%/bin/java -server %JMX% %DEBUG% -Xmx1024m -DBOT_HOME=%BOT_HOME% -DproductionMode=true -cp ../jar/flexibet-launcher.jar dk.flexibet.launcher.FlexiBetLauncher