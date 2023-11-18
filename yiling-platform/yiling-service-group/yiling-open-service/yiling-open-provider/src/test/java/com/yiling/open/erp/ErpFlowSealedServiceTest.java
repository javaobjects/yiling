package com.yiling.open.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;
import com.yiling.open.erp.service.ErpFlowSealedService;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpFlowSealedServiceTest extends BaseTest {

    @Autowired
    ErpFlowSealedService erpFlowSealedService;

    @Test
    public void saveTest() {
        QueryErpSealedSaveRequest request = new QueryErpSealedSaveRequest();

        List<Long> eidList = new ArrayList<>();
        eidList.add(27L);
        request.setEidList(eidList);

        List<Integer> typeList = new ArrayList<>();
        typeList.add(1);
        request.setTypeList(typeList);

        List<String> monthList = new ArrayList<>();
        monthList.add("2022年01月");
        monthList.add("2022年02月");
        monthList.add("2022年03月");
        request.setMonthList(monthList);

        Map<Long,String> enameMap = new HashMap<>();
        enameMap.put(27L, "周越二级商企业(测试)");
        request.setEnameMap(enameMap);

        erpFlowSealedService.save(request);
        System.out.println(">>>>> ***********");
    }

    @Test
    public void unlockTest(){
        ErpFlowSealedLockOrUnlockRequest request = new ErpFlowSealedLockOrUnlockRequest();
        request.setId(1L);
        request.setStatus(1);
        request.setOpUserId(1L);
        erpFlowSealedService.lockOrUnLock(request);
    }

}
