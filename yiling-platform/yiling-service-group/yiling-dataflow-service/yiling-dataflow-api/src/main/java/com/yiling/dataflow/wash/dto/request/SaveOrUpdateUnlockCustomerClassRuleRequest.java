package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveOrUpdateUnlockCustomerClassRuleRequest extends BaseRequest {

    private static final long serialVersionUID = -8966974988694497745L;

    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 排序序号 数值越小，排序越高
     */
    private Long orderNo;

    /**
     * 条件1 1-客户名称 2-客户名称结尾
     */
    private Integer condition1;

    /**
     * 运算符1 1-包含 2-不包含 3-等于
     */
    private Integer operator1;

    /**
     * 条件1的值
     */
    private String conditionValue1;

    /**
     * 条件2 1-客户名称 2-客户名称结尾
     */
    private Integer condition2;

    /**
     * 运算符2 1-包含 2-不包含 3-等于
     */
    private Integer operator2;

    /**
     * 条件2的值
     */
    private String conditionValue2;

    /**
     * 条件关系 1-且 2-或
     */
    private Integer conditionRelation;

    /**
     * 非锁终端分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;

    /**
     * 状态 1-开启 0-关闭
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
