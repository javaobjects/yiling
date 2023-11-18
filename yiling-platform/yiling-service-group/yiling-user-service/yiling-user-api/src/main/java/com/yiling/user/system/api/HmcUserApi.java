package com.yiling.user.system.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.CreateHmcUserRequest;
import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;
import com.yiling.user.system.dto.request.UpdateHmcUserRequest;

/**
 * 健康管理中心用户 API
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
public interface HmcUserApi {

    /**
     * 用户分页列表
     *
     * @param request
     * @return
     */
    Page<HmcUser> pageList(QueryHmcUserPageListRequest request);

    /**
     * 根据用户ID获取用户信息(无 openId 返回)
     *
     * @param id 用户ID
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/4/26
     **/
    HmcUser getById(Long id);

    /**
     * 根据用户ID获取用户信息（有 openId 返回）
     *
     * @param id 用户ID
     * @param appId
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/4/26
     **/
    HmcUser getByIdAndAppId(Long id, String appId);

    /**
     * 批量根据用户ID获取用户信息
     *
     * @param ids 用户ID列表
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    List<HmcUser> listByIds(List<Long> ids);

    /**
     * 根据微信unionId获取用户信息
     *
     *
     * @param unionId 微信unionId
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    HmcUser getByUnionId(String unionId);

    /**
     * 根据微信unionId获取用户信息
     *
     * @param openId 微信unionId
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    HmcUser getByOpenId(String openId);

    /**
     * 根据unionId保存或更新用户信息
     *
     * @param request 保存用户请求
     * @return java.lang.Long
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    Long create(CreateHmcUserRequest request);

    /**
     * 修改用户信息
     *
     * @param request 修改用户请求
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    Boolean update(UpdateHmcUserRequest request);

    /**
     * 修改密码
     *
     * @param id 用户ID
     * @param password 新密码
     * @param opUserId 操作人ID
     * @return
     */
    boolean updatePassword(Long id, String password, Long opUserId);

    /**
     * 重置密码
     *
     * @param id 用户ID
     * @param opUserId 操作人ID
     * @return
     */
    boolean resetPassword(Long id, Long opUserId);

    /**
     * 查询指定活动类型下每个医生邀请的用户数量
     * @param request
     * @return
     */
    List<Map<String,Long>> queryActivityDoctorInviteUserCount(QueryActivityDoctorUserCountRequest request);
}
