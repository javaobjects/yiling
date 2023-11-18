package com.yiling.user.userderegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.service.EsbEmployeeService;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.service.ShopService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.service.HmcUserService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.BaseTest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.service.MemberService;
import com.yiling.user.system.service.UserDeregisterAccountService;

/**
 * @author: lun.yu
 * @date: 2022-07-12
 */
public class UserDeregisterServiceTest extends BaseTest {

    @Autowired
    HmcUserService hmcUserService;

    @Autowired
    private UserDeregisterAccountService userDeregisterAccountService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private EsbEmployeeService esbEmployeeService;


    @Test
    public void openM1ember() {
        List<Long> objects = new ArrayList<>();


        objects = Arrays.asList(9792L, 12296L, 10515L, 12297L, 11026L, 9792L, 12930L, 13591L, 13512L, 10328L, 13798L, 13799L, 12298L, 10410L, 13840l);
        List<Long> object1s = new ArrayList<>();
        object1s.add(22812L);

        Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = esbEmployeeService.listByOrgIds(object1s, objects,null);

        System.out.println(longEsbOrganizationDTOMap);
    }

    @Test
    public void openMember() {
        boolean result = userDeregisterAccountService.deregisterAccountTask();

        Assert.assertTrue(result);
    }

    @Test
    public void getById() {
        HmcUser user = hmcUserService.getById(162L);
        System.out.println(user);
    }

    @Test
    public void test1() {
        QueryShopRequest request = new QueryShopRequest();
        request.setCurrent(1);
        request.setSize(10);
        request.setCustomerEid(221L);
        Page<ShopListItemDTO> page = shopService.queryPurchaseShopListPage(request);
        System.out.println(page);
    }
}
