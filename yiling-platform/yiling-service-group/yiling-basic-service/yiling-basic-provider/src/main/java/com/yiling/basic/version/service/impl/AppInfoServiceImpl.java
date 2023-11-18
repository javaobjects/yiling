package com.yiling.basic.version.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.version.dao.AppInfoMapper;
import com.yiling.basic.version.entity.AppChannelDO;
import com.yiling.basic.version.entity.AppInfoDO;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.basic.version.service.AppChannelService;
import com.yiling.basic.version.service.AppInfoService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 应用信息 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppInfoServiceImpl extends BaseServiceImpl<AppInfoMapper, AppInfoDO> implements AppInfoService {

    private final AppChannelService channelService;

    @Override
    public List<AppInfoDO> listAppInfoByChannelCode(AppChannelEnum channelEnum) {
        if (null != channelEnum) {
            List<AppChannelDO> channelDOS = channelService.queryByCode(channelEnum.getCode());
            if (CollUtil.isEmpty(channelDOS)) {
                return null;
            }
            List<Long> longList = channelDOS.stream().map(AppChannelDO::getAppId).collect(Collectors.toList());
            return this.listByIdList(longList);
        } else {
            return this.list();
        }
    }

    @Override
    public List<AppInfoDO> listByIdList(List<Long> idList) {
        QueryWrapper<AppInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(AppInfoDO::getId, idList);
        return this.list(wrapper);
    }
}
