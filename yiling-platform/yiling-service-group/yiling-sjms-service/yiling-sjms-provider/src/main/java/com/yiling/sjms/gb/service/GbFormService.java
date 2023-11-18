package com.yiling.sjms.gb.service;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.entity.GbFormDO;

/**
 * <p>
 * 团购表单 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbFormService extends BaseService<GbFormDO> {

    /**
     * 根据表单id查询团购信息
     * @param formId
     * @return
     */
    GbFormDO getOneByFormId(Long formId);

    /**
     * 保存团购信息
     * @param request
     * @return
     */
    Long saveGBInfo(SaveGBInfoRequest request);

    /**
     * 团购取消保存
     * @param request
     * @return
     */
    Long saveGbCancel(SaveGBCancelInfoRequest request);

    /**
     * 团购取消-驳回保存
     * @param request
     * @return
     */
    Long saveRejectGbCancel(SaveGBCancelInfoRequest request);

    /**
     * 获取
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFormListPage(QueryGBFormListPageRequest request);


    /**
     * 表单修改状态
     * @param request
     * @return
     */
    Boolean updateStatusById(UpdateGBFormInfoRequest request );

    /**
     * 提报表单统计每日提报/取消数量金额
     */
    void addStatisticFormQuantity(Long id);

    /**
     * 驳回表单统计每日提报/取消数量金额
     * @param id
     * @param submitTime 原来的提报时间
     */
    void reduceStatisticFormQuantity(Long id, Date submitTime);

    /**
     * 获取导出信息
     * @param request
     * @return
     */
    Page<GbFormExportListDTO> getGBFormExportListPage(QueryGBFormListPageRequest request);

    /**
     * 删除团购
     * @param formId
     * @param userId
     */
    void delete(Long formId , Long userId);

    /**
     * 团购费用申请保存
     * @param request
     * @return
     */
    Long saveGbFeeApplication(SaveGBCancelInfoRequest request);

    /**
     * 团购费用申请重新保存
     * @param request
     * @return
     */
    Long saveRejectGbFeeApplication(SaveGBCancelInfoRequest request);

    /**
     * 获取团购表单数据列表，过滤已经关联过费用申请的
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeApplicationFormListPage(QueryGBFormListPageRequest request);

    /**
     * 获取费用申请
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeFormListPage(QueryGBFormListPageRequest request);

    /**
     * 根据表单id更新费用申请状态，审核通过后
     * @param formId
     * @return
     */
    Boolean updateFeeApplicationInfo(Long formId);
}
