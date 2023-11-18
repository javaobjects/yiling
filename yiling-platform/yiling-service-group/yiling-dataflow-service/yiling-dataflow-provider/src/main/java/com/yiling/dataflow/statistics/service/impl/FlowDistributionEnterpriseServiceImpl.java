package com.yiling.dataflow.statistics.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dao.FlowDistributionEnterpriseMapper;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseEnterpriseRequest;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowDistributionEnterpriseDO;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业信息配置表 服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
@Service
public class FlowDistributionEnterpriseServiceImpl extends BaseServiceImpl<FlowDistributionEnterpriseMapper, FlowDistributionEnterpriseDO> implements FlowDistributionEnterpriseService {

    @Override
    public Page<FlowDistributionEnterpriseDO> getEnterpriseListByName(FlowAnalyseEnterpriseRequest request) {
        LambdaQueryWrapper<FlowDistributionEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getEname())) {
            queryWrapper.likeRight(FlowDistributionEnterpriseDO::getEname, request.getEname());
        }
        queryWrapper.orderByAsc(FlowDistributionEnterpriseDO::getEname);
        return baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public List<Long> getEidByParmam(StockForecastInfoRequest request) {
        if(Objects.nonNull(request.getEid())){
            return Arrays.asList(request.getEid());
        }
        LambdaQueryWrapper<FlowDistributionEnterpriseDO> queryWrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(request.getEnameGroup())){
            queryWrapper.eq(FlowDistributionEnterpriseDO::getEnameGroup,request.getEnameGroup());
        }
        if(StringUtils.isNotBlank(request.getEnameLevel())){
            queryWrapper.eq(FlowDistributionEnterpriseDO::getEnameLevel,request.getEnameLevel());
        }
        if(request.getEid() !=null){
            queryWrapper.eq(FlowDistributionEnterpriseDO::getEid,request.getEid());
        }
        List<FlowDistributionEnterpriseDO> selectResult= baseMapper.selectList(queryWrapper);

        return selectResult.stream().filter(s->s.getEid()!=null).map(FlowDistributionEnterpriseDO::getEid).collect(Collectors.toList());
    }

    @Override
    public List<Long> getEidList() {
        List<FlowDistributionEnterpriseDO> list=this.list();
        return list.stream().map(e->e.getEid()).distinct().filter(e->e>0).collect(Collectors.toList());
    }

    @Override
    public List<FlowDistributionEnterpriseDO> getListByCodeList(List<String> codeList) {
        Assert.notEmpty(codeList, "参数 codeList 不能为空");
        LambdaQueryWrapper<FlowDistributionEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowDistributionEnterpriseDO::getCode, codeList);
        List list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<FlowDistributionEnterpriseDO> getListByEidList(List<Long> eidList) {
        if (null == eidList) {
            return ListUtil.empty();
        } else if (0 == eidList.size()) {
            return this.list();
        } else {
            LambdaQueryWrapper<FlowDistributionEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(FlowDistributionEnterpriseDO::getEid, eidList);
            List list = this.list(wrapper);
            if (CollUtil.isEmpty(list)) {
                return ListUtil.empty();
            }
            return list;
        }
    }

    @Override
    public Integer getCountByEidList(List<Long> eidList) {
        if (null == eidList) {
            return 0;
        }
        Integer count = this.baseMapper.getCountByEidList(eidList);
        if (ObjectUtil.isNull(count)) {
            return 0;
        }
        return count;
    }

    @Override
    public List<FlowDistributionEnterpriseDO> getListByCrmIdListAndEnameLevel(List<Long> crmIdList, String enameLevel) {
        if (null == crmIdList) {
            return ListUtil.empty();
        }else {
            List list;
            if (CollUtil.isEmpty(crmIdList) && StrUtil.isBlank(enameLevel)) {
                list = this.list();
            } else {
                LambdaQueryWrapper<FlowDistributionEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.notIn(FlowDistributionEnterpriseDO::getCrmEnterpriseId, 0L);
                if (CollUtil.isNotEmpty(crmIdList)) {
                    wrapper.in(FlowDistributionEnterpriseDO::getCrmEnterpriseId, crmIdList);
                }
                if (StrUtil.isNotBlank(enameLevel)) {
                    wrapper.eq(FlowDistributionEnterpriseDO::getEnameLevel, enameLevel);
                }
                list = this.list(wrapper);
            }
            if (CollUtil.isEmpty(list)) {
                return ListUtil.empty();
            }
            return list;
        }
    }

    @Override
    public List<FlowDistributionEnterpriseDO> listByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        Assert.notNull(crmEnterpriseIdList, "参数 crmEnterpriseIdList 不能为空");
        LambdaQueryWrapper<FlowDistributionEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowDistributionEnterpriseDO::getCrmEnterpriseId, crmEnterpriseIdList);
        List<FlowDistributionEnterpriseDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

}
