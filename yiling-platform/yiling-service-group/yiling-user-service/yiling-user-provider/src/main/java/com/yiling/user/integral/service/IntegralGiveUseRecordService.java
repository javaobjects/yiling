package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveUseRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.entity.IntegralGiveUseRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分发放/扣减记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface IntegralGiveUseRecordService extends BaseService<IntegralGiveUseRecordDO> {

    /**
     * 积分发放/扣减记录分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralGiveUseRecordBO> queryListPage(QueryIntegralRecordRequest request);

    /**
     * 积分发放/扣减记录列表查询
     *
     * @param request
     * @return
     */
    List<IntegralGiveUseRecordDTO> queryList(QueryIntegralGiveUseRecordRequest request);

    /**
     * 添加积分发放/扣减记录
     *
     * @param request
     * @return
     */
    IntegralGiveUseRecordDO addRecord(AddIntegralRecordRequest request);
}
