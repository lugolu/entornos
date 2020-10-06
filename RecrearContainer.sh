@ECHO OFF

call cls

for /f "tokens=2,3,4,5,6 usebackq delims=:/ " %%a in ('%date% %time%') do set mydate={"FECHA":"%%a/%%b/%%c", "HORA":"%%d:%%e"}
echo Current time is %mydate%

call rmdir /S /Q logsEntornos
call rmdir /S /Q logsVue

call cd entornos
call yarn install
call echo %mydate% > generated.json
call cd ..

call cd entornosVue
call yarn install
call echo %mydate% > generated.json
call cd ..

call cd vueLogger
call yarn install
call echo %mydate% > generated.json
call cd ..

call docker-compose down 

call docker-compose up --build --force-recreate --detach
