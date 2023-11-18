package com.yiling.ih.patient.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.config.FeignConfig;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 问诊服务
 *
 * @author: fan.shen
 * @date: 2023/5/8
 */
@FeignClient(name = "diagnosisFeignClient", url = "${ih.service.baseUrl}", configuration = FeignConfig.class)
public interface HmcDiagnosisFeignClient {

    /**
     * 问诊医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/searchDoctorList")
    ApiResult<DiagnosisDoctorResultDTO> queryDiagnosisDoctor(HmcQueryDiagnosisDoctorPageRequest request);

    /**
     * 医生详情
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/searchDoctorInfo")
    ApiResult<HmcDiagnosisDoctorDetailDTO> diagnosisDoctorDetail(QueryDiagnosisDoctorDetailRequest request);

    /**
     * 科室列表
     *
     * @return
     */
    @GetMapping("/hmc/doctorConsultation/departmentList")
    ApiResult<List<DepartmentDTO>> getDepartmentList();

    /**
     * 保存诊后评价
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/user/evaluation/add")
    ApiResult<Boolean> saveComment(SaveDiagnosisCommentRequest request);

    /**
     * 查看诊后评价
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/user/evaluation/info")
    ApiResult<DiagnosisCommentDTO> queryComment(HmcQueryDiagnosisOrderCommentRequest request);

    /**
     * 医生号源
     *
     * @param doctorId
     * @return
     */
    @GetMapping("/hmc/doctorConsultation/getDoctorSignalSource")
    ApiResult<List<HmcDiagnosisDoctorSignalSourceDTO>> diagnosisDoctorSignalSource(@RequestParam("doctorId") Integer doctorId);

    /**
     * 搜索医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/searchDoctorModuleList")
    ApiResult<HmcSearchDoctorResultDTO> searchDoctor(SearchDiagnosisDoctorPageRequest request);

    /**
     * 认证详情
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/searchDoctorInfoAuthentication")
    ApiResult<HmcDoctorVerifyDetailDTO> doctorVerifyDetail(DoctorVerifyDetailRequest request);

    /**
     * 关注医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/user/follow/followDoctor")
    ApiResult<Boolean> subDoctor(HmcSubDoctorRequest request);

    /**
     * 取关医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/user/follow/cancelFollowDoctor")
    ApiResult<Boolean> cancelSubDoctor(HmcSubDoctorRequest request);

    /**
     * 提交预约问诊单
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/submitDiagnosisRecord")
    ApiResult<HmcSubmitDiagnosisOrderDTO> submitDiagnosisOrder(HmcSubmitDiagnosisOrderRequest request);

    /**
     * 问诊单支付回调
     *
     * @param request
     * @return 状态 1订单金额或状态异常无法更新 2成功
     */
    @PostMapping("/hmc/diagnosisRecord/notify")
    ApiResult<DiagnosisNotifyIhResponseDTO> diagnosisOrderPayNotify(DiagnosisOrderPaySuccessNotifyRequest request);

