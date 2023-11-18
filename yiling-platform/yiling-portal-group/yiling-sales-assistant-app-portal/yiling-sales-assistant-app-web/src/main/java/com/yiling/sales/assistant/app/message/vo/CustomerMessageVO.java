package com.yiling.sales.assistant.app.message.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 业务进度-客户维度(客户名称+提交时间+审核失败)
 *
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@Accessors(chain = true)
public class CustomerMessageVO implements Serializable {

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "提交时间")
    private Date   createTime;
}
