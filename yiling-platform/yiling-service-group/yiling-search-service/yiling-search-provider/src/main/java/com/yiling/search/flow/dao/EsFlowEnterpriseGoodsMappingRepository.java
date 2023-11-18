package com.yiling.search.flow.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.yiling.search.flow.entity.EsFlowEnterpriseGoodsMappingEntity;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingRepository
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Repository
public interface EsFlowEnterpriseGoodsMappingRepository extends ElasticsearchRepository<EsFlowEnterpriseGoodsMappingEntity,String> {
}
