package com.yiling.dataflow.crm;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationManorService;

/**
 * @author: gxl
 * @date: 2023/6/1
 */

public class CrmEnterpriseRelationManorTest extends BaseTest {
    @Autowired
    private CrmEnterpriseRelationManorService  crmEnterpriseRelationManorService;

    @Test
    public  void test(){
        List<SaveOrUpdateManorRelationRequest> request = Lists.newArrayList();
        SaveOrUpdateManorRelationRequest r = new SaveOrUpdateManorRelationRequest();
        r.setNewManorId(12L).setCrmManorId(10L).setCrmEnterpriseId(233732L).setCategoryId(2L);
        request.add(r);
        crmEnterpriseRelationManorService.updateBatch(request);
    }
}