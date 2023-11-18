package com.yiling.f2b.admin.agreementv2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 甲方信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@Accessors(chain = true)
public class FirstPartyListItemVO {

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

    /**
     * 甲方/乙方类型 1-生产厂家 2-品牌厂家 3-商业公司
     */
    @ApiModelProperty("甲方/乙方类型 1-生产厂家 2-品牌厂家 3-商业公司")
    private Integer type;

}
