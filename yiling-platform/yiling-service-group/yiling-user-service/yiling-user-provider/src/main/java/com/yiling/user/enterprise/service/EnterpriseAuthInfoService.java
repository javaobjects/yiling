package com.yiling.user.enterprise.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseAuthPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.entity.EnterpriseAuthInfoDO;

/**
 * <p>
 * 企业副本 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
public interface EnterpriseAuthInfoService extends BaseService<EnterpriseAuthInfoDO> {

    /**
     * 添加企业副本
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
     * 企业副本分页列表
     * @param request
     * @return
     */
    Page<EnterpriseAuthInfoDTO> pageList(QueryEnterpriseAuthPageRequest request);

    /**
     * 更新审核状态
     * @param request
     * @return
     */
    Boolean updateAuthStatus(UpdateEnterpriseAuthRequest request);

    /**
     * 审核
     * @param request
     * @return
     */
    Boolean updateAuth(UpdateEnterpriseAuthRequest request);

    /**
     * 获取企业审核信息集合
     * @param eid
     * @return
     */
    List<EnterpriseAuthInfoDTO> queryListByEid(Long eid);

}
