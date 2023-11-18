package com.yiling.open.erp.dto;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/5/20
 */
@Data
public class ClientRequestDTO {

    private String trace;

    private String taskNo;

    private Long suId;

    private Integer status;

    private String message;
}
