package com.yiling.hmc.remind.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.remind.dao.MedsRemindTaskDetailMapper;
import com.yiling.hmc.remind.dto.request.CheckMedsRemindRequest;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindTaskDetailDO;
import com.yiling.hmc.remind.enums.HmcConfirmStatusEnum;
import com.yiling.hmc.remind.enums.HmcSendStatusEnum;
import com.yiling.hmc.remind.service.MedsRemindTaskDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.SchemaOutputResolver;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用药提醒任务详情表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Slf4j
@Service
public class MedsRemindTaskDetailServiceImpl extends BaseServiceImpl<MedsRemindTaskDetailMapper, MedsRemindTaskDetailDO> implements MedsRemindTaskDetailService {

    @Override
    public void cancelHistoryMedsRemindTask(Long currentUserId, List<Long> userIdList, MedsRemindDO medsRemindDO) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getMedsRemindId, medsRemindDO.getId());
        wrapper.lambda().in(MedsRemindTaskDetailDO::getReceiveUserId, userIdList);

        List<MedsRemindTaskDetailDO> medsRemindTaskDetailDOS = this.getBaseMapper().selectList(wrapper);
        if (CollUtil.isEmpty(medsRemindTaskDetailDOS)) {
            log.info("[删除历史用药提醒任务] 未获取到提醒任务，跳过处理");
            return;
        }

        MedsRemindTaskDetailDO medsRemindTaskDetailDO = new MedsRemindTaskDetailDO();
        medsRemindTaskDetailDO.setUpdateUser(currentUserId);
        medsRemindTaskDetailDO.setSendStatus(HmcSendStatusEnum.CANCELED.getType());

        this.getBaseMapper().update(medsRemindTaskDetailDO, wrapper);

    }

    @Override
    public boolean checkMedsRemind(CheckMedsRemindRequest request) {
        MedsRemindTaskDetailDO taskDetailDO = new MedsRemindTaskDetailDO();
        taskDetailDO.setId(request.getId());
        taskDetailDO.setConfirmStatus(request.getConfirmStatus());
        int i = this.getBaseMapper().updateById(taskDetailDO);
        return i > 0;
    }

    @Override
    public void stopAllRemindTask(Long id) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getMedsRemindId, id);

        MedsRemindTaskDetailDO taskDetailDO = new MedsRemindTaskDetailDO();
        taskDetailDO.setSendStatus(HmcSendStatusEnum.CANCELED.getType());

        this.getBaseMapper().update(taskDetailDO, wrapper);

    }

    @Override
    public List<MedsRemindTaskDetailDO> getTodayRemindTaskDetail(Long currentUserId) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getReceiveUserId, currentUserId);
        Date today = DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_FORMAT).toJdkDate();
        Calendar nextDayCalendar = CalendarUtil.calendar(today);
        nextDayCalendar.add(Calendar.DATE, 1);
        wrapper.lambda().ge(MedsRemindTaskDetailDO::getInitSendTime, today);
        wrapper.lambda().lt(MedsRemindTaskDetailDO::getInitSendTime, nextDayCalendar.getTime());

        // sendStatus 不等于 取消发送
        wrapper.lambda().ne(MedsRemindTaskDetailDO::getSendStatus, HmcSendStatusEnum.CANCELED.getType());

        List<MedsRemindTaskDetailDO> medsRemindTaskDetailDOList = this.getBaseMapper().selectList(wrapper);
        return medsRemindTaskDetailDOList;
    }

    @Override
    public List<MedsRemindTaskDetailDO> getConfirmedTaskByMedsIds(List<Long> medsRemindIdList) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindTaskDetailDO::getMedsRemindId, medsRemindIdList);
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getConfirmStatus, HmcConfirmStatusEnum.CONFIRMED.getType());
        return this.getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<MedsRemindTaskDetailDO> getAllSameTimeRemindTask(MedsRemindTaskDetailDO taskDetailDO) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getReceiveUserId, taskDetailDO.getReceiveUserId());
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getInitSendTime, taskDetailDO.getInitSendTime());
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getConfirmStatus, HmcConfirmStatusEnum.UN_CONFIRMED.getType());
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getSendStatus, HmcSendStatusEnum.WAIT.getType());
        return this.getBaseMapper().selectList(wrapper);
    }

    @Override
    public MedsRemindTaskDetailDO getLatestRemindTaskDetail(Long id) {
        QueryWrapper<MedsRemindTaskDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDetailDO::getMedsRemindId, id);
        wrapper.last(" order by init_send_time desc limit 1 ");
        return this.getBaseMapper().selectOne(wrapper);
    }
}
