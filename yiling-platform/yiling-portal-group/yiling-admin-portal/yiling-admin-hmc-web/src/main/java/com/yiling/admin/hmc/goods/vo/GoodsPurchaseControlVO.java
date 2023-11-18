package com.yiling.admin.hmc.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 药品进货渠道管控
 * </p>
 *
 * @author gxl
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPurchaseControlVO extends BaseVO {





    /**
     * 1-线上 2-线下
     */
    @ApiModelProperty(value = "渠道类型：1-线上 2-线下")
    private Integer channelType;

    /**
     * 0-关闭 1-开启
     */
    @ApiModelProperty(value = "进货渠道管控状态：0-关闭 1-开启")
    private Integer controlStatus;

    @ApiModelProperty(value = "名称")
   private String enterpriseName;

    @ApiModelProperty(value = "机构代码")
   private String licenseNumber;

    private Date createTime;
}
