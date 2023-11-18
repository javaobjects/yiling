package com.yiling.hmc.enterprise.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.enterprise.dto.EnterpriseAccountDTO;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountSaveRequest;

/**
 * 保险药品商家结算账号表API
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
public interface EnterpriseAccountApi {

    /**
     * 保险药品商家结算账号表 保存
     *
     * @param request 保险药品商家结算账号参数
     * @return 成功/失败
     */
    boolean saveEnterpriseAccount(EnterpriseAccountSaveRequest request);

    /**
     * 根据id查询保险药品商家结算账号信息
     *
     * @param id id
     * @return 保险药品商家结算账号信息
     */
    EnterpriseAccountDTO queryById(Long id);

    /**
     * 根据企业id查询保险药品商家结算账号信息
     *
     * @param eid 企业id
     * @return 保险药品商家结算账号信息
     */
    EnterpriseAccountDTO queryByEid(Long eid);

    /**
     * 查询所有
     *
     * @return
     */
    List<EnterpriseAccountDTO> getAll();

    /**
     * 保险药品商家结算账号信息分页查询
     *
     * @param request 查询条件
     * @return 保险药品商家结算账号信息
     */
    Page<EnterpriseAccountDTO> pageList(EnterpriseAccountPageRequest request);
}
