package com.yiling.user.shop.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * <p>
 * B2B-保存店铺楼层 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class SaveShopFloorRequest extends BaseRequest {

    /**
     * ID（编辑时存在）
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 楼层名称
     */
    private String name;

    /**
     * 楼层状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 权重值
     */
    private Integer sort;

    /**
     * 店铺商品信息
     */
    private List<SaveShopGoodsRequest> shopGoodsList;

    @Data
    public static class SaveShopGoodsRequest implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 商品ID
         */
        private Long goodsId;

        /**
         * 排序值
         */
        private Integer sort;

    }

}
