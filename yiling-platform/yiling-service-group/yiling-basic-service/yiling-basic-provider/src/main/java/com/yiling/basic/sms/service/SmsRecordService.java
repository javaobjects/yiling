package com.yiling.basic.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.sms.dto.request.QuerySmsRecordPageListRequest;
import com.yiling.basic.sms.entity.SmsRecordDO;
import com.yiling.basic.sms.enums.SmsStatusEnum;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 短信记录表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-07
 */
public interface SmsRecordService extends BaseService<SmsRecordDO> {

    /**
     * 更新短信记录状态
     *
     * @param id 记录ID
     * @param statusEnum 状态枚举
     * @param remark 备注
     * @return
     */
    boolean updateStatus(Long id, SmsStatusEnum statusEnum, String remark);

    /**
     * 短信日志查询
     * @param request
     * @return
     */
    Page<SmsRecordDO> pageList(QuerySmsRecordPageListRequest request);
}
