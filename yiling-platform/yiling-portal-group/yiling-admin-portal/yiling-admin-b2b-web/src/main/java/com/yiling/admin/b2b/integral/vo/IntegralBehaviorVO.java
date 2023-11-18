package com.yiling.admin.b2b.integral.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分行为 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralBehaviorVO extends BaseVO {

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String name;

    /**
     * 行为描述
     */
    @ApiModelProperty("行为描述")
    private String description;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 适用平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手 10-所有业务线通用
     */
    @ApiModelProperty("适用平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手 10-所有业务线通用")
    private List<Integer> platformList;

}
