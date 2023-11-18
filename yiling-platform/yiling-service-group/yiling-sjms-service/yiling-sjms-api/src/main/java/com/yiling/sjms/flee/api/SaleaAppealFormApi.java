package com.yiling.sjms.flee.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormDetailRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealSubmitFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealFormRequest;

/**
 * 窜货申报表单API
 *
 * @author: yong.zhang
 * @date: 2023/3/10 0010
 */
public interface SaleaAppealFormApi {

    /**
     * 窜货申报主表单分页关联查询
     *
     * @param request 查询条件
     * @return 表单信息
     */
    Page<SalesAppealFormBO> pageForm(QuerySalesAppealPageRequest request);

    /**
     * 窜货申报数据列表查询
     *
     * @param formId 查询条件
     * @return 窜货申报数据
     */
    List<SalesAppealFormDTO> pageList(Long formId,Integer type);

    /**
     * 申诉文件上传数据新增
     *
     * @param request 申诉文件上传内容
     * @return 主表单id
     */
    Long saveUpload(SaveSalesAppealFormRequest request);

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
     * 保存草稿
     *
     * @param request 主表单id
     * @return 附件信息
     */
    Long saveDraft(SaveSalesAppealSubmitFormRequest request);

    /**
     * 提交审核
     *
     * @param request 主表单id
     * @return 附件信息
     */
    boolean submit(SubmitFleeingGoodsFormRequest request);

    /**
     * 校验文件名称
     *
     * @param fileName 文件名称
     * @return 附件信息
     */
    String checkFlowFileName(String fileName);

    /**
     * 保存确认表单
     *
     * @param request 保存属性
     * @return 是否成功
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
     * 获取文件对应的销售，采购，库存类型
     *
     * @param fileName 文件名称
     * @return 附件信息
     */
    Integer getDateType(String fileName);

    /**
     * 通过文件名获取确认记录
     *
     * @param fileName 文件名称
     * @return 附件信息
     */
    SalesAppealFormDTO getByFileName(String fileName);

    /**
     * 通过上传记录ID获取信息
     *
     * @param taskId 上传记录ID
     * @return 附件信息
     */
    SalesAppealFormDTO getByTaskId(Long taskId);

    /**
     * 更新确认表信息
     *
     * @param appealFormRequest 文件名称
     * @return 附件信息
     */
    Boolean updateSalesConfirm(UpdateSalesAppealFormRequest appealFormRequest);

    /**
     * 我的代办
     *
     * @param request 文件名称
     * @return 附件信息
     */
    List<SalesAppealFormBO> queryToDoList(QuerySalesAppealPageRequest request);

    /**
     * 查看附件
     *
     * @param formId 文件名称
     * @return 附件信息
     */
    SalesAppealExtFormDTO queryAppendix(Long formId);

    /**
     * 根据ID获取记录
     *
     * @param id ID
     * @return 记录
     */
    SalesAppealFormDTO getById(Long id);

    Long newSaveDraft(SaveSalesAppealSubmitFormRequest request);

    /**
     * 批量删除销量申诉上传数据
     * @param request
     * @return
     */
    boolean removeByIds(RemoveSelectAppealFlowFormRequest request);

    /**
     * 生成流向表单 兼容选择流向和上传excel两种数据
     * @param request 请求参数
     * @return 成功/失败
     */
    String compatibleCreateFleeFlowForm(CreateSalesAppealFlowRequest request);

    /**
     * 验证选择流向数据是否被锁定
     * @param formId
     * @return
     */
    boolean valid(Long formId);

    /**
     * 通过formId删除当前formId相关的所有上传excel的数据
     * @param formId
     * @return
     */
    boolean removeAllSelectByFormId(Long formId);

    /**
     * 通过formId删除当前formId相关的所有选择流向数据
     * @param formId
     * @return
     */
    boolean removeAllSelectFlowByFormId(Long formId);

    /**
     * 日程开启，重新开启销量申诉清洗任务
     * @return
     */
    boolean washSaleAppealFlowData();
}
