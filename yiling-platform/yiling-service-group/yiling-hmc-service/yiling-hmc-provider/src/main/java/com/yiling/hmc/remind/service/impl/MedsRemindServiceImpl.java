package com.yiling.hmc.remind.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.remind.context.BuildMedsRemindContext;
import com.yiling.hmc.remind.context.MedsRemindResult;
import com.yiling.hmc.remind.dao.MedsRemindMapper;
import com.yiling.hmc.remind.dto.MedsRemindDTO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.dto.request.AcceptMedsRemindRequest;
import com.yiling.hmc.remind.dto.request.MedsRemindBaseRequest;
import com.yiling.hmc.remind.dto.request.SaveMedsRemindRequest;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindTaskDetailDO;
import com.yiling.hmc.remind.entity.MedsRemindTimeDO;
import com.yiling.hmc.remind.entity.MedsRemindUserDO;
import com.yiling.hmc.remind.enums.HmcConfirmStatusEnum;
import com.yiling.hmc.remind.enums.HmcMedsClearStatusEnum;
import com.yiling.hmc.remind.enums.HmcMedsRemindCreateSourceEnum;
import com.yiling.hmc.remind.enums.HmcMedsRemindUseDaysEnum;
import com.yiling.hmc.remind.enums.HmcMedsRemindUseTimesEnum;
import com.yiling.hmc.remind.enums.HmcRemindStatusEnum;
import com.yiling.hmc.remind.enums.HmcRemindTypeEnum;
import com.yiling.hmc.remind.enums.HmcSendStatusEnum;
import com.yiling.hmc.remind.service.MedsRemindService;
import com.yiling.hmc.remind.service.MedsRemindTaskDetailService;
import com.yiling.hmc.remind.service.MedsRemindTimeService;
import com.yiling.hmc.remind.service.MedsRemindUserService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用药提醒主表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Slf4j
@Service
public class MedsRemindServiceImpl extends BaseServiceImpl<MedsRemindMapper, MedsRemindDO> implements MedsRemindService {

    @Autowired
    MedsRemindTimeService medsRemindTimeService;

    @Autowired
    MedsRemindTaskDetailService medsRemindTaskDetailService;

    @Autowired
    MedsRemindUserService medsRemindUserService;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public MedsRemindDTO saveOrUpdateMedsRemind(SaveMedsRemindRequest request) {

        // 1、保存用药提醒
        MedsRemindDO medsRemindDO = saveMedsRemind(request);

        // 2、保存用药提醒时间
        this.saveMedsRemindTime(request, medsRemindDO);

        // 3、保存用药提醒用户
        saveMedsRemindUser(medsRemindDO.getId(), request.getOpUserId());

        // 4、获取所有提醒用户
        List<Long> userIdList = this.medsRemindUserService.getAllByMedsId(medsRemindDO.getId()).stream().map(MedsRemindUserDO::getCreateUser).collect(Collectors.toList());

        // 5、删除历史用药提醒
        cancelHistoryMedsRemindTask(request.getOpUserId(), userIdList, medsRemindDO);

        // 6、构建今日用药提醒
        List<MedsRemindResult> medsRemindResults = buildTodayMedsRemindTask(medsRemindDO, HmcMedsRemindCreateSourceEnum.USER_START);

        // 7、保存今日用药提醒
        saveMedsRemindTask(medsRemindResults, userIdList);

        return PojoUtils.map(medsRemindDO, MedsRemindDTO.class);
    }

    /**
     * 保存用药提醒用户
     *
     * @param medsRemindId
     * @param userId
     */
    private void saveMedsRemindUser(Long medsRemindId, Long userId) {
        medsRemindUserService.saveMedsRemindUser(medsRemindId, userId);
    }

