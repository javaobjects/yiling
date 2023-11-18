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
@Document(indexName = "#{@flowPurchaseEsIndex}",createIndex = false)
public class EsFlowPurchaseEntity extends EsBaseEntity {

    @Field(name = "city_code")
    private Long cityCode;

    @Field(name = "crm_code")
    private String  crmCode;

    @Field(name = "supplier_level")
    private Integer  supplierLevel;

    @Field(name = "crm_goods_code")
    private Long crmGoodsCode;

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

    @Field(name = "po_batch_no")
    private String poBatchNo;

    @Field(name = "po_license")
    private String poLicense;

    @Field(name = "po_manufacturer")
    private String poManufacturer;

    @Field(name = "po_no")
    private String poNo;

    @Field(name = "po_price")
    private float poPrice;

    @Field(name = "po_quantity")
    private Long poQuantity;

    @Field(name = "po_specifications")
    private String poSpecifications;

    @Field(name = "po_time")
    private Date poTime;

    @Field(name = "po_product_time")
    private Long poProductTime;

    @Field(name = "po_effective_time")
    private Long poEffectiveTime;

    @Field(name = "po_unit")
    private String poUnit;

    @Field(name = "specification_id")
    private Long specificationId;

    @Field(name = "del_flag")
    private Integer delFlag;

    @Field(name = "create_time")
    private Date createTime;

    @Field(name = "update_time")
    private Date updateTime;

    @Field(name = "data_tag")
    private Integer dataTag;
}
