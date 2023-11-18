package com.yiling.dataflow.wash;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.api.UnlockAreaRecordApi;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;

import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/18
 */
public class UnlockAreaRecordServiceTest extends BaseTest {

    @Autowired
    private UnlockAreaRecordApi unlockAreaRecordApi;



    @Test
    public void listPageTest() {
        QueryUnlockAreaRecordPageRequest request = new QueryUnlockAreaRecordPageRequest();
        Page<UnlockAreaRecordDTO> result = unlockAreaRecordApi.listPage(request);
        System.out.println(JSONUtil.toJsonStr(result));
    }

    @Test
    public void addTest() {
        SaveOrUpdateUnlockAreaRecordRequest request = new SaveOrUpdateUnlockAreaRecordRequest();
        request.setCustomerClassification(1);
        request.setCategoryId(1L);
        request.setCategoryName("八子补肾");
        request.setType(1);
        request.setRepresentativeCode("YX123456");
        request.setRepresentativeName("张三");
        request.setRepresentativePostCode("12345");
        request.setRepresentativePostName("商务管理部广东区商务专员04");
        request.setRegionCodeList(Arrays.asList("110101", "110102", "110105"));
        unlockAreaRecordApi.add(request);
    }

    @Test
    public void updateTest() {
        SaveOrUpdateUnlockAreaRecordRequest request = new SaveOrUpdateUnlockAreaRecordRequest();
        request.setId(1L);
//        request.setCustomerClassification(1);
//        request.setCategoryId(1L);
//        request.setCategoryName("八子补肾");
        request.setType(1);
        request.setRepresentativeCode("YX123456");
        request.setRepresentativeName("张三");
        request.setRepresentativePostCode("12345");
        request.setRepresentativePostName("商务管理部广东区商务专员04");
        request.setRegionCodeList(Arrays.asList("110101", "110102", "110105", "110106"));
        unlockAreaRecordApi.update(request);
    }

    @Test
    public void getById() {
        UnlockAreaRecordDTO unlockAreaRecordDTO = unlockAreaRecordApi.getById(1L);
        System.out.println(JSONUtil.toJsonStr(unlockAreaRecordDTO));
    }





}
