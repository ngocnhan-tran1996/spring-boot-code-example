package com.springboot.code.example.database.domain.name;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NamePrefixRepository extends JpaRepository<NamePrefixEntity, BigDecimal> {

  @Query(nativeQuery = true, value = """
      SELECT *
      FROM TABLE(user_nhan.example_pack.name_info_func(:fromDate, :toDate))
      """,
      countQuery = """
          SELECT COUNT(*)
          FROM TABLE(user_nhan.example_pack.name_info_func(:fromDate, :toDate)) t
          """)
  Page<Object[]> executeNameInfo(String fromDate, String toDate, Pageable page);

  @Query(nativeQuery = true, value = """
      SELECT *
      FROM TABLE(user_nhan.example_pack.name_info_loop_func(:fromDate, :toDate))
      """,
      countQuery = """
          SELECT COUNT(*)
          FROM TABLE(user_nhan.example_pack.name_info_loop_func(:fromDate, :toDate)) t
          """)
  Page<Object[]> executeNameInfoLoop(String fromDate, String toDate, Pageable page);

  // @Query(nativeQuery = true, value = """
  // SELECT
  // *
  // FROM
  // (
  // SELECT
  // a.*,
  // ROWNUM rnum
  // FROM
  // (
  // SELECT
  // t.*,
  // COUNT(*) OVER() result_count
  // FROM
  // TABLE ( example_pack.name_info_func(:fromDate, :toDate) ) t
  // ) a
  // WHERE
  // ROWNUM <= :size
  // )
  // WHERE
  // rnum > :page
  // """)
  // List<AnimationProjection> getSomething3(
  // @Param("fromDate") String fromDate,
  // @Param("toDate") String toDate,
  // @Param("page") Integer pageZ,
  // @Param("size") Integer size);


  @Query(nativeQuery = true, value = """
      SELECT
          *
      FROM
          (
              SELECT
                  t.*,
                  ROWNUM row_index
              FROM
                  (
                      SELECT
                          t.*,
                          COUNT(*) OVER() result_count
                      FROM
                          TABLE ( example_pack.name_info_loop_func(:fromDate, :toDate) ) t
                  ) t
              WHERE
                  ROWNUM <= :size
          )
      WHERE
          row_index > :page
      """)
  List<Object[]> paginateNameInfoLoop(
      String fromDate,
      String toDate,
      int page,
      int size);

}
