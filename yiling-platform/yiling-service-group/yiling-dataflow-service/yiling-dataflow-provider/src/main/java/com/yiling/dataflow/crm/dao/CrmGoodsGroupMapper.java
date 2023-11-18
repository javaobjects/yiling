package com.yiling.dataflow.crm.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsGroupDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
@Repository
public interface CrmGoodsGroupMapper extends BaseMapper<CrmGoodsGroupDO> {

    /**
     * 分页查询商品组
     * @param page
     * @param request
     * @return
     */
    Page<CrmGoodsGroupDO> queryGroupPage(Page<CrmGoodsGroupDO> page,@Param("request") QueryCrmGoodsGroupPageRequest request);

}
