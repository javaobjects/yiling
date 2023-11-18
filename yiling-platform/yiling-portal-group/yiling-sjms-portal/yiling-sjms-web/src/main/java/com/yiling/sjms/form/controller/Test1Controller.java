package com.yiling.sjms.form.controller;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.sjms.form.vo.BizFormDetailVO;
import com.yiling.user.system.bo.SjmsUser;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 表单测试
 *
 * @author: xuan.zhou
 * @date: 2023/3/1
 */
@Slf4j
@RestController
@RequestMapping("/form/test1")
@Api(tags = "表单测试1")
public class Test1Controller extends AbstractBizFormController {

    @Override
    void setBizInfo(BizFormDetailVO vo) {
        SjmsUser sjmsUser = new SjmsUser();
        sjmsUser.setEmpId("test");
        sjmsUser.setName("测试人员");
        sjmsUser.setAdminFlag(0);
        sjmsUser.setCreateUser(0L);
        sjmsUser.setCreateTime(new Date());
        sjmsUser.setUpdateUser(0L);
        sjmsUser.setUpdateTime(new Date());
        sjmsUser.setRemark("");
        sjmsUser.setId(0L);
        sjmsUser.setUsername("");
        sjmsUser.setGender(0);
        sjmsUser.setBirthday(new Date());
        sjmsUser.setMobile("");
        sjmsUser.setEmail("");
        sjmsUser.setIdNumber("");
        sjmsUser.setNickName("");
        sjmsUser.setAvatarUrl("");
        sjmsUser.setStatus(0);
        vo.setBizInfo(sjmsUser);
    }
}
