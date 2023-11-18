package com.yiling.user.agreementv2.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利范围控销区域 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateControlAreaBO extends BaseVO implements Serializable {

    /**
     * 控销区域Json串
     */
    @ApiModelProperty("控销区域Json串")
    private String jsonContent;

    /**
     * 控销区域描述
     */
    @ApiModelProperty("控销区域描述")
    private String description;

}
