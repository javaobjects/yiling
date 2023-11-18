package com.yiling.dataflow.crm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmHosDruRelOrgIdBO;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelActiveRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelEffectiveListRequest;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.entity.CrmHospitalDrugstoreRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 院外药店关系绑定表 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-30
 */
public interface CrmHospitalDrugstoreRelationService extends BaseService<CrmHospitalDrugstoreRelationDO> {

    Page<CrmHospitalDrugstoreRelationDO> listPage(QueryHospitalDrugstoreRelationPageRequest request);

    void saveOrUpdate(SaveOrUpdateCrmHospitalDrugstoreRelRequest request);

    List<CrmHosDruRelOrgIdBO> listGroupByHospitalIdAndDrugstoreId();

    List<Long> selectDrugOrgIdByHosOrgId(Long hospitalOrgId);

    void delete(RemoveHospitalDrugstoreRelRequest request);

    void disable(SaveOrUpdateCrmHospitalDrugstoreRelRequest request);

    /**
     * 获取未生效或生效中，且未停用的数据
     * @param request
     * @return
     */
    List<CrmHospitalDrugstoreRelationDO> getEffectiveList(QueryCrmHosDruRelEffectiveListRequest request);

    /**
     * 根据根据药店+商品 获取生效中的，且未停用的绑定关系数据
     * @return
     */
    CrmHospitalDrugstoreRelationDO getActiveDataByDrugstoreIdAndGoodsCode(QueryCrmHosDruRelActiveRequest request);
}
