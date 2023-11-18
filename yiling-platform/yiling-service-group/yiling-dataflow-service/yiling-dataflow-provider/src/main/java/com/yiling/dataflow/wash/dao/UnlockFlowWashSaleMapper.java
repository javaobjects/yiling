package com.yiling.dataflow.wash.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 流向销售合并报 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Repository
public interface UnlockFlowWashSaleMapper extends BaseMapper<UnlockFlowWashSaleDO> {
    Integer deleteByUfwtId(@Param("ufwtId")Long ufwtId);
}
