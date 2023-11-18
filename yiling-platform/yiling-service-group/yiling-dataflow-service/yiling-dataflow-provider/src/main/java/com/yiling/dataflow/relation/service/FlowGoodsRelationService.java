package com.yiling.dataflow.relation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Service
public interface FlowGoodsRelationService extends BaseService<FlowGoodsRelationDO> {

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsRelationDO> page(QueryFlowGoodsRelationPageRequest request);

    /**
     * 根据id查询映射关系
     * @param id
     * @return
     */
    FlowGoodsRelationDO getById(Long id);

    /**
     * 根据id列表查询映射关系
     * @param idList
     * @return
     */
    List<FlowGoodsRelationDO> getByIdList(List<Long> idList);

    /**
     * 修改
     * @param request
     * @return
     */
    Boolean edit(UpdateFlowGoodsRelationRequest request);

    /**
     * 根据商业id、商业商品名称查询列表
     *
     * @param eid
     * @param goodsName
     * @return
     */
    List<FlowGoodsRelationDO> getListByEidAndGoodsName(Long eid, String goodsName);

    /**
     * 根据商业id、以岭商品id查询列表
     *
     * @param eid
     * @param ylGoodsId
     * @return
     */
    List<FlowGoodsRelationDO> getListByEidAndYlGoodsId(Long eid, Long ylGoodsId);

    /**
     * 根据商业id、商业商品内码查询映射关系
     *
     * @param eid
     * @param goodsInSn
     * @return
     */
    FlowGoodsRelationDO getByEidAndGoodsInSn(Long eid, String goodsInSn);

    /**
     * 获取映射关系的以岭品id
     *
     *
     * @param request
     * @return ylGoodsId 以岭品id，值为0表示没有匹配关系
     */
    void saveOrUpdateByFlowSync(QueryFlowGoodsRelationYlGoodsIdRequest request);

    /**
     * 根据商品审核的商品信息保存或更新映射关系
     *
      * @param request
     * @return
     */
    Boolean saveOrUpdateByGoodsAuditApproved(SaveOrUpdateFlowGoodsRelationRequest request);

    /**
     * 根据商业id、商业商品内码查询映射关系
     *
     * @param eid
     * @param goodsInSnList
     * @return
     */
    List<FlowGoodsRelationDO> getByEidAndGoodsInSn(Long eid, List<String> goodsInSnList);

    /**
     * 根据商业id、商业商品内码查询映射关系
     *
     * @param eidList
     * @param goodsInSnList
     * @return
     */
    List<FlowGoodsRelationDO> getByEidAndGoodsInSn(List<Long> eidList, List<String> goodsInSnList);

    /**
     * 根据商业id、商品内码、以岭品id查询映射关系
     *
     * @param list
     * @return
     */
    List<FlowGoodsRelationDO> getByEidAndGoodsInSnAndYlGoodsId(List<QueryFlowGoodsRelationListRequest> list);

    List<FlowGoodsRelationDO> statisticsByYlGoodsId();

}
