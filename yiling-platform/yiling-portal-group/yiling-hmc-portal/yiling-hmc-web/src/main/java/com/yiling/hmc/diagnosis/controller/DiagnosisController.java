package com.yiling.hmc.diagnosis.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.diagnosis.form.*;
import com.yiling.hmc.diagnosis.vo.*;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.request.*;
import com.yiling.hmc.wechat.enums.HmcDiagnosisMsgTypeEnum;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.payment.pay.dto.request.RefundParamRequest;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.HmcUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 问诊
 *
 * @author: fan.shen
 * @date: 2022-12-12
 */
@Api(tags = "问诊")
@RestController
@RequestMapping("/diagnosis")
@Slf4j
public class DiagnosisController extends BaseController {

    @Autowired
    FileService fileService;

    @Autowired
    RedisService redisService;

    @DubboReference(timeout = 1000 * 60)
    HmcDiagnosisApi diagnosisApi;

    @DubboReference
    PayApi payApi;

    @DubboReference
    RefundApi refundApi;

    @DubboReference
    TencentIMApi tencentIMApi;

    @DubboReference
    DoctorApi doctorApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    MarketOrderApi marketOrderApi;

    @Value("${env.name}")
    private String envName;


    @ApiOperation("首页问诊医生")
    @PostMapping("diagnosisDoctor")
    @Log(title = "首页问诊医生", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<DiagnosisDoctorVO>> searchDiagnosisDoctor(@RequestBody @Valid DiagnosisDoctorPageForm form) {
        HmcQueryDiagnosisDoctorPageRequest request = new HmcQueryDiagnosisDoctorPageRequest();
        request.setContent(form.getContent());
        request.setSize(form.getSize());
        request.setCurrent(form.getCurrent());
        DiagnosisDoctorResultDTO doctorResultDTO = diagnosisApi.searchDiagnosisDoctor(request);
        Page<DiagnosisDoctorVO> result = form.getPage();
        if (Objects.isNull(doctorResultDTO) || doctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(doctorResultDTO.getTotal());
        List<DiagnosisDoctorDTO> list = doctorResultDTO.getList();
        result.setRecords(PojoUtils.map(list, DiagnosisDoctorVO.class));
        return Result.success(result);
    }

    @ApiOperation("医生详情")
    @PostMapping("diagnosisDoctorDetail")
    @Log(title = "医生详情", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcDiagnosisDoctorDetailVO> diagnosisDoctorDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid GetDiagnosisDoctorDetailForm form) {
        QueryDiagnosisDoctorDetailRequest request = new QueryDiagnosisDoctorDetailRequest();
        request.setDoctorId(form.getDoctorId());
        if (Objects.nonNull(currentUser)) {
            request.setFromUserId(currentUser.getCurrentUserId().intValue());
        }
        HmcDiagnosisDoctorDetailDTO detail = diagnosisApi.diagnosisDoctorDetail(request);
        return Result.success(PojoUtils.map(detail, HmcDiagnosisDoctorDetailVO.class));
    }

    @ApiOperation("医生号源")
    @PostMapping("diagnosisDoctorSignalSource")
    @Log(title = "医生号源", businessType = BusinessTypeEnum.OTHER)
    public Result<List<HmcDiagnosisDoctorSignalSourceVO>> diagnosisDoctorSignalSource(@RequestBody @Valid DiagnosisDoctorSignalSourceForm form) {
        List<HmcDiagnosisDoctorSignalSourceDTO> sourceDTOList = diagnosisApi.diagnosisDoctorSignalSource(form.getDoctorId());
        return Result.success(PojoUtils.map(sourceDTOList, HmcDiagnosisDoctorSignalSourceVO.class));
    }

    @ApiOperation("已认证详情接口")
    @PostMapping("doctorVerifyDetail")
    @Log(title = "已认证详情接口", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcSearchDoctorVerifyDetailVO> doctorVerifyDetail(@RequestBody @Valid DiagnosisDoctorVerifyDetailForm form) {
        HmcDoctorVerifyDetailDTO verifyDetailDTO = diagnosisApi.doctorVerifyDetail(form.getDoctorId());
        return Result.success(PojoUtils.map(verifyDetailDTO, HmcSearchDoctorVerifyDetailVO.class));
    }

    @ApiOperation("关注医生接口")
    @PostMapping("subDoctor")
    @Log(title = "关注医生接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> subDoctor(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid DiagnosisDoctorVerifyDetailForm form) {
        HmcSubDoctorRequest request = PojoUtils.map(form, HmcSubDoctorRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        Boolean res = diagnosisApi.subDoctor(request);
        return Result.success(res);
    }

    @ApiOperation("取关医生接口")
    @PostMapping("cancelSubDoctor")
    @Log(title = "取关医生接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> cancelSubDoctor(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid DiagnosisDoctorVerifyDetailForm form) {
        HmcSubDoctorRequest request = PojoUtils.map(form, HmcSubDoctorRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        Boolean res = diagnosisApi.cancelSubDoctor(request);
        return Result.success(res);
    }

    @ApiOperation("咨询过的医生")
    @PostMapping("myDoctorConsultationList")
    @Log(title = "找医生", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcMyDoctorConsultationListVO>> myDoctorConsultationList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        MyDoctorPageRequest request = PojoUtils.map(form, MyDoctorPageRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcMyDoctorConsultationDTO searchDoctorResultDTO = diagnosisApi.myDoctorConsultationList(request);
        Page<HmcMyDoctorConsultationListVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcMyDoctorConsultationDetailDTO> list = searchDoctorResultDTO.getList();
        result.setRecords(PojoUtils.map(list, HmcMyDoctorConsultationListVO.class));
        return Result.success(result);
    }

    @ApiOperation("关注过的医生")
    @PostMapping("myDoctorFollowList")
    @Log(title = "关注过的医生", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcMyDoctorConsultationListVO>> myDoctorFollowList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        MyDoctorPageRequest request = PojoUtils.map(form, MyDoctorPageRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcMyDoctorConsultationDTO searchDoctorResultDTO = diagnosisApi.myDoctorFollowList(request);
        Page<HmcMyDoctorConsultationListVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcMyDoctorConsultationDetailDTO> list = searchDoctorResultDTO.getList();
        result.setRecords(PojoUtils.map(list, HmcMyDoctorConsultationListVO.class));
        return Result.success(result);
    }

    @ApiOperation("找医生")
    @PostMapping("searchDoctor")
    @Log(title = "找医生", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcSearchDoctorVO>> searchDoctor(@RequestBody @Valid SearchDoctorForm form) {
        SearchDiagnosisDoctorPageRequest request = PojoUtils.map(form, SearchDiagnosisDoctorPageRequest.class);
        request.setSize(form.getSize());
        request.setCurrent(form.getCurrent());
        HmcSearchDoctorResultDTO searchDoctorResultDTO = diagnosisApi.searchDoctor(request);
        Page<HmcSearchDoctorVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcSearchDoctorDTO> list = searchDoctorResultDTO.getList();
        result.setRecords(PojoUtils.map(list, HmcSearchDoctorVO.class));
        return Result.success(result);
    }

    @ApiOperation("科室列表")
    @PostMapping("departmentList")
    @Log(title = "科室列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<DepartmentVO>> departmentList(@CurrentUser CurrentUserInfo currentUser) {
        List<DepartmentDTO> departmentList = diagnosisApi.getDepartmentList();
        return Result.success(PojoUtils.map(departmentList, DepartmentVO.class));
    }

    @ApiOperation("提交预约问诊单")
    @PostMapping("submitDiagnosis")
    @Log(title = "提交预约问诊单", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcSubmitDiagnosisOrderVO> submitDiagnosis(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid HmcSubmitDiagnosisRecordForm form) {
        String userId = envName + "_" + "HMC_" + currentUser.getCurrentUserId();
        Integer patientId = form.getPatientId();
        String docId = envName + "_" + "IH_DOC_" + form.getDoctorId();
        // HMC_10|100|IH_DOC_200
        String redisKey = userId + "|" + patientId + "|" + docId;
        String conversationID;

        // 获取医生信息
        DoctorAppInfoDTO doctorInfo = doctorApi.getDoctorInfoByDoctorId(form.getDoctorId());

        // 判断redis是否存在群组 -> 如果存在，直接取出返回group，否则创建群组
        if (redisService.hasKey(redisKey)) {
            conversationID = redisService.get(redisKey).toString();
            log.info("通过redisKey：{}获取到会话信息，conversationID：{}", redisKey, conversationID);
        } else {

            // 获取用户信息
            HmcUser hmcUser = hmcUserApi.getById(currentUser.getCurrentUserId());

            // 导入医生
            tencentIMApi.accountImport(Long.valueOf(form.getDoctorId()), 1);

            // 创建群组
            CreateGroupRequest createGroupRequest = new CreateGroupRequest();
            createGroupRequest.setName("医患问诊");
            createGroupRequest.setType("Work");
            List<MemberListDTO> MemberList = new ArrayList<>();
            MemberList.add(MemberListDTO.builder().Member_Account(userId).build());
            MemberList.add(MemberListDTO.builder().Member_Account(docId).build());

            List<AppDefinedDTO> AppDefinedData = new ArrayList<>();
            AppDefinedData.add(AppDefinedDTO.builder().Key("groupDocPic").Value(doctorInfo.getPicture()).build());
            AppDefinedData.add(AppDefinedDTO.builder().Key("groupUserPic").Value(StrUtil.isBlank(hmcUser.getAvatarUrl()) ? Constants.HZ_DEFAULT_AVATAR : hmcUser.getAvatarUrl()).build());
            AppDefinedData.add(AppDefinedDTO.builder().Key("groupDocName").Value(doctorInfo.getDoctorName()).build());
            AppDefinedData.add(AppDefinedDTO.builder().Key("groupUserName").Value(hmcUser.getNickName()).build());

            createGroupRequest.setMemberList(MemberList);
            createGroupRequest.setAppDefinedData(AppDefinedData);
            log.info("创建群组参数:{}", JSONUtil.toJsonStr(createGroupRequest));
            conversationID = "GROUP" + tencentIMApi.createGroup(createGroupRequest);

            // 存入redis
            redisService.set(redisKey, JSONUtil.toJsonStr(conversationID));
            redisService.set(conversationID, JSONUtil.toJsonStr(redisKey));
        }
        HmcSubmitDiagnosisOrderRequest request = PojoUtils.map(form, HmcSubmitDiagnosisOrderRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        request.setGroupId(conversationID.substring(5));
        HmcSubmitDiagnosisOrderDTO hmcSubmitDiagnosisOrderDTO = diagnosisApi.submitDiagnosisOrder(request);
        if (Objects.isNull(hmcSubmitDiagnosisOrderDTO)) {
            return Result.failed("调用IH接口创建问诊单失败");
        }
        HmcSubmitDiagnosisOrderVO submitDiagnosisOrderVO = PojoUtils.map(hmcSubmitDiagnosisOrderDTO, HmcSubmitDiagnosisOrderVO.class);

        // 创建支付订单 -> 状态 1有正在进行的问诊单,diagnosisRecordId是正在进行的问诊单id 2所选号段已无号源的校验 3正常下单 4-0元订单
        if (hmcSubmitDiagnosisOrderDTO.getStatus() == 3 && hmcSubmitDiagnosisOrderDTO.getDiagnosisRecordPrice().compareTo(BigDecimal.ZERO) > 0) {
            CreatePayOrderRequest payOrderRequest = buildPayOrderRequest(hmcSubmitDiagnosisOrderDTO, currentUser);
            Result<String> payOrder = payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
            log.info("创建支付订单入参：{},返回参数:{}", JSONUtil.toJsonStr(payOrderRequest), JSONUtil.toJsonStr(payOrder));
            submitDiagnosisOrderVO.setPayTicket(payOrder.getData());
        }
        if (hmcSubmitDiagnosisOrderDTO.getStatus() == 4) {
            // 推送群聊消息
            HmcDiagnosisOrderDetailRequest detailRequest = new HmcDiagnosisOrderDetailRequest();
            detailRequest.setDiagnosisRecordId(hmcSubmitDiagnosisOrderDTO.getDiagnosisRecordId());
            HmcDiagnosisRecordDetailDTO diagnosisOrder = diagnosisApi.getDiagnosisOrderDetailById(detailRequest);
            SendGroupMsgRequest sendMsgToDoctorRequest = buildMsg(diagnosisOrder);
            log.info("0元订单直接推送卡片,卡片参数:{}", JSONUtil.toJsonStr(sendMsgToDoctorRequest));
            tencentIMApi.sendGroupMsg(sendMsgToDoctorRequest);
        }

        submitDiagnosisOrderVO.setConversationID(conversationID);
        submitDiagnosisOrderVO.setDocId(form.getDoctorId());
        submitDiagnosisOrderVO.setDocName(doctorInfo.getDoctorName());
        return Result.success(submitDiagnosisOrderVO);
    }

    @ApiOperation("根据房间号获取剩余通话时长")
    @PostMapping("getRemainingSecondsByRoom")
    @Log(title = "根据房间号获取剩余通话时长", businessType = BusinessTypeEnum.OTHER)
    public Result<Integer> getRemainingSecondsByRoom(@RequestBody @Valid GetRemainingSecondsForm form) {
        GetRemainingSecondsRequest request = PojoUtils.map(form, GetRemainingSecondsRequest.class);
        Integer seconds = diagnosisApi.getRemainingSecondsByRoom(request);
        return Result.success(seconds);
    }

    /**
     * 构建消息体
     *
     * @param diagnosisOrder
     * @return
     */
    public SendGroupMsgRequest buildMsg(HmcDiagnosisRecordDetailDTO diagnosisOrder) {

        String userId = envName + "_" + "HMC_" + diagnosisOrder.getHmcUserId();
        String docId = envName + "_" + "IH_DOC_" + diagnosisOrder.getDoctorId();
        Integer patientId = diagnosisOrder.getPatientId();
        String redisKey = userId + "|" + patientId + "|" + docId;
        if (!redisService.hasKey(redisKey)) {
            throw new BusinessException(HmcOrderErrorCode.IM_GET_REDIS_GROUP_ERROR);
        }
        List<MsgBodyDTO> MsgBody = new ArrayList<>();
        MsgBodyDTO msgBodyDTO = new MsgBodyDTO();
        msgBodyDTO.setMsgType("TIMCustomElem");
        MsgContentDTO MsgContent = new MsgContentDTO();
        Map<String, Object> customData = Maps.newHashMap();
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
            Date diagnosisRecordCreateTime = diagnosisOrder.getDiagnosisRecordCreateTime();
            String date = DateUtil.formatDate(diagnosisRecordCreateTime);
            String startTime = DateUtil.format(diagnosisRecordCreateTime, "HH:mm");
            String diagnosisTime = date + " " + startTime;
            customData.put("diagnosisRecordStartTime", diagnosisTime);
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


    @ApiOperation("IM 获取群组信息")
    @PostMapping("getIMGroupInfo")
    @Log(title = "IM 获取群组信息", businessType = BusinessTypeEnum.OTHER)
    public Result<List<IMGroupInfoVO>> getIMGroupInfo(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid List<GetGroupInfoForm> form) {
        ArrayList<IMGroupInfoVO> imGroupInfoVOList = Lists.newArrayList();
        if (CollUtil.isEmpty(form)) {
            return Result.success(imGroupInfoVOList);
        }
        List<String> conversationIDList = form.stream().filter(item -> item.getType().equals("GROUP")).map(GetGroupInfoForm::getConversationID).collect(Collectors.toList());
        conversationIDList = conversationIDList.stream().map(item -> item.substring(5)).collect(Collectors.toList());
        GetGroupInfoRequest request = new GetGroupInfoRequest();
        request.setGroupIdList(conversationIDList);
        String result = tencentIMApi.getGroupInfo(request);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONArray groupInfo = jsonObject.getJSONArray("GroupInfo");
        for (int i = 0; i < groupInfo.size(); i++) {
            JSONObject item = JSONUtil.parseObj(groupInfo.get(i).toString());
            String conversationID = "GROUP" + item.getStr("GroupId");
            IMGroupInfoVO groupInfoVO = new IMGroupInfoVO();
            groupInfoVO.setConversationID(conversationID);
            JSONArray appDefinedData = item.getJSONArray("AppDefinedData");
            if (Objects.isNull(appDefinedData)) {
                continue;
            }
            for (int j = 0; j < appDefinedData.size(); j++) {
                String key = JSONUtil.parseObj(appDefinedData.get(j).toString()).getStr("Key");
                String Value = JSONUtil.parseObj(appDefinedData.get(j).toString()).getStr("Value");
                if ("groupDocPic".equals(key)) {
                    groupInfoVO.setGroupDocPic(Value);
                }
                if ("groupUserPic".equals(key)) {
                    groupInfoVO.setGroupUserPic(Value);
                }
                if ("groupUserName".equals(key)) {
                    groupInfoVO.setGroupUserName(Value);
                }
                if ("groupDocName".equals(key)) {
                    groupInfoVO.setGroupDocName(Value);
                }
            }
            imGroupInfoVOList.add(groupInfoVO);
        }
        return Result.success(imGroupInfoVOList);
    }

    /**
     * 创建支付订单
     *
     * @return
     */
    public CreatePayOrderRequest buildPayOrderRequest(HmcSubmitDiagnosisOrderDTO hmcSubmitDiagnosisOrderDTO, CurrentUserInfo currentUser) {
        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        payOrderRequest.setTradeType(TradeTypeEnum.INQUIRY);

        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();
        CreatePayOrderRequest.appOrderRequest appOrderRequest = new CreatePayOrderRequest.appOrderRequest();
        appOrderRequest.setAppOrderId(Long.valueOf(hmcSubmitDiagnosisOrderDTO.getDiagnosisRecordId()));
        appOrderRequest.setAppOrderNo(hmcSubmitDiagnosisOrderDTO.getDiagnosisRecordOrderNo());
        appOrderRequest.setAmount(hmcSubmitDiagnosisOrderDTO.getDiagnosisRecordPrice());
        appOrderRequest.setUserId(currentUser.getCurrentUserId());
        // appOrderRequest.setSellerEid(marketOrderDO.getEid());

        appOrderList.add(appOrderRequest);
        payOrderRequest.setAppOrderList(appOrderList);
        return payOrderRequest;
    }

    @ApiOperation("获取问诊单详情接口")
    @PostMapping("getDiagnosisOrderDetailById")
    @Log(title = "获取问诊单详情接口", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcDiagnosisRecordDetailVO> getDiagnosisOrderDetailById(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid HmcSelectDiagnosisRecordInfoForm form) {
        HmcDiagnosisOrderDetailRequest request = PojoUtils.map(form, HmcDiagnosisOrderDetailRequest.class);
        HmcDiagnosisRecordDetailDTO hmcDiagnosisRecordDetailDTO = diagnosisApi.getDiagnosisOrderDetailById(request);
        HmcDiagnosisRecordDetailVO recordDetailVO = PojoUtils.map(hmcDiagnosisRecordDetailDTO, HmcDiagnosisRecordDetailVO.class);
        recordDetailVO.setConversationID("GROUP" + hmcDiagnosisRecordDetailDTO.getConversationID());
        return Result.success(recordDetailVO);
    }

    @ApiOperation("当前问诊单关联的所有处方列表")
    @PostMapping("getPrescriptionByDiagnosisOrderIdList")
    @Log(title = "当前问诊单关联的所有处方列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<HmcPrescriptionListVO>> getPrescriptionByDiagnosisOrderIdList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid HmcSelectDiagnosisRecordInfoForm form) {
        HmcDiagnosisOrderDetailRequest request = PojoUtils.map(form, HmcDiagnosisOrderDetailRequest.class);
        List<HmcPrescriptionListDTO> prescriptionList = diagnosisApi.getPrescriptionByDiagnosisOrderIdList(request);
        List<HmcPrescriptionListVO> voList = PojoUtils.map(prescriptionList, HmcPrescriptionListVO.class);
        for (HmcPrescriptionListVO hmcPrescriptionListVO : voList) {
            MarketOrderDTO marketOrderDTO = marketOrderApi.queryPrescriptionOrderByPrescriptionId(Long.parseLong(hmcPrescriptionListVO.getId().toString()));
            hmcPrescriptionListVO.setOrderId(Objects.nonNull(marketOrderDTO) ? marketOrderDTO.getId() : null);
        }
        return Result.success(voList);
    }

    @ApiOperation("用户取消咨询接口")
    @PostMapping("userCancelDiagnosisRecord")
    @Log(title = "取消咨询接口", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcCancelDiagnosisOrderVO> cancelDiagnosisRecord(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid HmcSelectDiagnosisRecordInfoForm form) {
        HmcCancelDiagnosisOrderRequest request = PojoUtils.map(form, HmcCancelDiagnosisOrderRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcCancelDiagnosisOrderDTO cancelDiagnosisRecord = diagnosisApi.cancelDiagnosisRecord(request);

        HmcDiagnosisOrderDetailRequest detailRequest = new HmcDiagnosisOrderDetailRequest();
        detailRequest.setDiagnosisRecordId(form.getDiagnosisRecordId());
        HmcDiagnosisRecordDetailDTO diagnosisOrderDetailById = diagnosisApi.getDiagnosisOrderDetailById(detailRequest);

        if (Objects.isNull(cancelDiagnosisRecord)) {
            return Result.failed("取消咨询失败");
        }

        HmcCancelDiagnosisOrderVO cancelDiagnosisOrderVO = new HmcCancelDiagnosisOrderVO();
        cancelDiagnosisOrderVO.setStatus(cancelDiagnosisRecord.getStatus());

        // 状态 1问诊单不存在 2问诊单状态异常无法取消 3问诊单错误无法取消 4成功
        if (cancelDiagnosisRecord.getStatus().equals(4)) {
            RefundParamRequest refundParam = RefundParamRequest.builder()
                    .appOrderId(Long.valueOf(form.getDiagnosisRecordId()))
                    .refundId(Long.valueOf(cancelDiagnosisRecord.getDiagnosisRecordRefundId()))
                    .payNo(cancelDiagnosisRecord.getMerTranNo())
                    .refundType(1)
                    .appOrderNo(diagnosisOrderDetailById.getOrderNo())
                    .refundAmount(cancelDiagnosisRecord.getPrice()).build();

            log.info("cancelDiagnosisRecord 用户取消咨询接口，refundApi.refundPayOrder入参:{}", JSONUtil.toJsonStr(refundParam));
            refundApi.refundPayOrder(refundParam);
            // 退款回调接口 com.yiling.ih.patient.api.HmcDiagnosisApi.cancelDiagnosisRecordNotify
        }

        return Result.success(cancelDiagnosisOrderVO);
    }


    @ApiOperation("补充症状描述接口")
    @PostMapping("supplementDiagnosisOrder")
    @Log(title = "补充症状描述接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> supplementDiagnosisOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid HmcSupplementDiseaseDescribeForm form) {
        HmcSupplementDiseaseDescribeRequest request = PojoUtils.map(form, HmcSupplementDiseaseDescribeRequest.class);
        return Result.success(diagnosisApi.supplementDiagnosisOrder(request));
    }

    @ApiOperation("待问诊列表接口")
    @PostMapping("selectDiagnosisRecordWaitList")
    @Log(title = "待问诊列表接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcSelectDiagnosisRecordListVO>> selectDiagnosisRecordWaitList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        SearchDiagnosisRecordPageRequest request = PojoUtils.map(form, SearchDiagnosisRecordPageRequest.class);
        request.setSize(form.getSize());
        request.setCurrent(form.getCurrent());
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcSelectDiagnosisRecordDTO searchDoctorResultDTO = diagnosisApi.selectDiagnosisRecordWaitList(request);
        Page<HmcSelectDiagnosisRecordListVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcSelectDiagnosisRecordListDTO> list = searchDoctorResultDTO.getList();
        List<HmcSelectDiagnosisRecordListVO> map = PojoUtils.map(list, HmcSelectDiagnosisRecordListVO.class);
        map.forEach(item -> item.setConversationID("GROUP" + item.getConversationID()));
        result.setRecords(map);
        return Result.success(result);
    }

    @ApiOperation("问诊中列表接口")
    @PostMapping("selectDiagnosisRecordGoing")
    @Log(title = "问诊中列表接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcSelectDiagnosisRecordListVO>> selectDiagnosisRecordGoing(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        SearchDiagnosisRecordPageRequest request = PojoUtils.map(form, SearchDiagnosisRecordPageRequest.class);
        request.setSize(form.getSize());
        request.setCurrent(form.getCurrent());
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcSelectDiagnosisRecordDTO searchDoctorResultDTO = diagnosisApi.selectDiagnosisRecordGoing(request);
        Page<HmcSelectDiagnosisRecordListVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcSelectDiagnosisRecordListDTO> list = searchDoctorResultDTO.getList();
        List<HmcSelectDiagnosisRecordListVO> map = PojoUtils.map(list, HmcSelectDiagnosisRecordListVO.class);
        map.forEach(item -> item.setConversationID("GROUP" + item.getConversationID()));
        result.setRecords(map);
        return Result.success(result);
    }

    @ApiOperation("历史问诊列表接口")
    @PostMapping("selectDiagnosisRecordHistory")
    @Log(title = "历史问诊列表接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcSelectDiagnosisRecordListVO>> selectDiagnosisRecordHistory(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        SearchDiagnosisRecordPageRequest request = PojoUtils.map(form, SearchDiagnosisRecordPageRequest.class);
        request.setSize(form.getSize());
        request.setCurrent(form.getCurrent());
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcSelectDiagnosisRecordDTO searchDoctorResultDTO = diagnosisApi.selectDiagnosisRecordHistory(request);
        Page<HmcSelectDiagnosisRecordListVO> result = form.getPage();
        if (Objects.isNull(searchDoctorResultDTO) || searchDoctorResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(searchDoctorResultDTO.getTotal());
        List<HmcSelectDiagnosisRecordListDTO> list = searchDoctorResultDTO.getList();
        List<HmcSelectDiagnosisRecordListVO> map = PojoUtils.map(list, HmcSelectDiagnosisRecordListVO.class);
        map.forEach(item -> item.setConversationID("GROUP" + item.getConversationID()));
        result.setRecords(map);
        return Result.success(result);
    }

    @ApiOperation("诊后评价")
    @PostMapping("diagnosisComment")
    @Log(title = "诊后评价", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> diagnosisComment(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid DiagnosisCommentForm form) {
        SaveDiagnosisCommentRequest request = PojoUtils.map(form, SaveDiagnosisCommentRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        return Result.success(diagnosisApi.saveDiagnosisComment(request));
    }

    @ApiOperation("查看评价")
    @PostMapping("queryComment")
    @Log(title = "诊后评价", businessType = BusinessTypeEnum.OTHER)
    public Result<DiagnosisCommentVO> queryComment(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid DiagnosisCommentBaseForm form) {
        DiagnosisCommentDTO commentDTO = diagnosisApi.queryComment(form.getId());
        return Result.success(PojoUtils.map(commentDTO, DiagnosisCommentVO.class));
    }

    // public static void main(String[] args) {
    //     String s ="test_HMC_36263|208|test_IH_DOC_2";
    //     s=s.replaceAll("test"+"_","");
    //     String[] split = s.split("\\|");
    //     int userId = Integer.parseInt(split[0].substring(4));
    //     int patientId = Integer.parseInt(split[1]);
    //     int docId = Integer.parseInt(split[2].substring(7));
    //     System.out.println(userId);
    //     System.out.println(patientId);
    //     System.out.println(docId);
    // }


    @ApiOperation("IM查询用户和医生关联问诊单")
    @PostMapping("imQueryDiagnosisOrderWithDoc")
    @Log(title = "IM查询用户和医生关联问诊单", businessType = BusinessTypeEnum.OTHER)
    public Result<IMChatRoomQueryDiagnosisRecordVO> imCharRoomQueryDiagnosisOrderWithDoc(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryDiagnosisOrderFromIMChatRoomForm form) {
        // HMC_10|100|IH_DOC_200
        // test_HMC_36263|208|test_IH_DOC_58
        // String key = userId + "|" + patientId + "|" + docId;
        if (!redisService.hasKey(form.getConversationID())) {
            log.info("根据会话id未获取到缓存信息：参数:{}", JSONUtil.toJsonStr(form));
            return Result.success();
        }

        String conversationValue = redisService.get(form.getConversationID()).toString();
        conversationValue=conversationValue.replaceAll(envName+"_","");
        String[] split = conversationValue.split("\\|");
        int userId = Integer.parseInt(split[0].substring(4));
        int patientId = Integer.parseInt(split[1]);
        int docId = Integer.parseInt(split[2].substring(7));
        QueryDiagnosisRecordByUserIdAndDocIdRequest request = new QueryDiagnosisRecordByUserIdAndDocIdRequest();
        request.setFromUserId(userId);
        request.setPatientId(patientId);
        request.setDocId(docId);
        HmcIMChatRootQueryDiagnosisRecordDTO diagnosisRecordDTO = diagnosisApi.imCharRoomQueryDiagnosisOrderWithDoc(request);
        IMChatRoomQueryDiagnosisRecordVO map = PojoUtils.map(diagnosisRecordDTO, IMChatRoomQueryDiagnosisRecordVO.class);
        map.setDoctorId(docId);
        return Result.success(map);
    }

    @ApiOperation("视频拒接及未接通记录接口")
    @PostMapping("videoRefuseOrNotThrough")
    @Log(title = "视频拒接及未接通记录接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> videoRefuseOrNotThrough(@RequestBody @Valid VideoRefuseOrNotThroughIMChatRoomForm form) {
        VideoRefuseOrNotThroughRequest request = PojoUtils.map(form, VideoRefuseOrNotThroughRequest.class);
        boolean res = diagnosisApi.videoRefuseOrNotThrough(request);
        return Result.success(res);
    }

    @ApiOperation("获取历史记录聊天记录")
    @PostMapping("getIMChatHistoryRecord")
    @Log(title = "获取历史记录聊天记录", businessType = BusinessTypeEnum.OTHER)
    public Result<List<HmcIMChatHistoryRecordVO>> getIMChatHistoryRecord(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid GetIMChatHistoryRecordPageForm form) {
        GetIMChatHistoryRecordPageRequest request = PojoUtils.map(form, GetIMChatHistoryRecordPageRequest.class);
        List<HmcIMChatHistoryRecordDTO> recordDTOList = diagnosisApi.getIMChatHistoryRecord(request);
        return Result.success((PojoUtils.map(recordDTOList, HmcIMChatHistoryRecordVO.class)));
    }

    @ApiOperation("获取IM会话列表")
    @PostMapping("getIMConversationList")
    @Log(title = "获取IM会话列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<HmcConversationListVO>> getIMConversationList(@CurrentUser CurrentUserInfo currentUser) {
        GetIMConversationListRequest request = new GetIMConversationListRequest();
        request.setFromUserId(currentUser.getCurrentUserId());
        List<HmcIMConversationListDTO> recordDTOList = diagnosisApi.getIMConversationList(request);
        return Result.success((PojoUtils.map(recordDTOList, HmcConversationListVO.class)));
    }


    @ApiOperation("上报已读接口")
    @PostMapping("reportRead")
    @Log(title = "上报已读接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> reportRead(@RequestBody @Valid ReportReadForm form) {
        ReadReportRequest map = PojoUtils.map(form, ReadReportRequest.class);
        diagnosisApi.reportRead(map);
        return Result.success(true);
    }


}
