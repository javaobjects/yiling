package com.yiling.marketing.couponactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseLimitDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseGiveRecordRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;

/**
 * <p>
 * 优惠券活动供应商限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityEnterpriseLimitService extends BaseService<CouponActivityEnterpriseLimitDO> {

    Boolean insertBatch(List<CouponActivityEnterpriseLimitDO> list);

    /**
     * 查询企业信息列表
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseDTO> queryEnterpriseListPage(QueryCouponActivityEnterpriseRequest request);

    /**
     * 查询已添加企业信息列表
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseLimitDTO> pageList(QueryCouponActivityEnterpriseLimitRequest request);

    /**
     * 添加企业信息
     * @param request
     * @return
     */
    Boolean saveEnterpriseLimit(SaveCouponActivityEnterpriseLimitRequest request);

    /**
     * 删除已添加企业信息
     * @param request
     * @return
     */
    Boolean deleteEnterpriseLimit(DeleteCouponActivityEnterpriseLimitRequest request);

    /**
     * 查询待发放企业信息
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseGiveDTO> queryEnterpriseGiveListPage(QueryCouponActivityEnterpriseGiveRequest request);

    /**
     * 查询已发放企业信息列表
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveEnterpriseInfoDO> queryEnterpriseGiveRecordListPage(QueryCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 查询已发放企业信息列表
     * @param request
     * @return
     */
    Boolean addGiveEnterpriseInfo(SaveCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 删除已发放企业信息
     * @param request
     * @return
     */
    Boolean deleteEnterpriseGiveRecord(DeleteCouponActivityEnterpriseGiveRecordRequest request);

    /**
     * 根据eid查询优惠券限制企业关系
     * @param eid
     * @return
     */
    List<CouponActivityEnterpriseLimitDO> getByEid(Long eid);

    /**
     * 根据eid列表 查询优惠券限制企业关系
     * @param eidList
     * @return
     */
    List<CouponActivityEnterpriseLimitDO> getByEidList(List<Long> eidList);

    /**
     * 根据优惠券活动id查询所有可使用企业关系
     * @param couponActivityIdList 优惠券活动id
     * @return
     */
    List<CouponActivityEnterpriseLimitDO> getByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 根据优惠券活动id查询优惠券可用企业关系
     * @param couponActivityId
     * @return
     */
    List<CouponActivityEnterpriseLimitDO> getByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券id删除
     * @param request
     * @return
     */
    Integer deleteByCouponActivityId(DeleteCouponActivityEnterpriseLimitRequest request);

}
