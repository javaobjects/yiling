package com.yiling.f2b.admin.agreementv2.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementManufacturerVO extends BaseVO {

    /**
     * 厂家编号
     */
    @ApiModelProperty("厂家编号")
    private Long eid;

    /**
     * 厂家名称
     */
    @ApiModelProperty("厂家名称")
    private String ename;

    /**
     * 厂家类型 1-生产厂家 2-品牌厂家
     */
    @ApiModelProperty("厂家类型 1-生产厂家 2-品牌厂家")
    private Integer type;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createUser;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
