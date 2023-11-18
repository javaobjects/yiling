package com.yiling.user.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.dao.UserAttachmentMapper;
import com.yiling.user.system.entity.UserAttachmentDO;
import com.yiling.user.system.enums.UserAttachmentTypeEnum;
import com.yiling.user.system.service.UserAttachmentService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 用户相关附件表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-21
 */
@Service
public class UserAttachmentServiceImpl extends BaseServiceImpl<UserAttachmentMapper, UserAttachmentDO> implements UserAttachmentService {

    @Override
    public boolean saveBatch(Long userId, UserAttachmentTypeEnum attachmentTypeEnum, List<String> attachmentKeyList, Long opUserId) {
        if (CollUtil.isEmpty(attachmentKeyList)) {
            return true;
        }

        List<UserAttachmentDO> list = CollUtil.newArrayList();
        for (String fileKey : attachmentKeyList) {
            UserAttachmentDO entity = new UserAttachmentDO();
            entity.setUserId(userId);
            entity.setFileType(attachmentTypeEnum.getCode());
            entity.setFileKey(fileKey);
            entity.setOpUserId(opUserId);
            list.add(entity);
        }
        return this.baseMapper.batchInsert(list) > 0;
    }

    @Override
    public List<UserAttachmentDO> listUserAttachmentsByType(Long userId, List<Integer> fileTypeList) {
        QueryWrapper<UserAttachmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAttachmentDO::getUserId, userId)
                .in(UserAttachmentDO::getFileType, fileTypeList)
                .orderByAsc(UserAttachmentDO::getId);
        List<UserAttachmentDO> list = this.list(queryWrapper);
        return CollUtil.isEmpty(list) ? ListUtil.empty() : list;
    }

    @Override
    public Boolean deleteByUserId(Long userId, UserAttachmentTypeEnum attachmentTypeEnum, Long opUserId) {
        QueryWrapper<UserAttachmentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserAttachmentDO::getUserId, userId)
                .eq(UserAttachmentDO::getFileType, attachmentTypeEnum.getCode());

        UserAttachmentDO entity = new UserAttachmentDO();
        entity.setOpTime(new Date());
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity,wrapper) > 0;
    }
}
