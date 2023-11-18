package com.yiling.mall.member;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.BaseTest;
import com.yiling.mall.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lun.yu
 * @date: 2021/12/13
 */
@Slf4j
public class MemberServiceTest extends BaseTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void test () {
        memberService.memberExpirationReminderHandler();
    }

}
