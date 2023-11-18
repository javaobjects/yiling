package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRecordRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;

/**
 * <p>
 * 自动发券企业参与记录表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGiveRecordService extends BaseService<CouponActivityAutoGiveRecordDO> {

    /**
     * 查询发券企业参与记录
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveRecordDTO> queryEnterpriseGiveRecordListPage(QueryCouponActivityAutoGiveRecordRequest request);

    /**
     * 根据发放记录id再次手动发放
     * @param request
     * @return
     */
    Boolean autoGive(CouponActivityAutoGiveRequest request);

    Boolean insertBatch(List<CouponActivityAutoGiveRecordDO> list);

    Boolean updateBatch(List<CouponActivityAutoGiveRecordDO> list);

    List<CouponActivityAutoGiveRecordDTO> getByIdList(List<Long> ids);

    List<CouponActivityAutoGiveRecordDTO> getByCouponActivityId(Long couponActivityId);

    Boolean deleteByIds(List<Long> ids, Long userId);

    /**
     * 根据企业ID 查询最近的优惠券自动发放活动记录
     * @param eid
     * @return
     */
    CouponActivityAutoGiveRecordDO getAutoGiveRecordLastOneByEid(Long eid);

    /**
     * 根据企业ID、发放活动IDS 查询优惠券自动发放活动记录
     * @param eid
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveRecordDO> getRecordListByEidAndAutoGiveIds(Long eid, List<Long> autoGiveIdList);

    /**
     *
     * @param autoGiveList
     * @return
     */
    Boolean saveAutoGiveRecordWithWaitStatus(List<CouponActivityAutoGiveRecordDO> autoGiveList);

    /**
     * 根据ID更新发放记录状态
     * @param autoGiveList
     * @return
     */
    Boolean updateRecordStatus(List<UpdateAutoGiveRecordStatusRequest> autoGiveList);

    /**
     * 根据自动发放批次号查找发放记录列表
     * @param batchNumber
     * @return
     */
    List<CouponActivityAutoGiveRecordDO> getListByBatchNumber(String batchNumber);

    /**
     * 根据发放活动IDS 查询优惠券自动发放活动记录
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveRecordDO> getRecordListByAutoGiveIds(List<Long> autoGiveIdList);

}
