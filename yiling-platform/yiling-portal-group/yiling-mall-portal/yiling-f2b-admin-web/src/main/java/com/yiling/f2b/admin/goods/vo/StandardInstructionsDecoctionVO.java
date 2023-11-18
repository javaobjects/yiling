package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsDecoctionVO extends BaseVO {

    /**
     * 净含量
     */
    @ApiModelProperty(value = "净含量")
    private String netContent;

    /**
     * 原产地
     */
    @ApiModelProperty(value = "原产地")
    private String sourceArea;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String expirationDate;

    /**
     * 包装清单
     */
    @ApiModelProperty(value = "包装清单")
    private String packingList;

    /**
     * 用法与用量
     */
    @ApiModelProperty(value = "用法与用量")
    private String usageDosage;

    /**
     * 储藏
     */
    @ApiModelProperty(value = "储藏")
    private String store;
}
