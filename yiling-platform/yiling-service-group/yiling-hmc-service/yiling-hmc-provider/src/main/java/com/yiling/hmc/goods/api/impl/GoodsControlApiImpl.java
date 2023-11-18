package com.yiling.hmc.goods.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.goods.api.GoodsControlApi;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.GoodsControlDTO;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.dto.request.GoodsControlSaveRequest;
import com.yiling.hmc.goods.service.GoodsControlService;

/**
 * @author shichen
 * @类名 GoodsControlApiImpl
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@DubboService
public class GoodsControlApiImpl implements GoodsControlApi {
    @Autowired
    private GoodsControlService goodsControlService;

    @Override
    public List<GoodsControlDTO> batchGetGoodsControlBySpecificationsIds(List<Long> specificationsIds) {
        return goodsControlService.batchGetGoodsControlBySpecificationsIds(specificationsIds);
    }

    @Override
    public Page<GoodsControlBO> pageList(GoodsControlPageRequest request) {
        return goodsControlService.pageList(request);
    }

    @Override
    public Long saveOrUpdateGoodsControl(GoodsControlSaveRequest request) {
        return goodsControlService.saveOrUpdateGoodsControl(request);
    }

    @Override
    public List<GoodsControlBO> getGoodsControlInfoByIds(List<Long> controlIds) {
        return goodsControlService.getGoodsControlInfoByIds(controlIds);
    }

    @Override
    public Page<GoodsControlDTO> queryUpGoodsControlPageList(GoodsControlPageRequest request) {
        return goodsControlService.queryUpGoodsControlPageList(request);
    }

    @Override
    public Page<GoodsControlBO> queryInsuranceGoodsControlPageList(GoodsControlPageRequest request) {
        return goodsControlService.queryInsuranceGoodsControlPageList(request);
    }
}
