package com.yiling.mall.openposition.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分页查询开屏位 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOpenPositionPageRequest extends QueryPageListRequest {

    /**
     * 平台：1-大运河 2-销售助手
     */
    @Eq
    private Integer platform;

}
