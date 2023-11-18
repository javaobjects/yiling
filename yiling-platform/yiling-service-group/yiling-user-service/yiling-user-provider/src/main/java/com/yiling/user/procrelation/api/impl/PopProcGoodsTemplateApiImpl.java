package com.yiling.user.procrelation.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.api.PopProcGoodsTemplateApi;
import com.yiling.user.procrelation.dto.PopProcGoodsTemplateDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsToTemplateRequest;
import com.yiling.user.procrelation.dto.request.UpdateGoodsRebateRequest;
import com.yiling.user.procrelation.entity.PopProcGoodsTemplateDO;
import com.yiling.user.procrelation.service.PopProcGoodsTemplateService;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@DubboService
public class PopProcGoodsTemplateApiImpl implements PopProcGoodsTemplateApi {

    @Autowired
    PopProcGoodsTemplateService goodsTemplateService;


    @Override
    public List<PopProcGoodsTemplateDTO> queryGoodsList(Long templateId, Long factoryEid) {
        List<PopProcGoodsTemplateDO> list = goodsTemplateService.queryGoodsList(templateId, factoryEid);
        return PojoUtils.map(list, PopProcGoodsTemplateDTO.class);
    }

    @Override
    public Boolean addGoodsToTemplate(Long templateId, List<AddGoodsToTemplateRequest> goodsList) {
        return goodsTemplateService.addGoodsToTemplate(templateId, goodsList);
    }

    @Override
    public PopProcGoodsTemplateDTO queryTemplateGoodsById(Long id) {
        PopProcGoodsTemplateDO popProcGoodsTemplateDO = goodsTemplateService.getById(id);
        return PojoUtils.map(popProcGoodsTemplateDO, PopProcGoodsTemplateDTO.class);
    }

    @Override
    public Boolean deleteGoods(Long id, Long opUser) {
        return goodsTemplateService.deleteGoods(id, opUser);
    }

    @Override
    public Boolean updateGoodsRebate(UpdateGoodsRebateRequest request) {
        return goodsTemplateService.updateGoodsRebate(request);
    }

    @Override
    public Map<String, List<PopProcGoodsTemplateDTO>> queryBatchGoodsList(List<String> templateIds) {
        Map<String, List<PopProcGoodsTemplateDO>> goodsMap = goodsTemplateService.queryBatchGoodsList(templateIds);
        return PojoUtils.map(goodsMap,PopProcGoodsTemplateDTO.class);
    }
}
