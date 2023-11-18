package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
public interface UnlockThirdRecordApi {

    Page<UnlockThirdRecordDTO> listPage(QueryUnlockThirdRecordPageRequest request);

    void add(SaveOrUpdateUnlockThirdRecordRequest request);

    void update(SaveOrUpdateUnlockThirdRecordRequest request);

    UnlockThirdRecordDTO getById(Long id);

    UnlockThirdRecordDTO getByOrgCrmId(Long orgCrmId);

    void delete(Long id);
}
