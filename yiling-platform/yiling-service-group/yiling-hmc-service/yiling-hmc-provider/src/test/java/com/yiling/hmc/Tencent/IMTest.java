package com.yiling.hmc.Tencent;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.service.MedsRemindService;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.TencentImSigDTO;
import com.yiling.hmc.tencent.dto.request.*;
import com.yiling.hmc.tencent.enums.OnlineStatusEnum;
import com.yiling.hmc.tencent.feign.TencentIMClient;
import com.yiling.hmc.tencent.feign.response.IMUnReadMsgNumResponse;
import com.yiling.hmc.wechat.enums.HmcDiagnosisMsgTypeEnum;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.HmcDiagnosisRecordDetailDTO;
import com.yiling.ih.patient.dto.request.HmcDiagnosisOrderDetailRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.Msg;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * IM
 */
@Slf4j
public class IMTest extends BaseTest {

    @Autowired
    TencentIMClient tencentIMClient;

    @DubboReference
    TencentIMApi tencentIMApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @Autowired
    RedisService redisService;

    @Test
    public void getSig() {
        TencentImSigDTO tencentIMSig = tencentIMApi.getTencentIMSig(100L, 1);
        log.info(JSONUtil.toJsonStr(tencentIMSig));
    }

    @Test
    public void accountImport() {
        Boolean aBoolean = tencentIMApi.accountImport(104L, 1);
    }

    @Test
    public void sendGroupMsg() {
        HmcDiagnosisOrderDetailRequest detailRequest = new HmcDiagnosisOrderDetailRequest();
        detailRequest.setDiagnosisRecordId(576);
        HmcDiagnosisRecordDetailDTO diagnosisOrder = diagnosisApi.getDiagnosisOrderDetailById(detailRequest);
        SendGroupMsgRequest sendMsgToDoctorRequest = buildMsg(diagnosisOrder);
        log.info("准备发送消息卡片，构建消息入参:{}", JSONUtil.toJsonStr(sendMsgToDoctorRequest));
        String groupInfo = tencentIMApi.sendGroupMsg(sendMsgToDoctorRequest);
        log.info(groupInfo);
    }

    @Test
    public void getDiagnosisOrderDetailById() {
        HmcDiagnosisOrderDetailRequest detailRequest = new HmcDiagnosisOrderDetailRequest();
        detailRequest.setDiagnosisRecordId(590);
        HmcDiagnosisRecordDetailDTO diagnosisOrder = diagnosisApi.getDiagnosisOrderDetailById(detailRequest);
        log.info("getDiagnosisOrderDetailById:{}", JSONUtil.toJsonStr(diagnosisOrder));
    }

    public static void main(String[] args) {
        DateTime start = DateUtil.date(1686033900000L);
        DateTime end = DateUtil.date(1686034800000L);
        // DateTime create = DateUtil.date(1686033474000L);
        // Date diagnosisRecordStartTime = diagnosisOrder.getDiagnosisRecordStartTime();
        // Date diagnosisRecordEndTime = diagnosisOrder.getDiagnosisRecordEndTime();
        String date = DateUtil.formatDate(start);
        String startTime = DateUtil.format(start, "HH:mm");
        String endTime = DateUtil.format(end, "HH:mm");
        String diagnosisTime = date + " " + startTime + "-" + endTime;

        System.out.println(start);
        System.out.println(end);
        System.out.println(diagnosisTime);

    }


    /**
     * 构建消息体
     *
     * @param diagnosisOrder
     * @return
     */
    public SendGroupMsgRequest buildMsg(HmcDiagnosisRecordDetailDTO diagnosisOrder) {
        String userId = "HMC_" + diagnosisOrder.getHmcUserId();
        String docId = "IH_DOC_" + diagnosisOrder.getDoctorId();
        Integer patientId = diagnosisOrder.getPatientId();
        String key = userId + "|" + patientId + "|" + docId;
        String redisKey = RedisKey.generate("HMC", "diagnosis", key);
        if (!redisService.hasKey(redisKey)) {
            log.info("redisKey:{}", redisKey);
            throw new BusinessException(HmcOrderErrorCode.IM_GET_REDIS_GROUP_ERROR);
        }
        List<MsgBodyDTO> MsgBody = new ArrayList<>();
        MsgBodyDTO msgBodyDTO = new MsgBodyDTO();
        msgBodyDTO.setMsgType("TIMCustomElem");
        MsgContentDTO MsgContent = new MsgContentDTO();
        Map<String, String> customData = Maps.newHashMap();
        String patientName = IdcardUtil.hide(diagnosisOrder.getPatientName(), 0, 1);
        // 就诊患者性别 0女 1男
        String gender = diagnosisOrder.getPatientGender() == 1 ? "男" : "女";
        Integer patientAge = diagnosisOrder.getPatientAge();
        String diseaseDescribe = diagnosisOrder.getDiseaseDescribePicture().get(0).getDiseaseDescribe();
        customData.put("diagnosisPatient", patientName + "，" + gender + "," + patientAge + "岁");
        customData.put("symptom", diseaseDescribe);
        customData.put("diagnosisRecordId", diagnosisOrder.getDiagnosisRecordId().toString());
        // 问诊类型，0图文1音频2视频
        Integer type = diagnosisOrder.getType();
        if (type == 0) {
            MsgContent.setData(HmcDiagnosisMsgTypeEnum.DIAGNOSIS_PAY_TIP.getCode());
        } else {
            MsgContent.setData(HmcDiagnosisMsgTypeEnum.DIAGNOSIS_VIDEO_PAY_TIP.getCode());
        }
        MsgContent.setDesc(JSONUtil.toJsonStr(customData));
        msgBodyDTO.setMsgContent(MsgContent);
        MsgBody.add(msgBodyDTO);

        String group = redisService.get(redisKey).toString();
        SendGroupMsgRequest groupMsgRequest = new SendGroupMsgRequest();
        groupMsgRequest.setGroupId(group.substring(5));
        groupMsgRequest.setFrom_Account(userId);
        groupMsgRequest.setRandom(RandomUtil.randomInt(10000, 1000000));
        groupMsgRequest.setMsgBody(MsgBody);

        return groupMsgRequest;
    }

