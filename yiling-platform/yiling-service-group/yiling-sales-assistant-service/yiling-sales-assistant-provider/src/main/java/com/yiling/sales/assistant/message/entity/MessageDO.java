package com.yiling.sales.assistant.message.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_message")
public class MessageDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long              userId;

    /**
     * 企业id
     */
    private Long              eid;

    /**
     * 消息来源：1-POP 2-销售助手 3-B2B
     */
    private Integer           source;

    /**
     * 消息角色：1-对用户 2-对企业
     */
    private Integer           role;

    /**
     * 业务类型，1-业务进度，2-发布任务，3-促销政策，4-学术下方
     */
    private Integer           type;

    /**
     * 业务进度的消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败
     */
    private Integer           node;

    /**
     * 是否已读：1-未读 2-已读
     */
    private Integer           isRead;

    /**
     * 消息数据内容：业务进度-订单维度(订单编号+下单时间+订单状态+提醒业务状态),业务进度-客户维度(客户名称+提交时间+审核失败)
     */
    private String            code;

    /**
     * 消息内容
     */
    private String            content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date              createTime;

    /**
     * 阅读时间
     */
    private Date              readTime;

}
