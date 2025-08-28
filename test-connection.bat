@echo off
echo Testing AI Support Connection...
echo.

echo Step 1: Testing basic connection...
mvn exec:java -Dexec.mainClass="com.aisupport.util.ConnectionTester"

echo.
echo Step 2: If connection test passes, try the main application...
echo Press any key to continue to main app...
pause >nul

echo.
echo Starting main application...
mvn exec:java -Dexec.mainClass="com.aisupport.Main"

pause
