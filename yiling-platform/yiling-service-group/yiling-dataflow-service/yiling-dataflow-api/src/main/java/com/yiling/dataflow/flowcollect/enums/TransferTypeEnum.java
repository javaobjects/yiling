package com.yiling.dataflow.flowcollect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 销量申诉传输类型
 * @author: xinxuan.jia
 * @date: 2023/6/29
 */
@Getter
@AllArgsConstructor
public enum TransferTypeEnum {

    // 传输类型：1、上传excel; 2、选择流向
    EXCEL_UPLOAD(1, "上传excel"),
    SELECT_APPEAL(2, "选择流向"),
    ;

    private Integer code;
    private String  desc;

    public static TransferTypeEnum getByCode(Integer code) {
        for(TransferTypeEnum e: TransferTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
