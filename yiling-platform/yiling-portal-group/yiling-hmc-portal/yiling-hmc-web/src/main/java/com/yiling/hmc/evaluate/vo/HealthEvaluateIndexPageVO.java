package com.yiling.hmc.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 健康测评首页
 * @author: fan.shen
 * @date: 2022/12-20
 */
@Data
@Accessors(chain = true)
public class HealthEvaluateIndexPageVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty(value = "健康")
    private List<HealthEvaluateVO> healthList;

    @ApiModelProperty(value = "心理")
    private List<HealthEvaluateVO> psychologyList;


}
