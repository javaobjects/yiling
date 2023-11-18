package com.yiling.sales.assistant.userteam;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sales.assistant.BaseTest;
import com.yiling.sales.assistant.userteam.dto.request.AcceptInviteRequest;
import com.yiling.sales.assistant.userteam.service.UserTeamService;

import lombok.extern.slf4j.Slf4j;


/**
* @author lun.yu
* @date 2022/01/11
*/
@Slf4j
public class UserTeamServiceTest extends BaseTest {


   @Autowired
   UserTeamService userTeamService;


   @Test
   public void test(){
       AcceptInviteRequest request = new AcceptInviteRequest();
       request.setInviteType(1);
       request.setParentId(2L);
       request.setMobilePhone("18800000000");
       Boolean aBoolean = userTeamService.acceptInvite(request);
       System.out.println(aBoolean);
   }

    @Test
    public void test2(){
        AcceptInviteRequest request = new AcceptInviteRequest();
        request.setInviteType(2);
        request.setParentId(2L);
        request.setMobilePhone("15172511725");
        Boolean aBoolean = userTeamService.acceptInvite(request);
        System.out.println(aBoolean);
    }


}
