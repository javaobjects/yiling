package com.yiling.dataflow.statistics.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseGoodsDO;
import com.yiling.dataflow.statistics.dao.FlowAnalyseGoodsMapper;
import com.yiling.dataflow.statistics.service.FlowAnalyseGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品信息配置表 服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
@Service
public class FlowAnalyseGoodsServiceImpl extends BaseServiceImpl<FlowAnalyseGoodsMapper, FlowAnalyseGoodsDO> implements FlowAnalyseGoodsService {

    @Override
    public Page<FlowAnalyseGoodsDO> getGoodsListByName(FlowAnalyseGoodsRequest request) {
        LambdaQueryWrapper<FlowAnalyseGoodsDO> queryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNotBlank(request.getGoodsName())){
            queryWrapper.likeRight(FlowAnalyseGoodsDO::getGoodsName,request.getGoodsName());
        }
        queryWrapper.orderByAsc(FlowAnalyseGoodsDO::getGoodsName);
        return baseMapper.selectPage(request.getPage(),queryWrapper);
    }

    @Override
    public List<Long> getSpecificationIdList() {
        List<FlowAnalyseGoodsDO> list=this.list();
        return list.stream().map(e->e.getSpecificationId()).distinct().filter(e->e>0).collect(Collectors.toList());
    }

    @Override
    public List<FlowAnalyseGoodsDTO> getGoodsNameBySpecificationIds(List<String> specificationIds) {
        if(specificationIds == null || specificationIds.size()==0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<FlowAnalyseGoodsDO> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(FlowAnalyseGoodsDO::getSpecificationId,FlowAnalyseGoodsDO::getGoodsName);
        queryWrapper.in(FlowAnalyseGoodsDO::getSpecificationId,specificationIds);
        return PojoUtils.map(list(queryWrapper),FlowAnalyseGoodsDTO.class);
    }
}
