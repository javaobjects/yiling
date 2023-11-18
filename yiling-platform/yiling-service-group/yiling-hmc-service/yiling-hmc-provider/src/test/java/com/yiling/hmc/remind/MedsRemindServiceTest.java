package com.yiling.hmc.remind;

import cn.hutool.json.JSONUtil;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.service.MedsRemindService;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用药管理
 */
@Slf4j
public class MedsRemindServiceTest extends BaseTest {

    @Autowired
    private MedsRemindService medsRemindService;
    @Autowired
    private MedsRemindSubscribeService subscribeService;

    @Test
    public void generateDailyMedsRemindTask() {
        medsRemindService.generateDailyMedsRemindTask();
    }

    @Test
    public void todayMedsRemindTask() {
        List<MedsRemindTaskDetailDTO> medsRemindTaskDetailDTOS = medsRemindService.todayRemind(17137L);
        System.out.println(JSONUtil.toJsonStr(medsRemindTaskDetailDTOS));
    }

    @Test
    public void midAutumnFestivalPushWxTemplateMsg() {
        subscribeService.midAutumnFestivalPushWxTemplateMsg();
    }

    @Test
    public void fatherFestivalPushWxTemplateMsg() {
        subscribeService.fatherFestivalPushWxTemplateMsg();
    }


}
