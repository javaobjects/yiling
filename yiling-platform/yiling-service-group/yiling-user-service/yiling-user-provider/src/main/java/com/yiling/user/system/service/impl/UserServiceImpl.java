package com.yiling.user.system.service.impl;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dao.UserMapper;
import com.yiling.user.system.dto.request.UpdateIdCardInfoRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.UserAttachmentTypeEnum;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserAttachmentService;
import com.yiling.user.system.service.UserAuthsService;
import com.yiling.user.system.service.UserSalesAreaService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Slf4j
@RefreshScope
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDO> implements UserService {

    private String specialPhonePrefix = "111,110";

    @Autowired
    private UserAuthsService userAuthsService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private UserSalesAreaService userSalesAreaService;
    @Autowired
    private UserAttachmentService userAttachmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdateUserRequest request) {
        Assert.notNull(request.getAppId(), "appId不能为空");
        Assert.notNull(request.getId(), "id不能为空");

        String username = request.getUsername();
        if (StrUtil.isNotEmpty(username)) {
            userAuthsService.updateUsername(request.getId(), request.getAppId(), username, request.getOpUserId());
        }

        String mobile = request.getMobile();
        if (StrUtil.isNotEmpty(mobile)) {
            userAuthsService.updateMobile(request.getId(), request.getAppId(), mobile, request.getOpUserId());
        }

        // 修改为注销状态
        UserStatusEnum statusEnum = UserStatusEnum.getByCode(request.getStatus());
        if (statusEnum == UserStatusEnum.DEREGISTER) {
            userAuthsService.clear(request.getId(), request.getOpUserId());
        }

        UserDO entity = PojoUtils.map(request, UserDO.class);
        return this.updateById(entity);
    }

    @Override
    public boolean updateStatus(UpdateUserStatusRequest request) {
        UserStatusEnum userStatusEnum = UserStatusEnum.getByCode(request.getStatus());
        if (userStatusEnum == null) {
            throw new BusinessException(ResultCode.FAILED, "用户状态值不正确");
        }

        // 修改为注销状态
        UserStatusEnum statusEnum = UserStatusEnum.getByCode(request.getStatus());
        if (statusEnum == UserStatusEnum.DEREGISTER) {
            userAuthsService.clear(request.getId(), request.getOpUserId());
        }

        UserDO userDO = new UserDO();
        userDO.setId(request.getId());
        userDO.setStatus(request.getStatus());
        userDO.setOpUserId(request.getOpUserId());
        return this.updateById(userDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeMobile(Long userId, String appId, String mobile, Long opUserId) {
        userAuthsService.updateMobile(userId, appId, mobile, opUserId);

        UserDO user = new UserDO();
        user.setId(userId);
        user.setMobile(mobile);
        user.setOpUserId(opUserId);
        return this.updateById(user);
    }

    @Override
    public String generateSpecialPhone() {
        String specialPhone = null;

        for (int num = 0 ; num <= 10 ; num++) {
            //从号段前缀组随机取一个前缀，再随机生成一个8位数字，组成11位特殊随机号码
            String[] phonePrefixArr = specialPhonePrefix.split(",");
            String randomNumbers = RandomUtil.randomNumbers(8);
            String randomEle = RandomUtil.randomEle(phonePrefixArr);

            specialPhone = randomEle + randomNumbers;

            //校验是否已经存在
            Staff staff = staffService.getByMobile(specialPhone);
            if (Objects.isNull(staff)) {
                break;
            }
            log.info("虚拟号码生成了已经存在的虚拟号，号码为：{}", specialPhone);
        }

        return specialPhone;
    }

    @Override
    public boolean checkSpecialPhone(String specialPhone) {
        if (StrUtil.isEmpty(specialPhone)) {
            return false;
        }

        //必须存在用户表、且手机号前缀符合定义的前缀
        Staff staff = staffService.getByMobile(specialPhone);
        if (Objects.isNull(staff)) {
            return false;
        }

        String prefix = specialPhone.substring(0, 3);
        String[] phonePrefixArr = specialPhonePrefix.split(",");
        return Arrays.asList(phonePrefixArr).contains(prefix);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateIdCardInfo(UpdateIdCardInfoRequest request) {
        // 更新个人信息
        UserDO userDO = new UserDO();
        userDO.setId(request.getUserId());
        userDO.setName(request.getName());
        userDO.setIdNumber(request.getIdNumber());
        this.updateById(userDO);

        // 更新身份证照
        {
            String idCardFrontPhotoKey = request.getIdCardFrontPhotoKey();
            if (StrUtil.isNotEmpty(idCardFrontPhotoKey)) {
                userAttachmentService.deleteByUserId(request.getUserId(), UserAttachmentTypeEnum.ID_CARD_FRONT_PHOTO, request.getOpUserId());
                userAttachmentService.saveBatch(request.getUserId(), UserAttachmentTypeEnum.ID_CARD_FRONT_PHOTO, ListUtil.toList(idCardFrontPhotoKey),
                        request.getOpUserId());
            }

            String idCardBackPhotoKey = request.getIdCardBackPhotoKey();
            if (StrUtil.isNotEmpty(idCardBackPhotoKey)) {
                userAttachmentService.deleteByUserId(request.getUserId(), UserAttachmentTypeEnum.ID_CARD_BACK_PHOTO, request.getOpUserId());
                userAttachmentService.saveBatch(request.getUserId(), UserAttachmentTypeEnum.ID_CARD_BACK_PHOTO, ListUtil.toList(idCardBackPhotoKey),
                        request.getOpUserId());
            }
        }

        return true;
    }

}
