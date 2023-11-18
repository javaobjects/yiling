package com.yiling.sales.assistant.app.order.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2022/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDeliveryBatchVO extends SimpleGoodsVO {

    /**
     * 商品标准库ID
     */
    @ApiModelProperty("商品标准库ID")
    private Long standardId;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 批次信息
     */
    @ApiModelProperty("批次信息")
    private List<BatchVO> batchVOList;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    public static class BatchVO extends BaseVO {

        /**
         * 订单ID
         */
        @ApiModelProperty("订单ID")
        private Long orderId;

        /**
         * 订单明细ID
         */
        @ApiModelProperty("订单明细ID")
        private Long detailId;

        /**
         * 批次号
         */
        @ApiModelProperty("批次号")
        private String batchNo;

        /**
         * 有效期
         */
        @ApiModelProperty("有效期")
        private Date expiryDate;

        /**
         * 商品ERP编码
         */
        private String goodsErpCode;

        /**
         * 生产日期
         */
        @ApiModelProperty("生产日期")
        private Date produceDate;

        /**
         * 购买数量
         */
        @ApiModelProperty("购买数量")
        private Integer goodsQuantity;

        /**
         * 发货数量
         */
        @ApiModelProperty("发货数量")
        private Integer deliveryQuantity;

        /**
         * 收货数量
         */
        @ApiModelProperty("收货数量")
        private Integer receiveQuantity;

    }

}
