package com.yiling.dataflow.crm.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationBO
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
public class CrmGoodsTagRelationBO implements Serializable {
    /**
     * 标签关联主键
     */
    private Long id;

    /**
     * 标签id
      */
    private Long tagId;

    /**
     * 类型 1：非锁标签  2：团购标签
     */
    private Integer tagType;


    /**
     * 标签名称
     */
    private String tagName;

    /**
     * crm商品id
     */
    private Long crmGoodsId;

    /**
     * crm商品名称
     */
    private String crmGoodsName;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品规格
     */
    private String crmGoodsSpec;

    /**
     * crm商品品类id
     */
    private Long crmGoodsCategoryId;
}
