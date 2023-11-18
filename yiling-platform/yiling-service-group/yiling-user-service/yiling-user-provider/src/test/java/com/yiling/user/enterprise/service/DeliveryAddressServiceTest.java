package com.yiling.user.enterprise.service;/**
 * @author: ray
 * @date: 2021/5/21
 */

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.BaseTest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.enterprise.entity.DeliveryAddressDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gxl
 * @description:
 * @date: Created in 8:44 2021/5/21
 * @modified By:
 */
@Slf4j
public class DeliveryAddressServiceTest extends BaseTest {


    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Test
    public void insert(){
        DeliveryAddressDO deliveryAddressBO = new DeliveryAddressDO().setAddress("北京天安门").setProvinceCode("110100")
                .setProvinceName("北京市").setCityCode("110000").setCityName("北京市").setRegionCode("110101").setRegionName("东城区").setEid(1L).setReceiver("收获人")
                .setMobile("18888888888").setCreateUser(1L);
        deliveryAddressService.save(deliveryAddressBO);
    }

    @Test
    public void getByEid(){
        LambdaQueryWrapper<DeliveryAddressDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DeliveryAddressDO::getEid,1L).last("limit 1");
        DeliveryAddressDO deliveryAddressDO =  deliveryAddressService.getOne(wrapper);
        log.info(deliveryAddressDO.toString());
    }

    @Test
    public void updateById(){
        UpdateDeliveryAddressRequest updateDeliveryAddressRequest = new UpdateDeliveryAddressRequest();
        updateDeliveryAddressRequest.setAddress("人民大会堂").setId(1L);
//        updateDeliveryAddressRequest.setUpdateUser(1L);
        deliveryAddressService.updateById(PojoUtils.map(updateDeliveryAddressRequest,DeliveryAddressDO.class));
    }
}
