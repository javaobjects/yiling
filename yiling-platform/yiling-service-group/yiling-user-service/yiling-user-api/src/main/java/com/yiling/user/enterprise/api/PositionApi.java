package com.yiling.user.enterprise.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterprisePositionDTO;
import com.yiling.user.enterprise.dto.request.QueryPositionPageListRequest;
import com.yiling.user.enterprise.dto.request.SavePositionRequest;
import com.yiling.user.enterprise.dto.request.UpdatePositionStatusRequest;

/**
 * 职位模块 API
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
public interface PositionApi {

    /**
     * 查询职位分页列表
     *
     * @param request
     * @return
     */
    Page<EnterprisePositionDTO> pageList(QueryPositionPageListRequest request);

    /**
     * 根据职位ID获取职位信息
     *
     * @param id 职位ID
     * @return
     */
    EnterprisePositionDTO getById(Long id);

    /**
     * 根据职位ID列表获取职位信息列表
     *
     * @param ids 职位ID列表
     * @return
     */
    List<EnterprisePositionDTO> listByIds(List<Long> ids);

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
