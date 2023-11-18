package com.yiling.sjms.flee.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.request.CreateFleeFlowRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingGoodsFormListRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveBatchFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingFormDraftRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;

/**
 * 窜货申报表单API
 *
 * @author: yong.zhang
 * @date: 2023/3/10 0010
 */
public interface FleeingGoodsFormApi {

    /**
     * 窜货申报主表单分页关联查询
     *
     * @param request 查询条件
     * @return 表单信息
     */
    Page<FleeingFormBO> pageForm(QueryFleeingFormPageRequest request);

    /**
     * 窜货申报数据列表查询
     *
     * @param request 查询条件
     * @return 窜货申报数据
     */
    List<FleeingGoodsFormDTO> pageList(QueryFleeingGoodsFormListRequest request);

    /**
     * 查询第一条窜货申报数据
     *
     * @param request 查询条件
     * @return 窜货申报数据
     */
    FleeingGoodsFormDTO getFirst(QueryFleeingGoodsFormListRequest request);

    /**
     * 通过文件名获取确认记录
     *
     * @param fileName 文件名称
     * @return 附件信息
     */
    FleeingGoodsFormDTO getByFileName(String fileName);

    /**
     * 通过文件名获取确认记录
     *
     * @param taskId 任务ID
     * @return 附件信息
     */
    FleeingGoodsFormDTO getByTaskId(Long taskId);

    /**
     * 申诉文件上传数据批量新增
     *
     * @param request 存储内容
     * @return formId
     */
    Long saveBatchUpload(SaveBatchFleeingGoodsFormRequest request);

    /**
     * 申诉文件上传数据新增
     *
     * @param request 申诉文件上传内容
     * @return formId
     */
    Long saveUpload(SaveFleeingGoodsFormRequest request);

    /**
     * 申诉文件上传数据新增
     *
     * @param request 申诉文件上传内容
     * @return id
     */
    Long saveUploadRecord(SaveFleeingGoodsFormRequest request);

    /**
     * 申诉文件上传数据修改
     *
     * @param request 修改内容
     * @return 成功/失败
     */
    boolean updateUploadRecord(UpdateFleeingGoodsFormRequest request);

    /**
     * 保存草稿
     *
     * @param request 草稿数据
     * @return 主表单id
     */
    Long saveDraft(SaveFleeingFormDraftRequest request);

    /**
     * 提交表单
     *
     * @param request 提交内容
     * @return 成功/失败
     */
    boolean submit(SubmitFleeingGoodsFormRequest request);

    /**
     * 根据id删除窜货申报数据
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeById(RemoveFleeingGoodsFormRequest request);

    /**
     * 根据formId查询附件信息
     *
     * @param formId 主表单id
     * @return 附件信息
     */
    FleeingGoodsFormExtDTO queryExtByFormId(Long formId);

    /**
     * 校验文件名称
     *
     * @param fileName 文件名称
     * @return 失败原因
     */
    String checkUploadFileName(String fileName);

    /**
     * 生成流向表单
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean createFleeFlowForm(CreateFleeFlowRequest request);

    /**
     * 根据ID获取记录
     *
     * @param id ID
     * @return 记录
     */
    FleeingGoodsFormDTO getById(Long id);
}
