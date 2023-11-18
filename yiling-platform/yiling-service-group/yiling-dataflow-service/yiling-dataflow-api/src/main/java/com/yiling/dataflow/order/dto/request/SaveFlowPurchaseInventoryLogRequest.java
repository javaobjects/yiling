package com.yiling.dataflow.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Data
public class SaveFlowPurchaseInventoryLogRequest extends BaseRequest {

    /**
     * 业务类型：1-erp流向库存同步 2-报表计算库存 3-erp流向库存总数量统计 4-人工调整 5-以岭品关系修改
     */
    private Integer businessType;

    /**
     * 流向商品库存id
     */
    private Long flowPurchaseInventoryId;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 变更前库存数量
     */
    private BigDecimal beforeQuantity;

    /**
     * 变更数量
     */
    private BigDecimal changeQuantity;

    /**
     * 变更后库存数量
     */
    private BigDecimal afterQuantity;


}
