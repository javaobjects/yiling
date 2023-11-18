package com.yiling.hmc.welfare.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 *
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 参与用户id
     */
    private Long userId;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 销售人员id
     */
    private Long sellerUserId;

    /**
     * 用药人姓名
     */
    private String medicineUserName;

    /**
     * 用药人手机号
     */
    private String medicineUserPhone;

    /**
     * 入组id
     */
    private Long joinGroupId;

}
