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
public class IncentiveStandardForm extends BaseForm {

    /**
     * 品规
     */
    private String goodsSpec;

    /**
     * 基础服务费标准
     */
    private String baseStandard;

    /**
     * 目标达成奖励标准
     */
    private String projectStandard;

    /**
     * 目标达成奖励标准
     */
    private String completeStandard;

    /**
     * 推荐级别
     */
    private String level;
}
