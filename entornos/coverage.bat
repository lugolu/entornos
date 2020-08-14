@ECHO OFF

call cls

call rmdir /S /Q .nyc_output
call rmdir /S /Q coverage

call yarn run coverage

call pause