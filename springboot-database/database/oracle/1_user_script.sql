ALTER SESSION SET "_oracle_script"=true;

-- drop user
DROP USER user_nhan cascade;

-- create user
CREATE USER user_nhan IDENTIFIED BY ngocnhan;

-- grant privilege
GRANT ALL PRIVILEGES TO user_nhan;

-- drop user
DROP USER user_extra cascade;

-- create user
CREATE USER user_extra IDENTIFIED BY ngocnhan;

-- grant privilege
GRANT ALL PRIVILEGES TO user_extra;