    @Test
    public void getGroupInfo() {
        List<String> groupIdList = Lists.newArrayList();
        groupIdList.add("@TGS#1PG57XXM6");
        groupIdList.add("@TGS#16AEAYXMZ");
        HashMap<String, List> map = Maps.newHashMap();
        map.put("GroupIdList", groupIdList);
        String groupInfo = tencentIMClient.getGroupInfo(JSONUtil.toJsonStr(map));
        log.info(groupInfo);
    }

    @Test
    public void createGroup() {
        /**
         * {
         * 	"Name": "doctorAndUserGroupWork",
         * 	"Type": "Work",
         * 	"MemberList": [{
         * 		"Member_Account": "HMC_177"
         *        }, {
         * 		"Member_Account": "HMC_219"
         *    }]
         * }
         */

        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("医患问诊");
        request.setType("Work");
        List<MemberListDTO> MemberList = new ArrayList<>();
        MemberList.add(MemberListDTO.builder().Member_Account("HMC_177").build());
        MemberList.add(MemberListDTO.builder().Member_Account("HMC_219").build());
        request.setMemberList(MemberList);
        String group = tencentIMClient.createGroup(JSONUtil.toJsonStr(request));
        log.info(group);

    }

    /**
     * 发送自定义消息
     */
    @Test
    public void sendCustomMsg() {
        PatientSendMsgToDoctorRequest request = new PatientSendMsgToDoctorRequest();
        request.setSyncOtherMachine(1);
        request.setFrom_Account("HMC_177");
        request.setTo_Account("IH_DOC_2");
        request.setMsgRandom(RandomUtil.randomInt(1000, 1000000));
        List<MsgBodyDTO> MsgBody = new ArrayList<>();
        MsgBodyDTO msgBodyDTO = new MsgBodyDTO();
        msgBodyDTO.setMsgType("TIMCustomElem");
        MsgContentDTO MsgContent = new MsgContentDTO();
        Map<String, String> customData = Maps.newHashMap();
        customData.put("diagnosisPatient", "*四，男，24岁");
        customData.put("symptom", "打喷嚏、流鼻涕、头疼、心里难受身体发软，没有力气，哪哪儿都不好…");
        customData.put("diagnosisRecordId", "100");

        // diagnosis_pay_tip:图文或极速问诊单支付成功
        // diagnosis_video_pay_tip:电话或视频问诊单支付成功
        MsgContent.setData("diagnosis_pay_tip");
        MsgContent.setDesc(JSONUtil.toJsonStr(customData));
        msgBodyDTO.setMsgContent(MsgContent);
        MsgBody.add(msgBodyDTO);
        request.setMsgBody(MsgBody);
        String result = tencentIMApi.patientSendMsgToDoctor(request);
        log.info(result);
    }

    @Test
    public void queryOnlineStatus() {
        List<String> accountList = new ArrayList<>();
        accountList.add("HMC_177");
        accountList.add("IH_DOC_2");
        Map<String, Integer> onlineStatusEnumMap = tencentIMApi.queryOnlineStatus(accountList);
        log.info(JSON.toJSONString(onlineStatusEnumMap));
    }

    /**
     * 发送文本消息
     */
    @Test
    public void sendTextMsg() {
        PatientSendMsgToDoctorRequest request = new PatientSendMsgToDoctorRequest();
        request.setSyncOtherMachine(1);
        request.setFrom_Account("HMC_177");
        request.setTo_Account("IH_DOC_2");
        request.setMsgRandom(RandomUtil.randomInt(1000, 1000000));
        List<MsgBodyDTO> MsgBody = new ArrayList<>();
        MsgBodyDTO msgBodyDTO = new MsgBodyDTO();
        msgBodyDTO.setMsgType("TIMTextElem");
        MsgContentDTO MsgContent = new MsgContentDTO();
        MsgContent.setText("测试消息123123");
        msgBodyDTO.setMsgContent(MsgContent);
        MsgBody.add(msgBodyDTO);
        request.setMsgBody(MsgBody);
        String result = tencentIMApi.patientSendMsgToDoctor(request);
        log.info(result);
    }

    @Test
    public void unreadMsgNum() {
        Map<String, String> param = new HashMap<>();
        param.put("To_Account", String.valueOf(219));
        String result = tencentIMClient.unreadMsgNum(JSONUtil.toJsonStr(param));
        IMUnReadMsgNumResponse imUnReadMsgNumResponse = JSONUtil.toBean(result, IMUnReadMsgNumResponse.class);
        log.info(JSONUtil.toJsonStr(imUnReadMsgNumResponse));
    }

}
