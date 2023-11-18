package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.entity.ErpGoodsDO;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpGoodsService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpGoodsHandler extends ErpServiceImpl {

    @Autowired
    public ErpGoodsService erpGoodsService;

    @Resource(name = "erpGoodsService")
    public ErpEntityService erpEntityService;


    public Boolean handleGoodsMqSync(Long suId, String taskNo, List<ErpGoodsDTO> erpGoodsList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpGoodsDTO erpGoods : erpGoodsList) {
            ErpGoodsDO erpOrderSendDO = PojoUtils.map(erpGoods, ErpGoodsDO.class);
            // 新增数据，设置药品状态默认值
            if (ObjectUtil.equal(OperTypeEnum.ADD.getCode(), erpOrderSendDO.getOperType()) && ObjectUtil.isNull(erpOrderSendDO.getGoodsStatus())) {
                erpOrderSendDO.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
            }
            entityList.add(erpOrderSendDO);
        }

        long start = System.currentTimeMillis();
        boolean bool = this.syncBaseData(suId, taskNo, entityList);
        long time = System.currentTimeMillis() - start;
        if (time > 3000) {
            log.warn("ERP数据同步耗时:{}ms,suId:{},taskNo:{}", time, suId, taskNo);
        }
        return bool;
    }

    @Override
    public ErpEntityService getErpEntityManager() {
        return erpEntityService;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        //todo 需要加上分布式锁 到最细
        return erpGoodsService.onlineData(baseErpEntity);
    }
}
