package com.yiling.dataflow.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.crm.dao.CrmGoodsMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmGoodsDO;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * crm商品信息 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-11
 */
@Service
@CacheConfig(cacheNames = "dataflow:CrmGoods")
public class CrmGoodsServiceImpl extends BaseServiceImpl<CrmGoodsMapper, CrmGoodsDO> implements CrmGoodsService {

    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;

    @Override
    @Cacheable(key = "#specificationId+'+getCrmGoodsBySpecificationId'")
    public List<CrmGoodsDTO> getCrmGoodsBySpecificationId(Long specificationId) {
        QueryWrapper<CrmGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmGoodsDO::getSpecificationId, specificationId).orderByAsc(CrmGoodsDO::getGoodsCode);
        return PojoUtils.map(list(queryWrapper), CrmGoodsDTO.class);
    }

    @Override
    public Map<Long, CrmGoodsInfoDTO> getCrmGoodsMap() {
        QueryWrapper<CrmGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(CrmGoodsDO::getSpecificationId, 0).orderByAsc(CrmGoodsDO::getGoodsCode);
        List<CrmGoodsDTO> crmGoodsDTOList = PojoUtils.map(list(queryWrapper), CrmGoodsDTO.class);
        List<CrmGoodsInfoDTO> crmGoodsInfoDTOList=crmGoodsInfoService.getCrmGoodsInfoAll();
        Map<Long,CrmGoodsInfoDTO> crmGoodsInfoDTOMap= crmGoodsInfoDTOList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        Map<Long,CrmGoodsInfoDTO> map=new HashMap<>();
        for (CrmGoodsDTO crmGoodsDTO : crmGoodsDTOList) {
            if(!map.containsKey(crmGoodsDTO.getSpecificationId())){
                map.put(crmGoodsDTO.getSpecificationId(),crmGoodsInfoDTOMap.get(crmGoodsDTO.getGoodsCode()));
            }
        }
        return map;
    }
}
