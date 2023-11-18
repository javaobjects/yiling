package com.yiling.user.procrelation.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationByTimePageRequest;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateRelationStatusRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * pop采购关系表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
public interface ProcurementRelationService extends BaseService<ProcurementRelationDO> {

    /**
     * 新增pop采购关系
     *
     * @param request 入参
     * @return 采购关系id
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
     * 根据采购关系id递增版本号
     *
     * @param procRelationId
     * @param opUser
     */
    void increaseVersion(Long procRelationId,Long opUser);

    /**
     * 根据渠道商eid查询正在进行中的采购关系
     *
     * @param channelPartnerEid
     * @return
     */
    List<ProcurementRelationDO> queryInProRelationListByChannelEid(Long channelPartnerEid);

    /**
     * 根据渠道商eid和配送商eid查询正在进行中的采购关系
     *
     * @param deliveryEid       配送商eid
     * @param channelPartnerEid 渠道商eid
     * @return
     */
    List<ProcurementRelationDO> queryInProRelationListByEachOther(Long deliveryEid,Long channelPartnerEid);

    /**
     * 分页查询pop采购关系
     *
     * @param request
     * @return
     */
    Page<ProcurementRelationDO> queryProcRelationPage(QueryProcRelationPageRequest request);

    /**
     * 停用pop采购关系
     *
     * @param relationId
     * @param opUser
     * @return
     */
    Boolean closeProcRelationById(Long relationId,Long opUser);

    /**
     * 删除pop采购关系
     *
     * @param relationId
     * @param opUser
     * @return
     */
    Boolean deleteProcRelationById(Long relationId,Long opUser);

    /**
     * 初始化pop采购关系
     */
    void initData();

    /**
     * 生效采购关系
     */
    void updateInProgress();

    /**
     * 过期采购关系
     */
    void updateExpired();

    /**
     * 初始化供应商管理
     */
    void initEnterpriseSupplier();

}
