package com.yiling.hmc.admin.employee.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 药品福利二维码 VO
 *
 * @author: fan.shen
 * @date: 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareQrCodeVO extends BaseVO {

    @ApiModelProperty("二维码路径")
    private String url;

    @ApiModelProperty("福利名称")
    private String name;

    /**
     * 药品规格id
     */
    @ApiModelProperty("药品规格id")
    private Long sellSpecificationsId;

    /**
     * 药品规格描述
     */
    @ApiModelProperty("药品规格描述")
    private String sellSpecifications;


}
