ALTER SESSION SET "_oracle_script"=true;

-- drop user
DROP USER user_transaction cascade;

-- create user
CREATE USER user_transaction IDENTIFIED BY ngocnhan;

-- grant privilege
GRANT ALL PRIVILEGES TO user_transaction;
GRANT EXECUTE ON dbms_xa TO user_transaction;
GRANT SELECT ON pending_trans$ TO user_transaction; 
GRANT SELECT ON dba_2pc_pending TO user_transaction;
GRANT SELECT ON dba_pending_transactions TO user_transaction;