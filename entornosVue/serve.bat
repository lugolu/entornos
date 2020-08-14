@ECHO OFF

call cls

call yarn install

call yarn run serve -- --port 9998

call pause