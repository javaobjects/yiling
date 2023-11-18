package com.yiling.dataflow.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.crm.entity.CrmGoodsCategoryDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryMapper
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Repository
public interface CrmGoodsCategoryMapper extends BaseMapper<CrmGoodsCategoryDO> {

    List<Map<Long, Long>> getGoodsCountByCategory(@Param("categoryIds") List<Long> categoryIds);

    /**
     * 获取所有层级
     * @return
     */
    List<Integer> getAllLevel();
}
