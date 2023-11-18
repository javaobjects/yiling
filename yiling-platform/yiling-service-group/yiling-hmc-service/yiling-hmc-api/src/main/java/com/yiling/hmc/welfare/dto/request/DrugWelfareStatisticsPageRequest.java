package com.yiling.hmc.welfare.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class DrugWelfareStatisticsPageRequest extends QueryPageListRequest {

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
     * 销售人员id
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
     * 用户入组id
     */
    private Long joinGroupId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
