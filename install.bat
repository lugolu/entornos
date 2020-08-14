@ECHO OFF

call cd entornos
call yarn install

call cd..

echo entornosVue
call cd entornosVue
call yarn install

call cd..

echo vueLogger
call cd vueLogger
call yarn install

call cd..

call pause