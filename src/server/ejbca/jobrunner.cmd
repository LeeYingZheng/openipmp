@echo off

rem JBoss
java -cp .;.\admin.jar;.\lib\jnp-client.jar;.\lib\jboss-client.jar;.\lib\jboss-jaas.jar;.\lib\log4j.jar;.\lib\jce-jdk13-112.jar se.anatom.ejbca.util.JobRunner %1 %2 %3 %4

rem Weblogic
rem java -cp .;.\admin.jar;.\lib\weblogic.jar;.\lib\log4j.jar;.\lib\jce-jdk13-112.jar se.anatom.ejbca.util.JobRunner %1 %2 %3 %4
