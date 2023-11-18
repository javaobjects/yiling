package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
public interface UnlockAreaRecordRelationApi {

    List<UnlockAreaRecordRelationDTO> getListByUnlockAreaRecordRelationId(Long unlockAreaRecordRelationId);

    List<UnlockAreaRecordRelationDTO> getByClassAndCategoryId(Integer customerClassification, Long categoryId);
}
