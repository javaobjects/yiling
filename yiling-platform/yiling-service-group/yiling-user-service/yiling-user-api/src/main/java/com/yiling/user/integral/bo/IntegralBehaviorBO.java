package com.yiling.user.integral.bo;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分行为 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralBehaviorBO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 行为名称
     */
    private String name;

    /**
     * 行为描述
     */
    private String description;

    /**
     * 图标
     */
    private String icon;

    /**
     * 行为类型：1-发放 2-消耗
     */
    private Integer type;

    /**
     * 适用平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手 10-所有业务线通用
     */
    private List<Integer> platformList;

}
