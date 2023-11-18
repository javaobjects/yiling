package com.yiling.sjms.crm.dao;

import java.util.List;

import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationFormDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 院外药店关系流程表单明细表 Dao 接口
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-06-07
 */
@Repository
public interface HospitalDrugstoreRelationFormMapper extends BaseMapper<HospitalDrugstoreRelationFormDO> {

    List<Long> selectDrugOrgIdFormIdByHosOrgId(@Param(value = "formId") Long formId, @Param(value = "hospitalOrgId") Long hospitalOrgId);
}
