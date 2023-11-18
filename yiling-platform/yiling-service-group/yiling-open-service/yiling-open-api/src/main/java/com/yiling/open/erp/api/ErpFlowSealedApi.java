package com.yiling.open.erp.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.bo.ErpClientBO;
import com.yiling.open.erp.dto.ErpFlowSealedDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedEnterprisePageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedPageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;

/**
 * 流向封存
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
public interface ErpFlowSealedApi {

    /**
     * 查询流向封存信息列表分页
     *
     * @param request
     * @return
     */
    Page<ErpFlowSealedDTO> page(QueryErpSealedPageRequest request);

    /**
     * 根据商业id查询列表
     *
     * @param eidList
     * @return
     */
    List<ErpFlowSealedDTO> getByEidList(List<Long> eidList);

    /**
     * 获取封存月份列表（仅支持前推6个整月）
     *
     * @param count
     * @return
     */
    List<String> monthList(Integer count);

    /**
     * 流向添加封存-保存
     *
     * @param request
     * @return
     */
    Boolean save(QueryErpSealedSaveRequest request);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    ErpFlowSealedDTO getErpFlowSealedByEidAndTypeAndMonth(Long id);

    /**
     * 封存、解封
     *
     * @return
     */
    Boolean lockOrUnLock(ErpFlowSealedLockOrUnlockRequest request);

    /**
     * 根据条件查询
     *
     * @param requerst
     * @return
     */
    ErpFlowSealedDTO getErpFlowSealedByEidAndTypeAndMonth(QueryErpFlowSealedRequest requerst);

}
