package com.yiling.dataflow.crm.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsInfoApiImpl
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@DubboService
@Slf4j
public class CrmGoodsInfoApiImpl implements CrmGoodsInfoApi {
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;

    @Override
    public CrmGoodsInfoDTO findByCodeAndName(Long goodsCode, String goodsName) {
        return crmGoodsInfoService.findByCodeAndName(goodsCode,goodsName);
    }

    @Override
    public List<CrmGoodsInfoDTO> findByNameAndSpec(String goodsName,String goodsSpec,Integer limit) {
        return crmGoodsInfoService.findByNameAndSpec(goodsName,goodsSpec,limit);
    }

    @Override
    public CrmGoodsInfoDTO getByNameAndSpec(String goodsName, String goodsSpec, Integer goodsStatus) {
        return crmGoodsInfoService.getByNameAndSpec(goodsName,goodsSpec,goodsStatus);
    }

    @Override
    public List<CrmGoodsInfoDTO> findByCodeList(List<Long> goodsCodeList) {
        return crmGoodsInfoService.findByCodeList(goodsCodeList);
    }

    @Override
    public List<CrmGoodsInfoDTO> getBakCrmGoodsInfoAll(String tableSuffix) {
        return crmGoodsInfoService.getBakCrmGoodsInfoAll(tableSuffix);
    }

    @Override
    public List<CrmGoodsInfoDTO> findBakByCodeList(List<Long> goodsCodeList, String tableSuffix) {
        return crmGoodsInfoService.findBakByCodeList(goodsCodeList,tableSuffix);
    }

    @Override
    public Long saveGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request) {
        return crmGoodsInfoService.saveGoodsInfo(request);
    }

    @Override
    public Long editGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request) {
        return crmGoodsInfoService.editGoodsInfo(request);
    }

    @Override
    public Page<CrmGoodsInfoDTO> getPage(QueryCrmGoodsInfoPageRequest request) {
        return crmGoodsInfoService.getPage(request);
    }

    @Override
    public Page<CrmGoodsInfoDTO> getBakPage(QueryCrmGoodsInfoPageRequest request, String tableSuffix) {
        return crmGoodsInfoService.getBakPage(request,tableSuffix);
    }

    @Override
    public Page<CrmGoodsInfoDTO> getPopupPage(QueryCrmGoodsInfoPageRequest request) {
        return crmGoodsInfoService.getPopupPage(request);
    }

    @Override
    public CrmGoodsInfoDTO findById(Long id) {
        return PojoUtils.map(crmGoodsInfoService.getById(id),CrmGoodsInfoDTO.class);
    }

    @Override
    public List<CrmGoodsInfoDTO> findByIds(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        return PojoUtils.map(crmGoodsInfoService.listByIds(ids),CrmGoodsInfoDTO.class);
    }

    @Override
    public Map<Long, List<CrmGoodsInfoDTO>> findByGroupIds(List<Long> groupIds) {
        return crmGoodsInfoService.findByGroupIds(groupIds);
    }
}
