package com.yiling.mall.agreement.service;

import java.util.List;

import com.yiling.mall.agreement.entity.AgreementImportTaskDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.agreement.enums.AgreeImportStatusEnum;

/**
 * <p>
 * 协议导入任务表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-18
 */
public interface AgreementImportTaskService extends BaseService<AgreementImportTaskDO> {


    /**
     * 根据任务编号生成协议
     *
     * @param taskCode
     */
    void  generateAgree(String taskCode);

    /**
     * 查询导入任务
     *
     * @param code
     * @param statusEnum
     * @return
     */
    List<AgreementImportTaskDO> queryAgreeImportListByTaskCode(String code, AgreeImportStatusEnum statusEnum);

}
