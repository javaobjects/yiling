package com.yiling.export.meeting;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.json.JSONUtil;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.export.BaseTest;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.util.ExportExcelUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.meeting.api.HmcMeetingSignApi;
import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.*;

/**
 * @author: shuang.zhang
 * @date: 2022/11/21
 */
@Slf4j
public class MeetingTest extends BaseTest {

    @DubboReference
    private HmcMeetingSignApi meetingSignApi;

    @Test
    public void importExcel() {
        File file = new File("/Users/shenfan/Downloads/副本第十届血管病学大会参会专家明细（信息部）的副本.xlsx");
        ImportParams params = new ImportParams();
        List<MeetingExcel> list = ExcelImportUtil.importExcel(file, MeetingExcel.class, params);
        log.info("list.size:{}", list.size());
        List<MeetingSignDTO> signDTOS = PojoUtils.map(list, MeetingSignDTO.class);
        meetingSignApi.saveMeeting(signDTOS);
        log.info(JSONUtil.toJsonStr(list));
    }

}