package com.yiling.basic.contract.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrowUpRequest extends BaseRequest {

    private static final long serialVersionUID = -2229446169815101326L;

    /**
     * 品规
     */
    private String goodsSpec;

    /**
     * 第一季度
     */
    private String quarter1;

    /**
     * 第二季度
     */
    private String quarter2;

    /**
     * 第三季度
     */
    private String quarter3;

    /**
     * 第四季度
     */
    private String quarter4;

    /**
     * 全年合计
     */
    private String quarterTotal;
}
