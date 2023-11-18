package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.GoodsSaleVolumeMapper;
import com.yiling.goods.medicine.dto.request.BatchSaveGoodsSaleVolumeRequest;
import com.yiling.goods.medicine.entity.GoodsSaleVolumeDO;
import com.yiling.goods.medicine.service.GoodsSaleVolumeService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author shichen
 * @类名 GoodsSaleVolumeServiceImpl
 * @描述
 * @创建时间 2023/5/8
 * @修改人 shichen
 * @修改时间 2023/5/8
 **/
@Service
public class GoodsSaleVolumeServiceImpl extends BaseServiceImpl<GoodsSaleVolumeMapper, GoodsSaleVolumeDO> implements GoodsSaleVolumeService {


    @Override
    public Boolean batchSaveSaleVolume(BatchSaveGoodsSaleVolumeRequest request) {
        Assert.notNull(request.getSaleDate(),"销量销售日期不能为空");
        LambdaQueryWrapper<GoodsSaleVolumeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(GoodsSaleVolumeDO::getSaleDate, DateUtil.beginOfDay(request.getSaleDate()),DateUtil.endOfDay(request.getSaleDate()));
        List<GoodsSaleVolumeDO> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            LambdaQueryWrapper<GoodsSaleVolumeDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.in(GoodsSaleVolumeDO::getId,list.stream().map(GoodsSaleVolumeDO::getId).collect(Collectors.toList()));
            GoodsSaleVolumeDO deleteDO = new GoodsSaleVolumeDO();
            deleteDO.setOpUserId(request.getOpUserId());
            this.batchDeleteWithFill(deleteDO,deleteWrapper);
        }
        if (CollectionUtil.isNotEmpty(request.getGoodsSaleVolumeList())){
            List<GoodsSaleVolumeDO> saveList = PojoUtils.map(request.getGoodsSaleVolumeList(), GoodsSaleVolumeDO.class);
            saveList.forEach(saveDO->{
                saveDO.setSaleDate(request.getSaleDate());
            });
            return this.saveBatch(saveList);
        }
        return true;
    }
}
