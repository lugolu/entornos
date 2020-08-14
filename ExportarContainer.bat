@ECHO OFF

call cls

call echo tag logger
call docker tag entornos_logger:latest entornos_logger:0.0.1
call echo tag entornos
call docker tag entornos_entornos:latest entornos_entornos:0.0.1
call echo tag vue
call docker tag entornos_vue:latest entornos_vue:0.0.1

call echo save logger
call docker save -o entornos_logger_0.0.1.tar entornos_logger
call echo save entornos
call docker save -o entornos_entornos_0.0.1.tar entornos_entornos
call echo save vue
call docker save -o entornos_vue_0.0.1.tar entornos_vue

call move /Y *.tar ./dist
