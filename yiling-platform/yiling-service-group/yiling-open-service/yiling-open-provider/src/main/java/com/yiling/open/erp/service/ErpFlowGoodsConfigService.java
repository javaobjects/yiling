package com.yiling.open.erp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.dto.request.DeleteErpFlowGoodsConfigRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowGoodsConfigPageRequest;
import com.yiling.open.erp.dto.request.SaveErpFlowGoodsConfigRequest;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;

/**
 * 流向非以岭商品配置 服务类
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */
public interface ErpFlowGoodsConfigService extends BaseService<ErpFlowGoodsConfigDO> {

    /**
     * 查询流向封存信息列表分页
     *
     * @param request
     * @return
     */
    Page<ErpFlowGoodsConfigDO> page(QueryErpFlowGoodsConfigPageRequest request);

    /**
     * 流向非以岭商品配置 保存
     *
     * @param request
     * @return
     */
    Boolean save(SaveErpFlowGoodsConfigRequest request);

    /**
     * 根据商业id、商品内码查询流向非以岭商品配置
     *
     * @return
     */
    ErpFlowGoodsConfigDO getByEidAndGoodsInSn(Long eid, String goodsInSn);

    /**
     * 删除
     *
     * @param request
     * @return
     */
    Integer deleteFlowGoodsConfigById(DeleteErpFlowGoodsConfigRequest request);

}
