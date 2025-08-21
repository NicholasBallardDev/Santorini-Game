@echo off
if "%~1" == "24" (
   set "JAVA_HOME=C:Program Files\Java\jdk-11"
) else (
   set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_151"
)
set "Path=%JAVA_HOME%\bin;%Path%"
java -version