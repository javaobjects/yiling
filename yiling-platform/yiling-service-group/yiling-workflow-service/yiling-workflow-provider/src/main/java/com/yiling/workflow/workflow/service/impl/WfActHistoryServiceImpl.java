package com.yiling.workflow.workflow.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dao.WfActHistoryMapper;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;
import com.yiling.workflow.workflow.entity.WfActHistoryDO;
import com.yiling.workflow.workflow.enums.WfActTypeEnum;
import com.yiling.workflow.workflow.service.WfActHistoryService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * <p>
 * 流程历史记录表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-02-15
 */
@Service
public class WfActHistoryServiceImpl extends BaseServiceImpl<WfActHistoryMapper, WfActHistoryDO> implements WfActHistoryService {

    @Lazy
    @Autowired
    WfActHistoryServiceImpl _this;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference
    FormApi formApi;

    @Override
    public List<WfActHistoryDO> listByFormId(Long formId) {
        QueryWrapper<WfActHistoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WfActHistoryDO::getFormId, formId)
                .orderByDesc(WfActHistoryDO::getId);
        return this.list(queryWrapper);
    }

    @GlobalTransactional
    public MqMessageBO prepareAddForwardHistory(AddForwardHistoryRequest request) {
        WfActHistoryDO entity = new WfActHistoryDO();
        entity.setFormId(request.getFormId());
        entity.setFromEmpId(request.getFromEmpId());
        entity.setFromEmpName(this.getEmpName(request.getFromEmpId()));
        entity.setToEmpIds(StrUtil.join(",", request.getToEmpIds()));
        entity.setToEmpNames(this.getEmpNames(request.getToEmpIds()));
        entity.setActType(WfActTypeEnum.FORWARD.getCode());
        entity.setActText(request.getText());
        entity.setActTime(request.getOpTime());
        entity.setOpUserId(request.getOpUserId());
        // 保存
        this.save(entity);

        MqMessageBO mqMessageBO = new MqMessageBO(FlowConstant.TOPIC_WF_ACT_FORWARD, "", entity.getId().toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    @Override
    public Long addForwardHistory(AddForwardHistoryRequest request) {
        MqMessageBO mqMessageBO = _this.prepareAddForwardHistory(request);

        mqMessageSendApi.send(mqMessageBO);

        return Convert.toLong(mqMessageBO.getBody());
    }

    @GlobalTransactional
    public MqMessageBO prepareAddCommentHistory(AddCommentHistoryRequest request) {
        WfActHistoryDO entity = new WfActHistoryDO();
        entity.setFormId(request.getFormId());
        entity.setFromEmpId(request.getFromEmpId());
        entity.setFromEmpName(this.getEmpName(request.getFromEmpId()));
        entity.setActType(WfActTypeEnum.COMMENT.getCode());
        entity.setActText(request.getText());
        entity.setActTime(request.getOpTime());
        entity.setForwardHistoryId(request.getForwardHistoryId());
        entity.setOpUserId(request.getOpUserId());
        // 保存
        this.save(entity);

        String body = "" + entity.getId() + "-" + request.getFromEmpId();
        MqMessageBO mqMessageBO = new MqMessageBO(FlowConstant.TOPIC_WF_ACT_COMMENT, "", body);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    @Override
    public Long addCommentHistory(AddCommentHistoryRequest request) {
        MqMessageBO mqMessageBO = _this.prepareAddCommentHistory(request);

        mqMessageSendApi.send(mqMessageBO);

        return Convert.toLong(mqMessageBO.getBody());
    }

    @Override
    public Long addActHistory(AddWfActHistoryRequest request) {
        WfActHistoryDO entity = new WfActHistoryDO();
        entity.setFormId(request.getFormId());
        entity.setFromEmpId(request.getFromEmpId());
        entity.setFromEmpName(this.getEmpName(request.getFromEmpId()));
        entity.setToEmpIds(request.getToEmpId());
        entity.setToEmpNames(this.getEmpName(request.getToEmpId()));
        entity.setActType(request.getType());
        entity.setActText(request.getText());
        entity.setActTime(request.getOpTime());
        entity.setOpUserId(request.getOpUserId());
        // 保存
        this.save(entity);

        return entity.getId();
    }

    @Override
    public Boolean hasComment(Long commentForwardHistoryId, Long opUserId) {
        WfActHistoryDO entity = this.getUserCommentedHistory(commentForwardHistoryId, opUserId);
        return entity != null;
    }

    private WfActHistoryDO getUserCommentedHistory(Long commentForwardHistoryId, Long opUserId) {
        QueryWrapper<WfActHistoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WfActHistoryDO::getForwardHistoryId, commentForwardHistoryId)
                .eq(WfActHistoryDO::getCreateUser, opUserId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    private String getEmpName(String empId) {
        if (StrUtil.isEmpty(empId)) {
            return null;
        }

        EsbEmployeeDTO empInfo = esbEmployeeApi.getByEmpId(empId);
        if (empInfo != null) {
            return empInfo.getEmpName();
        }
        return null;
    }

    private String getEmpNames(List<String> empIds) {
        if (CollUtil.isEmpty(empIds)) {
            return null;
        }

        List<EsbEmployeeDTO> empInfoList = esbEmployeeApi.listByEmpIds(empIds);
        if (CollUtil.isEmpty(empInfoList)) {
            return null;
        }

        return empInfoList.stream().map(e -> e.getEmpName()).collect(Collectors.joining("，"));
    }

}
