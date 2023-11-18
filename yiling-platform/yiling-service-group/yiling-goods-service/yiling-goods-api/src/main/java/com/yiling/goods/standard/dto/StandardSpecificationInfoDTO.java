package com.yiling.goods.standard.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

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
public class StandardSpecificationInfoDTO extends BaseDTO {

    /**
     * 标准库商品ID
     */
    private Long standardId;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;
    /**
     * 条形码
     */
    private String barcode;

    /**
     * YPID编码
     */
    private String ypidCode;

    /**
     * 图片信息
     */
    private List<StandardGoodsPicDTO> picInfoList;
}
