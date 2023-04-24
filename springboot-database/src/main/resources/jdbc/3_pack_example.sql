CREATE OR REPLACE PACKAGE user_nhan.pack_example
IS
    TYPE person_record IS RECORD (
        name  VARCHAR2(100),
        age VARCHAR2(100)
    );

    TYPE person_array IS TABLE OF person_record INDEX BY BINARY_INTEGER;

    PROCEDURE concatenate_text_proc (
        in_name IN VARCHAR2,
        in_persons IN pack_example.person_array,
        out_nbr IN OUT NUMBER,
        out_msg OUT VARCHAR2);

END pack_example;
/
CREATE OR REPLACE PACKAGE BODY user_nhan.pack_example
IS

    PROCEDURE concatenate_text_proc (
        in_name IN VARCHAR2,
        in_persons IN pack_example.person_array,
        out_nbr IN OUT NUMBER,
        out_msg OUT VARCHAR2)
    AS
    BEGIN
        FOR i IN in_persons.FIRST .. in_persons.LAST
        LOOP
            IF (out_msg IS NULL)
                THEN 
                out_msg := ' NAME: ' || in_persons(i).name || ' AND AGE: ' || in_persons(i).age;
            ELSE
                out_msg := out_msg || ' AND ' || ' NAME: ' || in_persons(i).name || ' AND AGE: ' || in_persons(i).age;
            END IF;
        END LOOP;

        out_msg := 'IN_NAME = ' || in_name || ' AND IN_PARAMS = ' || out_msg;
        out_nbr := 2010;
    END concatenate_text_proc;

END pack_example;

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