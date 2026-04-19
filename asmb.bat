@echo off
set "_ASMB_HOME=%~dp0"
java "-DASMBUILDER_HOME=%_ASMB_HOME%\" -jar "%~dp0target\asmbuilder.jar" %*
