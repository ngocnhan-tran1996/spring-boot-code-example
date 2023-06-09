CREATE OR REPLACE PACKAGE user_nhan.example_pack
IS
    TYPE person_record IS RECORD (
        name VARCHAR2(100),
        age VARCHAR2(100)
    );

    TYPE person_array IS TABLE OF person_record INDEX BY BINARY_INTEGER;

    TYPE person_table IS TABLE OF person_record;

    PROCEDURE concatenate_text_proc (
        in_name IN VARCHAR2,
        in_persons IN example_pack.person_array,
        out_nbr IN OUT NUMBER,
        out_msg OUT VARCHAR2);

    FUNCTION CARD_INFO_FUNC
        RETURN SYS_REFCURSOR;

    FUNCTION PLUS_ONE_FUNC (
        in_number IN NUMBER,
        out_nbr IN OUT NUMBER,
        out_msg OUT VARCHAR2)
        RETURN NUMBER;

     FUNCTION PERSON_INFO_FUNC
        RETURN person_table
        PIPELINED;
END example_pack;
/
CREATE OR REPLACE PACKAGE BODY user_nhan.example_pack
IS

    PROCEDURE concatenate_text_proc (
        in_name IN VARCHAR2,
        in_persons IN example_pack.person_array,
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

    FUNCTION CARD_INFO_FUNC
        RETURN SYS_REFCURSOR
    AS
        results SYS_REFCURSOR;
    BEGIN
        OPEN results
        FOR
            SELECT c.id, c.name
            FROM USER_NHAN.CAR c;
        RETURN results;

        EXCEPTION
            WHEN NO_DATA_FOUND
                THEN
                    OPEN results
                    FOR
                        SELECT
                            NULL AS ID,
                            NULL AS NAME
                        FROM dual;
                    RETURN results;
    END CARD_INFO_FUNC;

    FUNCTION PLUS_ONE_FUNC (
        in_number IN NUMBER,
        out_nbr IN OUT NUMBER,
        out_msg OUT VARCHAR2)
        RETURN NUMBER
    AS
    BEGIN
        IF (in_number < 0)
            THEN
            out_nbr := 0;
            out_msg := 'Negative number';
            RETURN in_number + ABS (in_number) + 1;
        ELSE
            out_nbr := 1;
            out_msg := 'Not negative number';
            RETURN in_number + 1;
        END IF;
    END PLUS_ONE_FUNC;

    FUNCTION PERSON_INFO_FUNC
        RETURN person_table
        PIPELINED
    AS
        table_output example_pack.person_record;
    BEGIN
        FOR record IN (SELECT c.* FROM USER_NHAN.CAR c)
        LOOP
            table_output.name := record.id;
            table_output.age := record.name;
            PIPE ROW (table_output);
        END LOOP;
        RETURN;
    END PERSON_INFO_FUNC;

END example_pack;