package com.yiling.user.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dao.MrUserRegisterMapper;
import com.yiling.user.system.dto.request.CreateMrUserRegisterRequest;
import com.yiling.user.system.entity.MrUserRegisterDO;
import com.yiling.user.system.service.MrUserRegisterService;
import com.yiling.user.system.service.StaffService;

/**
 * <p>
 * 医药代表用户注册信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-01-31
 */
@Service
public class MrUserRegisterServiceImpl extends BaseServiceImpl<MrUserRegisterMapper, MrUserRegisterDO> implements MrUserRegisterService {

    @Autowired
    StaffService staffService;

    @Override
    public MrUserRegisterDO getByMobile(String mobile) {
        QueryWrapper<MrUserRegisterDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MrUserRegisterDO::getMobile, mobile).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean create(CreateMrUserRegisterRequest request) {
        String mobile = request.getMobile();

        Staff staff = staffService.getByMobile(mobile);
        if (staff != null) {
            throw new BusinessException(ResultCode.FAILED, "手机号已存在，请返回首页登陆");
        }

        MrUserRegisterDO mrUserRegisterDO = this.getByMobile(mobile);
        if (mrUserRegisterDO != null) {
            throw new BusinessException(ResultCode.FAILED, "您的账号已申请注册，我们将在24小时内审核您的资料，请耐心等待");
        }

        MrUserRegisterDO entity = new MrUserRegisterDO();
        entity.setMobile(request.getMobile());
        entity.setPassword(request.getPassword());
        entity.setEname(request.getEname());
        entity.setAuditStatus(1);
        return this.save(entity);
    }
}
