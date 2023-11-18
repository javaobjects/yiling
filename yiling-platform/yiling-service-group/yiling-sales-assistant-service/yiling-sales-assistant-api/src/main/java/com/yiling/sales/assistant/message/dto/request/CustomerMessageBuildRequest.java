package com.yiling.sales.assistant.message.dto.request;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 业务进度-客户维度(客户名称+提交时间+审核失败)
 *
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerMessageBuildRequest extends MessageBuildRequest {
    /**
     * 编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号
     */
    private String code;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 提交时间
     */
    private Date   createTime;
}
