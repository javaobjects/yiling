package com.yiling.marketing.promotion.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动列表查询
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialActivityAppointmentPageDTO extends BaseDTO {

    /**
     * 专场活动表id
     */
    private Long specialActivityId;

    /**
     * 专场活动企业表id
     */
    private Long specialActivityEnterpriseId;

    /**
     * 专场活动企业名称
     */
    private String specialActivityEnterpriseName;

    /**
     * 预约时间
     */
    private Date appointmentTime;

    /**
     * 预约人id
     */
    private Long appointmentUserId;

    /**
     * 预约人姓名
     */
    private String appointmentUserName;

    /**
     * 预约人联系方式
     */
    private String mobile;

    /**
     * 预约人所在企业
     */
    private String appointmentUserEnterpriseName;

    /**
     * 预约人所属企业id
     */
    private Long appointmentUserEid;

    /**
     * 专场活动名称
     */
    private String specialActivityName;

    /**
     * 专场活动开始时间
     */
    private Date startTime;

    /**
     * 专场活动结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 活动类型（1-满赠,2-特价,3-秒杀,4-组合包）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；3，未开始，4进行中，5已结束 根据时间判断）
     */
    private Integer status;

    /**
     * 活动进程（1未开始，2进行中，3已结束）
     */
    private Integer process;

}
