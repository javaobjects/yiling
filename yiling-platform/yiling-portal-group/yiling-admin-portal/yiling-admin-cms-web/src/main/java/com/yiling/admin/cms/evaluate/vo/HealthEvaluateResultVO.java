package com.yiling.admin.cms.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评结果
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateResultVO extends BaseVO {

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 结果排序
     */
    @ApiModelProperty(value = "结果排序")
    private Integer resultRank;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    @ApiModelProperty(value = "分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于")
    private Integer scoreStartType;

    /**
     * 开始区间
     */
    @ApiModelProperty(value = "开始区间")
    private BigDecimal scoreStart;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    @ApiModelProperty(value = "分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于")
    private Integer scoreEndType;

    /**
     * 结束区间
     */
    @ApiModelProperty(value = "结束区间")
    private BigDecimal scoreEnd;

    /**
     * 测评结果
     */
    @ApiModelProperty(value = "测评结果")
    private String evaluateResult;

    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述")
    private String resultDesc;

    /**
     * 健康小贴士
     */
    @ApiModelProperty(value = "健康小贴士")
    private String healthTip;

}
