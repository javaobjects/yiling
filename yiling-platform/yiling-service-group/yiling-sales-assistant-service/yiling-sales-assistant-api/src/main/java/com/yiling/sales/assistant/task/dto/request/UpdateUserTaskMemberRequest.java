package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员推广任务记录
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
@Data
@Accessors(chain = true)
public class UpdateUserTaskMemberRequest implements Serializable {


    private static final long serialVersionUID = 7845235094969161173L;


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
     * 推广人
     */
    private Long  promoterUserId;
    /**
     * 购买会员
     */
    private Long memberId;

    /**
     * 会员购买条件表id
     */
    private Long memberStageId;


    private Long createUser;

    /**
     * 会员购买订单号
     */
    private String orderNo;

    /**
     * 推送任务类型：1-购买会员推送 2-运营后台修改推广人推送
     */
    private Integer pushType;
}
