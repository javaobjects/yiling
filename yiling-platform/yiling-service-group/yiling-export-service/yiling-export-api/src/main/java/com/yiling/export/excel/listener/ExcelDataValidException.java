package com.yiling.export.excel.listener;

import lombok.Getter;

@Getter
public class ExcelDataValidException extends RuntimeException {
    private Integer rowIndex;
    private Integer columnIndex;
    private String message;

    public ExcelDataValidException(Integer rowIndex, Integer columnIndex, String message) {
        super(message);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.message = message;
    }
}
