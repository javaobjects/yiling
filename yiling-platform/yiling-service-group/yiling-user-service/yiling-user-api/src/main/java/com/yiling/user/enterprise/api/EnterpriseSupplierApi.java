package com.yiling.user.enterprise.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;
import com.yiling.user.enterprise.dto.request.SaveOrDeleteSupplierRequest;
import com.yiling.user.enterprise.dto.request.UpdateSupplierRequest;

/**
 * 供应商管理 API
 *
 * @author: lun.yu
 * @date: 2023-06-15
 */
public interface EnterpriseSupplierApi {

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
