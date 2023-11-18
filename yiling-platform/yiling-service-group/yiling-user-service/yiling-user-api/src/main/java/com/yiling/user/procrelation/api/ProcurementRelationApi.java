package com.yiling.user.procrelation.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateProcRelationRequest;

/**
 * @author: dexi.yao
 * @date: 2023-05-19
 */
public interface ProcurementRelationApi {

    /**
     * 新增pop采购关系
     *
     * @param request
     * @return
     */
    Long saveProcurementRelation(SaveProcRelationRequest request);

    /**
     * 新增pop采购关系
     *
     * @param request 入参
     * @return 采购关系id
     */
    Long updateProcurementRelation(UpdateProcRelationRequest request);

    /**
     * 分页查询pop采购关系
     *
     * @param request
     * @return
     */
    Page<ProcurementRelationDTO> queryProcRelationPage(QueryProcRelationPageRequest request);

    /**
     * 根据id查询采购关系信息
     *
     * @param id
     * @return
     */
    ProcurementRelationDTO queryProcRelationById(Long id);

    /**
     * 停用pop采购关系
     *
     * @param relationId
     * @param opUser
     * @return
     */
    Boolean closeProcRelationById(Long relationId, Long opUser);

    /**
     * 删除pop采购关系
     *
     * @param relationId
     * @param opUser
     * @return
     */
    Boolean deleteProcRelationById(Long relationId, Long opUser);

    void initData();

    /**
     * 生效采购关系
     */
    void updateInProgress();

    /**
     * 过期采购关系
     */
    void updateExpired();

    void initEnterpriseSupplier();
}
