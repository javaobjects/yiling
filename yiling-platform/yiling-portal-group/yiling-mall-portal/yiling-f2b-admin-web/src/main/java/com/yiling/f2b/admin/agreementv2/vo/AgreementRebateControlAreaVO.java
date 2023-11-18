package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

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
public class AgreementRebateControlAreaVO extends BaseVO {

    /**
     * 控销区域Json串
     */
    @ApiModelProperty("控销区域Json串")
    private String jsonContent;

    /**
     * 控销区域描述
     */
    @ApiModelProperty(value = "控销区域描述")
    private String description;

}
