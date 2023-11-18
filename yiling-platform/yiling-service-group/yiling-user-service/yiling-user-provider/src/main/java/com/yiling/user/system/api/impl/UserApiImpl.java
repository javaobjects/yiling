package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserAttachmentDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.UserAttachmentService;
import com.yiling.user.system.service.UserRoleService;
import com.yiling.user.system.service.UserSalesAreaService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 用户 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/20
 */
@DubboService
public class UserApiImpl implements UserApi {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserSalesAreaService userSalesAreaService;
    @Autowired
    UserAttachmentService userAttachmentService;

    @Override
    public UserDTO getById(Long id) {
        return PojoUtils.map(userService.getById(id), UserDTO.class);
    }

    @Override
    public List<UserDTO> listByIds(List<Long> ids) {
        List<UserDO> list = userService.listByIds(ids);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return PojoUtils.map(list, UserDTO.class);
    }

    @Override
    public boolean updateStatus(UpdateUserStatusRequest request) {
        return userService.updateStatus(request);
    }

    @Override
    public boolean updateUserInfo(UpdateUserRequest request) {
        return userService.update(request);
    }

    @Override
    public boolean hasUserSalesAreaSetting(Long userId) {
        return userSalesAreaService.hasUserSalesAreaSetting(userId);
    }

    @Override
    public boolean saveUserSalesArea(SaveUserSalesAreaRequest request) {
        return userSalesAreaService.saveUserSalesArea(request);
    }

    @Override
    public UserSalesAreaDTO getSaleAreaByUserId(Long userId) {
        return PojoUtils.map(userSalesAreaService.getSaleAreaByUserId(userId), UserSalesAreaDTO.class);
    }

    @Override
    public List<String> getSaleAreaDetailByUserId(Long userId , Integer level) {
        return userSalesAreaService.getSaleAreaDetailByUserId(userId , level);
    }

    @Override
    public boolean changeMobile(Long userId, String appId, String mobile, Long opUserId) {
        return userService.changeMobile(userId, appId, mobile, opUserId);
    }

    @Override
    public boolean checkSpecialPhone(String specialPhone) {
        return userService.checkSpecialPhone(specialPhone);
    }

    @Override
    public List<UserAttachmentDTO> listUserAttachmentsByType(Long userId, List<Integer> fileTypeList) {
        return PojoUtils.map(userAttachmentService.listUserAttachmentsByType(userId, fileTypeList), UserAttachmentDTO.class);
    }

}
