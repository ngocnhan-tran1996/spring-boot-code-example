-- grant privilege
GRANT EXECUTE ON system.p_drop_car_tbl TO system;

-- create proc
CREATE OR REPLACE PROCEDURE system.p_drop_car_tbl (
    in_schema IN dba_tables.owner%TYPE,
    in_table_name IN dba_tables.table_name%TYPE)
IS
    tbl_count number;
    sql_stmt long;

BEGIN
    SELECT COUNT(1) INTO tbl_count
    FROM dba_tables
    WHERE UPPER(owner) = UPPER('SYSTEM')
        AND UPPER(table_name) = UPPER('CAR');

    IF (tbl_count > 0)
        THEN
        sql_stmt := 'DROP TABLE system.car';
        EXECUTE IMMEDIATE 'DROP TABLE system.car';
    END IF;
END;