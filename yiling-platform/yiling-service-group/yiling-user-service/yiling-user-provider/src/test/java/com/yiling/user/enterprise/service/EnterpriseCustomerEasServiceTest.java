package com.yiling.user.enterprise.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.BaseTest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;

/**
 * @author: lun.yu
 * @date: 2021/9/10
 */
public class EnterpriseCustomerEasServiceTest extends BaseTest {

    @Autowired
    private EnterpriseCustomerEasService enterpriseCustomerEasService;

    @Test
    public void pageListByCurrent() {
        QueryCustomerEasInfoPageListByCurrentRequest request = new QueryCustomerEasInfoPageListByCurrentRequest();
        request.setOpUserId(57L);
        request.setCustomerName("武汉一级商企业002");
        Page<EnterpriseCustomerEasDO> page = enterpriseCustomerEasService.pageListByCurrent(request);
        System.out.println(page.getTotal());
    }
}
