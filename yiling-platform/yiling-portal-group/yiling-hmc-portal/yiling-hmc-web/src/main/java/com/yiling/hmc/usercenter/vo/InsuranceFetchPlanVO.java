package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 拿药计划 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceFetchPlanVO extends BaseVO {

    private static final long serialVersionUID = 1L;


    /**
     * 参保记录id
     */
    @ApiModelProperty("参保记录id")
    private Long insuranceRecordId;

    /**
     * 初始拿药时间
     */
    @ApiModelProperty("初始拿药时间")
    private Date initFetchTime;

    /**
     * 实际拿药时间
     */
    @ApiModelProperty("实际拿药时间")
    private Date actualFetchTime;

    /**
     * 拿药状态 1-已拿，2-未拿
     */
    @ApiModelProperty("拿药状态 1-已拿，2-未拿")
    private Integer fetchStatus;

}
