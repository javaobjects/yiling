package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OSS Bucket 枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@Getter
@AllArgsConstructor
public enum CommandStatusEnum {
    def("0", "默认"),
    ser_send("1", "服务端发任务"),
    client_complete("2", "客户端任务完成"),
    ;

    private String code;
    private String name;


}