    /**
     * 保存今日用药提醒
     *
     * @param medsRemindResults
     */
    private void saveMedsRemindTask(List<MedsRemindResult> medsRemindResults, List<Long> userIdList) {
        if (CollUtil.isEmpty(medsRemindResults)) {
            log.info("待构建提醒任务为空，跳过处理");
        }

        if (CollUtil.isEmpty(userIdList)) {
            log.info("待构建提醒用户为空，跳过处理");
        }

        List<MedsRemindTaskDetailDO> taskDetailDOList = Lists.newArrayList();

        medsRemindResults.forEach(item -> userIdList.forEach(userId -> {
            MedsRemindTaskDetailDO taskDetailDO = new MedsRemindTaskDetailDO();
            taskDetailDO.setMedsRemindId(item.getMedsRemindId());
            taskDetailDO.setConfirmStatus(HmcConfirmStatusEnum.UN_CONFIRMED.getType());
            taskDetailDO.setInitSendTime(item.getRemindTime());
            taskDetailDO.setSendStatus(HmcSendStatusEnum.WAIT.getType());
            taskDetailDO.setReceiveUserId(userId);
            taskDetailDO.setCreateUser(userId);
            taskDetailDO.setUpdateUser(userId);

            taskDetailDOList.add(taskDetailDO);

        }));

        log.info("最终构建提醒任务参数：{}", JSONUtil.toJsonStr(taskDetailDOList));

        this.medsRemindTaskDetailService.saveBatch(taskDetailDOList);

        // 保存至redis
        saveToRedis(taskDetailDOList);

    }

    /**
     * 把提醒数据保存至redis
     *
     * @param taskDetailDOList
     */
    private void saveToRedis(List<MedsRemindTaskDetailDO> taskDetailDOList) {
        taskDetailDOList.stream().forEach(item -> {
            // 获取所有同一时刻的提醒任务 -> 首次同时间的，才放入redis
            List<MedsRemindTaskDetailDO> allSameTimeRemindTask = this.medsRemindTaskDetailService.getAllSameTimeRemindTask(item);
            if (1 == allSameTimeRemindTask.size()) {
                long expireTime = item.getInitSendTime().getTime() - DateUtil.date().getTime();
                String key = RedisKey.generate("hmc", "meds_remind", String.valueOf(item.getId()));
                redisService.set(key, item.getId(), expireTime / 1000);
            }

        });
    }

    /**
     * 创建用药提醒任务
     *
     * @param medsRemindDO
     */
    private List<MedsRemindResult> buildTodayMedsRemindTask(MedsRemindDO medsRemindDO, HmcMedsRemindCreateSourceEnum createSourceEnum) {

        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindDO::getId, medsRemindDO.getId());
        wrapper.lambda().eq(MedsRemindDO::getRemindStatus, HmcRemindStatusEnum.VALID.getType());

        // 用药提醒设置
        List<MedsRemindDO> medsRemindList = this.getBaseMapper().selectList(wrapper);

        if (CollUtil.isEmpty(medsRemindList)) {
            log.info("未获取到用药提醒记录");
            return Lists.newArrayList();
        }

        List<Long> medsRemindIdList = medsRemindList.stream().map(MedsRemindDO::getId).collect(Collectors.toList());

        // 获取当前用户下所有启用的用药提醒设置 - 时间设置
        List<MedsRemindTimeDO> remindTimeList = medsRemindTimeService.selectByMedsRemindIdList(medsRemindIdList);
        Map<Long, List<MedsRemindTimeDO>> medsRemindTimeMap = remindTimeList.stream().collect(Collectors.groupingBy(MedsRemindTimeDO::getMedsRemindId));

        List<MedsRemindResult> tempList = Lists.newArrayList();

        Map<Integer, Function<BuildMedsRemindContext, List<MedsRemindResult>>> buildMap = new HashMap<>();
        buildMap.put(HmcRemindTypeEnum.DAY.getType(), this::buildDayTask);
        buildMap.put(HmcRemindTypeEnum.HOUR.getType(), this::buildHourTask);

