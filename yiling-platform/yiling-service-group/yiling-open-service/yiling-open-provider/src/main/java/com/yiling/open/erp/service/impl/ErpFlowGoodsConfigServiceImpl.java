package com.yiling.open.erp.service.impl;

import java.util.Date;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dao.ErpFlowGoodsConfigMapper;
import com.yiling.open.erp.dto.request.DeleteErpFlowGoodsConfigRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowGoodsConfigPageRequest;
import com.yiling.open.erp.dto.request.SaveErpFlowGoodsConfigRequest;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向非以岭商品配置 服务实现类
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Slf4j
@Service("erpFlowGoodsConfigService")
@CacheConfig(cacheNames = "open:erpFlowGoodsConfig")
public class ErpFlowGoodsConfigServiceImpl extends BaseServiceImpl<ErpFlowGoodsConfigMapper, ErpFlowGoodsConfigDO> implements ErpFlowGoodsConfigService {

    @Override
    public Page<ErpFlowGoodsConfigDO> page(QueryErpFlowGoodsConfigPageRequest request) {
        Page<ErpFlowGoodsConfigDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getErpClientPageQueryWrapper(request));
    }

    @Override
    @CacheEvict(allEntries = true)
    public Boolean save(SaveErpFlowGoodsConfigRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getEid(), "eid不能为空");
        Assert.notNull(request.getEname(), "ename不能为空");
        Assert.notNull(request.getGoodsInSn(), "goodsInSn不能为空");

        ErpFlowGoodsConfigDO entity = PojoUtils.map(request, ErpFlowGoodsConfigDO.class);
        entity.setCreateUser(request.getOpUserId());
        entity.setCreateTime(new Date());
        return this.save(entity);
    }

    @Override
    @Cacheable(key="'getByEidAndGoodsInSn:eid_' + #eid + ':goodsInSn_' + #goodsInSn")
    public ErpFlowGoodsConfigDO getByEidAndGoodsInSn(Long eid, String goodsInSn) {
        Assert.notNull(eid, "eid不能为空");
        Assert.notNull(eid, "goodsInSn不能为空");
        LambdaQueryWrapper<ErpFlowGoodsConfigDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ErpFlowGoodsConfigDO::getEid, eid);
        lambdaQueryWrapper.eq(ErpFlowGoodsConfigDO::getGoodsInSn, goodsInSn);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Integer deleteFlowGoodsConfigById(DeleteErpFlowGoodsConfigRequest request) {
        return this.baseMapper.deleteFlowGoodsConfigById(request.getId());
    }

    private LambdaQueryWrapper<ErpFlowGoodsConfigDO> getErpClientPageQueryWrapper(QueryErpFlowGoodsConfigPageRequest request) {
        LambdaQueryWrapper<ErpFlowGoodsConfigDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        Long eid = request.getEid();
        if (eid != null && eid != 0) {
            lambdaQueryWrapper.eq(ErpFlowGoodsConfigDO::getEid, eid);
        }

        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(ErpFlowGoodsConfigDO::getEname, ename);
        }

        String goodsName = request.getGoodsName();
        if (StrUtil.isNotBlank(goodsName)) {
            lambdaQueryWrapper.like(ErpFlowGoodsConfigDO::getGoodsName, goodsName);
        }

        String goodsInSn = request.getGoodsInSn();
        if (StrUtil.isNotBlank(goodsInSn)) {
            lambdaQueryWrapper.like(ErpFlowGoodsConfigDO::getGoodsInSn, goodsInSn);
        }

        String startCreateTime = request.getStartCreateTime();
        if (StrUtil.isNotBlank(startCreateTime)) {
            lambdaQueryWrapper.ge(ErpFlowGoodsConfigDO::getCreateTime, DateUtil.beginOfDay(DateUtil.parse(startCreateTime, "yyyy-MM-dd")));
        }

        String endCreateTime = request.getEndCreateTime();
        if (StrUtil.isNotBlank(endCreateTime)) {
            lambdaQueryWrapper.le(ErpFlowGoodsConfigDO::getCreateTime, DateUtil.endOfDay(DateUtil.parse(endCreateTime, "yyyy-MM-dd")));
        }

        lambdaQueryWrapper.orderByDesc(ErpFlowGoodsConfigDO::getId);
        return lambdaQueryWrapper;
    }

}
