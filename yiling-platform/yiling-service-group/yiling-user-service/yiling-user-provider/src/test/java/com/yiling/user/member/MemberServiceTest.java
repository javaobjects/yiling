package com.yiling.user.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.BaseTest;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.esb.entity.EsbEmployeeDO;
import com.yiling.user.esb.service.impl.EsbEmployeeServiceImpl;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberImportOpenService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author: lun.yu
 * @date: 2021/11/11
 */
public class MemberServiceTest extends BaseTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    private MemberImportOpenService memberImportOpenService;

    @Autowired
    private EsbEmployeeServiceImpl esbEmployeeService;

    @Test
    public void openMember() {
        OpenMemberRequest request = new OpenMemberRequest();
        request.setOrderNo("V20211111174435201670");
        request.setStatus(20);
        request.setTradeNo("TV20211111174435201670");
        boolean result = memberService.openMember(request);

        Assert.assertTrue(result);
    }

    @Test
    public void getMemberEnterprise() {
        List<EnterpriseDTO> enterpriseDTOList = enterpriseMemberService.getMemberEnterprise();
        System.out.println("开通会员的企业数量：" + enterpriseDTOList.size());
    }

    @Test
    public void getEidListByMemberId() {
        Map<Long, List<Long>> map = enterpriseMemberService.getEidListByMemberId(ListUtil.toList(5L));
        System.out.println("根据会员ID集合批量查询对应的企业数量：" + map.get(5L).size());
    }

    @Test
    public void get() {
        Map<Long, List<Long>> map = memberBuyStageService.getEidListByBuyStageId(ListUtil.toList(10L,11L,12L,13L,14L,15L,16L,20L,21L));
        map.forEach((k,v) -> {
            System.out.println("根据订单编号获取开通会员且未过期的企业ID=" + k + "，企业数量=" + v.size());
        });

    }

    @Autowired
    MemberBuyRecordService memberBuyRecordService;

    @Test
    public void fixMemberBuyRecord() {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, 302L);
        wrapper.orderByAsc(MemberBuyRecordDO::getStartTime);
        List<MemberBuyRecordDO> list = memberBuyRecordService.list(wrapper);

        MemberBuyRecordDO preRecordDO = null;
        for (int i = 0; i < list.size(); i++) {

            if (i == 0) {
                preRecordDO = list.get(i);
                continue;
            }

            MemberBuyRecordDO currentRecordDO = list.get(i);

            Date startTime = currentRecordDO.getStartTime();
            Integer day = currentRecordDO.getValidDays();

            if (startTime.compareTo(preRecordDO.getEndTime()) <= 0) {

                Date newStartTime = preRecordDO.getEndTime();
                DateTime newEndTime = DateUtil.offsetDay(newStartTime, day);

                MemberBuyRecordDO recordDO = new MemberBuyRecordDO();
                recordDO.setId(currentRecordDO.getId());
                recordDO.setStartTime(newStartTime);
                recordDO.setEndTime(newEndTime);
                memberBuyRecordService.updateById(recordDO);
                System.out.println("更新数据："+ JSONObject.toJSONString(recordDO));

                preRecordDO = recordDO;

            } else {

                preRecordDO = list.get(i);

            }
        }

    }


    @Test
    public void ge1t() {
        CurrentMemberForMarketingDTO currentMemberForMarketing = memberService.getCurrentMemberForMarketing(220L);
        System.out.println(currentMemberForMarketing);
    }

    @Test
    public void listByJobIdsForAgency() {
        List<Long> objects = new ArrayList<>();
        objects.add(37165L);
        objects.add(37129L);
        List<EsbEmployeeDO> esbEmployeeDOS = esbEmployeeService.listByJobIdsForAgency(objects);
        System.out.println(esbEmployeeDOS);
    }
}
