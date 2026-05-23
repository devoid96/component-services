Instrucciones para ejecutar el proyecto con Temurin (Corretto/Adoptium) JDK 21

- Asegúrate de tener instalado Temurin 21 (p. ej. 21.0.11).
- Establece `JAVA_HOME` al directorio de instalación. En PowerShell:

```powershell
$Env:JAVA_HOME = 'C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.11.11-hotspot'
$Env:PATH = "$Env:JAVA_HOME\\bin;$Env:PATH"
```

En CMD (temporal para la sesión):

```cmd
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.11-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
```

- Opcional: añade `JAVA_HOME` como variable de sistema desde la configuración de Windows.

- En VS Code: abre la carpeta del proyecto y ajusta la ruta en `.vscode/settings.json` si tu instalación difiere.

- Para ejecutar la aplicación desde la terminal integrada:

```powershell
.\mvnw spring-boot:run
```

o en Windows (cmd/powershell):

```cmd
mvnw.cmd spring-boot:run
```

- Para depurar en VS Code: usa la configuración `Launch ComponentServiceApplication` en la vista Ejecutar y Depurar.

Si quieres, actualizo la ruta en `.vscode/settings.json` con la ruta exacta que tienes instalada.
