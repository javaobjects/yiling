package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockCustomerClassRuleVO extends BaseVO {

    /**
     * 规则ID
     */
    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    /**
     * 排序序号 数值越小，排序越高
     */
    @ApiModelProperty(value = "排序")
    private Long orderNo;

    /**
     * 条件1 1-客户名称 2-客户名称结尾
     */
    @ApiModelProperty(value = "条件1 1-客户名称 2-客户名称结尾")
    private Integer condition1;

    /**
     * 运算符1 1-包含 2-不包含 3-等于
     */
    @ApiModelProperty(value = "运算符1 1-包含 2-不包含 3-等于")
    private Integer operator1;

    /**
     * 条件1的值
     */
    @ApiModelProperty(value = "条件1的值")
    private String conditionValue1;

    /**
     * 条件2 1-客户名称 2-客户名称结尾
     */
    @ApiModelProperty(value = "条件2 1-客户名称 2-客户名称结尾")
    private Integer condition2;

    /**
     * 运算符2 1-包含 2-不包含 3-等于
     */
    @ApiModelProperty(value = "运算符2 1-包含 2-不包含 3-等于")
    private Integer operator2;

    /**
     * 条件2的值
     */
    @ApiModelProperty(value = "条件2的值")
    private String conditionValue2;

    /**
     * 条件关系 1-且 2-或
     */
    @ApiModelProperty(value = "条件关系 1-且 2-或")
    private Integer conditionRelation;

    /**
     * 非锁终端分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    @ApiModelProperty(value = "非锁客户分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构")
    private Integer customerClassification;

    /**
     * 状态 1-开启 0-关闭
     */
    @ApiModelProperty(value = "状态 1-开启 0-关闭")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
