package com.yiling.f2b.admin.agreementv2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-3-4
 */
@Data
@ApiModel
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseItemVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

}
