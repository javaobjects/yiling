package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wei.wang
 * @date: 2022-11-03
 */
@Data
public class B2BOrderGoodsLagsVO extends BaseVO  {

    /**
     * 名称
     */
    @ApiModelProperty("成交金额")
    private String name;



}
