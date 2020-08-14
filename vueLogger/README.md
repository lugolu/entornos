# Vue Logger API

## Desarrollo
### .env:
 * Variables para la ejecución.

### serve.bat:
 * borra directorios test y logs.
 * instala dependencias de Node.
 * Inicia server en modo dev.


## Test 
### coverage.bat:
 * borra directorios test.
 * ejecuta tests.
 * realiza reportes text, html y lcov (necesario para enviar a Sonarqube).

### nyc.config.js:
 * archivos y directorios al realizar análisis de cobertura.

### test.bat:
 * borra directorios test.
 * ejecuta tests.


## Sonarqube
### sonar-project.properties:
 * configuraciones para el análisis con sonar-scanner (debe descomprimirse el binario descargado y agregar variable de entorno en el PATH).

### sonar.bat:
 * ejecuta análisis sonar-scanner enviando los resultados al servidor de Sonarqube.


## Implementación Docker
### .dockerignore:
 * archivos a ignorar al construir imagen.

### createImage.bat:
 * genera la imagen para ser corrida en el entorno actual.

### Dockerfile:
 * archivo con comandos de Docker para generar la imagen.

### runImage.bat:
 * corre la imagen en el entorno actual.

### stopImage.bat:
 * detiene la imagen corriendo en el entorno actual.
