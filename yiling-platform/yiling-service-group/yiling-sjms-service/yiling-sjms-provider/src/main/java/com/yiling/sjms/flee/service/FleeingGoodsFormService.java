package com.yiling.sjms.flee.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.request.ApproveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.CreateFleeFlowRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingGoodsFormListRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveBatchFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingFormDraftRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;
import com.yiling.sjms.flee.entity.FleeingGoodsFormDO;

/**
 * <p>
 * 窜货申报表单 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
public interface FleeingGoodsFormService extends BaseService<FleeingGoodsFormDO> {

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
    List<FleeingGoodsFormDO> pageList(QueryFleeingGoodsFormListRequest request);

    /**
     * 查询第一条窜货申报数据
     *
     * @param request 查询条件
     * @return 窜货申报数据
     */
    FleeingGoodsFormDO getFirst(QueryFleeingGoodsFormListRequest request);

    /**
     * 通过文件名获取确认记录
     *
     * @param fileName 文件名称
     * @return 附件信息
     */
    FleeingGoodsFormDO getByFileName(String fileName);

    FleeingGoodsFormDO getByTaskId(Long taskId);
    /**
     * 根据导入文件名称查询第一条导入信息
     *
     * @param fileName 文件名称
     * @return 窜货申报文件数据
     */
    FleeingGoodsFormDO queryByFileName(String fileName);

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
     * @return 主表单id
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
     * 审核通过
     *
     * @param request 表单信息
     * @return 成功/失败
     */
    boolean approve(ApproveFleeingGoodsFormRequest request);

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
}
