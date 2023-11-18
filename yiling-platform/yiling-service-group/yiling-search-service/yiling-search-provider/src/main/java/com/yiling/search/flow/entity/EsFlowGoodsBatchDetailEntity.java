package com.yiling.search.flow.entity;

import java.util.Date;

import com.yiling.framework.common.base.EsBaseEntity;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Data
@Document(indexName = "#{@flowEsGoodsBatchDetailIndex}",createIndex = false)
public class EsFlowGoodsBatchDetailEntity extends EsBaseEntity {

    @Field(name = "city_code")
    private Long cityCode;

    @Field(name = "crm_code")
    private String  crmCode;

    @Field(name = "crm_goods_code")
    private Long crmGoodsCode;

    @Field(name = "supplier_level")
    private Integer  supplierLevel;

    @Field(name = "eid")
    private Long eid;

    @Field(name = "ename")
    private String ename;

    @Field(name = "enterprise_crm_code")
    private String enterpriseCrmCode;

    @Field(name = "gb_name")
    private String gbName;

    @Field(name = "province_code")
    private Long provinceCode;

    @Field(name = "region_code")
    private Long regionCode;

    @Field(name = "gb_batch_no")
    private String gbBatchNo;

    @Field(name = "gb_license")
    private String gbLicense;

    @Field(name = "gb_manufacturer")
    private String gbManufacturer;

    @Field(name = "gb_number")
    private Long gbNumber;

    @Field(name = "gb_specifications")
    private String gbSpecifications;

    @Field(name = "gb_detail_time")
    private Date gbDetailTime;

    @Field(name = "gb_unit")
    private String gbUnit;

    @Field(name = "gb_produce_time")
    private String gbProduceTime;

    @Field(name = "gb_end_time")
    private String gbEndTime;

    @Field(name = "specification_id")
    private Long specificationId;

    @Field(name = "del_flag")
    private Integer delFlag;

    @Field(name = "create_time")
    private Date createTime;

    @Field(name = "update_time")
    private Date updateTime;
}
