package com.yiling.user.agreementv2.service.impl;

import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementSecondUserDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementSecondUserPageRequest;
import com.yiling.user.agreementv2.dto.request.SaveAgreementSecondUserRequest;
import com.yiling.user.agreementv2.entity.AgreementSecondUserDO;
import com.yiling.user.agreementv2.dao.AgreementSecondUserMapper;
import com.yiling.user.agreementv2.service.AgreementSecondUserService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.UserErrorCode;

import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 协议乙方签订人表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
@Service
public class AgreementSecondUserServiceImpl extends BaseServiceImpl<AgreementSecondUserMapper, AgreementSecondUserDO> implements AgreementSecondUserService {

    @Override
    public Page<AgreementSecondUserDTO> querySecondUserListPage(QueryAgreementSecondUserPageRequest request) {
        LambdaQueryWrapper<AgreementSecondUserDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(request.getName())) {
            wrapper.like(AgreementSecondUserDO::getName, request.getName());
        }
        if (StrUtil.isNotEmpty(request.getMobile())) {
            wrapper.like(AgreementSecondUserDO::getMobile, request.getMobile());
        }
        if (StrUtil.isNotEmpty(request.getSecondName())) {
            wrapper.like(AgreementSecondUserDO::getSecondName, request.getSecondName());
        }

        return PojoUtils.map(this.page(request.getPage(), wrapper), AgreementSecondUserDTO.class);

    }

    @Override
    public Boolean saveAgreementSecondUser(SaveAgreementSecondUserRequest request) {
        LambdaQueryWrapper<AgreementSecondUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSecondUserDO::getName, request.getName());
        AgreementSecondUserDO secondUserDO = this.getOne(wrapper);

        AgreementSecondUserDO agreementSecondUserDO = PojoUtils.map(request, AgreementSecondUserDO.class);
        if (Objects.nonNull(request.getId()) && request.getId() != 0) {
            if (Objects.nonNull(secondUserDO) && secondUserDO.getName().equals(request.getName()) && !request.getId().equals(secondUserDO.getId())) {
                throw new BusinessException(UserErrorCode.SIGN_USER_NAME_EXIST);
            }

            return updateById(agreementSecondUserDO);
        } else {
            if (Objects.nonNull(secondUserDO) && secondUserDO.getName().equals(request.getName())) {
                throw new BusinessException(UserErrorCode.SIGN_USER_NAME_EXIST);
            }

            return save(agreementSecondUserDO);
        }
    }
}
