package com.yiling.sales.assistant.app.message.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageVO extends BaseVO {

    @ApiModelProperty(value = "用户id")
    private Long              userId;

    @ApiModelProperty(value = "企业id")
    private Long              eid;

    @ApiModelProperty(value = "消息来源：1-POP 2-销售助手 3-B2B")
    private Integer           source;

    @ApiModelProperty(value = "消息角色：1-对用户 2-对企业")
    private Integer           role;

    @ApiModelProperty(value = "业务类型，1-业务进度，2-发布任务，3-促销政策，4-学术下方")
    private Integer           type;

    @ApiModelProperty(value = "业务进度的消息节点：10-待付款 11-待审核 12-待发货 13-部分发货 14-已发货 15-已收货 16-已完成 17-已取消 20-客户信息认证失败")
    private Integer           node;

    @ApiModelProperty(value = "是否已读：1-未读 2-已读")
    private Integer           isRead;

    @ApiModelProperty(value = "编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号")
    private String            code;

    @ApiModelProperty(value = "消息数据内容：业务进度-订单维度(订单编号+下单时间+订单状态+提醒业务状态),业务进度-客户维度(客户名称+提交时间+审核失败)")
    private String            content;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

    @ApiModelProperty(value = "阅读时间")
    private Date              readTime;

    @ApiModelProperty(value = "业务进度-客户维度消息")
    private CustomerMessageVO customerMessageVO;

    @ApiModelProperty(value = "业务进度-订单维度消息")
    private OrderMessageVO    orderMessageVO;
}
