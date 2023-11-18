package com.yiling.sales.assistant.app.search.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppStandardGoodsVO extends BaseVO {

    /**
     * 生产地址
     */
    @ApiModelProperty("生产地址")
    private String manufacturerAddress;

    /**
     * 处方类型
     */
    @ApiModelProperty("处方类型")
    private Integer otcType;

    /**
     * 剂型名称
     */
    @ApiModelProperty("剂型名称")
    private String gdfName;

    /**
     * 生产
     */
    @ApiModelProperty("生产")
    private Integer isCn;

    /**
     * 保质期
     */
    @ApiModelProperty("保质期")
    private String expirationDate;
}
