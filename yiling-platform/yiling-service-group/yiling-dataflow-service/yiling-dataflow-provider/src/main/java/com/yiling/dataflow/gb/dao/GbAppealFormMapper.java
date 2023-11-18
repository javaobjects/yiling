package com.yiling.dataflow.gb.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.UpdateGbAppealFormFlowMatchNumberRequest;
import com.yiling.dataflow.gb.entity.GbAppealFormDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向团购申诉申请表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Repository
public interface GbAppealFormMapper extends BaseMapper<GbAppealFormDO> {

    Page<GbAppealFormDO> listPage(Page<GbAppealFormDO> page, @Param("request") QueryGbAppealFormListPageRequest request);

    Page<GbAppealFormFlowStatisticBO> flowStatisticListPage(@Param("request") QueryGbAppealFormFlowStatisticPageRequest request, Page<GbAppealFormFlowStatisticBO> page);

    BigDecimal getTotalFlowMatchQuantityByAppealFormId(@Param("appealFormId") Long appealFormId);

    boolean updateFlowMatchNumberById(@Param("request") UpdateGbAppealFormFlowMatchNumberRequest request);
}
