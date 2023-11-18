package com.yiling.sjms.wash.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleCustomerRangeVO extends BaseVO {

    /**
     * 范围类型：1省份2分类类型3关键词
     */
    @ApiModelProperty(value = "省份树对象")
    private LocationUnlockCustomerTreeVO locationUnlockCustomerTreeVO;

    @ApiModelProperty(value = "指定省份数量")
    private Integer provinceNumber;

    @ApiModelProperty(value = "客户分类集合")
    private List<Integer> classificationIds;

    @ApiModelProperty(value = "关键词集合")
    private String keywords;

}
