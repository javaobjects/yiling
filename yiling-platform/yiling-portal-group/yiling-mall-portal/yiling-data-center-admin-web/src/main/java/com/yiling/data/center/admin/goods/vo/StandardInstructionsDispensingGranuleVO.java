package com.yiling.data.center.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 StandardInstructionsDispensingGranuleVO
 * @描述
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsDispensingGranuleVO extends BaseVO {

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
