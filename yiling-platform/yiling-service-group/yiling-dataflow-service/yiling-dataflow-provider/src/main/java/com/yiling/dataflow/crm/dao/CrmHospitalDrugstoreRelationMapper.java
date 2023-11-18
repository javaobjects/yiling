package com.yiling.dataflow.crm.dao;

import java.util.List;

import com.yiling.dataflow.crm.bo.CrmHosDruRelOrgIdBO;
import com.yiling.dataflow.crm.entity.CrmHospitalDrugstoreRelationDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 院外药店关系绑定表 Dao 接口
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-30
 */
@Repository
public interface CrmHospitalDrugstoreRelationMapper extends BaseMapper<CrmHospitalDrugstoreRelationDO> {

    List<Long> selectDrugOrgIdByHosOrgId(@Param(value = "hospitalOrgId") Long hospitalOrgId);

    List<CrmHosDruRelOrgIdBO> listGroupByHospitalIdAndDrugstoreId();

}
