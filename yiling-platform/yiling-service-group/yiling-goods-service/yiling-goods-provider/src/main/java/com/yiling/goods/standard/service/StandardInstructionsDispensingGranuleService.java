package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsDispensingGranuleDO;

/**
 * @author shichen
 * @类名 StandardInstructionsDispensingGranuleService
 * @描述 配方颗粒
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
public interface StandardInstructionsDispensingGranuleService  extends BaseService<StandardInstructionsDispensingGranuleDO> {

    /**
     * 根据StandardId找到中药饮片商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsDispensingGranuleDO getInstructionsDispensingGranuleByStandardId(Long standardId);

    /**
     * 保存中药饮片商品说明书
     *
     * @param one
     * @return
     */
    Boolean saveInstructionsDispensingGranuleOne(StandardInstructionsDispensingGranuleDO one);
}
