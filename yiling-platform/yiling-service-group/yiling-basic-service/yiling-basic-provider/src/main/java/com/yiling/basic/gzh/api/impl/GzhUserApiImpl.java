package com.yiling.basic.gzh.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.gzh.entity.GzhUserDO;
import com.yiling.basic.gzh.service.GzhUserService;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.basic.wx.dto.request.CreateGzhUserRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class GzhUserApiImpl implements GzhUserApi {

    @Autowired
    private GzhUserService gzhUserService;

    /**
     * 根据unionId查询
     *
     * @param unionId
     * @return
     */
    @Override
    public GzhUserDTO getByUnionId(String unionId) {
        GzhUserDO gzhUserDO = gzhUserService.getByUnionId(unionId);
        return PojoUtils.map(gzhUserDO, GzhUserDTO.class);
    }

    @Override
    public GzhUserDTO getByUnionIdAndAppId(String unionId, String appId) {
        return gzhUserService.getByUnionIdAndAppId(unionId, appId);
    }

    @Override
    public GzhUserDTO getByGzhOpenId(String openId) {
        return gzhUserService.getByGzhOpenId(openId);
    }

    @Override
    public List<GzhUserDTO> getByUnionIdList(List<String> unionIdList) {
        return gzhUserService.getByUnionIdList(unionIdList);
    }

    @Override
    public GzhUserDTO createGzhUser(CreateGzhUserRequest request) {
        return gzhUserService.createGzhUser(request);
    }

    @Override
    public void updateGzhUser(GzhUserDTO gzhUserDTO) {
        gzhUserService.updateGzhUser(gzhUserDTO);
    }

    @Override
    public GzhUserDTO getById(Long id) {
        return PojoUtils.map(gzhUserService.getById(id), GzhUserDTO.class);
    }

    @Override
    public List<GzhUserDTO> getByIdList(List<Long> idList) {
        return PojoUtils.map(gzhUserService.getByIdList(idList), GzhUserDTO.class);
    }

    @Override
    public Page<GzhUserDTO> pageList(QueryPageListRequest request) {
        return gzhUserService.pageList(request);
    }
}
