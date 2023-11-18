package com.yiling.dataflow.backup;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;
import com.yiling.dataflow.backup.service.DataFlowBackupService;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: houjie.sun
 * @date: 2022/7/18
 */
public class DataFlowBackupServiceTest extends BaseTest {

    @Autowired
    private DataFlowBackupService dataFlowBackupService;

    @Test
    public void dataFlowBackupTest(){
        List<Long> eidList = ListUtil.toList(1270L,
                4L,
                49L,
                36L,
                52L,
                7L,
                1246L,
                1039L,
                1104L,
                192L,
                1331L,
                1680L,
                650L,
                123L,
                148387L,
                1506L,
                27L,
                64L,
                1227L,
                218630L,
                306054L,
                590L,
                347935L,
                2910L,
                394164L
                );

        DataFlowBackupRequest dataFlowBackupRequest = new DataFlowBackupRequest();
        dataFlowBackupRequest.setEidList(eidList);
        dataFlowBackupService.dataFlowBackup(dataFlowBackupRequest);
    }

    @Test
    public void dataFlowBackupNewTest(){
        List<Long> eidList = ListUtil.toList(
                1255L,
                181676L,
                308471L,
                663L,
                413984L,
                568L,
                155071L,
                136968L
        );

        DataFlowBackupRequest dataFlowBackupRequest = new DataFlowBackupRequest();
        dataFlowBackupRequest.setEidList(eidList);
        dataFlowBackupService.dataFlowBackupNew(dataFlowBackupRequest);
    }

}
