-- execute user_nhan.create_car_table_proc
DECLARE
    in_schema dba_tables.owner%TYPE;
    in_table_name dba_tables.table_name%TYPE;
    out_msg VARCHAR2(100);
BEGIN
    in_schema := 'user_nhan';
    in_table_name := 'car';
    user_nhan.create_car_table_proc (
        in_schema => in_schema,
        in_table_name => in_table_name,
        out_msg => out_msg);
END;