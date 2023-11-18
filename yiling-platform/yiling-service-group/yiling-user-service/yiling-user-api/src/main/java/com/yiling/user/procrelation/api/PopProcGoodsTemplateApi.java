package com.yiling.user.procrelation.api;

import java.util.List;
import java.util.Map;

import com.yiling.user.procrelation.dto.PopProcGoodsTemplateDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsToTemplateRequest;
import com.yiling.user.procrelation.dto.request.UpdateGoodsRebateRequest;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
public interface PopProcGoodsTemplateApi {

    /**
     * 查询商品列表
     *
     * @param templateId 模板id
     * @param factoryEid  工业id
     * @return
     */
    List<PopProcGoodsTemplateDTO> queryGoodsList(Long templateId, Long factoryEid);

    /**
     * 向导入模板中添加商品
     *
     * @param templateId 模板id
     * @param goodsList  商品列表
     * @return
     */
    Boolean addGoodsToTemplate(Long templateId,List<AddGoodsToTemplateRequest> goodsList);

    /**
     * 根据id查询模板商品
     *
     * @param id
     * @return
     */
    PopProcGoodsTemplateDTO queryTemplateGoodsById(Long id);
    /**
     * 从模板中移除商品
     *
     * @param id
     * @param opUser
     * @return
     */
    Boolean deleteGoods(Long id,Long opUser);

    /**
     * 批量更新优惠折扣
     *
     * @param request
     * @return
     */
    Boolean updateGoodsRebate(UpdateGoodsRebateRequest request);

    /**
     * 查询商品列表
     *
     * @param templateNumbers 模板id
     * @return
     */
    Map<String,List<PopProcGoodsTemplateDTO>> queryBatchGoodsList(List<String> templateNumbers);
}
