package com.yiling.user.procrelation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.procrelation.dto.request.QueryProcTemplatePageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcTemplateRequest;
import com.yiling.user.procrelation.entity.PopProcTemplateDO;

/**
 * <p>
 * pop采购关系导入商品模板表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-06-19
 */
public interface PopProcTemplateService extends BaseService<PopProcTemplateDO> {

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
    Page<PopProcTemplateDO> queryPageList(QueryProcTemplatePageRequest request);

    /**
     * 根据模板id删除模板
     *
     * @param templateId
     * @param opUser
     * @return
     */
    Boolean deleteById(Long templateId, Long opUser);

    /**
     * 根据模板id查询查询导入模板
     *
     * @param templateNumber
     * @return
     */
    PopProcTemplateDO queryTemplateByNumber(String templateNumber);
}
