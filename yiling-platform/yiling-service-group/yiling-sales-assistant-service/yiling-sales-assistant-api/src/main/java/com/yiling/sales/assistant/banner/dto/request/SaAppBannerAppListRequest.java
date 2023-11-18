package com.yiling.sales.assistant.banner.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手移动端查询请求参数
 * 
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaAppBannerAppListRequest extends BaseRequest {

    /**
     * 是否是以岭内部员工
     */
    private Boolean internalEmployeeFlag;

    /**
     * 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
     */
    private Integer usageScenario;

    /**
     * 需要展示的数量
     */
    private Integer count;
}
