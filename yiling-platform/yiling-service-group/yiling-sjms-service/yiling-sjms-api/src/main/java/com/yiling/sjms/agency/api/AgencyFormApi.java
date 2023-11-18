package com.yiling.sjms.agency.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryFirstAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SubmitAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyFormArchiveRequest;

/**
 * 机构新增修改表 Api
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
public interface AgencyFormApi {

    /**
     * 机构新增修改表 新增
     *
     * @param request 新增内容
     * @return form表id
     */
    Long saveAgencyForm(SaveAgencyFormRequest request);

    /**
     * 机构新增修改表 分页查询
     *
     * @param request 查询条件
     * @return 机构新增修改表单
     */
    Page<AgencyFormDTO> pageList(QueryAgencyFormPageRequest request);

    /**
     * 根据id查询机构待审核表单信息
     *
     * @param id 主键id
     * @return 机构新增修改表单
     */
    AgencyFormDTO queryById(Long id);

    /**
     * 根据表单id和机构id查询新增和修改表单信息
     *
     * @param formId 表单表id
     * @param crmEnterpriseId 机构id
     * @return 机构新增修改表单信息
     */
    List<AgencyFormDTO> listByFormIdAndCrmEnterpriseId(Long formId, Long crmEnterpriseId);

    /**
     * 根据条件查询出第一条满足条件的表单数据
     *
     * @param request 查询条件
     * @return 表单数据
     */
    AgencyFormDTO getFirstInfo(QueryFirstAgencyFormRequest request);

    /**
     * 根据id删除机构新增修改表数据
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeById(RemoveAgencyFormRequest request);

    /**
     * 提交表单(新增机构表单和修改机构表单)
     *
     * @param request 提交内容
     * @return 成功/失败
     */
    boolean submit(SubmitAgencyFormRequest request);


    /**
     * 根据id更新归档状态
     *
     * @param request 条件
     */
    void updateArchiveStatusById(UpdateAgencyFormArchiveRequest request);
}
