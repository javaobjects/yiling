package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordDO;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 区域备案 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
public interface UnlockAreaRecordService extends BaseService<UnlockAreaRecordDO> {

    Page<UnlockAreaRecordDO> listPage(QueryUnlockAreaRecordPageRequest request);

    void add(SaveOrUpdateUnlockAreaRecordRequest request);

    void update(SaveOrUpdateUnlockAreaRecordRequest request);

    UnlockAreaRecordDO getById(Long id);

    List<UnlockAreaRecordDO> getListByClassAndCategory(Integer customerClassification, Long categoryId);

    UnlockAreaRecordDO getByClassAndCategoryIdAndRegionCode(Integer customerClassification, Long categoryId, String regionCode);

    void delete(Long id);
}
