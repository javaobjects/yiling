package com.yiling.dataflow.relation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 商家品和以岭品关系表 Dao 接口
 * </p>
 *
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Repository
public interface FlowGoodsRelationMapper extends BaseMapper<FlowGoodsRelationDO> {

    Page<FlowGoodsRelationDO> page(Page page, @Param("request") QueryFlowGoodsRelationPageRequest request);

    List<FlowGoodsRelationDO> statisticsByYlGoodsId();
}
