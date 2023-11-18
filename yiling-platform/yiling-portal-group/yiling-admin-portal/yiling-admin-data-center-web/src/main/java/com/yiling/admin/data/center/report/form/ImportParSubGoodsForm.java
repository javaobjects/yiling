package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入参数 Form
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Data
public class ImportParSubGoodsForm implements IExcelModel, IExcelDataModel {


    @ExcelShow
    @Excel(name = "*促销名称")
    @NotEmpty
    private String activityName;

    //    @ExcelRepet
    @ExcelShow
    @Excel(name = "*商业ID")
    @NotNull(message = "不能为空")
    private Long eid;

    @ExcelShow
    @Excel(name = "商业名称")
    private String entName;

    @ExcelShow
    @Excel(name = "*以岭商品ID")
    @NotNull(message = "不能为空")
    private Long ylGoodsId;

    @ExcelShow
    @Excel(name = "以岭商品名称")
    private String goodsName;

    @Length(max = 50)
    @ExcelShow
    @Excel(name = "以岭商品规格")
    private String goodsSpec;

    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 0, message = "奖励金额不正确")
    @Excel(name = "*奖励金额")
    private BigDecimal price;

    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 1, max = 2, message = "金额类型不正确")
    @Excel(name = "*金额类型（1、元/盒，2、%销售额）")
    private Integer rewardType;

    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 0, max = 3, message = "金额类型不正确")
    @Excel(name = "*活动&阶梯的订单来源（0-全部，1-B2B，2-自建平台，3-三方平台及其他渠道订单）")
    private Integer orderSource;

    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 0,  message = "会员id不正确")
    @Excel(name = "*会员id（0-全部）")
    private Long memberId;

    /**
     * 开始时间
     */
    @NotEmpty
    @ExcelShow
    @Excel(name = "*开始时间（例：2022-7-11）")
    private String startTime;

    /**
     * 结束时间
     */
    @NotEmpty
    @ExcelShow
    @Excel(name = "*结束时间（例：2022-7-11）")
    private String endTime;

    /**
     * 该企业下的以岭品对应关系
     */
    private List<FlowGoodsRelationBO> goodsRelationList;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;
}
