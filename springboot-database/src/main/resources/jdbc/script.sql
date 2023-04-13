-- grant privilege
GRANT SELECT ON dba_tables TO system;

-- create proc
CREATE OR REPLACE PROCEDURE system.p_create_car_tbl (
    in_schema IN dba_tables.owner%TYPE,
    in_table_name IN dba_tables.table_name%TYPE,
    out_msg OUT VARCHAR2)
IS
    tbl_count number;
    sql_stmt long;
    insert_sql_stmt long;

BEGIN
    SELECT COUNT(1) INTO tbl_count
    FROM dba_tables
    WHERE UPPER(owner) = UPPER(in_schema)
        AND UPPER(table_name) = UPPER(in_table_name);

    IF (tbl_count = 0)
        THEN
        sql_stmt:='
        CREATE TABLE system.car (
            id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
            name VARCHAR (255) NOT NULL
        )';
        EXECUTE IMMEDIATE sql_stmt;
        out_msg := 'Table was created';

        insert_sql_stmt:='
        INSERT INTO system.car (name) VALUES
            (''FORD''),
            (''HONDA''),
            (''HUYNDAI''),
            (''TOYOTA'')';
        EXECUTE IMMEDIATE insert_sql_stmt;
        out_msg := out_msg || ' - ' || 'Table was inserted';
    ELSE
        out_msg := 'Table already exists';
    END IF;
END;