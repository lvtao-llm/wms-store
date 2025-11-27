# 创建数据库

# 创建外网用户：
-- 创建可以从任意主机访问的 root 用户
CREATE USER 'root'@'%' IDENTIFIED BY 'Monitor@20240101';
-- 或者授予现有 root 用户外网访问权限
UPDATE mysql.user SET Host='%' WHERE User='root' AND Host='localhost';
-- 授予所有权限
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
-- 刷新权限
FLUSH PRIVILEGES;

# 数据库自动备份