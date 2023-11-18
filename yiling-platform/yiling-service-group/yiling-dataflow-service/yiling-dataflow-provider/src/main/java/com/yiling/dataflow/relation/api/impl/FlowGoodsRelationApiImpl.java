package com.yiling.dataflow.relation.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@DubboService
public class FlowGoodsRelationApiImpl implements FlowGoodsRelationApi {

    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;

    @Override
    public Page<FlowGoodsRelationDTO> page(QueryFlowGoodsRelationPageRequest request) {
        Page<FlowGoodsRelationDTO> page = PojoUtils.map(flowGoodsRelationService.page(request), FlowGoodsRelationDTO.class);
        if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
            page.getRecords().forEach(p -> {
                Date createTime = p.getCreateTime();
                Date updateTime = p.getUpdateTime();
                if (ObjectUtil.isNotNull(updateTime) && !ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss"))) {
                    p.setOpTime(updateTime);
                } else {
                    p.setOpTime(createTime);
                }
            });
        }
        return page;
    }

    @Override
    public FlowGoodsRelationDTO getById(Long id) {
        Assert.notNull(id, "参数 id 不能为空");
        return PojoUtils.map(flowGoodsRelationService.getById(id), FlowGoodsRelationDTO.class);
    }

    @Override
    public List<FlowGoodsRelationDTO> getByIdList(List<Long> idList) {
        return PojoUtils.map(flowGoodsRelationService.getByIdList(idList), FlowGoodsRelationDTO.class);
    }

    @Override
    public Boolean edit(UpdateFlowGoodsRelationRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return flowGoodsRelationService.edit(request);
    }

    @Override
    public List<FlowGoodsRelationBO> getListByEidAndGoodsName(Long eid, String goodsName) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notBlank(goodsName, "参数 goodsName 不能为空");
        return PojoUtils.map(flowGoodsRelationService.getListByEidAndGoodsName(eid, goodsName), FlowGoodsRelationBO.class);
    }

    @Override
    public List<FlowGoodsRelationBO> getListByEidAndYlGoodsId(Long eid, Long ylGoodsId) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notNull(ylGoodsId, "参数 ylGoodsId 不能为空");
        return PojoUtils.map(flowGoodsRelationService.getListByEidAndYlGoodsId(eid, ylGoodsId), FlowGoodsRelationBO.class);
    }

    @Override
    public FlowGoodsRelationDTO getByEidAndGoodsInSn(Long eid, String goodsInSn) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notBlank(goodsInSn, "参数 goodsInSn 不能为空");
        return PojoUtils.map(flowGoodsRelationService.getByEidAndGoodsInSn(eid, goodsInSn), FlowGoodsRelationDTO.class);
    }

    @Override
    public void saveOrUpdateByFlowSync(QueryFlowGoodsRelationYlGoodsIdRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || request.getEid().intValue() == 0 || StrUtil.isBlank(request.getEname()) || StrUtil.isBlank(request.getGoodsName())
                || StrUtil.isBlank(request.getGoodsInSn()) || StrUtil.isBlank(request.getGoodsSpecifications()) ){
            return;
        }
        flowGoodsRelationService.saveOrUpdateByFlowSync(request);
    }

    @Override
    public List<FlowGoodsRelationDTO> getByEidAndGoodsInSn(List<Long> eidList, List<String> goodsInSnList) {
        return PojoUtils.map(flowGoodsRelationService.getByEidAndGoodsInSn(eidList, goodsInSnList), FlowGoodsRelationDTO.class);
    }

}
