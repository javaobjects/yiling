package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 创建拿药计划
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveInsuranceFetchPlanRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;


    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单支付记录表id
     */
    private Long recordPayId;

    /**
     * 初始拿药时间
     */
    private Date initFetchTime;

    /**
     * 实际拿药时间
     */
    private Date actualFetchTime;

    /**
     * 拿药状态 1-已拿，2-未拿
     */
    private Integer fetchStatus;
}
