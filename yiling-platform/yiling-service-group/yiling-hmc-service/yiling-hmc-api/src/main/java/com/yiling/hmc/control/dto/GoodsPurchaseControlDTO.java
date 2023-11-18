package com.yiling.hmc.control.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class GoodsPurchaseControlDTO extends BaseDTO {

    private Long goodsControlId;


    /**
     * 供应商eid
     */
    private Long sellerEid;

    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;

    /**
     * 1-线上 2-线下
     */
    private Integer channelType;

    /**
     * 0-关闭 1-开启
     */
    private Integer controlStatus;

    /**
     * 此字段需要通过enterpriseApi关联查询
     */
   private String enterpriseName;

   private String licenseNumber;

   private Date createTime;
}
