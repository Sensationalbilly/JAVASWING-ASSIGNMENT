@echo off
echo Compiling...
javac -d out src/BaseWindow.java src/SwingAssignment.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b 1
)
echo Running...
java -cp out SwingAssignment
