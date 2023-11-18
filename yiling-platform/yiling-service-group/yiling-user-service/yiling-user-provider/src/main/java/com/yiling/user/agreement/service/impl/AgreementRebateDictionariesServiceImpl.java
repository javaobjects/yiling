package com.yiling.user.agreement.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateDictionariesMapper;
import com.yiling.user.agreement.dto.AgreementRebateDictionariesDTO;
import com.yiling.user.agreement.entity.AgreementRebateDictionariesDO;
import com.yiling.user.agreement.enums.AgreementRebateDictionariesStatusEnum;
import com.yiling.user.agreement.service.AgreementRebateDictionariesService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 协议返利字典表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Service
public class AgreementRebateDictionariesServiceImpl extends BaseServiceImpl<AgreementRebateDictionariesMapper, AgreementRebateDictionariesDO> implements AgreementRebateDictionariesService {

	@Override
	public List<AgreementRebateDictionariesDO> listByIds(List<Long> ids, AgreementRebateDictionariesStatusEnum code) {
		LambdaQueryWrapper<AgreementRebateDictionariesDO> wrapper = Wrappers.lambdaQuery();
		if (ObjectUtil.isNotNull(code)){
			wrapper.eq(AgreementRebateDictionariesDO::getCode,code);
		}
		wrapper.in(AgreementRebateDictionariesDO::getId,ids);
		return baseMapper.selectList(wrapper);
	}

	@Override
	public Map<Long, List<AgreementRebateDictionariesDTO>> listByParentIds(List<Long> parentIds, AgreementRebateDictionariesStatusEnum code) {
		LambdaQueryWrapper<AgreementRebateDictionariesDO> wrapper = Wrappers.lambdaQuery();
		if (ObjectUtil.isNotNull(code)){
			wrapper.eq(AgreementRebateDictionariesDO::getCode,code);
		}
		wrapper.in(AgreementRebateDictionariesDO::getParentId,parentIds);
		List<AgreementRebateDictionariesDO> agreementRebateDictionariesDOS = baseMapper.selectList(wrapper);
		if(CollectionUtil.isEmpty(agreementRebateDictionariesDOS)){
			return Maps.newHashMap();
		}
		List<AgreementRebateDictionariesDTO> dtoList = PojoUtils.map(agreementRebateDictionariesDOS,AgreementRebateDictionariesDTO.class);
		return dtoList.stream().collect(Collectors.groupingBy(AgreementRebateDictionariesDTO::getParentId));
	}

	@Override
	public List<AgreementRebateDictionariesDO> listByNameList(List<String> nameList, AgreementRebateDictionariesStatusEnum code) {
		LambdaQueryWrapper<AgreementRebateDictionariesDO> wrapper = Wrappers.lambdaQuery();
		if (ObjectUtil.isNotNull(code)){
			wrapper.eq(AgreementRebateDictionariesDO::getCode,code);
		}
		wrapper.in(AgreementRebateDictionariesDO::getName,nameList);
		return baseMapper.selectList(wrapper);
	}
}
