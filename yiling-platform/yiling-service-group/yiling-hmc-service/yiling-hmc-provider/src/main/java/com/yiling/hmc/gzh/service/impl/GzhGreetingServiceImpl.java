package com.yiling.hmc.gzh.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;
import com.yiling.hmc.gzh.entity.GzhGreetingDO;
import com.yiling.hmc.gzh.dao.GzhGreetingMapper;
import com.yiling.hmc.gzh.enums.GzhGreetingPublishStatusEnum;
import com.yiling.hmc.gzh.service.GzhGreetingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 公众号欢迎语 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-28
 */
@Service
public class GzhGreetingServiceImpl extends BaseServiceImpl<GzhGreetingMapper, GzhGreetingDO> implements GzhGreetingService {

    @Override
    public Page<GzhGreetingDTO> pageList(QueryGzhGreetingRequest request) {
        QueryWrapper<GzhGreetingDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getSceneId()), GzhGreetingDO::getSceneId, request.getSceneId());
        Page<GzhGreetingDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, GzhGreetingDTO.class);
    }

    @Override
    public Long saveGreetings(SaveGzhGreetingRequest request) {
        if (Objects.nonNull(request.getId())) {
            GzhGreetingDO greetingDO = this.getById(request.getId());
            greetingDO.setDraftVersion(request.getDraftVersion());
            greetingDO.setPublishStatus(GzhGreetingPublishStatusEnum.UN_PUBLISHED.getType());
            this.updateById(greetingDO);
            return greetingDO.getId();
        } else {
            GzhGreetingDO greetingDO = PojoUtils.map(request, GzhGreetingDO.class);
            greetingDO.setPublishStatus(GzhGreetingPublishStatusEnum.UN_PUBLISHED.getType());
            this.save(greetingDO);
            return greetingDO.getId();
        }
    }

    @Override
    public Boolean publishGreetings(PublishGzhGreetingRequest request) {
        GzhGreetingDO greetingDO = this.getById(request.getId());
        greetingDO.setPublishVersion(greetingDO.getDraftVersion());
        greetingDO.setPublishStatus(GzhGreetingPublishStatusEnum.PUBLISHED.getType());
        greetingDO.setPublishDate(DateUtil.date());
        greetingDO.setOpUserId(request.getOpUserId());
        return this.updateById(greetingDO);
    }

    @Override
    public GzhGreetingDTO checkIsExists(Integer sceneId) {
        QueryWrapper<GzhGreetingDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhGreetingDO::getSceneId, sceneId);
        GzhGreetingDO greetingDO = this.getOne(wrapper, false);
        return PojoUtils.map(greetingDO, GzhGreetingDTO.class);
    }

    @Override
    public GzhGreetingDTO getDetailBySceneId(Long sceneId) {
        QueryWrapper<GzhGreetingDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhGreetingDO::getSceneId, sceneId);
        GzhGreetingDO greetingDO = this.getOne(wrapper, false);
        return PojoUtils.map(greetingDO, GzhGreetingDTO.class);
    }

    @Override
    public void updateTriggerCount(Long id) {
        GzhGreetingDO greetingDO = this.getById(id);
        greetingDO.setTriggerCount(greetingDO.getTriggerCount() + 1);
        this.updateById(greetingDO);
    }
}
