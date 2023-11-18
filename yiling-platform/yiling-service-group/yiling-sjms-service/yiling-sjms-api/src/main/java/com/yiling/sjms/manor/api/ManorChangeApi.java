package com.yiling.sjms.manor.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.manor.bo.ManorChangeBO;
import com.yiling.sjms.manor.dto.HospitalManorChangeFormDTO;
import com.yiling.sjms.manor.dto.request.DeleteManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.ManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.QueryChangePageRequest;
import com.yiling.sjms.manor.dto.request.UpdateArchiveRequest;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * @author: gxl
 * @date: 2023/5/12
 */
public interface ManorChangeApi {

    /**
     * 保存
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
}