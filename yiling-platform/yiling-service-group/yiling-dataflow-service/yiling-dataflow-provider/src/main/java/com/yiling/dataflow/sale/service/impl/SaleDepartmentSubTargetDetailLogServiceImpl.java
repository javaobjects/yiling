package com.yiling.dataflow.sale.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetDetailLogMapper;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetDetailMapper;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetResolveDetailLogMapper;
import com.yiling.dataflow.sale.dto.DeptTargetSplitDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailLogDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentSubTargetTypeEnum;
import com.yiling.dataflow.sale.enums.DeptTargetErrorCode;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailLogService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门销售指标子项配置详情 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Slf4j
@Service
public class SaleDepartmentSubTargetDetailLogServiceImpl extends BaseServiceImpl<SaleDepartmentSubTargetDetailLogMapper, SaleDepartmentSubTargetDetailLogDO> implements SaleDepartmentSubTargetDetailLogService {
}