    /**
     * 获取问诊单详情
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectDiagnosisRecordInfo")
    ApiResult<HmcDiagnosisRecordDetailDTO> getDiagnosisOrderDetailById(HmcDiagnosisOrderDetailRequest request);

    /**
     * 取消咨询接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/cancelDiagnosisRecord")
    ApiResult<HmcCancelDiagnosisOrderDTO> cancelDiagnosisRecord(HmcCancelDiagnosisOrderRequest request);

    /**
     * 取消咨询回调IH接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/cancelNotify")
    ApiResult<Boolean> cancelDiagnosisRecordNotify(HmcCancelDiagnosisOrderNotifyRequest request);

    /**
     * 补充症状描述
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/supplementDiseaseDescribe")
    ApiResult<Boolean> supplementDiagnosisOrder(HmcSupplementDiseaseDescribeRequest request);

    /**
     * 待问诊列表接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectDiagnosisRecordWait")
    ApiResult<HmcSelectDiagnosisRecordDTO> selectDiagnosisRecordWaitList(SearchDiagnosisRecordPageRequest request);

    /**
     * 问诊中列表接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectDiagnosisRecordGoing")
    ApiResult<HmcSelectDiagnosisRecordDTO> selectDiagnosisRecordGoing(SearchDiagnosisRecordPageRequest request);

    /**
     * 历史问诊列表接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectDiagnosisRecordHistory")
    ApiResult<HmcSelectDiagnosisRecordDTO> selectDiagnosisRecordHistory(SearchDiagnosisRecordPageRequest request);

    /**
     * 我咨询过的医生列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/myDoctorConsultationList")
    ApiResult<HmcMyDoctorConsultationDTO> myDoctorConsultationList(MyDoctorPageRequest request);

    /**
     * 我关注过的医生列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorConsultation/myDoctorFollowList")
    ApiResult<HmcMyDoctorConsultationDTO> myDoctorFollowList(MyDoctorPageRequest request);

    /**
     * 批量查询问诊订单
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectDiagnosisRecordByIdList")
    ApiResult<List<HmcDiagnosisRecordDTO>> getDiagnosisOrderByIdList(@RequestBody HmcDiagnosisOrderRequest request);

    /**
     * HMC同步药品到IH平台药品
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/commodity/syncCommodity")
    ApiResult<HmcSyncGoodsToIhDTO> syncHmcGoodsToIH(@RequestBody HmcSyncGoodsRequest request);

    /**
     * 获取IH商品价格库存信息
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/cooperationpharmacycommodity/getPriceStockByIds")
    ApiResult<List<HmcGetGoodsInfoDTO>> getIHPharmacyGoodsInfo(@RequestBody HmcGetIHGoodsInfoRequest request);

    /**
     * 修改价格库存
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/cooperationpharmacycommodity/update")
    ApiResult<Boolean> updateGoodsPriceAndStock(@RequestBody HmcUpdateGoodsPriceAndPriceRequest request);

    /**
     * 获取问诊单关联处方列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescription/selectPrescriptionListByDiagnosisRecordId")
    ApiResult<List<HmcPrescriptionListDTO>> getPrescriptionByDiagnosisOrderIdList(HmcDiagnosisOrderDetailRequest request);

    /**
     * 通过聊天室id查询-> 当前用户和问诊医生之间有没有进行中的问诊单、处方开方状态
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/selectUserAndDoctorDiagnosisRecordStatus")
    ApiResult<HmcIMChatRootQueryDiagnosisRecordDTO> imCharRoomQueryDiagnosisOrderWithDoc(QueryDiagnosisRecordByUserIdAndDocIdRequest request);

    /**
     * 消息回调
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/im/messageNotify")
    ApiResult<Boolean> messageNotify(@RequestBody MessageNotifyRequest request);

    /**
     * 事件回调-视频回调
     *
     * @param request 回调内容
     * @return  成功/失败
     */
    @PostMapping("/hmc/im/imVideoCallBack")
    ApiResult<Boolean> eventCallBack(@RequestBody MessageNotifyRequest request);

    /**
     * 根据房间号获取剩余通话时长
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/diagnosisRecord/getRemainingSecondsByRoom")
    ApiResult<HmcGetRemainingSecondsByRoomDTO> getRemainingSecondsByRoom(@RequestBody GetRemainingSecondsRequest request);

    /**
     * 视频拒接及未接通记录
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/im/videoThrough")
    ApiResult<Boolean> videoRefuseOrNotThrough(@RequestBody VideoRefuseOrNotThroughRequest request);

    /**
     * 获取im聊天记录
     * @param request
     * @return
     */
    @PostMapping("/hmc/consultMessage/getHistory")
    ApiResult<List<HmcIMChatHistoryRecordDTO>> getIMChatHistoryRecord(@RequestBody GetIMChatHistoryRecordPageRequest request);

    /**
     * 获取会话列表
     * @param request
     * @return
     */
    @PostMapping("/hmc/conversationList/list")
    ApiResult<List<HmcIMConversationListDTO>> getIMConversationList(@RequestBody GetIMConversationListRequest request);

    /**
     * 上报已读接口
     * @param request
     * @return
     */
    @PostMapping("/hmc/consultMessage/reportRead")
    ApiResult reportRead(@RequestBody ReadReportRequest request);
}
