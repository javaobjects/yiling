package com.yiling.sjms.form.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.QueryFormPageListRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.ResubmitFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormTypeEnum;

/**
 * <p>
 * 表单基础信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-28
 */
public interface FormService {

    /**
     * 查询表单分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.sjms.form.entity.FormDO>
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    Page<FormDO> pageList(QueryFormPageListRequest request);

    /**
     * 根据表单ID获取表单基础信息
     *
     * @param id 基础表单信息ID
     * @return com.yiling.sjms.form.entity.FormDO
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    FormDO getById(Long id);

    /**
     * 根据表单code获取表单基础信息
     *
     * @param code 基础表单信息code
     * @return com.yiling.sjms.form.entity.FormDO
     * @author xuan.zhou
     * @date 2023/4/13
     **/
    FormDO getByCode(String code);

    /**
     * 根据流程实例ID查询表单基础信息
     *
     * @param flowId 流程实例ID
     * @return com.yiling.sjms.form.entity.FormDO
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    FormDO getByFlowId(String flowId);

    /**
     * 批量根据表单ID获取表单基础信息
     *
     * @param ids 基础表单信息ID列表
     * @return java.util.List<com.yiling.sjms.form.entity.FormDO>
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    List<FormDO> listByIds(List<Long> ids);

    /**
     * 获取用户待提交表单列表
     *
     * @param formTypeEnum 表单业务类型枚举
     * @param userId 用户ID
     * @return java.util.List<com.yiling.sjms.form.entity.FormDO>
     * @author xuan.zhou
     * @date 2023/2/27
     **/
    List<FormDO> listUnsubmitFormsByUser(FormTypeEnum formTypeEnum, Long userId);

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
     * 提交表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    Boolean submit(SubmitFormRequest request);

    /**
     * 驳回表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    Boolean reject(RejectFormRequest request);

    /**
     * 审批通过表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    Boolean approve(ApproveFormRequest request);

    /**
     * （驳回后）重新提交
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/28
     **/
    Boolean resubmit(ResubmitFormRequest request);

    /**
     * 删除表单
     *
     * @param id 基础表单ID
     * @param opUserId 操作人ID
     * @return
     */
    Boolean delete(Long id, Long opUserId);

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
    List<FormDO> listByCodes(List<String> codes);

    /**
     * 更新传输方式
     * @param transferType 传输方式
     * @return
     */
    Boolean updateTransferType(Long id, Integer transferType);

    /**
     * 通过传输方式过滤出相关传输方式的表单
     * @param transferType 传输方式
     * @param formIds 表单id集合 销量申诉表中日程未开启 已提交/未清洗的表单id
     * @return 通过传输方式过滤出相关传输方式的表单
     */
    List<Long> formIdByTransferType(Integer transferType, List<Long> formIds);

}
