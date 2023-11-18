package com.yiling.search.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.EsBaseEntity;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Data
public class EsFlowSaleDTO extends BaseDTO {

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

    private String soBatchNo;

    private String soLicense;

    private String soManufacturer;

    private String soNo;

    private float soPrice;

    private Long soQuantity;

    private String soSpecifications;

    private Date soTime;

    private String soUnit;

    private Long specificationId;

    private Date soProductTime;

    private Date soEffectiveTime;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private Integer dataTag;

    private Integer supplierLevel;

}
