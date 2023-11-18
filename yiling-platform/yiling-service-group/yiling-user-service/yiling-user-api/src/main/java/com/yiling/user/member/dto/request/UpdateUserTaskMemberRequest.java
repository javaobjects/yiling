package com.yiling.user.member.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员推广任务推送 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-07-11
 */
@Data
@Accessors(chain = true)
public class UpdateUserTaskMemberRequest implements Serializable {


    private static final long serialVersionUID = 7845235094969161173L;

    /**
     * 会员订单号
     */
    private String orderNo;

    /**
     * 推送任务类型：1-购买会员推送 2-运营后台修改推广人推送
     */
    private Integer pushType;

    /**
     * 终端id
     */
    private Long eid;

    /**
     * 终端名称
     */
    private String ename;

    /**
     * 联系人
     */
    private String contactorPhone;

    /**
     * 联系人id
     */
    private Long contactorUserId;

    /**
     * 购买时间
     */
    private Date tradeTime;

    /**
     * 推广方id
     */
    private Long promoterId;

    /**
     * 推广用户ID
     */
    private Long promoterUserId;

    /**
     * 购买会员
     */
    private Long memberId;

    /**
     * 会员购买条件表id
     */
    private Long memberStageId;

    /**
     * 创建人
     */
    private Long createUser;
}
