package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsMedicalInstrumentDO;

/**
 * @author shichen
 * @类名 StandardInstructionsMedicalInstrumentService
 * @描述 医疗器械service层
 * @创建时间 2022/7/18
 * @修改人 shichen
 * @修改时间 2022/7/18
 **/
public interface StandardInstructionsMedicalInstrumentService extends BaseService<StandardInstructionsMedicalInstrumentDO> {

    /**
     * 根据StandardId找到医疗器械说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsMedicalInstrumentDO getInstructionsMedicalInstrumentByStandardId(Long standardId);

    /**
     * 保存医疗器械说明书
     * @param one
     * @return
     */
    Boolean saveInstructionsMedicalInstrumentOne(StandardInstructionsMedicalInstrumentDO one);
}
