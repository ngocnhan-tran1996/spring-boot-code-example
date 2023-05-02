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

-- execute pack_example.concatenate_text_proc
DECLARE
  IN_NAME VARCHAR2(200);
  IN_PERSONS USER_NHAN.PACK_EXAMPLE.PERSON_ARRAY;
  OUT_MSG VARCHAR2(200);
  OUT_NBR NUMBER;
BEGIN
  IN_NAME := '1';
  IN_PERSONS(0).name := '1';
  IN_PERSONS(0).age := '1';
  IN_PERSONS(1).name := '2';
  IN_PERSONS(1).age := '2';
  IN_PERSONS(2).name := '2';
  IN_PERSONS(2).age := '2';

  USER_NHAN.PACK_EXAMPLE.concatenate_text_proc(
    IN_NAME => IN_NAME,
    IN_PERSONS => IN_PERSONS,
    OUT_NBR => OUT_NBR,
    OUT_MSG => OUT_MSG
  );

  dbms_output.put_line(OUT_MSG);
  dbms_output.put_line(OUT_NBR);
END;

-- execute pack_example.card_info_func
DECLARE
    card_results SYS_REFCURSOR;
    l_id USER_NHAN.CAR.id%TYPE;
    l_name USER_NHAN.CAR.name%TYPE;
BEGIN

    card_results := user_nhan.pack_example.card_info_func();

    -- process each employee
    LOOP
        FETCH
            card_results
        INTO
            l_id,
            l_name;
        EXIT
    WHEN card_results%notfound;
        dbms_output.put_line(l_id || ' - ' || l_name );
    END LOOP;
    -- close the cursor
    CLOSE card_results;
END;

-- execute pack_example.PLUS_ONE
DECLARE
    output NUMBER;
    in_number NUMBER;
    out_nbr NUMBER;
    out_msg VARCHAR(100);
BEGIN
    in_number := 1;
    out_nbr := 1000;
    output := user_nhan.pack_example.PLUS_ONE (
        in_number => in_number,
        out_nbr => out_nbr,
        out_msg => out_msg);

    dbms_output.put_line(output);
    dbms_output.put_line(out_nbr);
    dbms_output.put_line(out_msg);
END;