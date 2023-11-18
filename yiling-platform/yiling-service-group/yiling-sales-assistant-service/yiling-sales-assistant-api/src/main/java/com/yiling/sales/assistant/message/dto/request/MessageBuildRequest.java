package com.yiling.sales.assistant.message.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增消息
 * 
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageBuildRequest extends BaseRequest {
    /**
     * 用户id
     */
    private Long            userId;
    /**
     * 企业id
     */
    private Long            eid;
    /**
     * 消息来源：1-POP 2-销售助手 3-B2B
     */
    private SourceEnum      sourceEnum;
    /**
     * 消息角色：1-对用户 2-对企业
     */
    private MessageRoleEnum messageRoleEnum;
    /**
     * 业务进度的消息节点：10-待付款 11-待审核 12-待发货 13-部分发货 14-已发货 15-已收货 16-已完成 17-已取消 20-客户信息认证失败
     */
    private MessageNodeEnum messageNodeEnum;

}
