package com.yiling.goods.standard.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsInfoRequest  extends QueryPageListRequest {

    private static final long serialVersionUID = -33371030428332221L;

    private Long standardId;

    private List<Long> standardIds;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAddress;


    /**
     * 分类名称1
     */
    private Long standardCategoryId1;

    /**
     * 分类名称2
     */
    private Long standardCategoryId2;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 多个类型查询
     */
    private List<Integer> goodsTypes;

    /**
     * 处方类型：1处方药 2甲类非处方药 3乙类非处方药 4其他
     */
    private Integer otcType;

    /**
     * 是否医保：1是 2非 3未采集到相关信息
     */
    private Integer isYb;

    /**
     * 管制类型：0非管制 1管制
     */
    private Integer controlType;


    /**
     * 特殊成分：0不含麻黄碱 1含麻黄碱
     */
    private Integer specialComposition;

    /**
     * 有无图片 0 有 1无
     */
    private Integer pictureFlag;

    /**
     * 查询是否以岭品 1：查询以岭品
     */
    private Integer ylFlag;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

}
