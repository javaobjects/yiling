package com.yiling.search.flow.dao;

import com.yiling.search.flow.entity.EsFlowGoodsBatchDetailEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Repository
public interface EsFlowGoodsBatchDetailRepository extends ElasticsearchRepository<EsFlowGoodsBatchDetailEntity, String> {
}
