package com.yiling.basic.gzh.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.article.entity.ArticleDO;
import com.yiling.basic.gzh.dao.GzhUserMapper;
import com.yiling.basic.gzh.entity.GzhUserDO;
import com.yiling.basic.gzh.service.GzhUserService;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.basic.wx.dto.request.CreateGzhUserRequest;
import com.yiling.basic.wx.enums.SubscribeStatusEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.YlStrUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公众号用户表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-26
 */
@Service
public class GzhUserServiceImpl extends BaseServiceImpl<GzhUserMapper, GzhUserDO> implements GzhUserService {

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    GzhUserServiceImpl _this;

    @Override
    public GzhUserDO getByUnionId(String unionId) {
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhUserDO::getUnionId, unionId);
        wrapper.lambda().orderByDesc(GzhUserDO::getCreateTime);
        return this.getOne(wrapper, false);
    }

    @Override
    public GzhUserDO getByOpenId(String appId, String openId) {
        QueryWrapper<GzhUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(GzhUserDO::getAppId, appId)
                .eq(GzhUserDO::getGzhOpenId, openId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Transactional
    public GzhUserDTO create(CreateGzhUserRequest request, MqMessageBO mqMessageBO) {
        // 保存用户
        GzhUserDO userDO = PojoUtils.map(request, GzhUserDO.class);
        this.save(userDO);

        mqMessageBO.setBody(String.valueOf(userDO.getId()));

        // 发送关注公众号消息
        mqMessageSendApi.prepare(mqMessageBO);

        return PojoUtils.map(userDO, GzhUserDTO.class);
    }

    @Override
    public GzhUserDTO createGzhUser(CreateGzhUserRequest request) {

        MqMessageBO subMq = new MqMessageBO(Constants.TOPIC_HMC_GZH_SUBSCRIBE, Constants.TAG_HMC_GZH_SUBSCRIBE, null);

        GzhUserDTO gzhUserDTO = _this.create(request, subMq);

        // 如果qrValue不为空 && 分享人id非空，则发送首次订阅mq
        // 字符串格式：qt:20_actId:61_uId:17426
        if (StrUtil.isNotBlank(request.getQrValue()) && YlStrUtils.dealParam(request.getQrValue()).containsKey("uId")) {
            MqMessageBO firstSubMq = new MqMessageBO(Constants.TOPIC_HMC_GZH_FIRST_SUBSCRIBE, Constants.TAG_HMC_GZH_FIRST_SUBSCRIBE, null);
            firstSubMq.setBody(request.getQrValue());
            mqMessageSendApi.send(firstSubMq);
        }

        mqMessageSendApi.send(subMq);

        return gzhUserDTO;

    }

    @Override
    public List<GzhUserDTO> getByUnionIdList(List<String> unionIdList) {
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GzhUserDO::getUnionId, unionIdList);
        List<GzhUserDO> userDOList = this.list(wrapper);
        return PojoUtils.map(userDOList, GzhUserDTO.class);
    }

    @Override
    public GzhUserDTO getByUnionIdAndAppId(String unionId, String appId) {
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhUserDO::getUnionId, unionId);
        wrapper.lambda().eq(GzhUserDO::getAppId, appId);
        wrapper.lambda().orderByDesc(GzhUserDO::getCreateTime);
        GzhUserDO gzhUserDO = this.getOne(wrapper, false);
        return PojoUtils.map(gzhUserDO, GzhUserDTO.class);
    }

    @Override
    public GzhUserDTO getByGzhOpenId(String openId) {
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhUserDO::getGzhOpenId, openId);
        wrapper.lambda().orderByDesc(GzhUserDO::getCreateTime);
        GzhUserDO gzhUserDO = this.getOne(wrapper, false);
        return PojoUtils.map(gzhUserDO, GzhUserDTO.class);
    }

    @Override
    public void updateGzhUser(GzhUserDTO gzhUserDTO) {
        GzhUserDO userDO = new GzhUserDO();
        userDO.setSubscribeStatus(gzhUserDTO.getSubscribeStatus());

        UpdateWrapper<GzhUserDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(GzhUserDO::getId, gzhUserDTO.getId());
        this.update(userDO, updateWrapper);
    }

    @Override
    public Page<GzhUserDTO> pageList(QueryPageListRequest request) {
        Page<GzhUserDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GzhUserDO::getSubscribeStatus, SubscribeStatusEnum.SUBSCRIBE.getType());
        wrapper.last("order by create_time asc");
        Page<GzhUserDO> result = this.page(page, wrapper);
        return PojoUtils.map(result, GzhUserDTO.class);
    }

    @Override
    public List<GzhUserDTO> getByIdList(List<Long> idList) {
        QueryWrapper<GzhUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GzhUserDO::getId, idList);
        List<GzhUserDO> list = this.list(wrapper);
        return PojoUtils.map(list, GzhUserDTO.class);
    }
}
