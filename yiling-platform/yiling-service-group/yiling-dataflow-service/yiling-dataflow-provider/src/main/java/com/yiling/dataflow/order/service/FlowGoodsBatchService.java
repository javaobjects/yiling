package com.yiling.dataflow.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchPageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 采购流向表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Service
public interface FlowGoodsBatchService extends BaseService<FlowGoodsBatchDO> {

    /**
     * 通过op库主键和商业公司查询流向是否存在
     * @param request
     * @return
     */
    Integer deleteFlowGoodsBatchByGbIdNoAndEid(DeleteFlowGoodsBatchRequest request);

    /**
     * 通过主键id列表删除
     *
     * @param idList
     * @return
     */
    Integer deleteByIdList(List<Long> idList);

    /**
     * 通过op库主键和商业公司查询流向列表数据
     * @param request
     * @return
     */
    List<FlowGoodsBatchDO> getFlowGoodsBatchDTOByGbIdNoAndEid(QueryFlowGoodsBatchRequest request);

    /**
     * 获取商品批次
     * @param request
     * @return
     */
    List<FlowGoodsBatchDO> getFlowGoodsBatchDTOByInSnAndEid(QueryFlowGoodsBatchRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Integer updateFlowGoodsBatchByGbIdNoAndEid(SaveOrUpdateFlowGoodsBatchRequest request);

    Integer updateFlowGoodsBatchByInSnAndEid(SaveOrUpdateFlowGoodsBatchRequest request);
    /**
     * 新增流向
     * @param request
     * @return
     */
    Integer insertFlowGoodsBatch(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDO> page(QueryFlowGoodsBatchListPageRequest request);

    /**
     * 硬删除
     * @param eids
     * @param createTime
     * @return
     */
    Integer deleteFlowGoodsBatchByEids(List<Long> eids, Date createTime);

    /**
     * 更新同一规格的商品库存总数 totalNumber
     *
     * @param idList
     * @param totalNumber
     * @return
     */
    Boolean updateFlowGoodsBatchTotalNumberByIdList(List<Long> idList, BigDecimal totalNumber);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowGoodsBatchById(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowGoodsBatchByIds(List<SaveOrUpdateFlowGoodsBatchRequest> request);
    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDO> pageByEidAndGoodsInSn(QueryFlowGoodsBatchPageByEidAndGoodsInSnRequest request);

    /**
     * 库存流向商品库存数量统计
     */
    void statisticsFlowGoodsBatchTotalNumber();

    void syncFlowGoodsBatchSpec();

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    boolean isUsedSpecificationId(Long specificationId);

    void updateFlowGoodsBatchCrmGoodsSign(List<Long> crmIds);

    /**
     * 根据企业id获取最新的销售时间
     *
     * @return
     */
    Date getMaxGbTimeByEid(Long eid);
}
