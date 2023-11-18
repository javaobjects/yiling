package com.yiling.user.system.api;

import java.util.List;

import com.yiling.user.system.dto.UserAttachmentDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;

/**
 * 用户 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/20
 */
public interface UserApi {

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    UserDTO getById(Long id);

    /**
     * 根据多个用户ID批量获取用户信息
     *
     * @param ids
     * @return
     */
    List<UserDTO> listByIds(List<Long> ids);

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateUserStatusRequest request);

    /**
     * 修改用户信息
     *
     * @param request
     * @return
     */
    boolean updateUserInfo(UpdateUserRequest request);

    /**
     * 判断用户是否设置销售区域
     *
     * @param userId 用户ID
     * @return
     */
    boolean hasUserSalesAreaSetting(Long userId);

    /**
     * 保存用户销售区域设置
     *
     * @param request
     * @return
     */
    boolean saveUserSalesArea(SaveUserSalesAreaRequest request);

    /**
     * 根据用户ID获取用户销售区域
     * @param userId
     * @return
     */
    UserSalesAreaDTO getSaleAreaByUserId(Long userId);

    /**
     * 根据用户ID获取用户销售区域编码，可获取城市和省份级别
     * @param userId 用户ID
     * @param level 根据Level指定，若要获取市级编码则level传入2，若要获取省级编码则level传入1，不传默认获取市级编码
     * @return 返回市级编码
     */
    List<String> getSaleAreaDetailByUserId(Long userId , Integer level);

    /**
     * 修改手机号
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param mobile 新手机号
     * @param opUserId 操作人ID
     * @return
     */
    boolean changeMobile(Long userId, String appId, String mobile, Long opUserId);

    /**
     * 校验是否为特殊号段
     * @param specialPhone
     * @return 返回true表示为特殊号段
     */
    boolean checkSpecialPhone(String specialPhone);

    /**
     * 根据类型获取用户附件列表
     *
     * @param userId 用户ID
     * @param fileTypeList 附件类型列表
     * @return java.util.List<com.yiling.user.system.dto.UserAttachmentDTO>
     * @author xuan.zhou
     * @date 2022/7/4
     **/
    List<UserAttachmentDTO> listUserAttachmentsByType(Long userId, List<Integer> fileTypeList);

}
