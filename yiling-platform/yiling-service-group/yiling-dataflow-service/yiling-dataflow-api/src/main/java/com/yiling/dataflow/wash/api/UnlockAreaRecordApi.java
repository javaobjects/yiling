package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordDTO;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
public interface UnlockAreaRecordApi {

    Page<UnlockAreaRecordDTO> listPage(QueryUnlockAreaRecordPageRequest request);

    void add(SaveOrUpdateUnlockAreaRecordRequest request);

    void update(SaveOrUpdateUnlockAreaRecordRequest request);

    UnlockAreaRecordDTO getById(Long id);

    UnlockAreaRecordDTO getByClassAndCategoryIdAndRegionCode(Integer customerClassification, Long categoryId, String RegionCode);

    void delete(Long id);
}
