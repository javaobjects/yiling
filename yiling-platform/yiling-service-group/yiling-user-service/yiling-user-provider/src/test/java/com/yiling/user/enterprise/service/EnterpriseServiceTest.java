package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.BaseTest;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerLineDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.shop.service.ShopService;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: xuan.zhou
 * @date: 2021/7/8
 */
public class EnterpriseServiceTest extends BaseTest {

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    private EnterpriseCustomerLineService enterpriseCustomerLineService;
    @Autowired
    private ShopService shopService;

    @Test
    public void importData() {
        ImportEnterpriseRequest request = new ImportEnterpriseRequest();
        request.setName("测试2021-7-8");
        request.setType(EnterpriseTypeEnum.INDUSTRY.getCode());
        request.setParentId(0L);
        request.setLicenseNumber("2021-7-8 11:39:34");
        request.setProvinceCode("130000");
        request.setProvinceName("河北省");
        request.setCityCode("130300");
        request.setCityName("石家庄市");
        request.setRegionCode("130304");
        request.setRegionName("长安区");
        request.setAddress("天山大街238号");
        request.setContactor("测试2021-7-8");
        request.setContactorPhone("17700000000");
        request.setPlatformEnumList(ListUtil.toList(PlatformEnum.POP, PlatformEnum.B2B));
        request.setChannelId(EnterpriseChannelEnum.INDUSTRY.getCode());
        request.setOpUserId(1L);

        boolean result = enterpriseService.importData(request);
        Assert.assertTrue(result);
    }

    @Test
    public void importCustomerLine() {
        List<EnterpriseCustomerDO> list = enterpriseCustomerService.list(new LambdaQueryWrapper<>());
        List<Long> eidList = list.stream().map(EnterpriseCustomerDO::getEid).collect(Collectors.toList());
        Map<Long, String> nameMap = enterpriseService.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDO::getId, EnterpriseDO::getName, (k1, k2) -> k1));

        List<AddCustomerLineRequest> request = ListUtil.toList();
        list.forEach(enterpriseCustomerDO -> {
            EnterpriseCustomerLineDO customerLineDO = new EnterpriseCustomerLineDO();
            customerLineDO.setCustomerId(enterpriseCustomerDO.getId());
            customerLineDO.setEid(enterpriseCustomerDO.getEid());
            customerLineDO.setEname(nameMap.get(enterpriseCustomerDO.getEid()));
            customerLineDO.setCustomerEid(enterpriseCustomerDO.getCustomerEid());
            customerLineDO.setCustomerName(enterpriseCustomerDO.getCustomerName());

            if(enterpriseCustomerDO.getSource() == 1 || enterpriseCustomerDO.getSource() == 4 || enterpriseCustomerDO.getSource() == 3){
                customerLineDO.setUseLine(1);
            } else if (enterpriseCustomerDO.getSource() == 2 || enterpriseCustomerDO.getSource() == 5){
                customerLineDO.setUseLine(2);
            } else {
                return;
            }
            request.add(PojoUtils.map(customerLineDO,AddCustomerLineRequest.class));
        });
        enterpriseCustomerLineService.add(request);

    }

    /**
     * 判断店铺是否可以销售给当前登录企业
     */
    @Test
    public void checkSaleArea(){
        Map<Long, Boolean> longBooleanMap = shopService.checkSaleAreaByCustomerEid(221L, ListUtil.toList(212L));
        System.out.println(JSONObject.toJSONString(longBooleanMap));
    }

    /**
     * 根据当前登录企业的销售区域，获取可以买哪些企业的接口
     */
    @Test
    public void getSellEidByArea(){
        List<Long> list = shopService.getSellEidByEidSaleArea(220L);
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void getTest(){
        enterpriseService.syncHandlerFlag();
    }

    @Test
    public void test1(){
        QueryCanBuyEidRequest request = new QueryCanBuyEidRequest();
        request.setCustomerEid(220L);
        request.setLimit(50);
        request.setLine(2);
        List<Long> list = enterpriseCustomerService.getEidListByCustomerEid(request);
        System.out.println(list);
    }

}
