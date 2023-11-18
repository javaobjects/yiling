package com.yiling.search.word.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.search.word.entity.EsWordExpansionDO;

/**
 * @author shichen
 * @类名 EsWordExpansionMapper
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Repository
public interface EsWordExpansionMapper extends BaseMapper<EsWordExpansionDO> {

    /**
     * 词语类型获取组号集合
     * @param type
     * @return
     */
    List<Long> getGroupByType(@Param("type") Integer type);
}
