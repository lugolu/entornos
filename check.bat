@ECHO OFF

call cls

echo **************************************************************************************************
echo entornos
call cd entornos

call yarn install
call yarn outdated

call cd..
echo **************************************************************************************************

echo **************************************************************************************************
echo entornosVue
call cd entornosVue

call yarn install
call yarn outdated

call cd..
echo **************************************************************************************************

echo **************************************************************************************************
echo vueLogger
call cd vueLogger

call yarn install
call yarn outdated

call cd..
echo **************************************************************************************************

call pause