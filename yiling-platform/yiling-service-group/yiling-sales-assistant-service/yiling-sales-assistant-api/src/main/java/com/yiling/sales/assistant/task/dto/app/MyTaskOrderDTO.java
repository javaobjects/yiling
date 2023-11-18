package com.yiling.sales.assistant.task.dto.app;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author gaoxinlei
 */
@Data
public class MyTaskOrderDTO extends BaseDTO {


    private String orderNo;

    private String terminalName;

    private Date orderTime;


}
