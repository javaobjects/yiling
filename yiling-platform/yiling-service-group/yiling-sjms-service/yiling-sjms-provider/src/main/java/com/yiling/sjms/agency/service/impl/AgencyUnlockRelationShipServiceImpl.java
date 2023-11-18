package com.yiling.sjms.agency.service.impl;

import java.util.Date;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyLockFormDO;
import com.yiling.sjms.agency.entity.AgencyUnlockFormDO;
import com.yiling.sjms.agency.entity.AgencyUnlockRelationShipDO;
import com.yiling.sjms.agency.dao.AgencyUnlockRelationShipMapper;
import com.yiling.sjms.agency.enums.AgencyLockErrorCode;
import com.yiling.sjms.agency.service.AgencyUnlockFormService;
import com.yiling.sjms.agency.service.AgencyUnlockRelationShipService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.service.FormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 机构解锁三者关系 服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
@Service
@Slf4j
public class AgencyUnlockRelationShipServiceImpl extends BaseServiceImpl<AgencyUnlockRelationShipMapper, AgencyUnlockRelationShipDO> implements AgencyUnlockRelationShipService {

    @Autowired
    private FormService formService;

    @Autowired
    private AgencyUnlockFormService unlockFormService;

    @Override
    public void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        //判断form状态
        AgencyUnlockFormDO lockFormDO = unlockFormService.getById(request.getId());
        if (ObjectUtil.isNull(lockFormDO)){
            log.warn("机构信息锁定表数据不存在，id={}",request.getId());
            throw  new BusinessException(AgencyLockErrorCode.DATA_NOT_FIND);
        }
        if (ObjectUtil.equal(lockFormDO.getArchiveStatus(),request.getArchiveStatus())){
            log.warn("当前状态与修改状态一致，无需修改，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
        FormDO formDO = formService.getById(lockFormDO.getFormId());

        if (ObjectUtil.isNull(formDO)){
            log.warn("更新数据归档状态时表单信息不存在，formId={}",lockFormDO.getFormId());
            throw new BusinessException(AgencyLockErrorCode.FORM_NOT_FIND);
        }
        Integer formStatus = formDO.getStatus();
        if (ObjectUtil.equal(FormStatusEnum.APPROVE.getCode(), formStatus)) {
            log.warn("当前流程状态不允许修改操作，formId={},状态={}",lockFormDO.getFormId(),FormStatusEnum.getByCode(formDO.getStatus()).getName());
            throw  new BusinessException(AgencyLockErrorCode.PROHIBIT_UPDATE);
        }
        AgencyUnlockFormDO aDo = PojoUtils.map(request, AgencyUnlockFormDO.class);
        aDo.setOpTime(new Date());
        boolean isSuccess = unlockFormService.updateById(aDo);
        if (!isSuccess){
            log.error("更新归档状态失败，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
    }
}
