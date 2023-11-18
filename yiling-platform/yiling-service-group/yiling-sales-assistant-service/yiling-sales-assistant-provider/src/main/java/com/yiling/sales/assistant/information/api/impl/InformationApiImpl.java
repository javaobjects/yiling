package com.yiling.sales.assistant.information.api.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.information.api.InformationApi;
import com.yiling.sales.assistant.information.dao.InformationMapper;
import com.yiling.sales.assistant.information.dto.InformationRequest;
import com.yiling.sales.assistant.information.entity.InformationDO;

/**
 * 销售助手-信息反馈API impl
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@DubboService
public class InformationApiImpl extends BaseServiceImpl<InformationMapper,InformationDO> implements InformationApi {

    @Override
    public boolean saveInformation(InformationRequest request) {
        InformationDO informationDO = PojoUtils.map(request,InformationDO.class);
        return this.save(informationDO);
    }
}
