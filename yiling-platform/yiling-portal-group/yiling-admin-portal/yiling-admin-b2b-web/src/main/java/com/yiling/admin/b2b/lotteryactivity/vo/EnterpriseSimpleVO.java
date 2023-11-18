package com.yiling.admin.b2b.lotteryactivity.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业信息 VO
 *
 * @author: lun.yu
 * @date: 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSimpleVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

}
