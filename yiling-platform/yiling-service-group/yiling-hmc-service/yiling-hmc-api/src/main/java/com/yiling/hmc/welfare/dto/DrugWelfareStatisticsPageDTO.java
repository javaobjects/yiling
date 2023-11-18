package com.yiling.hmc.welfare.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareStatisticsPageDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 福利计划id
     */
    private Long drugWelfareId;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家销售人员id
     */
    private Long sellerUserId;

    /**
     * 服药人姓名
     */
    private String medicineUserName;

    /**
     * 服药人手机号
     */
    private String medicineUserPhone;

    /**
     * 入组id
     */
    private Long joinGroupId;

    /**
     * 入组时间
     */
    private Date createTime;

}
