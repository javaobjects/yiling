package com.yiling.sjms.flow.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowBalanceStatisticsVO extends BaseVO {

    /**
     * 经销商编码
     */
    @ApiModelProperty("经销商编码")
    private Long crmEnterpriseId;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String ename;

    /**
     * 所属日期
     */
    @ApiModelProperty(value = "所属日期")
    private Date dateTime;

    /**
     * 经销商级别
     */
    @ApiModelProperty(value = "经销商级别")
    private String enameLevel;

    private Integer supplierLevel;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private Long poRowNumber;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private Long soRowNumber;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Long gbRowNumber;

    /**
     * 流向搜集方式
     */
    @ApiModelProperty(value = "流向搜集方式")
    private Integer flowMode;

    /**
     * 收集日期
     */
    @ApiModelProperty(value = "收集日期")
    private Date collectTime;
}
