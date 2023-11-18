package com.yiling.basic.mail.dao;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.basic.mail.entity.MailSendRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shichen
 * @类名 MailSendRecordMapper
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
public interface MailSendRecordMapper extends BaseMapper<MailSendRecordDO> {

    /**
     * 获取满足业务id和状态的记录
     * @param sendStatus
     * @param mailCode
     * @param businessIds
     * @return
     */
    public List<MailSendRecordDO> getRecordByBusinessIdsAndSendStatus(@Param("sendStatus") Integer sendStatus, @Param("mailCode") String mailCode, @Param("businessIds") List<Long> businessIds);


}
