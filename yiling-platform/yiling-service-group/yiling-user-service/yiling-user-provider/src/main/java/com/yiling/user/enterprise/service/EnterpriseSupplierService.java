package com.yiling.user.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.EnterpriseSupplierDTO;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;
import com.yiling.user.enterprise.dto.request.SaveOrDeleteSupplierRequest;
import com.yiling.user.enterprise.dto.request.UpdateSupplierRequest;
import com.yiling.user.enterprise.entity.EnterpriseSupplierDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 供应商管理表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
public interface EnterpriseSupplierService extends BaseService<EnterpriseSupplierDO> {

    /**
     * 分页查询供应商
     *
     * @param request
     * @return
     */
    Page<EnterpriseSupplierBO> queryListPage(QuerySupplierPageRequest request);

    /**
     * 新增供应商
     *
     * @param request
     * @return
     */
    boolean insertSupplier(SaveOrDeleteSupplierRequest request);

    /**
     * 获取供应商
     *
     * @param customerEid
     * @param eid
     * @return
     */
    EnterpriseSupplierDTO getSupplier(Long customerEid, Long eid);

    /**
     * 删除供应商
     *
     * @param request
     * @return
     */
    boolean deleteSupplier(SaveOrDeleteSupplierRequest request);

    /**
     * 更新供应商
     *
     * @param request
     * @return
     */
    boolean updateSupplier(UpdateSupplierRequest request);

    /**
     * 根据ID获取供应商详情
     *
     * @param id
     * @return
     */
    EnterpriseSupplierBO get(Long id);

}
