package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议全品表单 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AgreementAllProductFormVO extends AgreementDetailCommonFormVO implements Serializable {

    /**
     * 厂家名称
     */
    @ApiModelProperty("厂家名称")
    private String ename;

    /**
     * 当前商品数
     */
    @ApiModelProperty("当前商品数")
    private Long goodsNumber;

}
