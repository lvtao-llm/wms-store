@echo off
set DATE=%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=D:\xinruikc_wms\mysql\backup
set DATABASE_NAME=monitor_wms
set MYSQL_USER=root
set MYSQL_PASSWORD=Monitor@20240101
set MYSQL_PATH="CD:\xinruikc_cloud\mysql-8.2.0-winx64\bin"

REM 创建备份目录
if not exist %BACKUP_DIR% mkdir %BACKUP_DIR%

REM 执行备份
%MYSQL_PATH%\mysqldump.exe -u %MYSQL_USER% -p%MYSQL_PASSWORD% %DATABASE_NAME% > %BACKUP_DIR%\%DATABASE_NAME%_%DATE%.sql

REM 删除7天前的备份文件
forfiles /p %BACKUP_DIR% /m *.sql /d -30 /c "cmd /c del @path"
