@echo off
color 02
echo ============================================
echo          Compilation Java en cours...
echo ============================================
if not exist class (
    echo Creation du dossier 'class'...
    mkdir class
)
javac -d class *.java
if %errorlevel% neq 0 (
    echo ============================================
    echo        Erreur lors de la compilation.
    echo ============================================
    exit /b
)
echo ============================================
echo         Compilation terminee avec succes.
echo ============================================
echo Lancement du programme...
java -cp class Main
echo ============================================
echo           Programme termine.
echo ============================================
pause
exit