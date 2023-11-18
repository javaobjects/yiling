package com.yiling.dataflow.wash.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsCategoryBO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockSaleGoodsCategoryPageRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsCategoryDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Repository
public interface UnlockSaleGoodsCategoryMapper extends BaseMapper<UnlockSaleGoodsCategoryDO> {
    Page<UnlockSaleGoodsCategoryBO> listPage(@Param("request") QueryUnlockSaleGoodsCategoryPageRequest request, Page<UnlockSaleGoodsCategoryDO> page);
}
