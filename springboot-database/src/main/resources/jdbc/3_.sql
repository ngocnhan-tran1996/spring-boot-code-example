CREATE OR REPLACE PACKAGE user_nhan.pack_example
IS
    TYPE r_params IS RECORD (
        name  VARCHAR2(30),
        value VARCHAR2(100)
    );

    TYPE in_params IS TABLE OF r_params INDEX BY BINARY_INTEGER;

    PROCEDURE p_concatenate_text(in_name IN VARCHAR2,
                                 in_params IN pack_example.in_params,
                                 out_msg OUT VARCHAR2);

END pack_example;
/
CREATE OR REPLACE PACKAGE BODY user_nhan.pack_example
IS

    PROCEDURE p_concatenate_text(in_name IN VARCHAR2,
                                 in_params IN pack_example.in_params,
                                 out_msg OUT VARCHAR2)
    AS
    BEGIN
        FOR i IN 0 .. in_params.LAST
        LOOP
            IF (out_msg IS NULL)
                THEN 
                out_msg := in_params(i).value;
            ELSE
                out_msg := out_msg || ' AND ' || in_params(i).value;
            END IF;
        END LOOP;

        out_msg := 'NAME = ' || in_name || 'VALUE = ' || out_msg;
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