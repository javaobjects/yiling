package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.entity.UnlockAreaRecordRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 区域备案 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-15
 */
public interface UnlockAreaRecordRelationService extends BaseService<UnlockAreaRecordRelationDO> {

    List<UnlockAreaRecordRelationDO> getByUnlockAreaRecordId(Long unlockAreaRecord);

    void deleteByUnlockAreaRecordId(Long unlockAreaRecord);

    List<UnlockAreaRecordRelationDO> getByClassAndCategoryId(Integer customerClassification, Long categoryId);


}
