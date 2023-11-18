package com.yiling.user.system.api;

import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserAppRequest;

/**
 * 健康管理中心用户应用 API
 *
 * @author: fan.shen
 * @date: 2022-09-06
 */
public interface HmcUserAppApi {

    /**
     * 根据用户id + appId 获取用户信息
     * @param userId 用户id
     * @param appId 微信小程序id
     * @return
     */
    HmcUserApp getByUserIdAndAppId(Long userId, String appId);

    /**
     * 保存用户应用请求
     * @param request
     * @return
     */
    void createHmcUserApp(CreateHmcUserAppRequest request);

    /**
     * 根据微信openId获取用户信息
     * @param openId
     * @return
     */
    HmcUserApp getByOpenId(String openId);

//
//
//    /**
//     * 根据unionId保存或更新用户信息
//     *
//     * @param request 保存用户请求
//     * @return java.lang.Long
//     * @author xuan.zhou
//     * @date 2022/3/24
//     **/
//    Long create(CreateHmcUserRequest request);
//
//    /**
//     * 用户分页列表
//     *
//     * @param request
//     * @return
//     */
//    Page<HmcUser> pageList(QueryHmcUserPageListRequest request);
//
//    /**
//     * 根据用户ID获取用户信息
//     *
//     * @param id 用户ID
//     * @return com.yiling.user.system.bo.HmcUser
//     * @author xuan.zhou
//     * @date 2022/4/26
//     **/
//    HmcUser getById(Long id);
//
//    /**
//     * 批量根据用户ID获取用户信息
//     *
//     * @param ids 用户ID列表
//     * @return com.yiling.user.system.dto.HmcUserDTO
//     * @author xuan.zhou
//     * @date 2022/4/26
//     **/
//    List<HmcUserDTO> listByIds(List<Long> ids);
//
//    /**
//     * 根据微信unionId获取用户信息
//     *
//     *
//     * @param appId 微信appId
//     * @param unionId 微信unionId
//     * @return com.yiling.user.system.bo.HmcUser
//     * @author xuan.zhou
//     * @date 2022/3/24
//     **/
//    HmcUser getByAppIdAndUnionId(String appId, String unionId);
//
//    /**
//     * 根据微信unionId获取用户信息
//     *
//     * @param openId 微信unionId
//     * @return com.yiling.user.system.bo.HmcUser
//     * @author xuan.zhou
//     * @date 2022/3/24
//     **/
//    HmcUser getByOpenId(String openId);
//
//    /**
//     * 修改用户信息
//     *
//     * @param request 修改用户请求
//     * @return java.lang.Boolean
//     * @author xuan.zhou
//     * @date 2022/3/24
//     **/
//    Boolean update(UpdateHmcUserRequest request);
}
