mydate="{'FECHA':'01/01/2020', 'HORA':'00:00'}"
echo Current time is $mydate

rmdir /S /Q logsEntornos
rmdir /S /Q logsVue

cd entornos
yarn install
echo $mydate > generated.json
cd ..

cd entornosVue
yarn install
echo $mydate > generated.json
cd ..

cd vueLogger
yarn install
echo $mydate > generated.json
cd ..

docker-compose down 

docker-compose up --build --force-recreate --detach
