package com.yiling.sales.assistant.message.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息表实体
 *
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageDTO extends BaseDTO {
    /**
     * 用户id
     */
    private Long    userId;

    /**
     * 企业id
     */
    private Long    eid;

    /**
     * 消息来源：1-POP 2-销售助手 3-B2B
     */
    private Integer source;

    /**
     * 消息角色：1-对用户 2-对企业
     */
    private Integer role;

    /**
     * 业务类型，1-业务进度，2-发布任务，3-促销政策，4-学术下方
     */
    private Integer type;

    /**
     * 消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败
     */
    private Integer node;

    /**
     * 是否已读：1-未读 2-已读
     */
    private Integer isRead;

    /**
     * 编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号
     */
    private String  code;

    /**
     * 消息内容
     */
    private String  content;

    /**
     * 创建时间
     */
    private Date    createTime;

    /**
     * 阅读时间
     */
    private Date    readTime;
}
