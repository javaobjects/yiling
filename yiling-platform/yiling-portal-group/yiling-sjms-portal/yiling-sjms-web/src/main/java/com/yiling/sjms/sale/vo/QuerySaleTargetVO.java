package com.yiling.sjms.sale.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class QuerySaleTargetVO extends BaseVO {

    /**
     * 指标名称
     */
    @ApiModelProperty("指标名称")
    private String name;

    /**
     * 指标编号
     */
    @ApiModelProperty("指标编号")
    private String targetNo;

    /**
     * 指标年份
     */
    @ApiModelProperty("指标编号")
    private Long targetYear;

    /**
     * 销售目标金额
     */
    @ApiModelProperty("销售目标金额")
    private BigDecimal saleAmount;
    @ApiModelProperty("部门销售目标集合")
    private List<QuerySaleDepartmentTargetVO> departmentTargets;
    /**
     * 更新时间
     */
    @ApiModelProperty("最后更新时间")
    private Date updateTime;
}
