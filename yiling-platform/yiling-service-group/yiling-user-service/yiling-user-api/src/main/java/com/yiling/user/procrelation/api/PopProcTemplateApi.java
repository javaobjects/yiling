package com.yiling.user.procrelation.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.PopProcTemplateDTO;
import com.yiling.user.procrelation.dto.request.QueryProcTemplatePageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcTemplateRequest;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
public interface PopProcTemplateApi {

    /**
     * 保存或更新导入商品模板
     *
     * @param request
     * @return
     */
    Long saveProcTemplate(SaveProcTemplateRequest request);

    /**
     * 查询pop采购关系导入模板
     *
     * @param request
     * @return
     */
    Page<PopProcTemplateDTO> queryPageList(QueryProcTemplatePageRequest request);

    /**
     * 根据模板id查询查询导入模板
     *
     * @param templateId
     * @return
     */
    PopProcTemplateDTO queryTemplateById(Long templateId);

    /**
     * 根据模板编号查询查询导入模板
     *
     * @param templateNumber
     * @return
     */
    PopProcTemplateDTO queryTemplateByNumber(String templateNumber);

    /**
     * 根据模板id删除模板
     *
     * @param templateId
     * @param opUser
     * @return
     */
    Boolean deleteById(Long templateId,Long opUser);
}
