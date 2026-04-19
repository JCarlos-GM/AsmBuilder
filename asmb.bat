@echo off
java -DASMBUILDER_HOME="%~dp0" -jar "%~dp0target\asmbuilder.jar" %*
