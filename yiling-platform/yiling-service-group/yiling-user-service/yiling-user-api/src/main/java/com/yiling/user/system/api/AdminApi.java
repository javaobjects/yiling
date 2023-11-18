package com.yiling.user.system.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;
import com.yiling.user.system.dto.request.SaveAdminRequest;

/**
 * 后台管理员 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/12
 */
public interface AdminApi {

    /**
     * 获取用户分页列表
     *
     * @param request
     * @return
     */
    Page<Admin> pageList(QueryAdminPageListRequest request);

    /**
     * 根据ID获取用户信息
     *
     * @param id
     * @return
     */
    Admin getById(Long id);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return
     */
    Admin getByUsername(String username);

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 手机号
     * @return
     */
    Admin getByMobile(String mobile);

    /**
     * 根据姓名获取用户信息
     *
     * @param name 姓名
     * @return
     */
    Admin getByName(String name);

    /**
     * 根据姓名获取用户信息集合
     *
     * @param name 姓名
     * @param limit 限制返回的条数
     * @return
     */
    List<Admin> getListByName(String name, Integer limit);

    /**
     * 保存用户信息
     *
     * @param request
     * @return
     */
    boolean save(SaveAdminRequest request);

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
     * 校验用户密码
     *
     * @param userId 用户ID
     * @param password 密码明文
     * @return boolean
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    boolean checkPassword(Long userId, String password);

}
