package com.yiling.dataflow.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.crm.entity.CrmGoodsTagDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author shichen
 * @类名 CrmGoodsTagMapper
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Repository
public interface CrmGoodsTagMapper extends BaseMapper<CrmGoodsTagDO> {

    /**
     * 查询商品下所有标签
     * @param crmGoodsId
     * @return
     */
    List<CrmGoodsTagDO> findTagByGoodsId(@Param("crmGoodsId") Long crmGoodsId);
}
