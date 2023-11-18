package com.yiling.user.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.QueryUserDeregisterPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserDeregisterRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.entity.UserDeregisterAccountDO;
import com.yiling.user.system.dao.UserDeregisterAccountMapper;
import com.yiling.user.system.enums.UserDeregisterAccountSourceEnum;
import com.yiling.user.system.enums.UserDeregisterAccountStatusEnum;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.service.UserDeregisterAccountService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 注销账号表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Slf4j
@Service
@RefreshScope
public class UserDeregisterAccountServiceImpl extends BaseServiceImpl<UserDeregisterAccountMapper, UserDeregisterAccountDO> implements UserDeregisterAccountService {

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private UserService userService;

    /**
     * 最大注销时间（单位：秒）：默认为24小时（3600 * 24 = 86400秒）
     */
    @Value("${common.user.deregisterAccount.executeTime:86400}")
    private Integer executeTime;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyDeregisterAccount(AddDeregisterAccountRequest request) {
        // 身份为企业管理员的企业集合
        List<Long> adminFlagEidList = getAdminFlagEidList(request.getUserId());

        UserDO userDO = userService.getById(request.getUserId());

        // 生成注销信息，设置待注销状态
        UserDeregisterAccountDO userDeregisterAccountDO = PojoUtils.map(request, UserDeregisterAccountDO.class);
        userDeregisterAccountDO.setApplyTime(new Date());
        userDeregisterAccountDO.setStatus(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode());
        userDeregisterAccountDO.setMobile(userDO.getMobile());
        userDeregisterAccountDO.setUsername(userDO.getUsername());
        userDeregisterAccountDO.setName(userDO.getName());
        this.save(userDeregisterAccountDO);

        // 企业更新为停用状态
        adminFlagEidList.forEach(eid -> {
            EnterpriseDO enterprise = new EnterpriseDO();
            enterprise.setId(eid);
            enterprise.setStatus(EnableStatusEnum.DISABLED.getCode());
            enterprise.setOpUserId(request.getOpUserId());
            enterpriseService.updateById(enterprise);
            log.info("注销账号企业ID={} 已经更新企业为停用状态", eid);
        });

        return true;
    }

