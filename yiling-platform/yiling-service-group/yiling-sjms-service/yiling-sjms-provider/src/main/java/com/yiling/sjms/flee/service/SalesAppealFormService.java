package com.yiling.sjms.flee.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormDetailRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealSubmitFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealFormRequest;
import com.yiling.sjms.flee.entity.SalesAppealFormDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 销量申诉表单 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
public interface SalesAppealFormService extends BaseService<SalesAppealFormDO> {

    /**
     * 窜货申报主表单分页关联查询
     *
     * @param request 查询条件
     * @return 表单信息
     */
    Page<SalesAppealFormBO> pageForm(QuerySalesAppealPageRequest request);


    Long saveUpload(SaveSalesAppealFormRequest request);

    /**
     * 根据id删除窜货申报数据
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeById(RemoveFleeingGoodsFormRequest request);

    /**
     * 窜货申报数据列表查询
     *
     * @param formId 查询条件
     * @return 窜货申报数据
     */
    List<SalesAppealFormDO> ListByFormId(Long formId,Integer type);

    /**
     * 销量申诉保存草稿
     *
     * @param request 查询条件
     * @return 窜货申报数据
     */
    Long saveDraft(SaveSalesAppealSubmitFormRequest request);

    /**
     * 提交审核
     *
     * @param request 查询条件
     * @return 窜货申报数据
     */
    boolean submit(SubmitFleeingGoodsFormRequest request);

    /**
     * 校验文件名称
     *
     * @param fileName 文件名称
     * @return 结果
     */
    String checkFlowFileName(String fileName);

    /**
     * 保存确认文件
     *
     * @param request 文件名称
     * @return 结果
     */
    Long saveSalesConfirm(SaveSalesAppealFormDetailRequest request);

    /**
     * 生成流向表单
     *
     * @param flowRequest 请求参数
     * @return 成功/失败
     */
    String createFleeFlowForm(CreateSalesAppealFlowRequest flowRequest);

    /**
     * 获取销售类型
     *
     * @param fileName 请求参数
     * @return 成功/失败
     */
    Integer getDataType(String fileName);

    /**
     * 生成流向表单
     *
     * @param fileName 请求参数
     * @return 成功/失败
     */
    SalesAppealFormDO getByFileName(String fileName);

    SalesAppealFormDO getByTaskId(Long taskId);

    /**
     * 生成流向表单
     *
     * @param fileName 请求参数
     * @return 成功/失败
     */
    List<SalesAppealFormDO> getByLikeFileName(String fileName);

    /**
     * 更新确认表信息
     *
     * @param appealFormRequest 请求参数
     * @return 成功/失败
     */
    Boolean updateSalesConfirm(UpdateSalesAppealFormRequest appealFormRequest);

    /**
     * 更新确认表信息
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    List<SalesAppealFormBO> queryToDoList(QuerySalesAppealPageRequest request);

    /**
     * 我的代办
     *
     * @param formId 文件名称
     * @return 附件信息
     */
    SalesAppealExtFormDTO queryAppendix(Long formId);

    Long newSaveDraft(SaveSalesAppealSubmitFormRequest request);

    boolean deleteByIds(RemoveSelectAppealFlowFormRequest request);

    /**
     * 生成流向表单 兼容选择流向和上传excel
     * @param request 兼容选择流向和上传excel请求类
     * @return
     */
    String compatibleCreateFleeFlowForm(CreateSalesAppealFlowRequest request);

    boolean valid(Long formId);

    /**
     * 通过formId删除当前formId相关的所有上传excel的数据
     * @param formId 表单id
     * @return
     */
    boolean removeAllSelectByFormId(Long formId);

    /**
     * 清除当前formId相关的所有选择流向的数据
     * @param formId 表单id
     * @return
     */
    boolean removeAllSelectFlowByFormId(Long formId);

    /**
     * 清洗日程开启，重新开启销量申诉清洗任务
     * @return
     */
    boolean washSaleAppealFlowData();
}
