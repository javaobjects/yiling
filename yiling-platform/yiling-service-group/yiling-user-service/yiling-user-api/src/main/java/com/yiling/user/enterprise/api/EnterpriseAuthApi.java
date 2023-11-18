package com.yiling.user.enterprise.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseAuthPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;

/**
 * 企业副本 API
 *
 * @author: lun.yu
 * @date: 2021/11/2
 */
public interface EnterpriseAuthApi {

    /**
     * 新增企业副本
     * @param request
     * @return
     */
    Long addEnterpriseAuth(EnterpriseAuthInfoRequest request);

    /**
     * 获取最新的企业审核信息
     * @param eid
     * @return
     */
    EnterpriseAuthInfoDTO getByEid(Long eid);

    /**
     * 获取企业审核信息集合
     * @param eid
     * @return
     */
    List<EnterpriseAuthInfoDTO> queryListByEid(Long eid);

    /**
     * 获取企业副本列表
     * @param request
     * @return
     */
    Page<EnterpriseAuthInfoDTO> pageList(QueryEnterpriseAuthPageRequest request);

    /**
     * 审核
     * @param request
     * @return
     */
    Boolean updateAuth(UpdateEnterpriseAuthRequest request);
}
