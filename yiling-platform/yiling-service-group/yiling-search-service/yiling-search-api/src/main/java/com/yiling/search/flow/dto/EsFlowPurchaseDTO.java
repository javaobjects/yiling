package com.yiling.search.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Data
public class EsFlowPurchaseDTO extends BaseDTO {

    private Long cityCode;

    private String  crmCode;

    private Long crmGoodsCode;

    private Long eid;

    private String ename;

    private String enterpriseCrmCode;

    private String enterpriseName;

    private String goodsName;

    private Long provinceCode;

    private Long regionCode;

    private String poBatchNo;

    private String poLicense;

    private String poManufacturer;

    private String poNo;

    private float poPrice;

    private Long poQuantity;

    private String poSpecifications;

    private Date poTime;

    private String poUnit;

    private Long specificationId;

    private Date poProductTime;

    private Date poEffectiveTime;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private Integer dataTag;

    private Integer supplierLevel;

}
