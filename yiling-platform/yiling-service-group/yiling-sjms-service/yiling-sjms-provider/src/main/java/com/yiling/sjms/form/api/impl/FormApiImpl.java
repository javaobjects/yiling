package com.yiling.sjms.form.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.QueryFormPageListRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * 表单基础信息表
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class FormApiImpl implements FormApi {

    @Autowired
    private FormService formService;

    @Override
    public Page<FormDTO> pageList(QueryFormPageListRequest request) {
        return PojoUtils.map(formService.pageList(request), FormDTO.class);
    }

    @Override
    public FormDTO getById(Long id) {
        Assert.notNull(id, "参数id不能为空");
        return PojoUtils.map(formService.getById(id), FormDTO.class);
    }

    @Override
    public FormDTO getByFlowId(String flowId) {
        Assert.notEmpty(flowId, "参数flowId不能为空");
        return PojoUtils.map(formService.getByFlowId(flowId), FormDTO.class);
    }

    @Override
    public List<FormDTO> listByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }

        List<FormDO> list = formService.listByIds(ids);
        return PojoUtils.map(list, FormDTO.class);
    }

    @Override
    public List<FormDTO> listUnsubmitFormsByUser(FormTypeEnum formTypeEnum, Long userId) {
        Assert.notNull(formTypeEnum, "参数formTypeEnum不能为空");
        Assert.notNull(userId, "参数userId不能为空");
        List<FormDO> list = formService.listUnsubmitFormsByUser(formTypeEnum, userId);
        return PojoUtils.map(list, FormDTO.class);
    }

    @Override
    public Boolean delete(Long id, Long opUserId) {
        Assert.notNull(id, "参数id不能为空");
        Assert.notNull(opUserId, "参数opUserId不能为空");
        return formService.delete(id, opUserId);
    }

    @Override
    public Long create(CreateFormRequest request) {
        return formService.create(request);
    }

    @Override
    public Boolean updateRemark(UpdateRemarkRequest remarkRequest) {
        return formService.updateRemark(remarkRequest);
    }


    @Override
    public List<FormDTO> listByCodes(List<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return ListUtil.empty();
        }
        List<FormDO> list = formService.listByCodes(codes);
        return PojoUtils.map(list, FormDTO.class);
    }

}
