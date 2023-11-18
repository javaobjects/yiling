package com.yiling.admin.data.center.standard.vo;

import java.util.List;

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
public class StandardSpecificationInfoVO extends BaseVO {

    /**
     * 标准库商品ID
     */
    @ApiModelProperty(value = "标准库商品ID")
    private Long standardId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String sellSpecifications;

    /**
     * YPID编码
     */
    @ApiModelProperty(value = "YPID编码")
    private String ypidCode;

    /**
     * 条形码
     */
    @ApiModelProperty(value = "条形码")
    private String barcode;

    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private List<StandardGoodsPicVO> picInfoList;
}
