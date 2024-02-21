package io.ngocnhan_tran1996.code.example.database.dto;

import io.ngocnhan_tran1996.code.example.database.support.oracle.annotation.OracleColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NamePrefixInput {

    @OracleColumn("first_name")
    private String name;
    private String lastName;

}