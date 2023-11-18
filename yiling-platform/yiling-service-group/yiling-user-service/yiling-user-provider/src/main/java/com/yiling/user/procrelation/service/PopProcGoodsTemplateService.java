package com.yiling.user.procrelation.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.procrelation.dto.PopProcGoodsTemplateDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsToTemplateRequest;
import com.yiling.user.procrelation.dto.request.UpdateGoodsRebateRequest;
import com.yiling.user.procrelation.entity.PopProcGoodsTemplateDO;

/**
 * <p>
 * pop采购关系的可采商品导入模板表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-06-19
 */
public interface PopProcGoodsTemplateService extends BaseService<PopProcGoodsTemplateDO> {

    /**
     * 查询商品列表
     *
     * @param templateId 模板id
     * @param factoryEid 工业id
     * @return
     */
    List<PopProcGoodsTemplateDO> queryGoodsList(Long templateId, Long factoryEid);

    /**
     * 向导入模板中添加商品
     *
     * @param templateId 模板id
     * @param goodsList  商品列表
     * @return
     */
    Boolean addGoodsToTemplate(Long templateId,List<AddGoodsToTemplateRequest> goodsList);

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
     * @param templateNumbers 模板编码
     * @return
     */
    Map<String,List<PopProcGoodsTemplateDO>> queryBatchGoodsList(List<String> templateNumbers);
}
