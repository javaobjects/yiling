package com.yiling.user.procrelation.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.dao.PopProcGoodsTemplateMapper;
import com.yiling.user.procrelation.dao.PopProcTemplateMapper;
import com.yiling.user.procrelation.dto.request.AddGoodsToTemplateRequest;
import com.yiling.user.procrelation.dto.request.UpdateGoodsRebateRequest;
import com.yiling.user.procrelation.entity.PopProcGoodsTemplateDO;
import com.yiling.user.procrelation.entity.PopProcTemplateDO;
import com.yiling.user.procrelation.enums.ProcRelationErrorCode;
import com.yiling.user.procrelation.service.PopProcGoodsTemplateService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * pop采购关系的可采商品导入模板表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-06-19
 */
@Slf4j
@Service
public class PopProcGoodsTemplateServiceImpl extends BaseServiceImpl<PopProcGoodsTemplateMapper, PopProcGoodsTemplateDO> implements PopProcGoodsTemplateService {

    @Autowired
    PopProcTemplateMapper popProcTemplateMapper;

    @Override
    public List<PopProcGoodsTemplateDO> queryGoodsList(Long templateId, Long factoryEid) {
        if (templateId == null || templateId == 0) {
            return ListUtil.toList();
        }
        if (factoryEid == null || factoryEid == 0) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<PopProcGoodsTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PopProcGoodsTemplateDO::getTemplateId, templateId);
        wrapper.eq(PopProcGoodsTemplateDO::getFactoryEid, factoryEid);

        return list(wrapper);
    }

    @Override
    public Boolean addGoodsToTemplate(Long templateId, List<AddGoodsToTemplateRequest> goodsList) {
        if (templateId == null || templateId == 0) {
            return Boolean.FALSE;
        }
        PopProcTemplateDO templateDO = popProcTemplateMapper.selectById(templateId);
        if (templateDO == null) {
            log.warn("向导入模板新增商品时，模板不存在，模板id={}", templateId);
            throw new BusinessException(ProcRelationErrorCode.IMPORT_GOODS_TEMPLATE_NOT_FOUND);
        }
        if (CollUtil.isEmpty(goodsList)) {
            return Boolean.TRUE;
        }
        List<Long> goodsIdList = goodsList.stream().map(AddGoodsToTemplateRequest::getGoodsId).collect(Collectors.toList());
        LambdaQueryWrapper<PopProcGoodsTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PopProcGoodsTemplateDO::getTemplateId, templateId);
        wrapper.in(PopProcGoodsTemplateDO::getGoodsId, goodsIdList);
        List<PopProcGoodsTemplateDO> list = list(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            List<String> goodsNameList = list.stream().map(PopProcGoodsTemplateDO::getGoodsName).collect(Collectors.toList());
            log.error("向模板添加商品时，商品已经存在，商品列表为={}", list);
            throw new BusinessException(ProcRelationErrorCode.TEMPLATE_GOODS_ALREADY, String.join("，", goodsNameList) + "已经存在");
        }

        List<PopProcGoodsTemplateDO> templateDOList = PojoUtils.map(goodsList, PopProcGoodsTemplateDO.class);

        templateDOList.forEach(e -> {
            e.setTemplateId(templateId);
        });

        return saveBatch(templateDOList);
    }

    @Override
    public Boolean deleteGoods(Long id, Long opUser) {
        if (id == null || id == 0) {
            return Boolean.FALSE;
        }
        PopProcGoodsTemplateDO goodsTemplateDO = getById(id);
        if (goodsTemplateDO == null) {
            return Boolean.FALSE;
        }
        goodsTemplateDO.setOpUserId(opUser);
        deleteByIdWithFill(goodsTemplateDO);
        return deleteByIdWithFill(goodsTemplateDO) > 0;
    }

    @Override
    public Boolean updateGoodsRebate(UpdateGoodsRebateRequest request) {
        if (CollUtil.isEmpty(request.getIdList())) {
            return Boolean.FALSE;
        }
        List<PopProcGoodsTemplateDO> list = ListUtil.toList();
        request.getIdList().forEach(e -> {
            PopProcGoodsTemplateDO var = new PopProcGoodsTemplateDO();
            var.setId(e);
            var.setRebate(request.getRebate());
            var.setOpTime(request.getOpTime());
            var.setOpUserId(request.getOpUserId());
            list.add(var);
        });
        return updateBatchById(list);
    }

    @Override
    public Map<String, List<PopProcGoodsTemplateDO>> queryBatchGoodsList(List<String> templateNumbers) {
        Map<String, List<PopProcGoodsTemplateDO>> result=MapUtil.newHashMap();
        if (CollUtil.isEmpty(templateNumbers)) {
            return result;
        }
        LambdaQueryWrapper<PopProcTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(PopProcTemplateDO::getTemplateNumber, templateNumbers);
        List<PopProcTemplateDO> templateDOS = popProcTemplateMapper.selectList(wrapper);

        if (CollUtil.isEmpty(templateDOS)){
            return result;
        }
        Map<Long, String> templateMap = templateDOS.stream().collect(Collectors.toMap(PopProcTemplateDO::getId, PopProcTemplateDO::getTemplateNumber));


        LambdaQueryWrapper<PopProcGoodsTemplateDO> goodsWrapper = Wrappers.lambdaQuery();
        goodsWrapper.in(PopProcGoodsTemplateDO::getTemplateId, templateMap.keySet());

        List<PopProcGoodsTemplateDO> list = list(goodsWrapper);
        if (CollUtil.isEmpty(list)) {
            return result;
        }

        Map<Long, List<PopProcGoodsTemplateDO>> tempMap = list.stream().collect(Collectors.groupingBy(PopProcGoodsTemplateDO::getTemplateId));

        tempMap.forEach((k,v)->{
            String number = templateMap.getOrDefault(k,"");
            result.put(number,v);
        });
        return result;
    }
}
