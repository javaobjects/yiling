package com.yiling.basic.version.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.version.dao.AppChannelMapper;
import com.yiling.basic.version.entity.AppChannelDO;
import com.yiling.basic.version.service.AppChannelService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * <p>
 * 应用渠道信息 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Service
public class AppChannelServiceImpl extends BaseServiceImpl<AppChannelMapper, AppChannelDO> implements AppChannelService {

    @Override
    public List<AppChannelDO> queryByCode(String code) {
        QueryWrapper<AppChannelDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppChannelDO::getCode, code);
        return this.list(wrapper);
    }
}