    /**
     * 根据用户ID 获取身份为企业管理员的企业ID集合
     *
     * @param userId
     * @return
     */
    private List<Long> getAdminFlagEidList(Long userId) {
        List<Long> adminFlagEidList = ListUtil.toList();

        List<EnterpriseEmployeeDO> employeeDOList = enterpriseEmployeeService.listByUserId(userId, EnableStatusEnum.ENABLED);
        Map<Long, Integer> statusMap = employeeDOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDO::getEid, EnterpriseEmployeeDO::getAdminFlag));
        statusMap.forEach((eid, adminFlag) -> {
            if ( Objects.nonNull(adminFlag) && adminFlag == 1) {
                adminFlagEidList.add(eid);
            }
        });

        return adminFlagEidList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deregisterAccountTask() {
        // 超过24小时，定时任务执行真正的注销
        LambdaQueryWrapper<UserDeregisterAccountDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDeregisterAccountDO::getStatus, UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode());
        List<UserDeregisterAccountDO> deregisterAccountDOList = this.list(wrapper);
        if (CollUtil.isEmpty(deregisterAccountDOList)) {
            return true;
        }

        Date now = new Date();
        List<UserDeregisterAccountDO> list = ListUtil.toList();

        // 最大注销时间
        int MAX_HOURS = executeTime / 3600;

        // 达到24小时就执行注销
        deregisterAccountDOList.forEach(userDeregisterAccountDO -> {
            Date applyTime = userDeregisterAccountDO.getApplyTime();

            long betweenHours = DateUtil.between(applyTime, now, DateUnit.HOUR, false);
            if (betweenHours >= MAX_HOURS) {
                list.add(userDeregisterAccountDO);
            }
        });

        if (CollUtil.isEmpty(list)) {
            return true;
        }
        log.info("定时任务待执行注销的数据信息={}", JSONObject.toJSONString(list));

        // 执行注销
        list.forEach(userDeregisterAccountDO -> {
            try {
                UserDeregisterAccountDO deregisterAccountDO = new UserDeregisterAccountDO();
                deregisterAccountDO.setId(userDeregisterAccountDO.getId());
                deregisterAccountDO.setStatus(UserDeregisterAccountStatusEnum.HAD_DEREGISTER.getCode());
                deregisterAccountDO.setUpdateStatusTime(now);
                deregisterAccountDO.setAuthTime(now);
                this.updateById(deregisterAccountDO);

                // 更新用户为注销状态
                UpdateUserStatusRequest request = new UpdateUserStatusRequest();
                request.setId(userDeregisterAccountDO.getUserId());
                request.setStatus(UserStatusEnum.DEREGISTER.getCode());
                userService.updateStatus(request);
            }catch (Exception e) {
                log.error("用户ID={} 执行注销定时任务出现异常={}", userDeregisterAccountDO.getUserId(), e);
            }
        });

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deregisterAccountAuth(UpdateUserDeregisterRequest request) {
        UserDeregisterAccountDO userDeregisterAccountDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(ResultCode.PARAM_VALID_ERROR));
        if (UserDeregisterAccountStatusEnum.getByCode(userDeregisterAccountDO.getStatus()) != UserDeregisterAccountStatusEnum.WAITING_DEREGISTER) {
            throw new BusinessException(UserErrorCode.DEREGISTER_ACCOUNT_STATUS_ERROR);
        }

        UserDeregisterAccountDO deregisterAccountDO = new UserDeregisterAccountDO();
        deregisterAccountDO.setId(userDeregisterAccountDO.getId());
        deregisterAccountDO.setStatus(request.getStatus());
        deregisterAccountDO.setUpdateStatusTime(new Date());
        deregisterAccountDO.setAuthUser(request.getOpUserId());
        deregisterAccountDO.setOpUserId(request.getOpUserId());
        deregisterAccountDO.setAuthTime(new Date());
        this.updateById(deregisterAccountDO);

        // 审核为注销时：更新用户为注销状态，审核为撤销时：更新企业为启用状态（管理员）
        if (UserDeregisterAccountStatusEnum.getByCode(request.getStatus()) == UserDeregisterAccountStatusEnum.HAD_DEREGISTER) {
            UpdateUserStatusRequest userRequest = new UpdateUserStatusRequest();
            userRequest.setId(userDeregisterAccountDO.getUserId());
            userRequest.setStatus(UserStatusEnum.DEREGISTER.getCode());
            userRequest.setOpUserId(request.getOpUserId());
            userService.updateStatus(userRequest);

        } else if (UserDeregisterAccountStatusEnum.getByCode(request.getStatus()) == UserDeregisterAccountStatusEnum.HAD_REVERT) {
            // 根据用户ID 获取身份为企业管理员的企业ID集合
            List<Long> adminFlagEidList = this.getAdminFlagEidList(userDeregisterAccountDO.getUserId());

            adminFlagEidList.forEach(eid -> {
                EnterpriseDO enterprise = new EnterpriseDO();
                enterprise.setId(eid);
                enterprise.setStatus(EnableStatusEnum.ENABLED.getCode());
                enterprise.setOpUserId(request.getOpUserId());
                enterpriseService.updateById(enterprise);
                log.info("注销账号企业ID={} 审核已撤销注销 已经更新企业为启用状态", eid);
            });
        }
        log.info("注销审核完成 请求数据：{}", JSONObject.toJSONString(request));

        return true;
    }

    @Override
    public Page<UserDeregisterAccountDTO> queryPageList(QueryUserDeregisterPageListRequest request) {
        QueryWrapper<UserDeregisterAccountDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.page(request.getPage(), wrapper), UserDeregisterAccountDTO.class);
    }

    @Override
    public UserDeregisterAccountDO getByUserId(Long userId) {
        LambdaQueryWrapper<UserDeregisterAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDeregisterAccountDO::getUserId, userId);
        queryWrapper.orderByDesc(UserDeregisterAccountDO::getCreateTime);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

}
