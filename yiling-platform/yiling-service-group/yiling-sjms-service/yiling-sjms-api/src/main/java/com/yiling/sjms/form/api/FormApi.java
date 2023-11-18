package com.yiling.sjms.form.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.QueryFormPageListRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.enums.FormTypeEnum;

/**
 * 表单基础信息 API
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface FormApi {

    /**
     * 查询表单分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.sjms.form.entity.FormDO>
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    Page<FormDTO> pageList(QueryFormPageListRequest request);

    /**
     * 获取基础表单信息
     *
     * @param id 基础表单ID
     * @return com.yiling.sjms.form.dto.FormDTO
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    FormDTO getById(Long id);

    /**
     * 根据流程实例ID查询表单基础信息
     *
     * @param flowId 流程实例ID
     * @return com.yiling.sjms.form.entity.FormDO
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    FormDTO getByFlowId(String flowId);

    /**
     * 批量获取基础表单信息
     *
     * @param ids 基础表单ID列表
     * @return java.util.List<com.yiling.sjms.form.dto.FormDTO>
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    List<FormDTO> listByIds(List<Long> ids);

    /**
     * 获取用户待提交表单列表
     *
     * @param formTypeEnum 表单业务类型枚举
     * @param userId 用户ID
     * @return java.util.List<com.yiling.sjms.form.entity.FormDO>
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    List<FormDTO> listUnsubmitFormsByUser(FormTypeEnum formTypeEnum, Long userId);

    /**
     * 删除表单
     *
     * @param id 基础表单ID
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    Boolean delete(Long id, Long opUserId);

    /**
     * 创建基础表单信息
     *
     * @param request
     * @return java.lang.Long 基础表单信息ID
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    Long create(CreateFormRequest request);

    /**
     * 更新备注
     * @param remarkRequest
     * @return
     */
    Boolean updateRemark(UpdateRemarkRequest remarkRequest);



    /**
     * 根据code查询表单信息
     * @param codes
     * @return
     */
    List<FormDTO> listByCodes(List<String> codes);
}
