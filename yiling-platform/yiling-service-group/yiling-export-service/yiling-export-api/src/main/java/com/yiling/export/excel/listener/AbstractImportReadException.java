package com.yiling.export.excel.listener;

import lombok.Getter;

@Getter
public class AbstractImportReadException extends RuntimeException {
    private String message;

    public AbstractImportReadException(String message) {
        super(message);
        this.message = message;
    }
}
