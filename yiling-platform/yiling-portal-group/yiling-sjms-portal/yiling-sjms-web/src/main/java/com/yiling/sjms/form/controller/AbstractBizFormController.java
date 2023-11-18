package com.yiling.sjms.form.controller;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.vo.BizFormDetailVO;
import com.yiling.sjms.form.vo.FormVO;
import com.yiling.sjms.form.vo.OperateControlVO;
import com.yiling.sjms.gb.vo.GbProcessDetailVO;
import com.yiling.sjms.workflow.vo.WfActHistoryVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.api.WfActHistoryApi;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.WfActHistoryDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.enums.WfActTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 业务表单 Controller <br/>
 * 其他业务表单Controller继承此类 <br/>
 *
 * @author: xuan.zhou
 * @date: 2023/3/1
 */
@RestController("abstractBizFormController")
public abstract class AbstractBizFormController extends BaseController {

    @DubboReference
    FormApi formApi;
    @DubboReference
    TaskApi taskApi;

    @DubboReference
    private ProcessApi processApi;

    @DubboReference
    WfActHistoryApi wfActHistoryApi;

    @ApiOperation(value = "获取表单业务详情")
    @GetMapping("detail")
    public Result<BizFormDetailVO> detail(
            @CurrentUser CurrentSjmsUserInfo userInfo,
            @ApiParam(value = "单据ID", required = true) @RequestParam(required = true) Long formId,
            @ApiParam(value = "转发历史记录ID", required = false) @RequestParam(required = false) Long forwardHistoryId){
        return Result.success(this.getBizFormDetailVO(formId,userInfo,forwardHistoryId));
    }

    abstract void setBizInfo(BizFormDetailVO vo);

    /**
     * 获取业务表单详情 VO
     *
     * @param formId 表单ID
     * @return com.yiling.sjms.form.vo.BizFormDetailVO
     * @author xuan.zhou
     * @date 2023/3/1
     **/
    protected BizFormDetailVO getBizFormDetailVO(Long formId,CurrentSjmsUserInfo userInfo,Long forwardHistoryId) {
        BizFormDetailVO bizFormDetailVO = new BizFormDetailVO();
        bizFormDetailVO.setBasicInfo(this.getFormVO(formId));
        bizFormDetailVO.setWfActHistoryList(this.getWfActHistoryVOList(formId));
        bizFormDetailVO.setAuditHistoryVOList(this.queryAuditHistory(formId));
        bizFormDetailVO.setOperateControlVO(this.getOperateControl(formId,userInfo,forwardHistoryId));
        this.setBizInfo(bizFormDetailVO);
        return bizFormDetailVO;
    }

    private FormVO getFormVO(Long formId) {
        FormDTO formDTO = formApi.getById(formId);
        return PojoUtils.map(formDTO, FormVO.class);
    }

    private List<WfActHistoryVO> getWfActHistoryVOList(Long formId) {
        List<WfActHistoryDTO> wfActHistoryDTOList = taskApi.listHistoryByFormId(formId);
        if (CollUtil.isEmpty(wfActHistoryDTOList)) {
            return ListUtil.empty();
        }

        List<WfActHistoryVO> wfActHistoryVOList = CollUtil.newArrayList();
        for (WfActHistoryDTO wfActHistoryDTO : wfActHistoryDTOList) {
            WfActTypeEnum wfActTypeEnum = WfActTypeEnum.getByCode(wfActHistoryDTO.getActType());

            WfActHistoryVO wfActHistoryVO = new WfActHistoryVO();
            wfActHistoryVO.setFromEmpName(wfActHistoryDTO.getFromEmpName());
            wfActHistoryVO.setToEmpNames(wfActHistoryDTO.getToEmpNames());
            wfActHistoryVO.setActTypeName(wfActTypeEnum.getName());
            wfActHistoryVO.setActTextTypeName(wfActTypeEnum.getTextTypeName());
            wfActHistoryVO.setActText(wfActHistoryDTO.getActText());
            wfActHistoryVO.setActTime(wfActHistoryDTO.getActTime());

            wfActHistoryVOList.add(wfActHistoryVO);
        }

        return wfActHistoryVOList;
    }

    private List<GbProcessDetailVO> queryAuditHistory(Long formId){
        FormDTO formDTO = formApi.getById(formId);
        List<ProcessDetailDTO> processDetail = processApi.getProcessDetailByInsId(formDTO.getFlowId());
        List<GbProcessDetailVO> gbProcessDetailList = PojoUtils.map(processDetail, GbProcessDetailVO.class);
        return gbProcessDetailList;
    }

    private OperateControlVO getOperateControl(Long formId,CurrentSjmsUserInfo userInfo,Long forwardHistoryId){
        WfTaskDTO taskDTO = taskApi.getByFormId(formId,userInfo.getCurrentUserCode(),forwardHistoryId);
        OperateControlVO operateControlVO = new OperateControlVO();
        if(Objects.isNull(taskDTO) || FlowConstant.FORWARD_NODE.equals(taskDTO.getTaskName())){
            operateControlVO.setShowAuditButtonFlag(false);
        }else{
            operateControlVO.setShowAuditButtonFlag(true);
        }
        if (!operateControlVO.getShowAuditButtonFlag()) {
            if (forwardHistoryId != null && forwardHistoryId != 0L) {
                boolean hasComment = wfActHistoryApi.hasComment(forwardHistoryId, userInfo.getCurrentUserId());
                operateControlVO.setShowCommentButtonFlag(!hasComment);
            }else if(Objects.nonNull(taskDTO) && taskDTO.getTaskName().equals(FlowConstant.FORWARD_NODE)){
                boolean hasComment = wfActHistoryApi.hasComment(Long.parseLong(taskDTO.getExecutionId()), userInfo.getCurrentUserId());
                operateControlVO.setShowCommentButtonFlag(!hasComment);
            }
        }
        return operateControlVO;
    }
}
