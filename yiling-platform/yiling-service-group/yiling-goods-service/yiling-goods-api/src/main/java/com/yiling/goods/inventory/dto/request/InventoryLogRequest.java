package com.yiling.goods.inventory.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.goods.inventory.enums.InventoryLogEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.goods.inventory.dto.request
 * @date: 2021/9/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InventoryLogRequest extends BaseRequest {

    /**
     * 日志记录类型
     */
    private InventoryLogEnum inventoryLogEnum;

    /**
     * 库存调整数量
     */
    private Long changeQty;

    /**
     * 库存冻结数量
     */
    private Long changeFrozenQty;

    /**
     * 商品Id
     */
    private Long gid;

    /**
     * inventoryId
     */
    private Long inventoryId;

    /**
     * 业务单号
     */
    private String businessNo;

}
