mydate="{'FECHA':'01/01/2020', 'HORA':'00:00'}"
echo Current time is $mydate

rmdir /S /Q logsEntornos
rmdir /S /Q logsVue

cd entornos
echo $mydate > generated.json
cd ..

cd entornosVue
echo $mydate > generated.json
cd ..

cd vueLogger
echo $mydate > generated.json
cd ..

docker-compose down 

docker-compose up --build --force-recreate --detach
