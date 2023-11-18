package com.yiling.b2b.app.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分说明配置 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralInstructionConfigVO extends BaseVO {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @ApiModelProperty("平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手")
    private Integer platform;

    /**
     * 说明内容
     */
    @ApiModelProperty("说明内容")
    private String content;

}
