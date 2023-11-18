package com.yiling.user.enterprise.api;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.yiling.user.BaseTest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseChannelRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseTypeRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

/**
 * EnterpriseApiTest
 *
 * @author: xuan.zhou
 * @date: 2022/3/15
 */
public class EnterpriseApiTest extends BaseTest {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Test
    public void updateType() {
        UpdateEnterpriseTypeRequest request = new UpdateEnterpriseTypeRequest();
        request.setEid(9L);
        request.setEnterpriseTypeEnum(EnterpriseTypeEnum.HOSPITAL);
        request.setOpUserId(1L);
        enterpriseApi.updateType(request);
    }

    @Test
    public void updateChannel() {
        UpdateEnterpriseChannelRequest request = new UpdateEnterpriseChannelRequest();
        request.setEid(9L);
        request.setEnterpriseChannelEnum(EnterpriseChannelEnum.Z2P1);
        request.setOpUserId(1L);
        enterpriseApi.updateChannel(request);
    }
}
