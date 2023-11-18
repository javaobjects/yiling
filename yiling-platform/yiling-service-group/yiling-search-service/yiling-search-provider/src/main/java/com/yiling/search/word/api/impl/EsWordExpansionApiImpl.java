package com.yiling.search.word.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.search.word.api.EsWordExpansionApi;
import com.yiling.search.word.bo.EsWordAllTypeBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.QueryEsWordPageListRequest;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.enums.EsWordTypeEnum;
import com.yiling.search.word.service.EsWordExpansionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsWordExpansionApiImpl
 * @描述
 * @创建时间 2022/4/25
 * @修改人 shichen
 * @修改时间 2022/4/25
 **/
@DubboService
@Slf4j
public class EsWordExpansionApiImpl implements EsWordExpansionApi {
    @Autowired
    private EsWordExpansionService esWordExpansionService;

    @Value("${elasticsearch.goodsEsIndex}")
    private String goodsEsIndex;

    @Override
    public Page<EsWordExpansionDTO> pageListByType(QueryEsWordPageListRequest request) {
        return esWordExpansionService.pageListByType(request);
    }

    @Override
    public EsWordAllTypeBO findByWord(String word) {
        return esWordExpansionService.findByWord(word);
    }

    @Override
    public void saveOrUpdateWord(SaveOrUpdateEsWordExpansionRequest request) {
        esWordExpansionService.saveOrUpdateWord(request);
    }

    @Override
    public boolean addSyncJob(Integer type) {
        //暂时固定维护商品索引写死
        return esWordExpansionService.addSyncJob(type,goodsEsIndex);
    }

    @Override
    public void syncEsWordDic() {
        esWordExpansionService.syncEsWordDic();
    }

    @Override
    public void syncGoodsWordByType(EsWordTypeEnum typeEnum) {
        //暂时固定维护商品索引写死
        esWordExpansionService.syncWordByIndexAndType(typeEnum, goodsEsIndex,true);
    }

    @Override
    public void updateEsWordDic(EsWordTypeEnum esWordTypeEnum) {
        esWordExpansionService.updateEsWordDic(esWordTypeEnum,goodsEsIndex);
    }

    @Override
    public String getDownloadUrl(Integer type) {
        return esWordExpansionService.getDownloadUrl(type);
    }
}
