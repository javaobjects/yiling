package com.yiling.user.usercustomer.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.AddUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateCustomerStatusRequest;
import com.yiling.user.usercustomer.dto.request.UpdateUserCustomerRequest;

/**
 * 用户客户Service
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
public interface UserCustomerService {


    /**
     * 添加客户
     * @param request
     * @return
     */
    boolean add(AddUserCustomerRequest request);

    /**
     * 根据id获取客户信息
     * @param id
     * @return
     */
    UserCustomerDTO getCustomerById(Long id);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<UserCustomerDTO> pageList(QueryUserCustomerRequest request);

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
     * 根据客户ID，查询第一个绑定审核通过的用户
     * @param customerEid
     * @return
     */
    UserCustomerDTO getByCustomerEid(Long customerEid);

    /**
     * 检查用户的客户是否存在
     * @param userId
     * @param customerEid
     * @return
     */
    boolean checkUserCustomer(Long userId, Long customerEid);

    /**
     * 检查用户销售区域
     * @param userId
     * @param eid
     * @param userType
     * @param cityCode
     * @return
     */
    boolean checkUserSaleArea(Long userId, Long eid, UserTypeEnum userType, String cityCode);

    /**
     * 根据用户ID和客户企业ID获取用户客户对象
     * @param userId
     * @param customerEid
     * @return
     */
    UserCustomerDTO getByUserAndCustomerEid(Long userId, Long customerEid);
}
