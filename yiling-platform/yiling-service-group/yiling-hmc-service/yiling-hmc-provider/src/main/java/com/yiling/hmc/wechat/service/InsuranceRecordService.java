package com.yiling.hmc.wechat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.bo.InsuranceRecordBO;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.request.*;
import com.yiling.hmc.wechat.entity.InsuranceRecordDO;

/**
 * <p>
 * C端参保记录表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
public interface InsuranceRecordService extends BaseService<InsuranceRecordDO> {

    /**
     * 保存参保记录
     * @param request
     * @return
     */
    Long saveInsuranceRecord(SaveInsuranceRecordRequest request);

    /**
     * 根据保单号查询参保记录
     * @param policyNo
     * @return
     */
    InsuranceRecordDTO getByPolicyNo(String policyNo);

    /**
     * 根据保单号查询参保记录
     * @param insuranceRecordId
     * @return
     */
    InsuranceRecordDTO getById(Long insuranceRecordId);

    /**
     * 更新保单状态
     * @param policyNo
     * @return
     */
    Boolean updatePolicyStatus(String policyNo, Integer endPolicyType);

    /**
     * 更新结束时间
     * @param request
     * @return
     */
    Boolean updatePolicyEndTime(UpdateInsuranceRecordRequest request);

    /**
     * 查询C端我的参保
     * @param request
     * @return
     */
    Page<InsuranceRecordDTO> pageList(QueryInsuranceRecordPageRequest request);

    /**
     * 是否有过参保记录
     * @param userId
     * @return
     */
    boolean hasInsurance(Long userId);

    /**
     * 后台-参保记录分页
     * @param recordPageRequest
     * @return
     */
    Page<InsuranceRecordBO> queryPage(QueryBackInsuranceRecordPageRequest recordPageRequest);

    /**
     * 保单过期提醒
     */
    void expireNotify();

    /**
     * 查询参保记录
     * @param policyNo
     * @param insuranceCompanyId
     * @return
     */
    boolean checkInsuranceRecord(String policyNo, Long insuranceCompanyId);

    /**
     * 参保回调
     * @param context
     * @return
     */
    Long joinNotify(InsuranceJoinNotifyContext context);

    /**
     * 保单状态同步
     * @param request
     */
    void statusAsync(SaveInsuranceRetreatRequest request);

    /**
     * 主动支付回调
     * @param payNotifyContext
     */
    void payNotify(InsurancePayNotifyContext payNotifyContext);

    /**
     * 更新身份证照片、手写签名
     * @param insuranceRecordId
     * @param request
     */
    void updateSfzAndSignature(Long insuranceRecordId, SaveClaimInformationRequest request);

    /**
     * 上传电子保单
     * @param insuranceRecordDTO
     * @return
     */
    String uploadPolicyFile(InsuranceRecordDTO insuranceRecordDTO);
}
