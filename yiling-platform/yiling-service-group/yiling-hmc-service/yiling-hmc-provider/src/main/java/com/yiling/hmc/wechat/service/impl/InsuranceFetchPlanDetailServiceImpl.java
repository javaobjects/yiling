package com.yiling.hmc.wechat.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.bo.GoodsCountBO;
import com.yiling.hmc.wechat.dao.InsuranceFetchPlanDetailMapper;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanDetailRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDetailDO;
import com.yiling.hmc.wechat.entity.InsuranceRecordDO;
import com.yiling.hmc.wechat.enums.HmcPolicyStatusEnum;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanDetailService;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;

import cn.hutool.core.collection.CollUtil;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * C端拿药计划明细表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
@Service
public class InsuranceFetchPlanDetailServiceImpl extends BaseServiceImpl<InsuranceFetchPlanDetailMapper, InsuranceFetchPlanDetailDO> implements InsuranceFetchPlanDetailService {

    @Autowired
    private InsuranceFetchPlanService insuranceFetchPlanService;

    @Autowired
    private InsuranceRecordService insuranceRecordService;


    @Override
    public Boolean saveFetchPlanDetail(List<SaveInsuranceFetchPlanDetailRequest> requestList) {
        List<InsuranceFetchPlanDetailDO> list = PojoUtils.map(requestList, InsuranceFetchPlanDetailDO.class);
        return this.saveBatch(list);
    }

    @Override
    public List<InsuranceFetchPlanDetailDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        QueryWrapper<InsuranceFetchPlanDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InsuranceFetchPlanDetailDO::getInsuranceRecordId, insuranceRecordId);

        List<InsuranceFetchPlanDetailDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, InsuranceFetchPlanDetailDTO.class);
    }

    @Override
    public List<InsuranceFetchPlanGroupDTO> groupByInsuranceRecordId(List<Long> insuranceRecordIdList) {
        return this.baseMapper.groupByInsuranceRecordId(insuranceRecordIdList);
    }

    @Override
    public  Map<Long, List<Long>> getPerMonthCountMap(List<Long> insuranceRecordIdList, List<Long> recordPayIdList){
        LambdaQueryWrapper<InsuranceFetchPlanDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(insuranceRecordIdList),InsuranceFetchPlanDetailDO::getInsuranceRecordId, insuranceRecordIdList).select(InsuranceFetchPlanDetailDO::getId, InsuranceFetchPlanDetailDO::getInsuranceRecordId,InsuranceFetchPlanDetailDO::getPerMonthCount);
        wrapper.in(CollUtil.isNotEmpty(recordPayIdList), InsuranceFetchPlanDetailDO::getRecordPayId, recordPayIdList);
        List<InsuranceFetchPlanDetailDO> list = this.list(wrapper);
        Map<Long, List<Long>> detailMap = list.stream().collect(Collectors.groupingBy(InsuranceFetchPlanDetailDO::getInsuranceRecordId, Collectors.mapping(InsuranceFetchPlanDetailDO::getPerMonthCount, toList())));
        return detailMap;
    }

    @Override
    public Map<Long, Long> queryGoodsCount(List<Long> goodsIdList) {
        LambdaQueryWrapper<InsuranceFetchPlanDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(InsuranceFetchPlanDetailDO::getGoodsId,goodsIdList);
        List<InsuranceFetchPlanDetailDO> fetchPlanDetailDOS = this.list(wrapper);
        if(CollUtil.isEmpty(fetchPlanDetailDOS)){
            return Maps.newHashMap();
        }
        List<Long> idList = fetchPlanDetailDOS.stream().map(InsuranceFetchPlanDetailDO::getInsuranceRecordId).distinct().collect(toList());
        List<InsuranceRecordDO> insuranceRecordDOS = insuranceRecordService.listByIds(idList);
        if(CollUtil.isEmpty(insuranceRecordDOS)){
            return Maps.newHashMap();
        }
        //进行中的保单
        List<Long> filterIds = insuranceRecordDOS.stream().filter(insuranceRecordDO -> insuranceRecordDO.getPolicyStatus().equals(HmcPolicyStatusEnum.PROCESSING.getType())).map(InsuranceRecordDO::getId).collect(toList());
        if(CollUtil.isEmpty(filterIds)){
            return Maps.newHashMap();
        }
        //查询兑付次数
        LambdaQueryWrapper<InsuranceFetchPlanDO> fetchWrapper = Wrappers.lambdaQuery();
        //未拿
        fetchWrapper.eq(InsuranceFetchPlanDO::getFetchStatus,2);
        fetchWrapper.in(InsuranceFetchPlanDO::getInsuranceRecordId, filterIds).select(InsuranceFetchPlanDO::getId, InsuranceFetchPlanDO::getInsuranceRecordId);
        List<InsuranceFetchPlanDO> list = insuranceFetchPlanService.list(fetchWrapper);
        Map<Long, List<InsuranceFetchPlanDO>> map = Maps.newHashMap();
        if (CollUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(InsuranceFetchPlanDO::getInsuranceRecordId));
        }
        List<GoodsCountBO> goodsCountBOList = Lists.newArrayListWithExpectedSize(fetchPlanDetailDOS.size());
        Map<Long, List<InsuranceFetchPlanDO>> finalMap = map;
        fetchPlanDetailDOS.forEach(detail->{
            GoodsCountBO goodsCountBO = new GoodsCountBO();
            goodsCountBO.setGoodsId(detail.getGoodsId());
            if (!finalMap.isEmpty()) {
                List<InsuranceFetchPlanDO> insuranceFetchPlanDOS = finalMap.get(detail.getInsuranceRecordId());
                if (CollUtil.isNotEmpty(insuranceFetchPlanDOS)) {
                    goodsCountBO.setCount(insuranceFetchPlanDOS.size()*detail.getPerMonthCount());
                } else {
                    goodsCountBO.setCount(0L);
                }
            }else{
                goodsCountBO.setCount(0L);
            }
            goodsCountBOList.add(goodsCountBO);
        });
        Map<Long, Long> collect = goodsCountBOList.stream().collect(Collectors.groupingBy(GoodsCountBO::getGoodsId, Collectors.summingLong(GoodsCountBO::getCount)));
        return collect;
    }
}
