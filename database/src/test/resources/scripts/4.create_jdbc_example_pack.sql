CREATE OR REPLACE PACKAGE user_nhan.jdbc_example_pack IS
    TYPE name_record IS RECORD (
        first_name VARCHAR(255),
        last_name  VARCHAR(255)
    );
    TYPE number_array IS
        TABLE OF NUMBER INDEX BY BINARY_INTEGER;
    TYPE name_array IS
        TABLE OF name_record INDEX BY BINARY_INTEGER;
    TYPE name_table IS
        TABLE OF name_record;
    PROCEDURE concat_name_proc (
        in_name  IN OUT VARCHAR2,
        in_names IN name_array,
        out_msg  OUT VARCHAR2
    );

    PROCEDURE complex_type_out_proc (
        out_names   OUT name_array,
        out_numbers OUT number_array
    );

    FUNCTION name_info_func RETURN SYS_REFCURSOR;

    FUNCTION plus_one_func (
        in_number IN NUMBER,
        out_nbr   IN OUT NUMBER,
        out_msg   OUT VARCHAR2
    ) RETURN NUMBER;

    FUNCTION name_info_table_func RETURN name_table
        PIPELINED;

END jdbc_example_pack;
/

CREATE OR REPLACE PACKAGE BODY user_nhan.jdbc_example_pack IS

    PROCEDURE concat_name_proc (
        in_name  IN OUT VARCHAR2,
        in_names IN name_array,
        out_msg  OUT VARCHAR2
    ) AS
    BEGIN
        FOR i IN in_names.first..in_names.last LOOP
            IF ( out_msg IS NULL ) THEN
                out_msg := ' NAME: '
                           || in_names(i).first_name
                           || ' '
                           || in_names(i).last_name;

            ELSE
                out_msg := out_msg
                           || ' AND '
                           || ' NAME: '
                           || in_names(i).first_name
                           || ' '
                           || in_names(i).last_name;
            END IF;
        END LOOP;

        out_msg := 'IN_NAME = '
                   || in_name
                   || ' AND IN_PARAMS = '
                   || out_msg;
    END concat_name_proc;

    PROCEDURE complex_type_out_proc (
        out_names   OUT name_array,
        out_numbers OUT number_array
    ) AS
        names   name_array;
        numbers number_array;
    BEGIN
        FOR i IN 0..5 LOOP
            names(i).first_name := 'first_name ' || i;
            names(i).last_name := 'last_name ' || i;
            numbers(i) := i;
        END LOOP;

        out_names := names;
        out_numbers := numbers;
    END complex_type_out_proc;

    FUNCTION name_info_func RETURN SYS_REFCURSOR AS
        results SYS_REFCURSOR;
    BEGIN
        OPEN results FOR SELECT
                                              a.first_name,
                                              a.last_name
                                          FROM
                                              user_nhan.name_prefix a
                         WHERE
                             a.id < 10;

        RETURN results;
    EXCEPTION
        WHEN no_data_found THEN
            OPEN results FOR SELECT
                                 NULL AS first_name,
                                 NULL AS last_name
                             FROM
                                 dual;

            RETURN results;
    END name_info_func;

    FUNCTION plus_one_func (
        in_number IN NUMBER,
        out_nbr   IN OUT NUMBER,
        out_msg   OUT VARCHAR2
    ) RETURN NUMBER AS
    BEGIN
        IF ( in_number < 0 ) THEN
            out_nbr := 0;
            out_msg := 'Negative number';
            RETURN in_number + abs(in_number) + 1;
        ELSE
            out_nbr := 1;
            out_msg := 'Not negative number';
            RETURN in_number + 1;
        END IF;
    END plus_one_func;

    FUNCTION name_info_table_func RETURN name_table
        PIPELINED
    AS
        table_output name_record;
    BEGIN
        FOR record IN (
            SELECT
                a.first_name,
                a.last_name
            FROM
                user_nhan.name_prefix a
            WHERE
                a.id < 10
        ) LOOP
            table_output.first_name := record.first_name;
            table_output.last_name := record.last_name;
            PIPE ROW ( table_output );
        END LOOP;

        RETURN;
    END name_info_table_func;

END jdbc_example_pack;