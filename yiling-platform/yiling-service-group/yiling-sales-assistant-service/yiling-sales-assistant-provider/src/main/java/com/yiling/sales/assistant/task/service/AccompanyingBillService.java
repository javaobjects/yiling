package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.QueryAccompanyingBillPage;
import com.yiling.sales.assistant.task.dto.request.app.QueryAccompanyingBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.sales.assistant.task.entity.AccompanyingBillDO;

/**
 * <p>
 * 随货同行单上传 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
public interface AccompanyingBillService extends BaseService<AccompanyingBillDO> {

    /**
     * 保存随货同行单
     * @param request
     */
    void save(SaveAccompanyingBillRequest request);

    /**
     * 保存随货同行单 自动审批通过
     * @param request
     */
    void saveV2(SaveAccompanyingBillRequest request);


    /**
     * 审批
     * @param request
     */
    void auditAccompanyingBill(SaveAccompanyingBillRequest request);

    /**
     * 单个
     * @param id
     * @return
     */
    AccompanyingBillDTO getDetailById(Long id);

    /**
     * app分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillDTO> queryAppPage(QueryAccompanyingBillPageRequest request);

    /**
     * 运营后台分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillDTO> queryPage(QueryAccompanyingBillPage request);
}
