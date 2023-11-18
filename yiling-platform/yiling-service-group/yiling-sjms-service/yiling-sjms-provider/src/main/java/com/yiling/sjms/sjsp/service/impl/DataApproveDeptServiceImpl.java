package com.yiling.sjms.sjsp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.sjsp.dao.DataApproveDeptMapper;
import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;
import com.yiling.sjms.sjsp.entity.DataApproveDeptDO;
import com.yiling.sjms.sjsp.enums.SjshErrorCode;
import com.yiling.sjms.sjsp.service.DataApproveDeptService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据审批部门领导对应关系 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-03-03
 */
@Slf4j
@Service
public class DataApproveDeptServiceImpl extends BaseServiceImpl<DataApproveDeptMapper, DataApproveDeptDO> implements DataApproveDeptService {

    @Override
    public DataApproveDeptDTO getDataApproveDeptByDeptName(String deptName) {
        if (StrUtil.isBlank(deptName)) {
            return null;
        }
        LambdaQueryWrapper<DataApproveDeptDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataApproveDeptDO::getDepName, deptName);

        List<DataApproveDeptDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            log.error("数据审批部门领导对应关系存在多条对应关系，部门名称={}", deptName);
            throw new BusinessException(SjshErrorCode.TOO_MANY_DEPT_RELATION);
        }
        return PojoUtils.map(list.stream().findAny().get(), DataApproveDeptDTO.class);
    }

    @Override
    public DataApproveDeptDTO getDataApproveDeptByDeptId(String deptId) {
        if (StrUtil.isBlank(deptId)) {
            return null;
        }
        LambdaQueryWrapper<DataApproveDeptDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataApproveDeptDO::getDepId, deptId);

        List<DataApproveDeptDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            log.error("数据审批部门领导对应关系存在多条对应关系，部门id={}", deptId);
            throw new BusinessException(SjshErrorCode.TOO_MANY_DEPT_RELATION);
        }
        return PojoUtils.map(list.stream().findAny().get(), DataApproveDeptDTO.class);
    }
}
