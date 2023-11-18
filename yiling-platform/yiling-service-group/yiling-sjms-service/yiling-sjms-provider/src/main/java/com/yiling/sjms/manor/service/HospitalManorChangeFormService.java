package com.yiling.sjms.manor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.manor.bo.ManorChangeBO;
import com.yiling.sjms.manor.dto.HospitalManorChangeFormDTO;
import com.yiling.sjms.manor.dto.request.ApproveManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.DeleteManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.ManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.QueryChangePageRequest;
import com.yiling.sjms.manor.dto.request.UpdateArchiveRequest;
import com.yiling.sjms.manor.entity.HospitalManorChangeFormDO;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * <p>
 * 医院辖区变更表单 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
public interface HospitalManorChangeFormService extends BaseService<HospitalManorChangeFormDO> {

    /**
     * 添加
     * @param request
     * @return
     */
    Long save(ManorChangeFormRequest request);


    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<HospitalManorChangeFormDTO> listPage(QueryChangePageRequest request);

    /**
     * 根据id删除
     * @param request
     */
    void deleteById(DeleteManorChangeFormRequest request);

    /**
     * 表单提交审核
     * @param request
     * @return
     */
    Boolean submit(SubmitFormBaseRequest request);

    /**
     * 归档
     * @param request
     * @return
     */
    Boolean updateArchiveStatusById(UpdateArchiveRequest request);

    /**
     * 根据formId查询 辖区变更详情
     * @param formId
     * @return
     */
    ManorChangeBO queryByFormId(Long formId);

    /**
     * 审批通过修改数据
     * @param approveManorChangeFormRequest
     * @return
     */
    Boolean approveToChange(ApproveManorChangeFormRequest approveManorChangeFormRequest);
}
