package com.yiling.goods.medicine.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品sku
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsSkuRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 公司Id
     */
    private Long eid;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 包装数量
     */
    private Long packageNumber;

    /**
     * 商品产品线
     */
    private Integer goodsLine;

    /**
     * ERP内码
     */
    private String inSn;

    /**
     * ERP编码
     */
    private String sn;

    /**
     * 状态0正常 1停用 2隐藏
     */
    private Integer status;

    /**
     * sku属性扩展字段
     */
    private String extensionField;

    /**
     * 库存
     */
    private Long qty;

    /**
     * 批号
     */
    private String batchNumber;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 备注
     */
    private String remark;


}
