package com.yiling.user.system.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.CreateHmcUserRequest;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;
import com.yiling.user.system.dto.request.UpdateHmcUserRequest;
import com.yiling.user.system.entity.HmcUserDO;

/**
 * <p>
 * 健康管理中心用户表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-03-24
 */
public interface HmcUserService extends BaseService<HmcUserDO> {

    /**
     * 用户分页列表
     *
     * @param request
     * @return
     */
    Page<HmcUser> pageList(QueryHmcUserPageListRequest request);

    /**
     * 批量根据ID获取用户信息
     *
     * @param ids 用户ID列表
     * @return
     */
    List<HmcUser> listByIds(List<Long> ids);

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    HmcUser getById(Long id);

    /**
     * 根据用户ID获取HMC用户信息
     *
     * @param userId 用户ID
     * @return
     */
    HmcUserDO getByUserId(Long userId);

    /**
     * 根据unionId获取用户信息
     *
     *
     * @param unionId 微信unionId
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    HmcUser getByUnionId(String unionId);

    /**
     * 根据unionId获取用户信息
     *
     * @param openId openId
     * @return com.yiling.user.system.bo.HmcUser
     * @author xuan.zhou
     * @date 2022/3/24
     **/
    HmcUser getByOpenId(String openId);

    /**
     * 创建用户信息
     *
     * @param request 创建用户请求
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
     * 更新用户登录次数、最后登录时间
     *
     * @param userId 用户ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/3/28
     **/
    Boolean updateLoginTime(Long userId);

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
