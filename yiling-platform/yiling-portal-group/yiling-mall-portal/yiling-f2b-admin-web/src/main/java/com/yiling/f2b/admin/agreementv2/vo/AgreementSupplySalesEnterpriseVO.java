package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销指定商业公司 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesEnterpriseVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String ename;

}
