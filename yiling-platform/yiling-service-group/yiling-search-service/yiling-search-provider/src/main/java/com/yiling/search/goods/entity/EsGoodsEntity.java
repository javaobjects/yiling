package com.yiling.search.goods.entity;

import java.math.BigDecimal;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.TermVector;

import com.yiling.framework.common.base.EsBaseEntity;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsGoodsEntity
 * @描述
 * @创建时间 2022/8/18
 * @修改人 shichen
 * @修改时间 2022/8/18
 **/
@Data
@Document(indexName = "#{@goodsEsIndex}",createIndex = false)
@Setting(settingPath = "setting/goods_setting.json")
@Mapping(mappingPath = "mapping/goods_mapping.json") //@Mapping和@Field同时存在 优先使用@Mapping配置
public class EsGoodsEntity extends EsBaseEntity {

    @Field(name = "combination_key",type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String combinationKey;

    @MultiField(
            mainField = @Field(type = FieldType.Text,copyTo="combination_key",analyzer = "ik_max_word",searchAnalyzer = "ik_max_synonym"),
            otherFields = {@InnerField(suffix="name_keyword",type = FieldType.Keyword,ignoreAbove=256),
            @InnerField(suffix="pinyin",type = FieldType.Text,termVector = TermVector.with_offsets,analyzer = "pinyin_analyzer")}
    )
    private String name;

    @MultiField(
            mainField = @Field(name = "common_name",type = FieldType.Text,copyTo="combination_key",analyzer = "ik_max_word",searchAnalyzer = "ik_max_synonym"),
            otherFields = @InnerField(suffix="pinyin",type = FieldType.Text,termVector = TermVector.with_offsets,analyzer = "pinyin_analyzer")
    )
    private String commonName;

    @Field(name = "standard_id",type = FieldType.Long)
    private Long standardId;

    @Field(name = "sell_specifications_id",type = FieldType.Long)
    private Long sellSpecificationsId;

    @Field(name = "sell_specifications",type = FieldType.Text)
    private String sellSpecifications;

    @Field(type = FieldType.Long)
    private Long eid;

    @MultiField(
            mainField = @Field(type = FieldType.Text,copyTo="combination_key",analyzer = "ik_max_word",searchAnalyzer = "ik_smart"),
            otherFields = {@InnerField(suffix="ename_keyword",type = FieldType.Keyword,ignoreAbove=256),
                    @InnerField(suffix="pinyin",type = FieldType.Text,termVector = TermVector.with_offsets,analyzer = "pinyin_analyzer")}
    )
    private String ename;

    @Field(name = "license_no",type = FieldType.Text)
    private String licenseNo;

    @MultiField(
            mainField = @Field(type = FieldType.Text,copyTo="combination_key",analyzer = "ik_max_word",searchAnalyzer = "ik_smart"),
            otherFields = {@InnerField(suffix="manufacturer_keyword",type = FieldType.Keyword,ignoreAbove=256),
                    @InnerField(suffix="pinyin",type = FieldType.Text,termVector = TermVector.with_offsets,analyzer = "pinyin_analyzer")}
    )
    private String manufacturer;

    @Field(type = FieldType.Float)
    private BigDecimal price;

    @Field(name = "audit_status",type = FieldType.Long)
    private Integer auditStatus;

    @Field(name = "enterprise_type",type = FieldType.Long)
    private Integer enterpriseType;

    @Field(name = "mall_flag",type = FieldType.Long)
    private Integer mallFlag;

    @Field(name = "pop_flag",type = FieldType.Long)
    private Integer popFlag;

    @Field(name = "gdf_name",type = FieldType.Text)
    private String gdfName;

    @Field(name = "has_b2b_stock",type = FieldType.Integer)
    private Integer hasB2bStock;

}
