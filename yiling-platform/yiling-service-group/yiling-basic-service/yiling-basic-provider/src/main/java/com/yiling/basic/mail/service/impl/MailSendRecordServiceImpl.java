package com.yiling.basic.mail.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.basic.mail.dao.MailSendRecordMapper;
import com.yiling.basic.mail.entity.MailSendRecordDO;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.basic.mail.enums.MailSendStatusEnum;
import com.yiling.basic.mail.service.MailSendRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author shichen
 * @类名 MailSendRecordServiceImpl
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
@Service
public class MailSendRecordServiceImpl extends BaseServiceImpl<MailSendRecordMapper, MailSendRecordDO> implements MailSendRecordService {
    @Resource
    private MailSendRecordMapper    mailSendRecordMapper;

    @Override
    public List<Long> getSendSuccessBusinessIds(MailEnum mailEnum,List<Long> businessIds) {
        if(CollectionUtils.isEmpty(businessIds)){
            return null;
        }
        List<MailSendRecordDO> recordDOList = mailSendRecordMapper.getRecordByBusinessIdsAndSendStatus(MailSendStatusEnum.SUCCESS.getCode(),mailEnum.getCode(),businessIds);
        return recordDOList.stream().map(MailSendRecordDO::getBusinessId).collect(Collectors.toList());
    }

    @Override
    public void saveSendMail(MailEnum mailEnum, MailSendStatusEnum mailSendStatusEnum, JSONArray jsonArray, String sendMsg, String mailUid){
        List<JSONObject> jsonList = jsonArray.toJavaList(JSONObject.class);
        Date date = new Date();
        List<MailSendRecordDO> recordList = jsonList.stream().map(json -> {
            MailSendRecordDO recordDO = new MailSendRecordDO();
            recordDO.setMailCode(mailEnum.getCode());
            recordDO.setSendStatus(mailSendStatusEnum.getCode());
            recordDO.setSendTime(date);
            recordDO.setBusinessId(Long.valueOf(String.valueOf(json.get("id"))));
            recordDO.setSendDataJson(JSONObject.toJSONString(json));
            recordDO.setSendResultMsg(sendMsg);
            recordDO.setMailUid(mailUid);
            return recordDO;
        }).collect(Collectors.toList());
        this.saveBatch(recordList);
    }
}
