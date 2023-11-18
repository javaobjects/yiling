package com.yiling.hmc.wechat.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.request.*;


/**
 * 参保记录API
 *
 * @Author fan.shen
 * @Date 2022/3/26
 */
public interface InsuranceRecordApi {

    /**
     * 保存参保记录
     *
     * @param request
     * @return
     */
    Long saveInsuranceRecord(SaveInsuranceRecordRequest request);

    /**
     * 根据保单号查询参保记录
     *
     * @param policyNo
     * @return
     */
    InsuranceRecordDTO getByPolicyNo(String policyNo);

    /**
     * 更新保单终止时间
     *
     * @param request
     * @return
     */
    Boolean updatePolicyEndTime(UpdateInsuranceRecordRequest request);

    /**
     * 查询C端我的参保
     *
     * @param request
     * @return
     */
    Page<InsuranceRecordDTO> pageList(QueryInsuranceRecordPageRequest request);

    /**
     * 查询C端我的参保-详情
     *
     * @param id
     * @return
     */
    InsuranceRecordDTO getById(Long id);


    /**
     * 判断用户是否参保
     *
     * @param userId
     * @return
     */
    boolean hasInsurance(Long userId);

    /**
     * 保单过期提醒
     */
    void expireNotify();

    /**
     * 根据保单号 + 保司id查询参保记录
     *
     * @param policyNo
     * @param insuranceCompanyId
     * @return
     */
    boolean checkInsuranceRecord(String policyNo, Long insuranceCompanyId);

    /**
     * 参保回调
     *
     * @param context
     * @return
     */
    Long joinNotify(InsuranceJoinNotifyContext context);

    /**
     * 保单状态同步
     *
     * @param request
     */
    void statusAsync(SaveInsuranceRetreatRequest request);

    /**
     * 主动支付回调
     *
     * @param payNotifyContext
     */
    void payNotify(InsurancePayNotifyContext payNotifyContext);

    /**
     * 上传电子保单
     * @param insuranceRecordDTO
     * @return
     */
    String uploadPolicyFile(InsuranceRecordDTO insuranceRecordDTO);
}
