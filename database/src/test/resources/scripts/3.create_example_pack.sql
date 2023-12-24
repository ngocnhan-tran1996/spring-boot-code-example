create or replace PACKAGE user_nhan.example_pack IS

    TYPE name_record IS RECORD (
        first_name VARCHAR(255),
        last_name  VARCHAR(255),
        age        NUMBER
    );

    TYPE name_table IS
        TABLE OF name_record;

    FUNCTION name_info_func (
        fromdate VARCHAR,
        todate   VARCHAR
    ) RETURN name_table
        PIPELINED;

    FUNCTION name_info_loop_func (
        fromdate VARCHAR,
        todate   VARCHAR
    ) RETURN name_table
        PIPELINED;

END example_pack;
/
create or replace PACKAGE BODY user_nhan.example_pack IS

    FUNCTION name_info_func (
        fromdate VARCHAR,
        todate   VARCHAR
    ) RETURN name_table
        PIPELINED
    AS

        table_output       name_record;
        CURSOR name_cursor (
            fromdate VARCHAR,
            todate   VARCHAR
        ) IS
        SELECT
            a.first_name,
            a.last_name,
            a.age
        FROM
            user_nhan.name_prefix a,
            (
                SELECT
                    *
                FROM
                    user_nhan.name_prefix
                WHERE
                    id < 100000
            )                     b
        WHERE
                a.id = b.id
            AND b.createdatetime >= TO_DATE(fromdate, 'YYYY-MM-DD')
            AND b.createdatetime <= TO_DATE(todate, 'YYYY-MM-DD');

        name_cursor_record name_cursor%rowtype;
        first_name         VARCHAR(255);
        last_name          VARCHAR(255);
        age                NUMBER;
    BEGIN
        OPEN name_cursor(fromdate, todate);
        LOOP
            FETCH name_cursor INTO
                first_name,
                last_name,
                age;
            EXIT WHEN name_cursor%notfound;
            table_output.first_name := first_name;
            table_output.last_name := last_name;
            table_output.age := age;
            PIPE ROW ( table_output );
        END LOOP;

        CLOSE name_cursor;
        RETURN;
    END name_info_func;

    FUNCTION name_info_loop_func (
        fromdate VARCHAR,
        todate   VARCHAR
    ) RETURN name_table
        PIPELINED
    AS

        table_output name_record;
        CURSOR name_cursor (
            fromdate VARCHAR,
            todate   VARCHAR
        ) IS
        SELECT
            a.first_name,
            a.last_name,
            a.age
        FROM
            user_nhan.name_prefix a,
            (
                SELECT
                    *
                FROM
                    user_nhan.name_prefix
                WHERE
                    id < 100000
            )                     b
        WHERE
                a.id = b.id
            AND b.createdatetime >= TO_DATE(fromdate, 'YYYY-MM-DD')
            AND b.createdatetime <= TO_DATE(todate, 'YYYY-MM-DD');

    BEGIN
        FOR record IN name_cursor(fromdate, todate) LOOP
            table_output.first_name := record.first_name;
            table_output.last_name := record.last_name;
            table_output.age := record.age;
            PIPE ROW ( table_output );
        END LOOP;

        RETURN;
    END name_info_loop_func;

END example_pack;