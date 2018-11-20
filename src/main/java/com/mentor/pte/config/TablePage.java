package com.mentor.pte.config;

import lombok.Data;

import java.util.List;

@Data
public class TablePage {

    public Long total;
    public List<?> rows;

    public static TablePage of(Long total,List rows){
        TablePage page =new TablePage();
        page.setTotal(total);
        page.setRows(rows);
        return page;
    }

}
