package com.yiling.ih.patient.api;

import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;

import java.util.List;

/**
 * HMC问诊API
 *
 * @author: fan.shen
 * @date: 2022/8/23
 */
public interface HmcDiagnosisApi {

    /**
     * 搜索医生
     *
     * @param request
     * @return
     */
    DiagnosisDoctorResultDTO searchDiagnosisDoctor(HmcQueryDiagnosisDoctorPageRequest request);

    /**
     * 问诊医生详情
     *
     * @param request
     * @return
     */
    HmcDiagnosisDoctorDetailDTO diagnosisDoctorDetail(QueryDiagnosisDoctorDetailRequest request);

    /**
     * 科室列表
     *
     * @return
     */
    List<DepartmentDTO> getDepartmentList();

    /**
     * 提交预约问诊单
     *
     * @param request
     * @return
     */
    HmcSubmitDiagnosisOrderDTO submitDiagnosisOrder(HmcSubmitDiagnosisOrderRequest request);

    /**
     * 保存诊后评价
     *
     * @param request
     * @return
     */
    Boolean saveDiagnosisComment(SaveDiagnosisCommentRequest request);

    /**
     * 查看评价
     *
     * @param id
     * @return
     */
    DiagnosisCommentDTO queryComment(Long id);

    /**
     * 医生号源
     *
     * @param doctorId
     * @return
     */
    List<HmcDiagnosisDoctorSignalSourceDTO> diagnosisDoctorSignalSource(Integer doctorId);

    /**
     * 找医生
     *
     * @param request
     * @return
     */
    HmcSearchDoctorResultDTO searchDoctor(SearchDiagnosisDoctorPageRequest request);

    /**
     * 已认证详情接口
     *
     * @param doctorId
     * @return
     */
    HmcDoctorVerifyDetailDTO doctorVerifyDetail(Integer doctorId);

    /**
     * 关注医生
     *
     * @param request
     * @return
     */
    Boolean subDoctor(HmcSubDoctorRequest request);

    /**
     * 取关医生
     *
     * @param request
     * @return
     */
    Boolean cancelSubDoctor(HmcSubDoctorRequest request);

    /**
     * 问诊单支付回调
     *
     * @param request
     * @return
     */
    Boolean diagnosisOrderPayNotify(DiagnosisOrderPaySuccessNotifyRequest request);

    /**
     * 获取问诊单详情
     *
     * @param request
     * @return
     */
    HmcDiagnosisRecordDetailDTO getDiagnosisOrderDetailById(HmcDiagnosisOrderDetailRequest request);

    /**
     * 获取问诊单详情
     *
     * @param request
     * @return
     */
    List<HmcPrescriptionListDTO> getPrescriptionByDiagnosisOrderIdList(HmcDiagnosisOrderDetailRequest request);

    /**
     * 根据问诊订单id批量查询问诊订单
     * @param request
     * @return
     */
    List<HmcDiagnosisRecordDTO> getDiagnosisOrderByIdList(HmcDiagnosisOrderRequest request);

    /**
     * 取消咨询
     *
     * @param request
     * @return
     */
    HmcCancelDiagnosisOrderDTO cancelDiagnosisRecord(HmcCancelDiagnosisOrderRequest request);

    /**
     * 取消咨询-退款回调
     *
     * @param request
     * @return
     */
    Boolean cancelDiagnosisRecordNotify(HmcCancelDiagnosisOrderNotifyRequest request);

    /**
     * 补充症状描述
     *
     * @param request
     * @return
     */
    Boolean supplementDiagnosisOrder(HmcSupplementDiseaseDescribeRequest request);

    /**
     * 待问诊列表接口
     * @param request
     * @return
     */
    HmcSelectDiagnosisRecordDTO selectDiagnosisRecordWaitList(SearchDiagnosisRecordPageRequest request);

    /**
     * 问诊中列表接口
     * @param request
     * @return
     */
    HmcSelectDiagnosisRecordDTO selectDiagnosisRecordGoing(SearchDiagnosisRecordPageRequest request);

    /**
     * 历史问诊列表接口
     * @param request
     * @return
     */
    HmcSelectDiagnosisRecordDTO selectDiagnosisRecordHistory(SearchDiagnosisRecordPageRequest request);

    /**
     * 我咨询过的医生
     * @param request
     * @return
     */
    HmcMyDoctorConsultationDTO myDoctorConsultationList(MyDoctorPageRequest request);

    /**
     * 我关注过的医生
     * @param request
     * @return
     */
    HmcMyDoctorConsultationDTO myDoctorFollowList(MyDoctorPageRequest request);

    /**
     * 同步药品到IH
     * @param request
     * @return
     */
    HmcSyncGoodsToIhDTO syncHmcGoodsToIH(HmcSyncGoodsRequest request);

    /**
     * 获取IH配送商药品信息
     * @param request
     * @return
     */
    List<HmcGetGoodsInfoDTO> getIHPharmacyGoodsInfo(HmcGetIHGoodsInfoRequest request);

    /**
     * 修改价格库存
     * @param request
     * @return
     */
    boolean updateGoodsPriceAndStock(HmcUpdateGoodsPriceAndPriceRequest request);


    /**
     * IM聊天室查询患者和医生之间的问诊单
     * @param request
     * @return
     */
    HmcIMChatRootQueryDiagnosisRecordDTO imCharRoomQueryDiagnosisOrderWithDoc(QueryDiagnosisRecordByUserIdAndDocIdRequest request);

    /**
     * 消息回调
     *
     * @param param 消息回调内容
     * @return  成功/失败
     */
    Boolean messageNotify(String param);

    /**
     * 事件回调-视频回调
     *
     * @param param 回调内容
     * @return  成功/失败
     */
    Boolean eventCallBack(String param);

    /**
     * 根据房间号获取剩余通话时长
     *
     * @param request   查询条件
     * @return  剩余通话时长
     */
    Integer getRemainingSecondsByRoom(GetRemainingSecondsRequest request);

    /**
     * 视频拒接及未接通记录
     * @param request
     * @return
     */
    boolean videoRefuseOrNotThrough(VideoRefuseOrNotThroughRequest request);

    /**
     * 查询聊天记录
     * @param request
     * @return
     */
    List<HmcIMChatHistoryRecordDTO> getIMChatHistoryRecord(GetIMChatHistoryRecordPageRequest request);

    /**
     * 获取会话列表
     * @param request
     * @return
     */
    List<HmcIMConversationListDTO> getIMConversationList(GetIMConversationListRequest request);

    /**
     * 获取会话列表
     * @param request
     * @return
     */
    void reportRead(ReadReportRequest request);
}
