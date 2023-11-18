package com.yiling.ih.user.api.impl;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.api.IHUserApi;
import com.yiling.ih.user.dto.IHUserDTO;
import com.yiling.ih.user.dto.SavePatientRelDTO;
import com.yiling.ih.user.feign.IHUserFeignClient;
import com.yiling.ih.user.feign.dto.response.IHUserResponse;
import com.yiling.ih.user.feign.dto.response.SavePatientRelResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * IH 用户服务api
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
@Slf4j
@DubboService
public class IHUserApiImpl implements IHUserApi {

    @Autowired
    private IHUserFeignClient ihUserFeignClient;

    @Override
    public List<IHUserDTO> getUserListByName(String name) {
        ApiResult<List<IHUserResponse>> apiResult = ihUserFeignClient.getUserListByName(name);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            log.error("[getUserListByName]调用IH服务获取用户失败");
            return null;
        }
        return PojoUtils.map(apiResult.getData(), IHUserDTO.class);
    }

    @Override
    public List<IHUserDTO> getUserListByIds(List<Integer> ids) {
        ApiResult<List<IHUserResponse>> apiResult = ihUserFeignClient.getUserListByIds(ids);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            log.error("[getUserListByIds]调用IH服务获取用户失败");
            return null;
        }
        return PojoUtils.map(apiResult.getData(), IHUserDTO.class);
    }
}
