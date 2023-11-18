package com.yiling.basic.tianyancha.api.impl;

import com.yiling.basic.tianyancha.api.TycEnterpriseApi;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.service.TycEnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author shichen
 * @类名 TycEnterpriseApiImpl
 * @描述
 * @创建时间 2022/1/12
 * @修改人 shichen
 * @修改时间 2022/1/12
 **/
@DubboService
@Slf4j
public class TycEnterpriseApiImpl implements TycEnterpriseApi {
    @Resource
    private TycEnterpriseService tycEnterpriseService;

    @Override
    public TycResultDTO<TycEnterpriseInfoDTO> findEnterpriseByKeyword(TycEnterpriseQueryRequest request) {
        return tycEnterpriseService.findEnterpriseByKeyword(request);
    }
}
