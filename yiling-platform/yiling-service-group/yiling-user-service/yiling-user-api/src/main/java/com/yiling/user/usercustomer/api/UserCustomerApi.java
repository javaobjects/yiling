package com.yiling.user.usercustomer.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.AddUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateCustomerStatusRequest;
import com.yiling.user.usercustomer.dto.request.UpdateUserCustomerRequest;

/**
 * 用户客户管理API
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
public interface UserCustomerApi {

    /**
     * 添加客户
     * @param request
     * @return
     */
    boolean add(AddUserCustomerRequest request);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<UserCustomerDTO> pageList(QueryUserCustomerRequest request);

    /**
     * 根据ID获取用户客户信息
     * @param id
     * @return
     */
    UserCustomerDTO getById(Long id);

    /**
     * 更新客户
     * @param request
     * @return
     */
    boolean update(UpdateUserCustomerRequest request);

    /**
     * 列表查询
     * @param request
     * @return
     */
    List<UserCustomerDTO> queryCustomerList(QueryUserCustomerRequest request);

    /**
     * 根据客户企业ID更新客户状态
     * @param request
     * @return
     */
    boolean updateCustomerStatus(UpdateCustomerStatusRequest request);

    /**
     * 根据用户ID和客户企业ID获取用户客户对象
     * @param userId
     * @param customerEid
     * @return
     */
    UserCustomerDTO getByUserAndCustomerEid(Long userId, Long customerEid);

    /**
     * 根据客户ID，查询第一个绑定审核通过的用户
     * @param customerEid
     * @return 返回用户
     */
    UserCustomerDTO getByCustomerEid(Long customerEid);

    /**
     * 检查用户的客户是否存在
     * @param userId 当前用户
     * @param customerEid 客户企业ID
     * @return 返回true标识已经存在，false标识不存在
     */
    boolean checkUserCustomer(Long userId, Long customerEid);

    /**
     * 检查用户销售区域是否可售
     * @param userId 当前用户
     * @param eid 当前用户的企业ID
     * @param userType 用户类型
     * @param cityCode 将要添加客户的城市编码
     * @return true 标识可销售 false标识不可售
     */
    boolean checkUserSaleArea(Long userId, Long eid, UserTypeEnum userType, String cityCode);
}
