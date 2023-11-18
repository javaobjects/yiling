package com.yiling.goods.inventory.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.inventory.dao.GoodsInventoryLogMapper;
import com.yiling.goods.inventory.dao.InventoryMapper;
import com.yiling.goods.inventory.dto.request.InventoryLogRequest;
import com.yiling.goods.inventory.entity.GoodsInventoryLogDO;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.enums.InventoryLogEnum;
import com.yiling.goods.inventory.service.GoodsInventoryLogService;

import cn.hutool.core.comparator.CompareUtil;

/**
 * <p>
 *  记录库存变动日志
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-09-02
 */
@Service
public class GoodsInventoryLogServiceImpl extends BaseServiceImpl<GoodsInventoryLogMapper, GoodsInventoryLogDO> implements GoodsInventoryLogService {

    @Autowired
    private InventoryMapper inventoryMapper;

    private InventoryDO getByInventoryId(Long inventoryId) {
        QueryWrapper<InventoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InventoryDO::getId, inventoryId).last("limit 1");
        return inventoryMapper.selectOne(queryWrapper);
    }


    @Override
    public boolean insertGoodInventoryLog(InventoryLogRequest inventoryLogRequest) {

        // 查询变更前商品库存数量
        InventoryDO inventoryDO = Optional.ofNullable(this.getByInventoryId(inventoryLogRequest.getInventoryId())).orElseGet(() -> new InventoryDO().setFrozenQty(0l).setQty(0l));

        GoodsInventoryLogDO goodsInventoryLogDO = new GoodsInventoryLogDO();
        goodsInventoryLogDO.setBeforeFrozenQty(inventoryDO.getFrozenQty());
        goodsInventoryLogDO.setInventoryId(inventoryLogRequest.getInventoryId());
        goodsInventoryLogDO.setGid(inventoryLogRequest.getGid());
        goodsInventoryLogDO.setBeforeQty(inventoryDO.getQty());
        goodsInventoryLogDO.setBusinessNo(Optional.ofNullable(inventoryLogRequest.getBusinessNo()).orElse(""));
        goodsInventoryLogDO.setCreateUser(inventoryLogRequest.getOpUserId());

        switch (inventoryLogRequest.getInventoryLogEnum()){

            case FROZEN:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.FROZEN.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty() + inventoryLogRequest.getChangeFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryDO.getQty());
                goodsInventoryLogDO.setChangeQty(0l);
                goodsInventoryLogDO.setChangeFrozenQty(inventoryLogRequest.getChangeFrozenQty());
                break;

            case UNFROZEN:
            case BATCH_UNFROZEN:
                goodsInventoryLogDO.setBusinessType(inventoryLogRequest.getInventoryLogEnum().getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty() - inventoryLogRequest.getChangeFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryDO.getQty());
                goodsInventoryLogDO.setChangeQty(0l);
                goodsInventoryLogDO.setChangeFrozenQty( -inventoryLogRequest.getChangeFrozenQty());
                break;

            case MODIFY:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.MODIFY.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeQty(inventoryLogRequest.getChangeQty() - inventoryDO.getQty());
                goodsInventoryLogDO.setChangeFrozenQty(0l);
            case SUBSCRIPTION_MODIFY:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.SUBSCRIPTION_MODIFY.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeQty(inventoryLogRequest.getChangeQty() - inventoryDO.getQty());
                goodsInventoryLogDO.setChangeFrozenQty(0l);
                break;

            case INSTOCK:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.INSTOCK.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeQty(inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeFrozenQty(0l);
                break;

            case OUTSTOCK:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.OUTSTOCK.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty() - inventoryLogRequest.getChangeFrozenQty());
                Long afterQty = inventoryLogRequest.getChangeQty()>inventoryDO.getQty() ? 0L : inventoryDO.getQty() - inventoryLogRequest.getChangeQty();
                goodsInventoryLogDO.setAfterQty(afterQty);
                goodsInventoryLogDO.setChangeQty(-inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeFrozenQty(-inventoryLogRequest.getChangeFrozenQty());
                break;

            case BACKSTOCK:
                goodsInventoryLogDO.setBusinessType(InventoryLogEnum.BACKSTOCK.getCode());
                goodsInventoryLogDO.setAfterFrozenQty(inventoryDO.getFrozenQty() + inventoryLogRequest.getChangeFrozenQty());
                goodsInventoryLogDO.setAfterQty(inventoryDO.getQty() + inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeQty(inventoryLogRequest.getChangeQty());
                goodsInventoryLogDO.setChangeFrozenQty(inventoryLogRequest.getChangeFrozenQty());
                break;

            default: break;
        }

        // 如果前后库存没发生变动,日志无需记录
        if (CompareUtil.compare(goodsInventoryLogDO.getBeforeQty(),goodsInventoryLogDO.getAfterQty()) == 0
            && CompareUtil.compare(goodsInventoryLogDO.getBeforeFrozenQty(),goodsInventoryLogDO.getAfterFrozenQty()) == 0) {

            return true;
        }

        // 记录变更后的库存数量
        return this.save(goodsInventoryLogDO);
    }


}
