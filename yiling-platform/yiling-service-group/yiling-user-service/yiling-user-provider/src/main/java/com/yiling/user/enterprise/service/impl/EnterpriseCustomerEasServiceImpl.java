package com.yiling.user.enterprise.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.enterprise.dao.EnterpriseCustomerEasMapper;
import com.yiling.user.enterprise.dto.request.AddCustomerEasInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerEasService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 企业客户对应的eas信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-26
 */
@Service
public class EnterpriseCustomerEasServiceImpl extends BaseServiceImpl<EnterpriseCustomerEasMapper, EnterpriseCustomerEasDO> implements EnterpriseCustomerEasService {

    @Override
    public Page<EnterpriseCustomerEasDO> pageList(QueryCustomerEasInfoPageListRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public Page<EnterpriseCustomerEasDO> pageListByCurrent(QueryCustomerEasInfoPageListByCurrentRequest request) {
        return this.baseMapper.pageListByCurrent(request.getPage(), request);
    }

    @Override
    public List<EnterpriseCustomerEasDO> listByCustomer(Long eid, Long customerEid) {
        QueryWrapper<EnterpriseCustomerEasDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerEasDO::getEid, eid)
                .eq(EnterpriseCustomerEasDO::getCustomerEid, customerEid);

        List<EnterpriseCustomerEasDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public Map<Long, List<EnterpriseCustomerEasDO>> listCustomerEasInfos(Long eid, List<Long> customerEids) {
        QueryWrapper<EnterpriseCustomerEasDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerEasDO::getEid, eid)
                .in(EnterpriseCustomerEasDO::getCustomerEid, customerEids);

        List<EnterpriseCustomerEasDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.groupingBy(EnterpriseCustomerEasDO::getCustomerEid));
    }

    @Override
    public Long getCustomerEidByEasCode(Long eid, String easCode) {
        QueryWrapper<EnterpriseCustomerEasDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerEasDO::getEid, eid)
                .in(EnterpriseCustomerEasDO::getEasCode, easCode);

        EnterpriseCustomerEasDO enterpriseCustomerEasDO = Optional.ofNullable(this.getOne(queryWrapper)).orElse(new EnterpriseCustomerEasDO());
        return enterpriseCustomerEasDO.getCustomerEid();
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Boolean updateAppliedAmount(Long eid, String easCode, BigDecimal amount) {
		Assert.notNull(eid,"企业id不能为空");
		Assert.notBlank(easCode,"easCode不能为空");
		Assert.notNull(amount,"金额不能为空");

		int row = baseMapper.updateAppliedAmount(eid, easCode, amount);
		if(row <= 0){
            throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_UPDATE_EAS);
        }
		return Boolean.TRUE;
	}

    @Override
    public Boolean add(AddCustomerEasInfoRequest request) {
        boolean exists = this.existsEasCode(request.getEid(), request.getEasCode());
        if (exists) {
            throw new BusinessException(ResultCode.FAILED, "EAS编码已存在");
        }

        EnterpriseCustomerEasDO entity = PojoUtils.map(request, EnterpriseCustomerEasDO.class);
        return this.save(entity);
    }

    /**
     * EAS编码是否已存在
     *
     * @param eid 企业ID
     * @param easCode EAS编码
     * @return boolean
     * @author xuan.zhou
     * @date 2022/5/25
     **/
    private boolean existsEasCode(Long eid, String easCode) {
        QueryWrapper<EnterpriseCustomerEasDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerEasDO::getEid, eid)
                .eq(EnterpriseCustomerEasDO::getEasCode, easCode)
                .last("limit 1");

        EnterpriseCustomerEasDO entity = this.getOne(queryWrapper);
        return entity != null;
    }
}
