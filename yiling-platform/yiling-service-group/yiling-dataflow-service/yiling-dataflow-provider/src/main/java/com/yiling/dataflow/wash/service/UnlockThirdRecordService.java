package com.yiling.dataflow.wash.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;
import com.yiling.dataflow.wash.entity.UnlockThirdRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 小三批备案表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
public interface UnlockThirdRecordService extends BaseService<UnlockThirdRecordDO> {

    Page<UnlockThirdRecordDO> listPage(QueryUnlockThirdRecordPageRequest request);

    void add(SaveOrUpdateUnlockThirdRecordRequest request);

    void update(SaveOrUpdateUnlockThirdRecordRequest request);


    UnlockThirdRecordDO getByOrgCrmId(Long orgCrmId);

    void delete(Long id);
}
