package com.yiling.sjms.gb.vo;

import java.util.Date;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团购取消列表信息
 */
@Data
public class GbCancelFormInfoListVO extends BaseVO {
    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private Date month;

    /**
     * 团购单位
     */
    @ApiModelProperty(value = "团购单位")
    private String customerName;

    /**
     * 团购出库终端名称
     */
    @ApiModelProperty(value = "团购出库终端名称")
    private String termainalCompanyName;

    /**
     * 团购出库商业
     */
    @ApiModelProperty(value = "团购出库商业")
    private String businessCompanyName;

}
