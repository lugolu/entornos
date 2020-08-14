@ECHO OFF

call cls

for /f "tokens=2,3,4,5,6 usebackq delims=:/ " %%a in ('%date% %time%') do set mydate={"FECHA":"%%a/%%b/%%c", "HORA":"%%d:%%e"}
echo Current time is %mydate%
call echo %mydate% > generated.json

call yarn install

call yarn run test

call pause