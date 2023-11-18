package com.yiling.ih.patient.api.impl;

import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import com.yiling.ih.patient.feign.HmcDiagnosisFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * HMC问诊API实现类
 *
 * @author: fan.shen
 * @date: 2022/6/7
 */
@Slf4j
@DubboService
public class HmcDiagnosisApiImpl implements HmcDiagnosisApi {

    @Autowired
    private HmcDiagnosisFeignClient hmcDiagnosisFeignClient;
    //
    // @DubboReference
    // StandardGoodsApi standardGoodsApi;
    //
    // @DubboReference
    // StandardGoodsSpecificationApi specificationApi;

    @Override
    public DiagnosisDoctorResultDTO searchDiagnosisDoctor(HmcQueryDiagnosisDoctorPageRequest request) {
        log.info("[searchDiagnosisDoctor]首页问诊医生，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<DiagnosisDoctorResultDTO> apiResult = hmcDiagnosisFeignClient.queryDiagnosisDoctor(request);
        log.info("[searchDiagnosisDoctor]首页问诊医生，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new DiagnosisDoctorResultDTO();
    }

    @Override
    public HmcDiagnosisDoctorDetailDTO diagnosisDoctorDetail(QueryDiagnosisDoctorDetailRequest request) {
        log.info("[diagnosisDoctorDetail]获取问诊医生详情，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcDiagnosisDoctorDetailDTO> apiResult = hmcDiagnosisFeignClient.diagnosisDoctorDetail(request);
        log.info("[diagnosisDoctorDetail]获取问诊医生详情，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcDiagnosisDoctorDetailDTO();
    }

    @Override
    public List<DepartmentDTO> getDepartmentList() {
        ApiResult<List<DepartmentDTO>> apiResult = hmcDiagnosisFeignClient.getDepartmentList();
        log.info("[getDepartmentList]获取部门，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public HmcSubmitDiagnosisOrderDTO submitDiagnosisOrder(HmcSubmitDiagnosisOrderRequest request) {
        log.info("[submitDiagnosisOrder]调用IH接口创建问诊单，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcSubmitDiagnosisOrderDTO> apiResult = hmcDiagnosisFeignClient.submitDiagnosisOrder(request);
        log.info("[submitDiagnosisOrder]调用IH接口创建问诊单，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public Boolean saveDiagnosisComment(SaveDiagnosisCommentRequest request) {
        log.info("[saveDiagnosisComment]保存评论，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.saveComment(request);
        log.info("[saveDiagnosisComment]保存评论，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (!apiResult.success()) {
            log.error("调用问诊远程接口saveDiagnosisComment出错");
            return false;
        }
        return true;
    }

    @Override
    public DiagnosisCommentDTO queryComment(Long id) {
        HmcQueryDiagnosisOrderCommentRequest request = new HmcQueryDiagnosisOrderCommentRequest();
        request.setDiagnosisRecordId(id.intValue());
        log.info("[queryComment]查询评论，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<DiagnosisCommentDTO> apiResult = hmcDiagnosisFeignClient.queryComment(request);
        log.info("[queryComment]查询评论，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (!apiResult.success()) {
            log.error("调用问诊远程接口queryComment出错");
            return new DiagnosisCommentDTO();
        }
        return apiResult.getData();
    }

    @Override
    public List<HmcDiagnosisDoctorSignalSourceDTO> diagnosisDoctorSignalSource(Integer doctorId) {
        log.info("[diagnosisDoctorSignalSource]获取医生排班，入参:{}", JSONUtil.toJsonStr(doctorId));
        ApiResult<List<HmcDiagnosisDoctorSignalSourceDTO>> apiResult = hmcDiagnosisFeignClient.diagnosisDoctorSignalSource(doctorId);
        log.info("[diagnosisDoctorSignalSource]获取医生排班，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (!apiResult.success()) {
            log.error("调用问诊远程接口queryComment出错");
            return Lists.newArrayList();
        }
        return apiResult.getData();
    }

    @Override
    public HmcSearchDoctorResultDTO searchDoctor(SearchDiagnosisDoctorPageRequest request) {
        log.info("[searchDoctor]搜索医生参数:{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcSearchDoctorResultDTO> apiResult = hmcDiagnosisFeignClient.searchDoctor(request);
        log.info("[searchDoctor]搜索医生返回:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcSearchDoctorResultDTO();

    }

    @Override
    public HmcDoctorVerifyDetailDTO doctorVerifyDetail(Integer doctorId) {
        DoctorVerifyDetailRequest request = new DoctorVerifyDetailRequest();
        request.setDoctorId(doctorId);
        ApiResult<HmcDoctorVerifyDetailDTO> apiResult = hmcDiagnosisFeignClient.doctorVerifyDetail(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public Boolean subDoctor(HmcSubDoctorRequest request) {
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.subDoctor(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean cancelSubDoctor(HmcSubDoctorRequest request) {
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.cancelSubDoctor(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean diagnosisOrderPayNotify(DiagnosisOrderPaySuccessNotifyRequest request) {
        log.info("[diagnosisOrderPayNotify]问诊单支付回调通知-入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<DiagnosisNotifyIhResponseDTO> apiResult = hmcDiagnosisFeignClient.diagnosisOrderPayNotify(request);
        log.info("[diagnosisOrderPayNotify]问诊单支付回调通知-返参：{}", JSONUtil.toJsonStr(apiResult));
        // 状态 1订单金额或状态异常无法更新 2成功
        if (apiResult.success() && apiResult.getData().getStatus().equals(2)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public HmcDiagnosisRecordDetailDTO getDiagnosisOrderDetailById(HmcDiagnosisOrderDetailRequest request) {
        log.info("[getDiagnosisOrderDetailById]获取问诊单详情-入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcDiagnosisRecordDetailDTO> apiResult = hmcDiagnosisFeignClient.getDiagnosisOrderDetailById(request);
        log.info("[getDiagnosisOrderDetailById]获取问诊单详情-返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public List<HmcPrescriptionListDTO> getPrescriptionByDiagnosisOrderIdList(HmcDiagnosisOrderDetailRequest request) {
        log.info("[getPrescriptionByDiagnosisOrderIdList]获取处方详情-入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcPrescriptionListDTO>> apiResult = hmcDiagnosisFeignClient.getPrescriptionByDiagnosisOrderIdList(request);
        log.info("[getPrescriptionByDiagnosisOrderIdList]获取处方详情-返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<HmcDiagnosisRecordDTO> getDiagnosisOrderByIdList(HmcDiagnosisOrderRequest request) {
        log.info("[getDiagnosisOrderByIdList]获取问诊订单详情-入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcDiagnosisRecordDTO>> apiResult = hmcDiagnosisFeignClient.getDiagnosisOrderByIdList(request);
        log.info("[getDiagnosisOrderByIdList]获取问诊订单详情-返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public HmcCancelDiagnosisOrderDTO cancelDiagnosisRecord(HmcCancelDiagnosisOrderRequest request) {
        log.info("[cancelDiagnosisRecord]取消-入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcCancelDiagnosisOrderDTO> apiResult = hmcDiagnosisFeignClient.cancelDiagnosisRecord(request);
        log.info("[cancelDiagnosisRecord]取消-返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public Boolean cancelDiagnosisRecordNotify(HmcCancelDiagnosisOrderNotifyRequest request) {
        log.info("[cancelDiagnosisRecordNotify]取消咨询-退款回调入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.cancelDiagnosisRecordNotify(request);
        log.info("[cancelDiagnosisRecordNotify]取消咨询-返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean supplementDiagnosisOrder(HmcSupplementDiseaseDescribeRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.supplementDiagnosisOrder(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public HmcSelectDiagnosisRecordDTO selectDiagnosisRecordWaitList(SearchDiagnosisRecordPageRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<HmcSelectDiagnosisRecordDTO> apiResult = hmcDiagnosisFeignClient.selectDiagnosisRecordWaitList(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcSelectDiagnosisRecordDTO();
    }

    @Override
    public HmcSelectDiagnosisRecordDTO selectDiagnosisRecordGoing(SearchDiagnosisRecordPageRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<HmcSelectDiagnosisRecordDTO> apiResult = hmcDiagnosisFeignClient.selectDiagnosisRecordGoing(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcSelectDiagnosisRecordDTO();
    }

    @Override
    public HmcSelectDiagnosisRecordDTO selectDiagnosisRecordHistory(SearchDiagnosisRecordPageRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<HmcSelectDiagnosisRecordDTO> apiResult = hmcDiagnosisFeignClient.selectDiagnosisRecordHistory(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcSelectDiagnosisRecordDTO();
    }

    @Override
    public HmcMyDoctorConsultationDTO myDoctorConsultationList(MyDoctorPageRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<HmcMyDoctorConsultationDTO> apiResult = hmcDiagnosisFeignClient.myDoctorConsultationList(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcMyDoctorConsultationDTO();
    }

    @Override
    public HmcMyDoctorConsultationDTO myDoctorFollowList(MyDoctorPageRequest request) {
        log.info("入参：{}",JSONUtil.toJsonStr(request));
        ApiResult<HmcMyDoctorConsultationDTO> apiResult = hmcDiagnosisFeignClient.myDoctorFollowList(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new HmcMyDoctorConsultationDTO();
    }

    @Override
    public HmcSyncGoodsToIhDTO syncHmcGoodsToIH(HmcSyncGoodsRequest request) {
        log.info("[syncHmcGoodsToIH]HMC同步商品到IH入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcSyncGoodsToIhDTO> apiResult = hmcDiagnosisFeignClient.syncHmcGoodsToIH(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public List<HmcGetGoodsInfoDTO> getIHPharmacyGoodsInfo(HmcGetIHGoodsInfoRequest request) {
        log.info("[syncHmcGoodsToIH]HMC同步商品到IH入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcGetGoodsInfoDTO>> apiResult = hmcDiagnosisFeignClient.getIHPharmacyGoodsInfo(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean updateGoodsPriceAndStock(HmcUpdateGoodsPriceAndPriceRequest request) {
        log.info("[updateGoodsPriceAndStock]修改价格库存-退款回调入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.updateGoodsPriceAndStock(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public HmcIMChatRootQueryDiagnosisRecordDTO imCharRoomQueryDiagnosisOrderWithDoc(QueryDiagnosisRecordByUserIdAndDocIdRequest request) {
        log.info("通过聊天室id查询-> 当前用户和问诊医生之间有没有进行中的问诊单、处方开方状态入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcIMChatRootQueryDiagnosisRecordDTO> apiResult = hmcDiagnosisFeignClient.imCharRoomQueryDiagnosisOrderWithDoc(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public Boolean messageNotify(String param) {
        MessageNotifyRequest request = new MessageNotifyRequest();
        request.setParam(param);
        log.info("消息回调-> 请求入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.messageNotify(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return false;
    }

    @Override
    public Boolean eventCallBack(String param) {
        MessageNotifyRequest request = new MessageNotifyRequest();
        request.setParam(param);
        log.info("视频回调-> 请求入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.eventCallBack(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return false;
    }

    @Override
    public Integer getRemainingSecondsByRoom(GetRemainingSecondsRequest request) {
        log.info("根据房间号获取剩余通话时长-> 请求入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcGetRemainingSecondsByRoomDTO> apiResult = hmcDiagnosisFeignClient.getRemainingSecondsByRoom(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData() != null ? apiResult.getData().getSeconds() : null;
        }
        return null;
    }

    @Override
    public boolean videoRefuseOrNotThrough(VideoRefuseOrNotThroughRequest request) {
        log.info("视频拒接及未接通记录 -> 请求入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcDiagnosisFeignClient.videoRefuseOrNotThrough(request);
        log.info("返参：{}",JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return false;
    }

    @Override
    public List<HmcIMChatHistoryRecordDTO> getIMChatHistoryRecord(GetIMChatHistoryRecordPageRequest request) {
        log.info("查询聊天记录-> 请求入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcIMChatHistoryRecordDTO>> apiResult = hmcDiagnosisFeignClient.getIMChatHistoryRecord(request);
        log.info("查询聊天记录-> 请求返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<HmcIMConversationListDTO> getIMConversationList(GetIMConversationListRequest request) {
        log.info("查询会话列表-> 入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcIMConversationListDTO>> apiResult = hmcDiagnosisFeignClient.getIMConversationList(request);
        log.info("查询会话列表-> 返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public void reportRead(ReadReportRequest request) {
        log.info("reportRead 已读上报-> 入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<List<HmcIMConversationListDTO>> apiResult = hmcDiagnosisFeignClient.reportRead(request);
        log.info("reportRead 已读上报 -> 返参：{}", JSONUtil.toJsonStr(apiResult));
    }

}