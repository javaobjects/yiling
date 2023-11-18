package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.dao.EnterpriseSalesAreaDetailMapper;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDetailDO;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaDetailService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业销售区域详情 服务实现类
 * </p>
 *
 * @author zhouxuan
 * @date 2021-10-29
 */
@Service
public class EnterpriseSalesAreaDetailServiceImpl extends BaseServiceImpl<EnterpriseSalesAreaDetailMapper, EnterpriseSalesAreaDetailDO> implements EnterpriseSalesAreaDetailService {

    @Autowired
    EnterpriseSalesAreaDetailMapper enterpriseSalesAreaDetailMapper;

    @Override
    public List<EnterpriseSalesAreaDetailDO> getEnterpriseSaleAreaDetail(Long eid) {
        LambdaQueryWrapper<EnterpriseSalesAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseSalesAreaDetailDO::getEid,eid);

        return this.list(wrapper);
    }

    @Override
    public Map<Long, List<EnterpriseSalesAreaDetailDO>> getEnterpriseSaleAreaDetailByList(List<Long> eidList) {
        LambdaQueryWrapper<EnterpriseSalesAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseSalesAreaDetailDO::getEid,eidList);

        List<EnterpriseSalesAreaDetailDO> list = this.list(wrapper);
        return list.stream().collect(Collectors.groupingBy(EnterpriseSalesAreaDetailDO::getEid));
    }

    @Override
    public List<Long> getEnterpriseSaleAreaDetailByListAndArea(List<Long> eidList, String areaCode) {
        if (CollUtil.isEmpty(eidList) || StrUtil.isEmpty(areaCode)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<EnterpriseSalesAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseSalesAreaDetailDO::getEid,eidList);
        wrapper.eq(EnterpriseSalesAreaDetailDO::getAreaCode, areaCode);

        List<EnterpriseSalesAreaDetailDO> list = this.list(wrapper);
        return list.stream().map(EnterpriseSalesAreaDetailDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    public Boolean insertEnterpriseSaleAreaDetail(List<EnterpriseSalesAreaDetailDO> enterpriseSalesAreaDetailDoList) {
        if(CollUtil.isEmpty(enterpriseSalesAreaDetailDoList)){
            return true;
        }

        return enterpriseSalesAreaDetailMapper.addEnterpriseAreaCodeList(enterpriseSalesAreaDetailDoList) > 0;
    }
}
