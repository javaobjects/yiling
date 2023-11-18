package com.yiling.dataflow.flowcollect.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowMonthUploadRecordBO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthUploadPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

/**
 * <p>
 * 月流向上传记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-03
 */
public interface FlowMonthUploadRecordService extends BaseService<FlowMonthUploadRecordDO> {

    /**
     * 月流向上传记录列表分页
     *
     * @param request
     * @return
     */
    Page<FlowMonthUploadRecordBO> queryFlowMonthPage(QueryFlowMonthUploadPageRequest request);

    /**
     * 检查月流向文件名格式
     *
     * @param fileName 文件名称
     * @return 错误信息
     */
    String checkFlowFileName(String fileName);

    /**
     * 根据文件名获取上传流向文件记录
     *
     * @param fileName
     * @return
     */
    FlowMonthUploadRecordDTO getByFileName(String fileName);

    FlowMonthUploadRecordDTO getByRecordId(Long recordId);

    /**
     * 保存月流向上传记录
     *
     * @param request
     * @return
     */
    Long saveFlowMonthRecord(SaveFlowMonthUploadRecordRequest request);

    /**
     * 更新流向上传记录
     *
     * @param request
     * @return
     */
    boolean updateFlowMonthRecord(UpdateFlowMonthUploadRecordRequest request);

    /**
     * 根据文件名获取文件类型
     *
     * @param fileName
     * @return
     */
    Integer getDataType(String fileName);

    /**
     * 根据文件名获取excelCode
     *
     * @param fileName
     * @return
     */
    String getExcelCodeByFileName(String fileName);

    /**
     * 删除月流向记录
     *
     * @param id
     * @param currentUserId
     * @return
     */
    Boolean deleteRecord(Long id, Long currentUserId);

    /**
     * 检查月流向文件名格式 - 销售申诉DIH-403二期校验新逻辑
     *@param isFixUpload 是否补传
     * @param fileName 文件名称
     * @return 错误信息
     */
    String checkFlowFileNameNew(String fileName, CurrentSjmsUserInfo userInfo, boolean isFixUpload);

    /**
     * 根据文件名获取流向类型
     *
     * @param fileName
     * @return
     */
    Integer getFlowType(String fileName);

    /**
     * 补传月流向 创建清洗任务
     * @return
     */
    Boolean createWashTask();

}
