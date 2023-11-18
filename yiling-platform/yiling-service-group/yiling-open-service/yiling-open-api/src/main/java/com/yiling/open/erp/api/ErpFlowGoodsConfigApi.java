package com.yiling.open.erp.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ErpFlowGoodsConfigDTO;
import com.yiling.open.erp.dto.request.DeleteErpFlowGoodsConfigRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowGoodsConfigPageRequest;
import com.yiling.open.erp.dto.request.SaveErpFlowGoodsConfigRequest;

/**
 * @author: houjie.sun
 * @date: 2022/4/26
 */
public interface ErpFlowGoodsConfigApi {

    /**
     * 查询流向封存信息列表分页
     *
     * @param request
     * @return
     */
    Page<ErpFlowGoodsConfigDTO> page(QueryErpFlowGoodsConfigPageRequest request);

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
    ErpFlowGoodsConfigDTO getByEidAndGoodsInSn(Long eid, String goodsInSn);

    /**
     * 根据id查询流向商品配置
     *
     * @param id
     * @return
     */
    ErpFlowGoodsConfigDTO getById(Long id);

    /**
     * 删除
     *
     * @param request
     * @return
     */
    Boolean deleteById(DeleteErpFlowGoodsConfigRequest request);

}
