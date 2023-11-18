package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询我的参保
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryInsuranceRecordPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 人名或者手机号
     */
    private String nameOrPhone;

    /**
     * 用户ID
     */
    private Long currentUserId;


}