        medsRemindList.forEach(item -> {
            List<MedsRemindTimeDO> remindTimeDOList = medsRemindTimeMap.get(item.getId());

            if (CollUtil.isEmpty(remindTimeDOList)) {
                log.info("未获取到时间设置数据，当前item : {}", item);
                return;
            }

            HmcMedsRemindUseTimesEnum useTimesEnum = HmcMedsRemindUseTimesEnum.getByCode(item.getUseTimesType());
            if (Objects.isNull(useTimesEnum)) {
                log.info("未匹配到用药次数枚举，当前item : {}", item);
                return;
            }
            log.info("用药次数配置：{}", useTimesEnum);

            BuildMedsRemindContext context = BuildMedsRemindContext.builder().remind(item).remindTimeList(remindTimeDOList).createSource(createSourceEnum).build();

            List<MedsRemindResult> result = buildMap.get(useTimesEnum.getType()).apply(context);
            tempList.addAll(result);

        });

        // 处理任务明细，分组 -> 生成主任务
        log.info("待分组任务明细list：{}", JSONUtil.toJsonStr(tempList));

        return tempList;
    }

    /**
     * 保存用药提醒
     *
     * @param request
     * @return
     */
    private MedsRemindDO saveMedsRemind(SaveMedsRemindRequest request) {
        MedsRemindDO medsRemindDO;
        HmcMedsRemindUseDaysEnum useDaysEnum = HmcMedsRemindUseDaysEnum.getByCode(request.getUseDaysType());
        if (Objects.nonNull(request.getId())) {
            // 历史数据
            medsRemindDO = this.getBaseMapper().selectById(request.getId());
            // 如果两次用药天数类型不一样，则延长用药天数
            if (!request.getUseDaysType().equals(medsRemindDO.getUseDaysType())) {
                Calendar calendar = CalendarUtil.calendar();
                calendar.add(Calendar.DATE, useDaysEnum.getTimes());
                medsRemindDO.setExpireDate(calendar.getTime());
            }
            medsRemindDO.setUseTimesType(request.getUseTimesType());
            medsRemindDO.setUseDaysType(request.getUseDaysType());
            medsRemindDO.setUseAmount(request.getUseAmount());
            medsRemindDO.setUseAmountUnit(request.getUseAmountUnit());
            medsRemindDO.setSellSpecificationsId(request.getSellSpecificationsId());
            medsRemindDO.setGoodsName(request.getGoodsName());
        } else {
            medsRemindDO = PojoUtils.map(request, MedsRemindDO.class);
            Calendar calendar = CalendarUtil.calendar();
            calendar.add(Calendar.DATE, useDaysEnum.getTimes());
            medsRemindDO.setExpireDate(calendar.getTime());
            medsRemindDO.setClearStatus(HmcMedsClearStatusEnum.UN_CLEARED.getType());
        }
        medsRemindDO.setRemindStatus(HmcRemindStatusEnum.VALID.getType());
        this.saveOrUpdate(medsRemindDO);
        return medsRemindDO;
    }

    /**
     * 取消发送当前用药提醒对应的提醒任务
     *
     * @param medsRemindDO
     */
    private void cancelHistoryMedsRemindTask(Long currentUserId, List<Long> userIdList, MedsRemindDO medsRemindDO) {
        medsRemindTaskDetailService.cancelHistoryMedsRemindTask(currentUserId, userIdList, medsRemindDO);
    }

    /**
     * 构建每日任务
     */
    public List<MedsRemindResult> buildDayTask(BuildMedsRemindContext context) {
        List<MedsRemindTimeDO> medsRemindTimeDOS = context.getRemindTimeList();
        List<MedsRemindResult> resultList = Lists.newArrayList();

        MedsRemindDO remind = context.getRemind();

        medsRemindTimeDOS.forEach(item -> {
            String timeStr = DateUtil.today() + " " + item.getRemindTime();
            Date date = DateUtil.parse(timeStr, DatePattern.NORM_DATETIME_MINUTE_PATTERN).toJdkDate();
            if (date.compareTo(DateTime.now()) < 0) {
                log.info("目标时间已经过期，跳过，时间：{}", date);
                return;
            }
            if (remind.getExpireDate().compareTo(date) < 0) {
                log.info("提醒时间已过效期，跳过，时间：{}", date);
                return;
            }
            resultList.add(MedsRemindResult.builder().medsRemindId(remind.getId()).remindTime(date).build());
        });
        return resultList;
    }

    /**
     * 构建每小时任务
     */
    public List<MedsRemindResult> buildHourTask(BuildMedsRemindContext context) {
        MedsRemindDO remind = context.getRemind();
        HmcMedsRemindUseTimesEnum useTime = HmcMedsRemindUseTimesEnum.getByCode(remind.getUseTimesType());
        if (Objects.isNull(useTime)) {
            log.info("根据userTimesType未获取到枚举");
            return Lists.newArrayList();
        }

        MedsRemindTimeDO medsRemindTimeDO = context.getRemindTimeList().get(0);
        List<MedsRemindResult> resultList = Lists.newArrayList();

        String today = DateUtil.today();
        Date date;
        Calendar currentCalendar;
        // 如果是用户发起的，就从首次提醒时间开始生成，否则job发起从上一次提醒时间开始 + 提醒周期
        if (HmcMedsRemindCreateSourceEnum.USER_START.equals(context.getCreateSource())) {
            String startTime = today + " " + medsRemindTimeDO.getRemindTime();
            date = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN).toJdkDate();
            currentCalendar = CalendarUtil.calendar(date);
        } else {
            MedsRemindTaskDetailDO taskDetailDO = medsRemindTaskDetailService.getLatestRemindTaskDetail(remind.getId());
            if (Objects.isNull(taskDetailDO)) {
                log.info("获取最近一次提醒任务为空，跳过处理");
                return Lists.newArrayList();
            }
            date = taskDetailDO.getInitSendTime();
            currentCalendar = CalendarUtil.calendar(date);
            currentCalendar.add(Calendar.HOUR, useTime.getTimes());
        }

        Date todayDate = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT).toJdkDate();
        Calendar nextDayCalendar = CalendarUtil.calendar(todayDate);
        nextDayCalendar.add(Calendar.DATE, 1);

        while (currentCalendar.compareTo(nextDayCalendar) <= 0) {
            if (currentCalendar.getTime().compareTo(DateTime.now()) >= 0) {
                Date time = currentCalendar.getTime();
                resultList.add(MedsRemindResult.builder().medsRemindId(remind.getId()).remindTime(time).build());
            }
            currentCalendar.add(Calendar.HOUR, useTime.getTimes());
        }
        return resultList;
    }


    /**
     * 用药管理时间
     *
     * @param request
     * @param medsRemindDO
     * @return
     */
    private List<MedsRemindTimeDO> saveMedsRemindTime(SaveMedsRemindRequest request, MedsRemindDO medsRemindDO) {
        List<String> medsRemindTimeList = request.getMedsRemindTimeList();
        List<MedsRemindTimeDO> medsRemindDOList = medsRemindTimeList.stream().map(item -> {
            MedsRemindTimeDO medsRemindTimeDO = new MedsRemindTimeDO();
            medsRemindTimeDO.setRemindTime(item);
            medsRemindTimeDO.setMedsRemindId(medsRemindDO.getId());
            return medsRemindTimeDO;
        }).collect(Collectors.toList());

        // 删除历史数据
        List<MedsRemindTimeDO> remindTimeDOList = this.medsRemindTimeService.selectByMedsRemindIdList(Arrays.asList(medsRemindDO.getId()));
        if (CollUtil.isNotEmpty(remindTimeDOList)) {
            this.medsRemindTimeService.deleteByMedsRemindId(medsRemindDO.getId(), request.getOpUserId());
        }
        // 保存新数据
        this.medsRemindTimeService.saveBatch(medsRemindDOList);
        return medsRemindDOList;
    }

    @Override
    public List<MedsRemindDTO> getAllMedsRemind(Long currentUserId) {

        // 1、获取当前用户关注的所有提醒
        List<MedsRemindUserDO> medsRemindUserList = medsRemindUserService.getAllMedsRemindByUserId(currentUserId);
        if (CollUtil.isEmpty(medsRemindUserList)) {
            log.info("未获取到用药提醒数据");
            return Lists.newArrayList();
        }

        // 2、获取所有提醒id
        List<Long> medsRemindIdList = medsRemindUserList.stream().map(MedsRemindUserDO::getMedsRemindId).collect(Collectors.toList());

        // 3、获取所有提醒设置
        List<MedsRemindDO> remindDOList = getMedsRemind(medsRemindIdList);
        if (CollUtil.isEmpty(remindDOList)) {
            log.info("未获取到用药提醒数据");
            return Lists.newArrayList();
        }

        // 4、提醒设置的用户id
        List<Long> userIdList = remindDOList.stream().map(MedsRemindDO::getCreateUser).collect(Collectors.toList());

        // 5、根据用户id获取用户
        List<HmcUser> hmcUserList = hmcUserApi.listByIds(userIdList);

        // 6、把用户分组
        Map<Long, HmcUser> userDTOMap = hmcUserList.stream().collect(Collectors.toMap(HmcUser::getUserId, o -> o, (k1, k2) -> k1));

        List<MedsRemindDTO> medsRemindDTOList = PojoUtils.map(remindDOList, MedsRemindDTO.class);
        medsRemindDTOList.forEach(item -> {
            // 7、设置创建人头像
            item.setCreatorAvatarUrl(userDTOMap.get(item.getCreateUser()).getAvatarUrl());

            // 判断是否过期 -> 设为无效
            if (DateUtil.date().compareTo(item.getExpireDate()) > 0) {
                item.setRemindStatus(HmcRemindStatusEnum.INVALID.getType());
            }
        });

        return medsRemindDTOList;
    }

    /**
     * 获取用药提醒
     *
     * @param medsRemindIdList
     */
    private List<MedsRemindDO> getMedsRemind(List<Long> medsRemindIdList) {
        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindDO::getId, medsRemindIdList);
        wrapper.lambda().orderByDesc(MedsRemindDO::getCreateTime);
        List<MedsRemindDO> remindList = this.getBaseMapper().selectList(wrapper);
        return remindList;
    }

    @Override
    @Transactional
    public boolean stopMedsRemind(Long id) {

        // 1、更新提醒状态
        updateMedsRemindStatus(id);

        // 2、更新所有关联用户
        updateMedsRemindUser(id, null);

        // 3、停止所有提醒任务
        stopAllRemindTask(id);

        return Boolean.TRUE;

    }

    @Override
    @Transactional
    public boolean acceptRemind(AcceptMedsRemindRequest request) {

        MedsRemindDO medsRemindDO = this.getById(request.getId());

        // 状态判断
        if (!HmcRemindStatusEnum.VALID.getType().equals(medsRemindDO.getRemindStatus())) {
            log.info("当前用药提醒设置状态为无效，跳过处理");
            return Boolean.TRUE;
        }

        // 1、删除历史用药提醒
        cancelHistoryMedsRemindTask(request.getOpUserId(), Arrays.asList(request.getOpUserId()), medsRemindDO);

        // 2、保存用药提醒用户
        saveMedsRemindUser(medsRemindDO.getId(), request.getOpUserId());

        // 3、生成今日用药提醒
        List<MedsRemindResult> medsRemindResults = buildTodayMedsRemindTask(medsRemindDO, HmcMedsRemindCreateSourceEnum.USER_START);

        // 4、保存今日用药提醒
        saveMedsRemindTask(medsRemindResults, Arrays.asList(request.getOpUserId()));
        return Boolean.TRUE;
    }

    /**
     * 停止所有提醒任务
     *
     * @param id
     */
    private void stopAllRemindTask(Long id) {
        medsRemindTaskDetailService.stopAllRemindTask(id);
    }

    /**
     * 更新提醒状态
     *
     * @param id
     */
    private void updateMedsRemindStatus(Long id) {
        MedsRemindDO medsRemindDO = new MedsRemindDO();
        medsRemindDO.setId(id);
        medsRemindDO.setRemindStatus(HmcRemindStatusEnum.INVALID.getType());
        this.getBaseMapper().updateById(medsRemindDO);
    }

    @Override
    public MedsRemindDTO getMedsRemindDetail(Long currentUserId, Long id) {

        // 1、获取提醒
        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindDO::getId, id);
        MedsRemindDO medsRemindDO = this.getBaseMapper().selectOne(wrapper);

        MedsRemindDTO medsRemindDTO = PojoUtils.map(medsRemindDO, MedsRemindDTO.class);

        // 2、设置编辑按钮
        if (medsRemindDO.getCreateUser().compareTo(currentUserId) == 0) {
            medsRemindDTO.setShowEditButton(true);
        }

        // 3、设置提醒时间
        List<MedsRemindTimeDO> remindTimeDOList = this.medsRemindTimeService.selectByMedsRemindIdList(Collections.singletonList(id));
        List<String> remindTimeList = remindTimeDOList.stream().map(MedsRemindTimeDO::getRemindTime).collect(Collectors.toList());
        medsRemindDTO.setMedsRemindTimeList(remindTimeList);

        // 4、设置用户头像
        List<MedsRemindUserDO> allByMedsIdList = medsRemindUserService.getAllByMedsIdList(Collections.singletonList(id));
        List<Long> userIdList = allByMedsIdList.stream().filter(item -> HmcRemindStatusEnum.VALID.getType().equals(item.getRemindStatus()))
                .map(MedsRemindUserDO::getCreateUser).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(userIdList)) {
            List<HmcUser> hmcUserList = hmcUserApi.listByIds(userIdList);
            List<String> avatarUrlList = hmcUserList.stream().map(HmcUser::getAvatarUrl).collect(Collectors.toList());
            medsRemindDTO.setSubAvatarUrlList(avatarUrlList);
        }

        // 5、查看当前用户是否接受提醒
        if (currentUserId > 0) {
            medsRemindDTO.setSubFlag(allByMedsIdList.stream().anyMatch(
                    item -> item.getCreateUser().equals(currentUserId) && HmcRemindStatusEnum.VALID.getType().equals(item.getRemindStatus()))
            );
        }

        // 判断是否过期 -> 设为无效
        if (DateUtil.date().compareTo(medsRemindDTO.getExpireDate()) > 0) {
            medsRemindDTO.setRemindStatus(HmcRemindStatusEnum.INVALID.getType());
        }

        return medsRemindDTO;
    }

    @Override
    public boolean cancelRemind(MedsRemindBaseRequest request) {

        MedsRemindDO medsRemindDO = this.getById(request.getId());

        // 1、删除历史用药提醒
        cancelHistoryMedsRemindTask(request.getOpUserId(), Arrays.asList(request.getOpUserId()), medsRemindDO);

        // 2、更新提醒用户状态
        deleteMedsRemindUser(medsRemindDO.getId(), request.getOpUserId());

        return Boolean.TRUE;
    }

    /**
     * 更新用户状态
     *
     * @param medsRemindId
     * @param userId
     */
    private void updateMedsRemindUser(Long medsRemindId, Long userId) {
        this.medsRemindUserService.updateMedsRemindUser(medsRemindId, userId);
    }

    /**
     * 删除用户
     *
     * @param medsRemindId
     * @param userId
     */
    private void deleteMedsRemindUser(Long medsRemindId, Long userId) {
        this.medsRemindUserService.deleteMedsRemindUser(medsRemindId, userId);
    }

    @Override
    public List<MedsRemindTaskDetailDTO> todayRemind(Long currentUserId) {

        // 1、获取今日提醒任务
        List<MedsRemindTaskDetailDO> todayRemindTaskList = this.medsRemindTaskDetailService.getTodayRemindTaskDetail(currentUserId);
        if (CollUtil.isEmpty(todayRemindTaskList)) {
            log.info("获取今日提醒任务为空");
            return Lists.newArrayList();
        }
        List<Long> medsRemindIdList = todayRemindTaskList.stream().map(MedsRemindTaskDetailDO::getMedsRemindId).distinct().collect(Collectors.toList());
        List<Long> receiveUserIdList = todayRemindTaskList.stream().map(MedsRemindTaskDetailDO::getReceiveUserId).distinct().collect(Collectors.toList());

        // 根据用户id获取用户
        List<HmcUser> hmcUserList = hmcUserApi.listByIds(receiveUserIdList);

        // 把用户分组
        Map<Long, HmcUser> userDTOMap = hmcUserList.stream().collect(Collectors.toMap(HmcUser::getUserId, o -> o, (k1, k2) -> k1));

        // 2、完善任务信息
        List<MedsRemindDO> medsRemindList = this.getMedsRemind(medsRemindIdList);

        Map<Long, MedsRemindDO> medsRemindIdMap = medsRemindList.stream().collect(Collectors.toMap(MedsRemindDO::getId, o -> o, (k1, k2) -> k1));

        List<MedsRemindTaskDetailDTO> result = Lists.newArrayList();

        for (MedsRemindTaskDetailDO item : todayRemindTaskList) {
            MedsRemindTaskDetailDTO taskDetailDTO = PojoUtils.map(item, MedsRemindTaskDetailDTO.class);
            taskDetailDTO.setGoodsName(medsRemindIdMap.get(item.getMedsRemindId()).getGoodsName());
            taskDetailDTO.setUseAmount(medsRemindIdMap.get(item.getMedsRemindId()).getUseAmount());
            taskDetailDTO.setUseAmountUnit(medsRemindIdMap.get(item.getMedsRemindId()).getUseAmountUnit());
            taskDetailDTO.setUseTimesType(medsRemindIdMap.get(item.getMedsRemindId()).getUseTimesType());
            taskDetailDTO.setAvatarUrl(Optional.ofNullable(userDTOMap.get(item.getReceiveUserId())).map(HmcUser::getAvatarUrl).orElse(null));

            result.add(taskDetailDTO);
        }

        return result;
    }

    @Override
    public void generateDailyMedsRemindTask() {

        // 1、获取所有的有效提醒数量
        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindDO::getRemindStatus, HmcRemindStatusEnum.VALID.getType());
        wrapper.lambda().ge(MedsRemindDO::getExpireDate, DateUtil.date());
        int current = 1;
        QueryPageListRequest request = new QueryPageListRequest();
        request.setCurrent(current);
        request.setSize(50);
        Page<MedsRemindDO> page = this.page(request.getPage(), wrapper);
        log.info("[generateDailyMedsRemindTask]获取提醒任务设置结果：{}", JSONUtil.toJsonStr(page));

        // 2、处理结果集
        while (CollUtil.isNotEmpty(page.getRecords())) {
            List<MedsRemindDO> records = page.getRecords();
            log.info("获取结果：{}", JSONUtil.toJsonStr(records));
            records.stream().forEach(item -> {

                // 2.1、获取所有提醒用户
                List<Long> userIdList = this.medsRemindUserService.getAllByMedsId(item.getId()).stream().map(MedsRemindUserDO::getCreateUser).collect(Collectors.toList());

                // 2.2、构建今日用药提醒
                List<MedsRemindResult> medsRemindResults = buildTodayMedsRemindTask(item, HmcMedsRemindCreateSourceEnum.JOB_START);

                // 2.3、保存今日用药提醒
                saveMedsRemindTask(medsRemindResults, userIdList);

            });

            // 这里继续查询下一页
            request.setCurrent(++current);
            page = this.page(request.getPage(), wrapper);
        }

        log.info("[generateDailyMedsRemindTask]处理完成");

    }

    @Override
    public List<MedsRemindDTO> medsHistory(Long currentUserId) {
        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindDO::getCreateUser, currentUserId);
        wrapper.lambda().eq(MedsRemindDO::getClearStatus, HmcMedsClearStatusEnum.UN_CLEARED.getType());
        wrapper.lambda().orderByDesc(MedsRemindDO::getCreateTime);
        wrapper.last(" limit 10");
        List<MedsRemindDO> remindList = this.getBaseMapper().selectList(wrapper);
        return PojoUtils.map(remindList, MedsRemindDTO.class);
    }

    @Override
    public boolean clearHistory(Long currentUserId) {

        QueryWrapper<MedsRemindDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindDO::getCreateUser, currentUserId);
        wrapper.lambda().eq(MedsRemindDO::getClearStatus, HmcMedsClearStatusEnum.UN_CLEARED.getType());

        MedsRemindDO remind = new MedsRemindDO();
        remind.setClearStatus(HmcMedsClearStatusEnum.CLEARED.getType());
        remind.setUpdateUser(currentUserId);

        return this.update(remind, wrapper);
    }
}
