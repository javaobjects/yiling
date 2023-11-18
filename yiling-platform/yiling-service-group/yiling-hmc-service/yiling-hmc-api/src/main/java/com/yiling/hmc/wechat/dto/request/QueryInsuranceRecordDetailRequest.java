package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询保单详情
 *
 * @author fan.shen
 * @date 2022-4-6
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryInsuranceRecordDetailRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 保单id
     */
    private Long id;

    /**
     * 保单支付记录id
     */
    private Long recordPayId;


}
