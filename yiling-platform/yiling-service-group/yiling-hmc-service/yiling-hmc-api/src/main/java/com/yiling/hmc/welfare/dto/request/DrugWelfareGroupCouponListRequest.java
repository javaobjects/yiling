package com.yiling.hmc.welfare.dto.request;

import java.util.Date;
import java.util.List;

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
public class DrugWelfareGroupCouponListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1L;

    /**
     * idList
     */
    private List<Long> idList;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;


    /**
     * 使用状态 1-待激活，2-已激活，3-已核销
     */
    private Integer couponStatus;


}
