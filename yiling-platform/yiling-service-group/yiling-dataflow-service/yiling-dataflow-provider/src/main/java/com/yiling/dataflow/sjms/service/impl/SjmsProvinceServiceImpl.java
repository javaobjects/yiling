package com.yiling.dataflow.sjms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.sjms.dao.SjmsProvinceMapper;
import com.yiling.dataflow.sjms.entity.SjmsProvinceDO;
import com.yiling.dataflow.sjms.service.SjmsProvinceService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 数据洞察系统省份对照关系 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
@Service
public class SjmsProvinceServiceImpl extends BaseServiceImpl<SjmsProvinceMapper, SjmsProvinceDO> implements SjmsProvinceService {

    @Override
    public List<SjmsProvinceDO> listByBizProvinceName(List<String> bizProvinceNames) {
        if (CollUtil.isEmpty(bizProvinceNames)) {
            return ListUtil.empty();
        }

        QueryWrapper<SjmsProvinceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SjmsProvinceDO::getBizProvinceName, bizProvinceNames);
        return this.list(queryWrapper);
    }
}
