package com.yiling.search.flow.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.yiling.framework.common.base.EsBaseEntity;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingEntity
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Data
@Document(indexName = "#{@flowEnterpriseGoodsMappingIndex}",createIndex = false)
public class EsFlowEnterpriseGoodsMappingEntity extends EsBaseEntity {

    /**
     * 流向原始名称
     */
    @Field(name = "flow_goods_name")
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    @Field(name = "flow_specification")
    private String flowSpecification;

    /**
     * 流向原始商品内码
     */
    @Field(name = "flow_goods_in_sn")
    private String flowGoodsInSn;

    /**
     * 流向原始商品厂家
     */
    @Field(name = "flow_manufacturer")
    private String flowManufacturer;

    /**
     * 流向原始商品厂家
     */
    @Field(name = "flow_unit")
    private String flowUnit;

    /**
     * crm商品编码
     */
    @Field(name = "crm_goods_code")
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    @Field(name = "goods_name")
    private String goodsName;

    /**
     * 标准商品规格
     */
    @Field(name = "goods_specification")
    private String goodsSpecification;

    /**
     * 经销商名称
     */
    @Field(name = "enterprise_name")
    private String enterpriseName;

    /**
     * 经销商编码
     */
    @Field(name = "crm_enterprise_id")
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    @Field(name = "province_code")
    private String provinceCode;

    /**
     * 省份
     */
    @Field(name = "province")
    private String province;
    /**
     * 转换单位：1-乘 2-除
     */
    @Field(name = "convert_unit")
    private Integer convertUnit;

    /**
     * 转换系数
     */
    @Field(name = "convert_number")
    private BigDecimal convertNumber;

    /**
     * 最后上传时间
     */
    @Field(name = "last_upload_time")
    private String lastUploadTime;

    @Field(name = "del_flag")
    private Integer delFlag;

    @Field(name = "create_time")
    private String createTime;

    @Field(name = "create_user")
    private Long createUser;

    @Field(name = "update_time")
    private String updateTime;

    @Field(name = "update_user")
    private Long updateUser;

}
