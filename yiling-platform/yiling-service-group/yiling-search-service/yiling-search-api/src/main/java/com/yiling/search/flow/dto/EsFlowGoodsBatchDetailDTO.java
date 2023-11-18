package com.yiling.search.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Data
public class EsFlowGoodsBatchDetailDTO extends BaseDTO {

    private Long cityCode;

    private String  crmCode;

    private Long crmGoodsCode;

    private Long eid;

    private String ename;

    private String enterpriseCrmCode;

    private String gbName;

    private Long provinceCode;

    private Long regionCode;

    private String gbBatchNo;

    private String gbLicense;

    private String gbManufacturer;

    private Long gbNumber;

    private String gbSpecifications;

    private Date gbDetailTime;

    private String gbUnit;

    private String gbProduceTime;

    private String gbEndTime;

    private Long specificationId;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private Integer supplierLevel;
}
