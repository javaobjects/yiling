package com.yiling.hmc.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountSaveRequest;
import com.yiling.hmc.enterprise.entity.EnterpriseAccountDO;

/**
 * <p>
 * 保险药品商家结算账号表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
public interface EnterpriseAccountService extends BaseService<EnterpriseAccountDO> {

    /**
     * 保险药品商家结算账号表 保存
     *
     * @param request 保险药品商家结算账号参数
     * @return 成功/失败
     */
    boolean saveEnterpriseAccount(EnterpriseAccountSaveRequest request);

    /**
     * 根据企业id查询保险药品商家结算账号信息
     *
     * @param eid 企业id
     * @return 保险药品商家结算账号信息
     */
    EnterpriseAccountDO queryByEid(Long eid);

    /**
     * 保险药品商家结算账号信息分页查询
     *
     * @param request 查询条件
     * @return 保险药品商家结算账号信息
     */
    Page<EnterpriseAccountDO> pageList(EnterpriseAccountPageRequest request);
}
