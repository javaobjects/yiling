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
@Document(indexName = "#{@flowSaleEsIndex}",createIndex = false)
public class  EsFlowSaleEntity extends EsBaseEntity {

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

    @Field(name = "enterprise_name")
    private String enterpriseName;

    @Field(name = "goods_name")
    private String goodsName;

    @Field(name = "province_code")
    private Long provinceCode;

    @Field(name = "region_code")
    private Long regionCode;

    @Field(name = "so_batch_no")
    private String soBatchNo;

    @Field(name = "so_license")
    private String soLicense;

    @Field(name = "so_manufacturer")
    private String soManufacturer;

    @Field(name = "so_no")
    private String soNo;

    @Field(name = "so_price")
    private float soPrice;

    @Field(name = "so_quantity")
    private Long soQuantity;

    @Field(name = "so_specifications")
    private String soSpecifications;

    @Field(name = "so_time")
    private Date soTime;

    @Field(name = "so_unit")
    private String soUnit;

    @Field(name = "specification_id")
    private Long specificationId;

    @Field(name = "so_product_time")
    private Long soProductTime;

    @Field(name = "so_effective_time")
    private Long soEffectiveTime;

    @Field(name = "del_flag")
    private Integer delFlag;

    @Field(name = "create_time")
    private Date createTime;

    @Field(name = "update_time")
    private Date updateTime;

    @Field(name = "data_tag")
    private Integer dataTag;
}
