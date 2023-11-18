package com.yiling.open.cms.contract.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrowUpForm extends BaseForm {

    /**
     * 品规
     */
    private String goodsSpec;

    /**
     * 第一季度
     */
    private String quarter1;

    /**
     * 第二季度
     */
    private String quarter2;

    /**
     * 第三季度
     */
    private String quarter3;

    /**
     * 第四季度
     */
    private String quarter4;

    /**
     * 全年合计
     */
    private String quarterTotal;
}
