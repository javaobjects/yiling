package com.yiling.open.erp.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ErpFlowSealedDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedPageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;
import com.yiling.open.erp.entity.ErpFlowSealedDO;

/**
 * 流向封存 服务类
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
public interface ErpFlowSealedService {

    /**
     * 查询流向封存信息列表分页
     *
     * @param request
     * @return
     */
    Page<ErpFlowSealedDO> page(QueryErpSealedPageRequest request);

    /**
     * 根据商业id查询列表
     *
     * @param eidList
     * @return
     */
    List<ErpFlowSealedDO> getByEidList(List<Long> eidList);

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
    ErpFlowSealedDO getErpFlowSealedById(Long id);

    /**
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
    ErpFlowSealedDO getErpFlowSealedByEidAndTypeAndMonth(QueryErpFlowSealedRequest requerst);

}
