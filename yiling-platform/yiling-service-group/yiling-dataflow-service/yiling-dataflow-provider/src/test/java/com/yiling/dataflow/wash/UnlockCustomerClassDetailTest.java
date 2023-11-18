package com.yiling.dataflow.wash;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.api.UnlockCustomerClassDetailApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCustomerClassificationRequest;
import com.yiling.dataflow.wash.service.UnlockCustomerClassDetailService;

import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/18
 */
public class UnlockCustomerClassDetailTest extends BaseTest {

    @Autowired
    private UnlockCustomerClassDetailApi unlockCustomerClassDetailApi;

    @Autowired
    private UnlockCustomerClassDetailService unlockCustomerClassDetailService;

    @Test
    public void listPageTest() {
        QueryUnlockCustomerClassDetailPageRequest request  = new QueryUnlockCustomerClassDetailPageRequest();
        Page<UnlockCustomerClassDetailDTO> result = unlockCustomerClassDetailApi.listPage(request);
        System.out.println(JSONUtil.toJsonStr(result));
    }

    @Test
    public void resetCustomerClassificationTest() {
        UpdateCustomerClassificationRequest request = new UpdateCustomerClassificationRequest();
        request.setIdList(Arrays.asList(32L));
        request.setCustomerClassification(3);
        request.setRemark("123");
        unlockCustomerClassDetailApi.resetCustomerClassification(request);
    }

    @Test
    public void unlockCustomerClassMappingHandleTest() {
        unlockCustomerClassDetailService.unlockCustomerClassMappingHandle(1L);
    }
}
