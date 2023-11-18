package com.yiling.dataflow.sale.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentTargetBO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentTargetPageRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 销售指标部门配置 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
public interface SaleDepartmentTargetService extends BaseService<SaleDepartmentTargetDO> {

    /**
     * 销售指标分解列表
     * @param request
     * @return
     */
    Page<SaleDepartmentTargetBO> querySaleDepartmentTargetPage(QuerySaleDepartmentTargetPageRequest request);

    /**
     * 根据指标id查询部门
     *
     * @param targetId
     * @param deptId
     * @return 只传任意一个参数时查出的结果可能有多个，会随机返回一个
     */
    SaleDepartmentTargetDTO queryListByTargetId(Long targetId,Long deptId);


    List<SaleDepartmentTargetDTO> listBySaleTargetId(Long saleTargetId);


    /**
     * 更新配置状态
     *
     * @param request
     */
    Boolean updateConfigStatus(UpdateConfigStatusRequest request);

    /**
     * 根据任务id生成模板
     *
     * @param targetId
     * @param deptId
     */
    void generateMould(Long targetId,Long deptId);

    /**
     * 根据指标id和部门id 清空模板
     *
     * @param departmentTargetDO
     */
    void deleteOldData(SaleDepartmentTargetDO departmentTargetDO);

    /**
     * 根据任务id更新模板任务状态
     *
     * @param targetId
     * @param deptId
     * @param errMsg
     */
    void updateTaskStatus(Long targetId,Long deptId,String errMsg);

}
