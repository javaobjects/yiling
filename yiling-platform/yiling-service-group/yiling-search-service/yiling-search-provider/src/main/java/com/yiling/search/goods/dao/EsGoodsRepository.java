package com.yiling.search.goods.dao;


import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.yiling.search.goods.entity.EsGoodsEntity;

/**
 * @author shichen
 * @类名 EsGoodsRepository
 * @描述
 * @创建时间 2022/8/18
 * @修改人 shichen
 * @修改时间 2022/8/18
 **/
@Repository
public interface EsGoodsRepository extends ElasticsearchRepository<EsGoodsEntity, String> {

    List<EsGoodsEntity> findByNameAndEid(String name,Long eid);

}
