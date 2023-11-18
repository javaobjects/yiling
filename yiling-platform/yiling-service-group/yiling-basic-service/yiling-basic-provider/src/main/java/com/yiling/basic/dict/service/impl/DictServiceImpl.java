package com.yiling.basic.dict.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.entity.DictTypeDO;
import com.yiling.basic.dict.service.DictDataService;
import com.yiling.basic.dict.service.DictService;
import com.yiling.basic.dict.service.DictTypeService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据字典 Servcie 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Slf4j
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictTypeService dictTypeService;
    @Autowired
    DictDataService  dictDataService;

    @Override
    public List<DictBO> getEnabledList() {
        List<DictTypeDO> typeList = dictTypeService.getEnabledList();
        if (CollUtil.isEmpty(typeList)) {
            return ListUtil.empty();
        }

        List<DictBO> dictList = PojoUtils.map(typeList, DictBO.class);

        List<DictDataDO> dataList = dictDataService.getEnabledList();
        if (CollUtil.isEmpty(dataList)) {
            return dictList;
        }

        dictList.forEach(e -> {
            e.setDataList(PojoUtils.map(dataList.stream().filter(data -> data.getTypeId().equals(e.getId())).collect(Collectors.toList()), DictBO.DictData.class));
        });

        return dictList;
    }

    @Override
    public DictBO getDictByName(String name) {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictTypeDO::getName, name);
        DictTypeDO typeDO = dictTypeService.getOne(queryWrapper);
        if(null==typeDO){
            return null;
        }
        DictBO dictBO = PojoUtils.map(typeDO, DictBO.class);
        List<DictDataDO> dataDOList = dictDataService.getEnabledByTypeIdList(typeDO.getId());
        dictBO.setDataList(PojoUtils.map(dataDOList,DictBO.DictData.class));
        return dictBO;
    }
}
