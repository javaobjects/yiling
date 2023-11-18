package com.yiling.dataflow.wash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.api.UnlockThirdRecordApi;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;
import com.yiling.dataflow.wash.entity.UnlockThirdRecordDO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/18
 */
public class UnlockThirdRecordTest extends BaseTest {

    @Autowired
    private UnlockThirdRecordApi unlockThirdRecordApi;

    @Autowired
    private LocationApi locationApi;

    @Test
    public void listPageTest() {
        QueryUnlockThirdRecordPageRequest request = new QueryUnlockThirdRecordPageRequest();
        request.setOrgCrmId(1L);
//        request.setOpStartTime(DateUtil.parse("2023-06-05", "yyyy-MM-dd"));
//        request.setOpEndTime(DateUtil.parse("2023-06-05", "yyyy-MM-dd"));
        Page<UnlockThirdRecordDTO> page = unlockThirdRecordApi.listPage(request);
        System.out.println(JSONUtil.toJsonStr(page));
    }

    @Test
    public void addTest() {
        SaveOrUpdateUnlockThirdRecordRequest request = new SaveOrUpdateUnlockThirdRecordRequest();
        request.setOrgCrmId(432096L);
        request.setPurchaseQuota(new BigDecimal(2));

        List<SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo> list = new ArrayList<>();
        SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo departmentInfo1 = new SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo();
        departmentInfo1.setCode("7801");
        departmentInfo1.setName("社区部成都二区");
        list.add(departmentInfo1);

        SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo departmentInfo2 = new SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo();
        departmentInfo2.setCode("7802");
        departmentInfo2.setName("社区部成都三区");
        list.add(departmentInfo2);

        request.setDepartmentInfoList(list);
        unlockThirdRecordApi.add(request);
    }

    @Test
    public void updateTest() {
        SaveOrUpdateUnlockThirdRecordRequest request = new SaveOrUpdateUnlockThirdRecordRequest();
        request.setId(1L);
        request.setPurchaseQuota(new BigDecimal(3));

        List<SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo> list = new ArrayList<>();
        SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo departmentInfo1 = new SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo();
        departmentInfo1.setCode("7801");
        departmentInfo1.setName("社区部成都二区");
        list.add(departmentInfo1);

//        SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo departmentInfo2 = new SaveOrUpdateUnlockThirdRecordRequest.DepartmentInfo();
//        departmentInfo2.setCode("7802");
//        departmentInfo2.setName("社区部成都三区");
//        list.add(departmentInfo2);

        request.setDepartmentInfoList(list);
        unlockThirdRecordApi.update(request);
    }

    @Test
    public void getByIdTest() {
        UnlockThirdRecordDTO unlockThirdRecordDTO = unlockThirdRecordApi.getById(1L);
        System.out.println(JSONUtil.toJsonStr(unlockThirdRecordDTO));
    }

    @Test
    public void locationListTest() {
        List<RegionFullViewDTO> pcrInfoList = locationApi.getAllProvinceCityRegionList();
        System.out.println(pcrInfoList.size());
    }
}
