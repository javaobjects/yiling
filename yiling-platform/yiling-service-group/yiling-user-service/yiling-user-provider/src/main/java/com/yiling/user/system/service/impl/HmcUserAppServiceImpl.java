package com.yiling.user.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserAppRequest;
import com.yiling.user.system.entity.HmcUserAppDO;
import com.yiling.user.system.dao.HmcUserAppMapper;
import com.yiling.user.system.service.HmcUserAppService;
import com.yiling.framework.common.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 健康管理中心用户应用表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-09-06
 */
@Slf4j
@Service
public class HmcUserAppServiceImpl extends BaseServiceImpl<HmcUserAppMapper, HmcUserAppDO> implements HmcUserAppService {

    @Override
    public HmcUserApp getByUserIdAndAppId(Long userId, String appId) {
        QueryWrapper<HmcUserAppDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HmcUserAppDO::getUserId, userId);
        wrapper.lambda().eq(HmcUserAppDO::getAppId, appId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), HmcUserApp.class);
    }

    @Override
    public void createHmcUserApp(CreateHmcUserAppRequest request) {
        HmcUserApp hmcUserApp = getByUserIdAndAppId(request.getUserId(), request.getAppId());
        if (Objects.nonNull(hmcUserApp)) {
            log.info("用户小程序应用已经存在，跳过处理");
            return;
        }
        // 创建用户应用
        HmcUserAppDO userAppDO = PojoUtils.map(request, HmcUserAppDO.class);
        this.save(userAppDO);
    }

    @Override
    public HmcUserApp getByOpenId(String openId) {
        QueryWrapper<HmcUserAppDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HmcUserAppDO::getOpenId, openId);
        wrapper.last("order by create_time desc limit 1");
        return PojoUtils.map(this.getOne(wrapper), HmcUserApp.class);
    }
}
