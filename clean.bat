@ECHO OFF

call rmdir /S /Q logsEntornos
call rmdir /S /Q logsVue

echo entornos
call cd entornos
call rmdir /S /Q .nyc_output
call rmdir /S /Q .scannerwork
call rmdir /S /Q coverage
call rmdir /S /Q dockerLogs
call rmdir /S /Q logs
call rmdir /S /Q node_modules

call cd..

echo entornosVue
call cd entornosVue
call rmdir /S /Q .nyc_output
call rmdir /S /Q .scannerwork
call rmdir /S /Q coverage
call rmdir /S /Q dockerLogs
call rmdir /S /Q logs
call rmdir /S /Q node_modules

call cd..

echo vueLogger
call cd vueLogger
call rmdir /S /Q .nyc_output
call rmdir /S /Q .scannerwork
call rmdir /S /Q coverage
call rmdir /S /Q dockerLogs
call rmdir /S /Q logs
call rmdir /S /Q node_modules

call cd..

call pause