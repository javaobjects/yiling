package com.yiling.search.flow.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yiling.search.flow.entity.EsFlowEnterpriseCustomerMappingEntity;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingRepository
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
public interface EsFlowEnterpriseCustomerMappingRepository extends ElasticsearchRepository<EsFlowEnterpriseCustomerMappingEntity,String> {
}
