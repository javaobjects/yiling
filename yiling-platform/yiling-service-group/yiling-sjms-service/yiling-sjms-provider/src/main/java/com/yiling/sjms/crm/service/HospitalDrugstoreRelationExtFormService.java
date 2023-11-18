package com.yiling.sjms.crm.service;

import java.util.List;

import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.crm.dto.request.DeleteHosDruRelFormAppendixRequest;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationExtFormDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 院外药店关系流程表单扩展信息表 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-06-07
 */
public interface HospitalDrugstoreRelationExtFormService extends BaseService<HospitalDrugstoreRelationExtFormDO> {

    void removeById(DeleteHosDruRelFormAppendixRequest request);

    HospitalDrugstoreRelationExtFormDO detailByFormId(Long formId);
}
