package com.yiling.dataflow.order.service.impl;

import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dao.FlowGoodsSpecMappingMapper;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.medicine.api.GoodsMatchApi;
import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import org.apache.dubbo.config.annotation.DubboReference;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "dataflow:flowGoodsSpecMapping")
public class FlowGoodsSpecMappingServiceImpl extends BaseServiceImpl<FlowGoodsSpecMappingMapper, FlowGoodsSpecMappingDO> implements FlowGoodsSpecMappingService {

    @DubboReference
    private GoodsMatchApi                 goodsMatchApi;
    @DubboReference
    private StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @Autowired
    private RedisDistributedLock          redisDistributedLock;

    @Override
    @Cacheable(key="#goodsName+'+'+#spec+'+'+#manufacturer+'+findByGoodsNameAndSpec'")
    public FlowGoodsSpecMappingDO findByGoodsNameAndSpec(String goodsName, String spec,String manufacturer) {
        if (goodsName != null) {
            goodsName = goodsName.trim();
        }
        if (spec != null) {
            spec = spec.trim();
        }
        if (manufacturer != null) {
            manufacturer = manufacturer.trim();
        }

        LambdaQueryWrapper<FlowGoodsSpecMappingDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsSpecMappingDO::getGoodsName, goodsName);
        wrapper.eq(FlowGoodsSpecMappingDO::getSpec, spec);
        wrapper.eq(FlowGoodsSpecMappingDO::getManufacturer, manufacturer);
        wrapper.eq(FlowGoodsSpecMappingDO::getDelFlag, 0);
        wrapper.last("limit 1");
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveFlowGoodsSpecMapping(SaveFlowGoodsSpecMappingRequest request) {
        return this.save(PojoUtils.map(request,FlowGoodsSpecMappingDO.class));
    }

    @Override
    public void updateRecommendInfoByGoodsNameAndSpec() {
        //先获取所有的规格信息
        List<StandardGoodsSpecificationDTO> standardGoodsSpecificationDTOList =getStandardGoodsSpecificationDTOAll();
        log.info("加载线上规格商品完成,一共{}",standardGoodsSpecificationDTOList.size());
        List<MatchGoodsDTO> targets=new ArrayList<>();
        for(StandardGoodsSpecificationDTO standardGoodsSpecificationDTO:standardGoodsSpecificationDTOList){
            MatchGoodsDTO matchGoodsDTO=new MatchGoodsDTO();
            matchGoodsDTO.setName(standardGoodsSpecificationDTO.getName());
            matchGoodsDTO.setSpecification(standardGoodsSpecificationDTO.getSellSpecifications());
            matchGoodsDTO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
            matchGoodsDTO.setId(standardGoodsSpecificationDTO.getId());
            targets.add(matchGoodsDTO);
        }

        LambdaQueryWrapper<FlowGoodsSpecMappingDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsSpecMappingDO::getRecommendSpecificationId,0).last("limit 100");
        List<FlowGoodsSpecMappingDO> list = this.list(wrapper);
        log.info("需要匹配的商品规格,一共{}",list.size());
        for (FlowGoodsSpecMappingDO flowGoodsSpecMappingDO : list) {
            MatchGoodsDTO matchGoodsDTO = new MatchGoodsDTO();
            matchGoodsDTO.setName(flowGoodsSpecMappingDO.getGoodsName());
            matchGoodsDTO.setSpecification(flowGoodsSpecMappingDO.getSpec());
            matchGoodsDTO.setManufacturer(flowGoodsSpecMappingDO.getManufacturer());
            matchGoodsDTO.setId(flowGoodsSpecMappingDO.getId());

            MatchedGoodsDTO matchedGoods=goodsMatchApi.matchingGoodsWithSpec(matchGoodsDTO,targets);
            if(matchedGoods!=null) {
                flowGoodsSpecMappingDO.setRecommendSpecificationId(matchedGoods.getTargetId());
                flowGoodsSpecMappingDO.setRecommendGoods(matchedGoods.getName());
                flowGoodsSpecMappingDO.setRecommendSpec(matchedGoods.getSpecification());
                flowGoodsSpecMappingDO.setRecommendManufacturer(matchedGoods.getManufacturer());
                flowGoodsSpecMappingDO.setRecommendScore(new Double(matchedGoods.getPer() * 100).intValue());
                this.updateById(flowGoodsSpecMappingDO);
            }
        }
    }

    @Override
    public void flushGoodsSpecificationId(List<FlushGoodsSpecIdRequest.FlushDataRequest> flushDataList) {
        for (FlushGoodsSpecIdRequest.FlushDataRequest flushData : flushDataList) {
            if (flushData.getRecommendSpecificationId() == null || flushData.getRecommendSpecificationId() == 0) {
                continue;
            }
            LambdaUpdateWrapper<FlowGoodsSpecMappingDO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(FlowGoodsSpecMappingDO::getGoodsName, flushData.getGoodsName());
            wrapper.eq(FlowGoodsSpecMappingDO::getSpec, flushData.getSpec());
            wrapper.eq(FlowGoodsSpecMappingDO::getManufacturer, flushData.getManufacturer());
            wrapper.eq(FlowGoodsSpecMappingDO::getSpecificationId, 0);

            FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = new FlowGoodsSpecMappingDO();
            flowGoodsSpecMappingDO.setRecommendGoods(flushData.getRecommendGoodsName());
            flowGoodsSpecMappingDO.setRecommendSpec(flushData.getRecommendSpec());
            flowGoodsSpecMappingDO.setRecommendSpecificationId(flushData.getRecommendSpecificationId());
            flowGoodsSpecMappingDO.setRecommendScore(flushData.getRecommendScore());
            flowGoodsSpecMappingDO.setSpecificationId(flushData.getRecommendSpecificationId());

            baseMapper.update(flowGoodsSpecMappingDO, wrapper);
        }
    }

    @Override
    public boolean isUsedSpecificationId(Long specificationId) {
        LambdaQueryWrapper<FlowGoodsSpecMappingDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsSpecMappingDO::getSpecificationId, specificationId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public List<StandardGoodsSpecificationDTO> getStandardGoodsSpecificationDTOAll() {
        //需要循环调用
        List<StandardGoodsSpecificationDTO> list = new ArrayList<>();
        StandardSpecificationPageRequest request = new StandardSpecificationPageRequest();
        Page<StandardGoodsSpecificationDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = standardGoodsSpecificationApi.getSpecificationPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //插入任务
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }
}
