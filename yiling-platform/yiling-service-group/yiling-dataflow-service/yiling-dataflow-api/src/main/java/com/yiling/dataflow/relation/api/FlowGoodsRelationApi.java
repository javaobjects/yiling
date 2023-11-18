package com.yiling.dataflow.relation.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;

/**
 * 商家品和以岭品关系 接口
 *
 * @author: houjie.sun
 * @date: 2022/5/23
 */
public interface FlowGoodsRelationApi {

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsRelationDTO> page(QueryFlowGoodsRelationPageRequest request);

    /**
     * 根据id查询映射关系
     * @param id
     * @return
     */
    FlowGoodsRelationDTO getById(Long id);

    /**
     * 根据id列表查询映射关系
     *
     * @param idList
     * @return
     */
    List<FlowGoodsRelationDTO> getByIdList(List<Long> idList);

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
    List<FlowGoodsRelationBO> getListByEidAndGoodsName(Long eid, String goodsName);

    /**
     * 根据商业id、以岭商品id查询列表
     *
     * @param eid
     * @param ylGoodsId
     * @return
     */
    List<FlowGoodsRelationBO> getListByEidAndYlGoodsId(Long eid, Long ylGoodsId);

    /**
     * 根据商业id、商业商品内码查询映射关系
     *
     * @param eid
     * @param goodsInSn
     * @return
     */
    FlowGoodsRelationDTO getByEidAndGoodsInSn(Long eid, String goodsInSn);

    /**
     * 获取映射关系的以岭品id
     *
     * @param request
     * @return ylGoodsId 以岭品id，值为0表示没有匹配关系
     */
    void saveOrUpdateByFlowSync(QueryFlowGoodsRelationYlGoodsIdRequest request);

    /**
     * 根据商业id、商业商品内码查询映射关系
     *
     * @param eidList
     * @param goodsInSnList
     * @return
     */
    List<FlowGoodsRelationDTO> getByEidAndGoodsInSn(List<Long> eidList, List<String> goodsInSnList);
}
