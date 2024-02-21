package io.ngocnhan_tran1996.code.example.database.jdbc.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table
public class Author {

    @Id
    Integer id;
    String name;

}