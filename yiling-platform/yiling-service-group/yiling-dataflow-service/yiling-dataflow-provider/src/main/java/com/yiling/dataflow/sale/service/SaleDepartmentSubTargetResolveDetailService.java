package com.yiling.dataflow.sale.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetResolveDetailDTO;
import com.yiling.dataflow.sale.dto.request.ImportSubTargetResolveDetailRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 部门销售指标子项配置分解详情 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
public interface SaleDepartmentSubTargetResolveDetailService extends BaseService<SaleDepartmentSubTargetResolveDetailDO> {

    /**
     * 根据指标id查询明细
     *
     * @param targetId
     * @param deptId
     * @return
     */
    List<SaleDepartmentSubTargetResolveDetailDTO> queryListByTargetId(Long targetId,Long deptId);

    /**
     * 分解模板分页列表
     * @param request
     * @return
     */
    Page<SaleDepartmentSubTargetResolveDetailBO> queryPage(QueryResolveDetailPageRequest request);

    /**
     * 导入指标分解
     * @param request
     * @return
     */
    Boolean importSubTargetResolveDetail(List<ImportSubTargetResolveDetailRequest> request);

    /**
     * 生成分解模板
     * @param request
     */
    //String genTemplate(QueryResolveDetailPageRequest request);
}
