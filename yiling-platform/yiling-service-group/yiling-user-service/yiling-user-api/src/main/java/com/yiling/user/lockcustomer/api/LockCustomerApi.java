package com.yiling.user.lockcustomer.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.AddLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.ImportLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.QueryLockCustomerPageRequest;
import com.yiling.user.lockcustomer.dto.request.UpdateLockCustomerStatusRequest;

/**
 * <p>
 * 锁定用户 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
public interface LockCustomerApi {

    /**
     * 锁定用户分页列表
     *
     * @param request
     * @return
     */
    Page<LockCustomerDTO> queryListPage(QueryLockCustomerPageRequest request);

    /**
     * 锁定用户列表
     *
     * @param request
     * @return
     */
    List<LockCustomerDTO> queryList(QueryLockCustomerPageRequest request);

    /**
     * 根据执业许可证号/社会信用统一代码查询单个锁定客户对象
     *
     * @param licenseNumber
     * @return
     */
    LockCustomerDTO getByLicenseNumber(String licenseNumber);

    /**
     * 根据批量 执业许可证号/社会信用统一代码 查询状态
     *
     * @param licenseNumberList 执业许可证号/社会信用统一代码集合
     * @return key为licenseNumber，value为状态：1-启用 2-停用
     */
    Map<String,Integer> batchQueryByLicenseNumber(List<String> licenseNumberList);

    /**
     * 添加锁定用户
     *
     * @param request
     */
    boolean addLockCustomer(AddLockCustomerRequest request);

    /**
     * 批量更新用户锁定状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateLockCustomerStatusRequest request);

    /**
     * 导入锁定用户数据
     *
     * @param request
     * @return
     */
    boolean importData(ImportLockCustomerRequest request);


}
