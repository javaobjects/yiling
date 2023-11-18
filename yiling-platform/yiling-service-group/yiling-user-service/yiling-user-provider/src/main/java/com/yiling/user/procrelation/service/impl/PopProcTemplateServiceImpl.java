package com.yiling.user.procrelation.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.dao.PopProcTemplateMapper;
import com.yiling.user.procrelation.dto.request.QueryProcTemplatePageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcTemplateRequest;
import com.yiling.user.procrelation.entity.PopProcTemplateDO;
import com.yiling.user.procrelation.enums.ProcRelationErrorCode;
import com.yiling.user.procrelation.service.PopProcTemplateService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * pop采购关系导入商品模板表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-06-19
 */
@Slf4j
@Service
public class PopProcTemplateServiceImpl extends BaseServiceImpl<PopProcTemplateMapper, PopProcTemplateDO> implements PopProcTemplateService {

    @Autowired
    DataSource dataSource;

    @Override
    public Long saveProcTemplate(SaveProcTemplateRequest request) {
        if (request == null || StrUtil.isBlank(request.getTemplateName())) {
            return 0L;
        }
        LambdaQueryWrapper<PopProcTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PopProcTemplateDO::getTemplateName, request.getTemplateName());

        List<PopProcTemplateDO> list = list(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            log.warn("新增或更新pop采购关系模板时，模板名称已存在，参数={}", request);
            throw new BusinessException(ProcRelationErrorCode.TEMPLATE_TEMPLATE_ALREADY);
        }

        PopProcTemplateDO template = PojoUtils.map(request, PopProcTemplateDO.class);
//        if (request.getId() == null || request.getId() != 0) {
//            String number = generateRelationNum();
//            template.setTemplateNumber(number);
//        }
        boolean isSuccess = saveOrUpdate(template);
        if (!isSuccess) {
            log.error("保存pop建采导商品模板失败，参数={}", template);
            throw new ServiceException(ResultCode.FAILED);
        }
        if (request.getId() == null || request.getId() != 0) {
            String number = "PT" + String.format("%03d", template.getId());
            template.setTemplateNumber(number);
            isSuccess=updateById(template);
            if (!isSuccess) {
                log.error("更新pop建采导商品模板编号失败，参数={}", template);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        return template.getId();
    }

    @Override
    public Page<PopProcTemplateDO> queryPageList(QueryProcTemplatePageRequest request) {
        LambdaQueryWrapper<PopProcTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(request.getTemplateNumber()), PopProcTemplateDO::getTemplateNumber, request.getTemplateNumber());
        wrapper.like(StrUtil.isNotBlank(request.getTemplateName()), PopProcTemplateDO::getTemplateName, request.getTemplateName());
        wrapper.in(CollUtil.isNotEmpty(request.getUserIdList()), PopProcTemplateDO::getUpdateUser, request.getUserIdList());

        Page<PopProcTemplateDO> page = page(request.getPage(), wrapper);

        return page;
    }

    @Override
    public Boolean deleteById(Long templateId, Long opUser) {
        if (templateId == null || templateId == 0) {
            return Boolean.FALSE;
        }
        PopProcTemplateDO templateDO = getById(templateId);
        if (templateDO == null) {
            return Boolean.FALSE;
        }
        templateDO.setOpUserId(opUser);
        deleteByIdWithFill(templateDO);
        return deleteByIdWithFill(templateDO) > 0;
    }

    @Override
    public PopProcTemplateDO queryTemplateByNumber(String templateNumber) {
        if (StrUtil.isBlank(templateNumber)) {
            return null;
        }
        LambdaQueryWrapper<PopProcTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PopProcTemplateDO::getTemplateNumber, templateNumber);

        return getOne(wrapper);
    }

    private String generateRelationNum() {
        String databaseName = getDatabaseName();
        Long autoIncrement = baseMapper.queryAutoIncrement(databaseName);
        if (autoIncrement != null && autoIncrement != 0) {
            return "PT" + String.format("%03d", autoIncrement);
        } else {
            log.error("采购关系编号生成异常，autoIncrement={}", autoIncrement);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    private String getDatabaseName() {
        String dbName = null;
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String url = metaData.getURL();
            dbName = extractDatabaseNameFromUrl(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbName;
    }

    private String extractDatabaseNameFromUrl(String url) {
        String[] parts = url.split("\\?");
        String[] pathParts = parts[0].split("/");
        return pathParts[pathParts.length - 1];
    }
}
