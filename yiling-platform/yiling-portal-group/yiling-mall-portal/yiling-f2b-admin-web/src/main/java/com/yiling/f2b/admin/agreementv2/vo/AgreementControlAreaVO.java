package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议控销区域 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementControlAreaVO extends BaseVO {

    /**
     * 控销区域Json串
     */
    @ApiModelProperty(value = "控销区域Json串")
    private String jsonContent;

    /**
     * 控销区域描述
     */
    @ApiModelProperty(value = "控销区域描述")
    private String description;

}
