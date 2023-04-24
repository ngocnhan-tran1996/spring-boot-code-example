CREATE OR REPLACE PACKAGE user_nhan.pack_example
IS
    TYPE person_record IS RECORD (
        name  VARCHAR2(100),
        age VARCHAR2(100)
    );

    TYPE person_array IS TABLE OF person_record INDEX BY BINARY_INTEGER;

    PROCEDURE p_concatenate_text(in_name IN VARCHAR2,
                                 in_persons IN pack_example.person_array,
                                 out_msg OUT VARCHAR2);

END pack_example;
/
CREATE OR REPLACE PACKAGE BODY user_nhan.pack_example
IS

    PROCEDURE p_concatenate_text(in_name IN VARCHAR2,
                                 in_persons IN pack_example.person_array,
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
    END p_concatenate_text;

END pack_example;
/
DECLARE
  IN_NAME VARCHAR2(200);
  IN_PARAMS USER_NHAN.PACK_EXAMPLE.IN_PARAMS;
  OUT_MSG VARCHAR2(200);
BEGIN
  IN_NAME := '1';
  IN_PARAMS(0).name := '1';
  IN_PARAMS(0).value := '1';
  IN_PARAMS(1).name := '2';
  IN_PARAMS(1).value := '2';

  USER_NHAN.PACK_EXAMPLE.P_CONCATENATE_TEXT(
    IN_NAME => IN_NAME,
    IN_PARAMS => IN_PARAMS,
    OUT_MSG => OUT_MSG
  );
  
  dbms_output.put_line(OUT_MSG);

END;