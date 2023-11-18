package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

	/**
	 * 企业id
	 */
	private List<Long> eidList;

    /**
     * 商品Id
     */
    private Long goodsId;

	/**
	 * 标准库ID
	 */
	private Long standardId;

	/**
	 * 注册证号(右模糊搜索）
	 */
	private String licenseNo;

	/**
	 * 生产厂家(全模糊搜索）
	 */
	private String manufacturer;

	/**
	 * 商品名称(全模糊搜索）
	 */
	private String name;

	/**
	 * 一级分类id
	 */
	private Long standardCategoryId1;

	/**
	 * 二级分类id
	 */
	private Long standardCategoryId2;

	/**
	 * 销售规格(全模糊搜索）
	 */
	private String sellSpecifications;

    /**
     * 销售规格ID
     */
    private Long sellSpecificationsId;

	/**
	 * 商品状态0全部：1上架 2下架 3待设置
	 */
	private Integer goodsStatus;

    /**
     * 多个商品状态
     */
    private List<Integer> goodsStatusList;

    /**
     * 商品状态：4审核通过6审核不通 5待审核
     */
    private Integer auditStatus;

    /**
     * 是否查询默认图片0查询1是不查询
     */
    private Integer isShowDefaultPic;

    /**
	 * 下架原因
	 */
	private Integer outReason;

	/**
	 * erp内码
	 */
	private String sn;

    /**
     * erp内码
     */
    private String inSn;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 产品线
     */
    private Integer goodsLine;

    /**
     * 是否以岭商品 0-全部 1-以岭 2-非以岭
     */
    private Integer yilingGoodsFlag;

	/**
	 * 是否超卖商品 0-非超卖  1-超卖
	 */
	private Integer overSoldType;

    /**
     * 可用库存是否大于零 0全部库存 1可用库存大于0
     */
    private Integer isAvailableQty;

    /**
     * 可销售的商业公司
     */
    private List<Long> includeEids;

    /**
     * 可销售的销售规格ID集合
     */
    private List<Long> includeSellSpecificationsIds;

    /**
     * 采购商ID
     */
    private Long buyerEid;
}
