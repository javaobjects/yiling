package com.yiling.user.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.util.LocationTreeUtils;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.dao.StaffExternaAuditMapper;
import com.yiling.user.system.dto.request.AuditStaffExternaInfoRequest;
import com.yiling.user.system.dto.request.CreateStaffExternaAuditRequest;
import com.yiling.user.system.dto.request.QueryStaffExternaAuditPageListRequest;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.dto.request.UpdateIdCardInfoRequest;
import com.yiling.user.system.entity.StaffExternaAuditDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.StaffExternaAuditService;
import com.yiling.user.system.service.UserSalesAreaService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 外部员工账户审核信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-17
 */
@Service
public class StaffExternaAuditServiceImpl extends BaseServiceImpl<StaffExternaAuditMapper, StaffExternaAuditDO> implements StaffExternaAuditService {

    @Autowired
    UserService userService;
    @Autowired
    UserSalesAreaService userSalesAreaService;

    @Override
    public Page<StaffExternaAuditDO> pageList(QueryStaffExternaAuditPageListRequest request) {
        LambdaQueryWrapper<StaffExternaAuditDO> queryWrapper = Wrappers.lambdaQuery();

        String name = request.getName();
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(StaffExternaAuditDO::getName, name);
        }

        String idNumber = request.getIdNumber();
        if (StrUtil.isNotEmpty(idNumber)) {
            queryWrapper.like(StaffExternaAuditDO::getIdNumber, idNumber);
        }

        String auditUserName = request.getAuditUserName();
        if (StrUtil.isNotEmpty(auditUserName)) {
            queryWrapper.like(StaffExternaAuditDO::getAuditUserName, auditUserName);
        }

        Integer auditStatus = request.getAuditStatus();
        if (auditStatus != null && auditStatus != 0) {
            queryWrapper.eq(StaffExternaAuditDO::getAuditStatus, auditStatus);
        }

        Date auditTimeBegin = request.getAuditTimeBegin();
        if (auditTimeBegin != null) {
            queryWrapper.ge(StaffExternaAuditDO::getAuditTime, DateUtil.beginOfDay(auditTimeBegin));
        }

        Date auditTimeEnd = request.getAuditTimeEnd();
        if (auditTimeEnd != null) {
            queryWrapper.le(StaffExternaAuditDO::getAuditTime, DateUtil.endOfDay(auditTimeEnd));
        }

        return this.page(request.getPage(), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(AuditStaffExternaInfoRequest request) {
        StaffExternaAuditDO staffExternaAuditDO = this.getById(request.getId());

        StaffExternaAuditDO entity = new StaffExternaAuditDO();
        entity.setId(request.getId());

        Integer auditStatus = request.getAuditStatus();
        if (auditStatus != null && auditStatus == 2) {
            entity.setAuditStatus(2);

            // 更新个人身份证信息
            UpdateIdCardInfoRequest updateIdCardInfoRequest = new UpdateIdCardInfoRequest();
            updateIdCardInfoRequest.setUserId(staffExternaAuditDO.getUserId());
            updateIdCardInfoRequest.setName(staffExternaAuditDO.getName());
            updateIdCardInfoRequest.setIdNumber(staffExternaAuditDO.getIdNumber());
            updateIdCardInfoRequest.setIdCardFrontPhotoKey(staffExternaAuditDO.getIdCardFrontPhotoKey());
            updateIdCardInfoRequest.setIdCardBackPhotoKey(staffExternaAuditDO.getIdCardBackPhotoKey());
            updateIdCardInfoRequest.setOpUserId(request.getOpUserId());
            userService.updateIdCardInfo(updateIdCardInfoRequest);

            // 更新用户销售区域
            SaveUserSalesAreaRequest saveUserSalesAreaRequest = new SaveUserSalesAreaRequest();
            saveUserSalesAreaRequest.setUserId(staffExternaAuditDO.getUserId());
            if (staffExternaAuditDO.getSalesAreaAllFlag() == 0) {
                List<LocationTreeDTO> salesAreaTree = JSONUtil.toList(JSONUtil.parseArray(staffExternaAuditDO.getSalesAreaJson()), LocationTreeDTO.class);
                saveUserSalesAreaRequest.setSalesAreaTree(salesAreaTree);
            }
            saveUserSalesAreaRequest.setOpUserId(request.getOpUserId());
            userSalesAreaService.saveUserSalesArea(saveUserSalesAreaRequest);
        } else {
            entity.setAuditStatus(3);
            entity.setAuditRejectReason(request.getAuditRejectReason());
        }

        UserDO userDO = userService.getById(request.getOpUserId());
        entity.setAuditUserId(userDO.getId());
        entity.setAuditUserName(userDO.getName());
        entity.setAuditTime(new Date());
        entity.setOpUserId(request.getOpUserId());
        return this.saveOrUpdate(entity);
    }

    @Override
    public StaffExternaAuditDO getUserLatestAuditInfo(Long userId) {
        QueryWrapper<StaffExternaAuditDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StaffExternaAuditDO::getUserId, userId)
                .orderByDesc(StaffExternaAuditDO::getId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean create(CreateStaffExternaAuditRequest request) {
        UserDO userDO = userService.getById(request.getUserId());

        StaffExternaAuditDO entity = PojoUtils.map(request, StaffExternaAuditDO.class);
        entity.setMobile(userDO.getMobile());
        entity.setSalesAreaDesc(request.getSalesAreaAllFlag() == 1 ? "全国" : LocationTreeUtils.getLocationTreeDesc(request.getSalesAreaTree(), 2));
        entity.setSalesAreaJson(JSONUtil.toJsonStr(request.getSalesAreaTree()));
        entity.setAuditStatus(1);
        return this.save(entity);
    }
}
