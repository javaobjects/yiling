package com.yiling.user.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.QueryPositionPageListRequest;
import com.yiling.user.enterprise.dto.request.SavePositionRequest;
import com.yiling.user.enterprise.dto.request.UpdatePositionStatusRequest;
import com.yiling.user.enterprise.entity.EnterprisePositionDO;

/**
 * <p>
 * 企业职位信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-09-29
 */
public interface EnterprisePositionService extends BaseService<EnterprisePositionDO> {

    /**
     * 查询职位分页列表
     *
     * @param request
     * @return
     */
    Page<EnterprisePositionDO> pageList(QueryPositionPageListRequest request);

    /**
     * 新增/修改职位信息
     *
     * @param request
     * @return
     */
    boolean save(SavePositionRequest request);

    /**
     * 修改职位状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdatePositionStatusRequest request);

}
