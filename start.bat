@echo off
color 02
if exist class (
    echo Suppression des fichiers précédents...
    rmdir /s /q class
    mkdir class
)
echo ============================================
echo          Compilation Java en cours...
echo ============================================
if not exist gs-ui-swing.jar (
    echo Erreur : Le fichier gs-ui-swing.jar est introuvable.
    pause
    exit /b
)
if not exist gs-core.jar (
    echo Erreur : Le fichier gs-core.jar est introuvable.
    pause
    exit /b
)
if not exist gson.jar (
    echo Erreur : Le fichier gson.jar est introuvable.
    pause
    exit /b
)
if not exist class (
    echo Creation du dossier 'class'...
    mkdir class
)
javac -cp "gs-ui-swing.jar;gs-core.jar;gson.jar" -d class *.java 
if %errorlevel% neq 0 (
    echo ============================================
    echo        Erreur lors de la compilation.
    echo ============================================
    pause
    exit /b
)
echo ============================================
echo         Compilation terminee avec succes.
echo ============================================
echo Lancement du programme...

java -cp "class;gs-ui-swing.jar;gs-core.jar;gson.jar" Main 
echo ============================================
echo           Programme termine.
echo ============================================
pause
