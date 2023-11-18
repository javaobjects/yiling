package com.yiling.user.integral.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.bo.GenerateUserSignRecordBO;
import com.yiling.user.integral.bo.UserSignDetailBO;
import com.yiling.user.integral.bo.UserSignRecordDetailBO;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;
import com.yiling.user.integral.dto.IntegralUserSignRecordDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordTurnPageRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralGiveUseRecordDO;
import com.yiling.user.integral.entity.IntegralUserSignRecordDO;
import com.yiling.user.integral.dao.IntegralUserSignRecordMapper;
import com.yiling.user.integral.entity.UserIntegralDO;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.integral.service.IntegralBehaviorService;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.user.integral.service.IntegralPeriodConfigService;
import com.yiling.user.integral.service.IntegralUserSignRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.UserIntegralService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户签到记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralUserSignRecordServiceImpl extends BaseServiceImpl<IntegralUserSignRecordMapper, IntegralUserSignRecordDO> implements IntegralUserSignRecordService {

    @Autowired
    IntegralPeriodConfigService integralPeriodConfigService;
    @Autowired
    UserIntegralService userIntegralService;
    @Autowired
    IntegralGiveUseRecordService integralGiveUseRecordService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    IntegralGiveRuleService integralGiveRuleService;
    @Autowired
    IntegralBehaviorService integralBehaviorService;

    @Override
    public List<IntegralUserSignRecordDTO> getUserIntegralRecord(QueryUserSignRecordRequest request) {
        QueryWrapper<IntegralUserSignRecordDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().orderByAsc(IntegralUserSignRecordDO::getSignTime);
        return PojoUtils.map(this.list(wrapper), IntegralUserSignRecordDTO.class);
    }

    @Override
    public List<GenerateUserSignRecordBO> generateUserSignData(QueryUserSignRecordRequest request) {
        // 获取签到周期配置
        List<IntegralPeriodConfigDTO> periodConfigList = integralPeriodConfigService.getIntegralPeriodConfigList(request.getGiveRuleId());
        // 判断当日是否已经签到
        boolean signFlag = this.getTodaySignFlag(request.getGiveRuleId(), request.getPlatform(), request.getUid());
        if (!signFlag) {
            // 计算签到数据进行签到
            this.sign(request, periodConfigList);
        }

        // 获取用户积分签到记录
        Map<Date, IntegralUserSignRecordDTO> map = this.getDateIntegralUserSignRecordDTOMap(request);

        // 当月是哪个月
        int currentMonth = DateUtil.thisMonth() + 1;
        // 当月一共多少天
        int lengthOfMonth = DateUtil.lengthOfMonth(currentMonth, DateUtil.isLeapYear(DateUtil.year(new Date())));
        // 今天是这个月的第多少天(1月31号返回31)
        int thisDayOfMonth = DateUtil.thisDayOfMonth();
        // 今天
        Date today = DateUtil.beginOfDay(new Date());

        List<GenerateUserSignRecordBO> generateUserSignList = ListUtil.toList();

        // 当前天数据处理
        GenerateUserSignRecordBO userSignRecordBO = new GenerateUserSignRecordBO();
        userSignRecordBO.setSignTime(today);
        IntegralUserSignRecordDTO todaySignRecordDTO = map.get(today);
        userSignRecordBO.setSignIntegral(todaySignRecordDTO.getSignIntegral());
        userSignRecordBO.setSignFlag(2);
        userSignRecordBO.setContinueDays(todaySignRecordDTO.getContinueDays());
        userSignRecordBO.setContinueSignIntegral(todaySignRecordDTO.getContinueSignIntegral());
        generateUserSignList.add(userSignRecordBO);

        // 第一天到当前天的前一天数据处理
        if (thisDayOfMonth > 1) {
            for (int i=1; i < thisDayOfMonth; i++) {
                DateTime date = DateUtil.offsetDay(today, -i);
                IntegralUserSignRecordDTO userSignRecordDTO = map.get(date);

                // 构建用户签到日历记录
                GenerateUserSignRecordBO signRecordBO = new GenerateUserSignRecordBO();
                signRecordBO.setSignTime(date);

                if (Objects.nonNull(userSignRecordDTO)) {
                    signRecordBO.setSignIntegral(userSignRecordDTO.getSignIntegral());
                    signRecordBO.setSignFlag(2);
                    signRecordBO.setContinueDays(userSignRecordDTO.getContinueDays());
                    signRecordBO.setContinueSignIntegral(userSignRecordDTO.getContinueSignIntegral());
                } else {
                    signRecordBO.setSignFlag(1);
                }
                generateUserSignList.add(signRecordBO);

            }
        }

        // 当前天后面一天开始到当月最后一天数据处理
        if (thisDayOfMonth != lengthOfMonth) {
            // 当前连续签到天数
            Integer continueDays = todaySignRecordDTO.getContinueDays();
            // 明日
            int featureDays = thisDayOfMonth + 1;
            // 下一天为连续签到的第几天
            int tomorrowContinueDays = continueDays == periodConfigList.size() ? 1 : continueDays + 1;

            // 当前天的下一天作为开始，故k从1开始
            int k = 1;
            for (int i=featureDays; i <= lengthOfMonth; i++) {
                DateTime date = DateUtil.offsetDay(today, k);
                for (int j=0; j <= periodConfigList.size(); j++) {
                    IntegralPeriodConfigDTO configDTO = periodConfigList.get(j);
                    if (tomorrowContinueDays == configDTO.getDays()) {
                        GenerateUserSignRecordBO signRecordBO = new GenerateUserSignRecordBO();
                        signRecordBO.setSignTime(date);
                        signRecordBO.setSignIntegral(configDTO.getCurrentDayIntegral());
                        signRecordBO.setSignFlag(3);
                        signRecordBO.setContinueDays(configDTO.getDays());
                        signRecordBO.setContinueSignIntegral(configDTO.getContinuousReward());
                        generateUserSignList.add(signRecordBO);

                        tomorrowContinueDays++;
                        break;
                    }
                }

                // 如果为最后一天，那么又重置为第一天
                if (tomorrowContinueDays == periodConfigList.size() + 1) {
                    tomorrowContinueDays = 1;
                }
                k++;

            }

        }

        // 根据签到日期升序
        return generateUserSignList.stream().sorted(Comparator.comparing(GenerateUserSignRecordBO::getSignTime)).collect(Collectors.toList());
    }

    /**
     * 获取用户积分签到记录
     *
     * @param request
     * @return
     */
    private Map<Date, IntegralUserSignRecordDTO> getDateIntegralUserSignRecordDTOMap(QueryUserSignRecordRequest request) {
        // 获取用户积分签到记录
        QueryUserSignRecordRequest recordRequest = new QueryUserSignRecordRequest();
        recordRequest.setPlatform(request.getPlatform());
        recordRequest.setUid(request.getUid());
        recordRequest.setGiveRuleId(request.getGiveRuleId());
        recordRequest.setStartSignTime(request.getStartSignTime());
        recordRequest.setEndSignTime(request.getEndSignTime());
        List<IntegralUserSignRecordDTO> userSignRecordDTOList = this.getUserIntegralRecord(recordRequest);
        return userSignRecordDTOList.stream().collect(Collectors.toMap(IntegralUserSignRecordDTO::getSignTime, Function.identity(), (k1, k2) -> k2));
    }

    /**
     * 计算签到数据
     *
     * @param request
     * @return
     */
    private void sign(QueryUserSignRecordRequest request, List<IntegralPeriodConfigDTO> periodConfigList) {

        // 获取当前是第多少天签到，签到奖励多少积分
        DateTime today = DateUtil.beginOfDay(new Date());

        IntegralPeriodConfigDTO periodConfigDTO = this.getTodayPeriodConfigDTO(request, periodConfigList, today);

        if (Objects.isNull(periodConfigDTO)) {
            throw new BusinessException(UserErrorCode.INTEGRAL_USER_SIGN_ERROR);
        }
        // 连签天数、当天发放积分数、连签奖励
        Integer continueDays = periodConfigDTO.getDays();
        Integer currentDayIntegral = periodConfigDTO.getCurrentDayIntegral();
        Integer continuousReward = periodConfigDTO.getContinuousReward();

        // 生成签到记录、添加用户积分和用户积分变更记录、添加积分发放记录
        this.insertRecord(request, continueDays, currentDayIntegral, continuousReward);
        log.info("用户UID={} 签到完成 当前签到为第{}天", request.getUid(), continueDays);

    }

    /**
     * 获取今日签到周期配置
     *
     * @param request
     * @param periodConfigList
     * @param today
     * @return
     */
    private IntegralPeriodConfigDTO getTodayPeriodConfigDTO(QueryUserSignRecordRequest request, List<IntegralPeriodConfigDTO> periodConfigList, DateTime today) {
        QueryUserSignRecordRequest recordRequest = new QueryUserSignRecordRequest();
        recordRequest.setPlatform(request.getPlatform());
        recordRequest.setUid(request.getUid());
        recordRequest.setGiveRuleId(request.getGiveRuleId());
        List<IntegralUserSignRecordDTO> userSignRecordDTOList = this.getUserIntegralRecord(recordRequest);

        IntegralPeriodConfigDTO periodConfigDTO = null;
        // 如果为空，则为第一天
        if (CollUtil.isEmpty(userSignRecordDTOList)) {
            periodConfigDTO = periodConfigList.get(0);

        } else {
            Map<Date, IntegralUserSignRecordDTO> map = userSignRecordDTOList.stream().collect(Collectors.toMap(IntegralUserSignRecordDTO::getSignTime, Function.identity()));

            for (int i = 1; i <= periodConfigList.size(); i++ ) {
                DateTime offsetDay = DateUtil.offsetDay(today, -i);
                // 昨天如果为空，那么直接结束循环
                if (Objects.isNull(map.get(offsetDay)) && i == 1) {
                    periodConfigDTO = periodConfigList.get(0);
                    break;
                } else if (Objects.isNull(map.get(offsetDay))) {
                    periodConfigDTO = periodConfigList.get(i-1);
                    break;
                } else if (Objects.nonNull(map.get(offsetDay)) && i == periodConfigList.size()) {
                    // 7天周期为例：第8天为新的一个周期
                    periodConfigDTO = periodConfigList.get(0);
                    break;
                }
            }

        }

        return periodConfigDTO;
    }

    /**
     * 生成签到记录、添加用户积分和用户积分变更记录、添加积分发放记录
     *
     * @param request
     * @param continueDays
     * @param currentDayIntegral
     * @param continuousReward
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertRecord(QueryUserSignRecordRequest request, Integer continueDays, Integer currentDayIntegral, Integer continuousReward) {
        // 生成签到记录
        IntegralUserSignRecordDO signRecordDO = PojoUtils.map(request, IntegralUserSignRecordDO.class);
        signRecordDO.setContinueDays(continueDays);
        signRecordDO.setSignIntegral(currentDayIntegral);
        signRecordDO.setContinueSignIntegral(continuousReward);
        signRecordDO.setSignTime(DateUtil.beginOfDay(new Date()));
        signRecordDO.setOpUserId(request.getOpUserId());
        this.save(signRecordDO);

        // 添加用户积分和用户积分变更记录
        UpdateIUserIntegralRequest userIntegralRequest = new UpdateIUserIntegralRequest();
        userIntegralRequest.setPlatform(request.getPlatform());
        userIntegralRequest.setUid(request.getUid());
        userIntegralRequest.setIntegralValue(currentDayIntegral + continuousReward);
        userIntegralRequest.setChangeType(UserIntegralChangeTypeEnum.SIGN_GIVE_INTEGRAL.getCode());
        userIntegralRequest.setOpUserId(request.getOpUserId());
        userIntegralService.updateIntegral(userIntegralRequest);

        // 添加签到的积分发放记录
        AddIntegralRecordRequest integralRecordRequest = new AddIntegralRecordRequest();
        integralRecordRequest.setPlatform(request.getPlatform());
        integralRecordRequest.setUid(request.getUid());
        if (request.getPlatform().equals(IntegralRulePlatformEnum.B2B.getCode())) {
            integralRecordRequest.setUname(Optional.ofNullable(enterpriseService.getById(request.getUid())).orElse(new EnterpriseDO()).getName());
        }
        integralRecordRequest.setChangeType(UserIntegralChangeTypeEnum.SIGN_GIVE_INTEGRAL.getCode());
        // 连签奖励记录
        if (continuousReward > 0) {
            integralRecordRequest.setContinueSignFlag(true);
        }
        integralRecordRequest.setIntegralValue(currentDayIntegral + continuousReward);
        integralRecordRequest.setOpRemark(continueDays.toString());
        integralRecordRequest.setRuleId(request.getGiveRuleId());
        IntegralGiveRuleDO giveRuleDO = Optional.ofNullable(integralGiveRuleService.getById(request.getGiveRuleId())).orElse(new IntegralGiveRuleDO());
        integralRecordRequest.setRuleName(giveRuleDO.getName());
        integralRecordRequest.setBehaviorId(giveRuleDO.getBehaviorId());
        integralRecordRequest.setBehaviorName(Optional.ofNullable(integralBehaviorService.getById(giveRuleDO.getBehaviorId())).orElse(new IntegralBehaviorDO()).getName());
        integralRecordRequest.setOpUserId(request.getOpUserId());
        integralGiveUseRecordService.addRecord(integralRecordRequest);
    }

    @Override
    public boolean getTodaySignFlag(Long giveRuleId, Integer platform, Long uid) {
        LambdaQueryWrapper<IntegralUserSignRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralUserSignRecordDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralUserSignRecordDO::getPlatform, platform);
        wrapper.eq(IntegralUserSignRecordDO::getUid, uid);
        wrapper.eq(IntegralUserSignRecordDO::getSignTime, DateUtil.beginOfDay(new Date()));
        wrapper.last("limit 1");
        return Objects.nonNull(this.getOne(wrapper));
    }

    @Override
    public List<GenerateUserSignRecordBO> userSignRecordTurnPage(QueryUserSignRecordTurnPageRequest request) {
        List<GenerateUserSignRecordBO> list = ListUtil.toList();
        // 获取用户积分签到记录
        QueryUserSignRecordRequest recordRequest = new QueryUserSignRecordRequest();
        recordRequest.setPlatform(request.getPlatform());
        recordRequest.setUid(request.getUid());
        recordRequest.setGiveRuleId(request.getGiveRuleId());
        Map<Date, IntegralUserSignRecordDTO> map = this.getDateIntegralUserSignRecordDTOMap(recordRequest);

        // 当前月份
        int currentMonth = DateUtil.thisMonth() + 1;
        // 当前年份
        int currentYear = DateUtil.year(new Date());
        // 获取需要查看的年份
        int year = request.getYear();
        if (request.getMonth() == 1 && request.getTurnType() == 1) {
            year = request.getYear() - 1;
        } else if (request.getMonth() == 12 && request.getTurnType() == 2) {
            year = request.getYear() + 1;
        }
        // 获取需要查看的月份
        int month = request.getMonth();
        if (request.getTurnType() == 1) {
            if (request.getMonth() == 1) {
                month = 12;
            } else {
                month = request.getMonth() - 1;
            }

        } else if (request.getTurnType() == 2) {
            if (request.getMonth() == 12) {
                month = 1;
            } else {
                month = request.getMonth() + 1;
            }
        }

        // 查看未来的月份
        if (request.getTurnType() == 2 && request.getMonth() >= currentMonth && currentYear == request.getYear()) {
            // 校验只能往后的一个月
            if (request.getMonth() != currentMonth) {
                throw new BusinessException(UserErrorCode.INTEGRAL_SIGN_DETAIL_LOOK_ERROR);
            }

            // 获取签到周期配置
            List<IntegralPeriodConfigDTO> periodConfigList = integralPeriodConfigService.getIntegralPeriodConfigList(request.getGiveRuleId());
            // 今天是这个月的第多少天(1月31号返回31)
            int thisDayOfMonth = DateUtil.thisDayOfMonth();
            // 今天
            Date today = DateUtil.beginOfDay(new Date());
            // 当月一共多少天
            int lengthOfMonth = DateUtil.lengthOfMonth(currentMonth, DateUtil.isLeapYear(DateUtil.year(new Date())));
            // 获取用户积分签到记录
            IntegralUserSignRecordDTO todaySignRecordDTO = map.get(today);

            // 当前连续签到天数
            Integer continueDays = todaySignRecordDTO.getContinueDays();
            // 明日
            int featureDays = thisDayOfMonth + 1;
            // 下一天为连续签到的第几天
            int tomorrowContinueDays = continueDays == periodConfigList.size() ? 1 : continueDays + 1;

            // 循环完当月的，得出当月最后一一天的连签日数据
            for (int i=featureDays; i <= lengthOfMonth; i++) {
                for (int j=0; j <= periodConfigList.size(); j++) {
                    IntegralPeriodConfigDTO configDTO = periodConfigList.get(j);
                    if (tomorrowContinueDays == configDTO.getDays()) {
                        tomorrowContinueDays++;
                        break;
                    }
                }

                // 如果为最后一天，那么又重置为第一天
                if (tomorrowContinueDays == periodConfigList.size() + 1) {
                    tomorrowContinueDays = 1;
                }
            }

            // 真正生成未来月的数据
            list = this.generateFeatureMonth(periodConfigList, month, tomorrowContinueDays);


        } else if (currentYear == year && currentMonth == month ) {
            // 查看本月
            QueryUserSignRecordRequest record = new QueryUserSignRecordRequest();
            record.setGiveRuleId(request.getGiveRuleId());
            record.setPlatform(request.getPlatform());
            record.setUid(request.getUid());
            list = this.generateUserSignData(record);

        } else {
            // 查看历史已经过去的月份
            // 该月一共多少天
            int lengthOfMonth = DateUtil.lengthOfMonth(month, DateUtil.isLeapYear(year));

            String dateStr = year + "-" + month + "-" + "01";
            DateTime now = DateUtil.parse(dateStr, "yyyy-MM-dd");

            for (int i=1; i <= lengthOfMonth; i++) {
                int instance = i-1;
                DateTime date = DateUtil.beginOfDay(DateUtil.offsetDay(now, +instance));
                IntegralUserSignRecordDTO userSignRecordDTO = map.get(date);

                // 构建用户签到日历记录
                GenerateUserSignRecordBO signRecordBO = new GenerateUserSignRecordBO();
                signRecordBO.setSignTime(date);
                signRecordBO.setSignFlag(1);

                if (Objects.nonNull(userSignRecordDTO)) {
                    signRecordBO.setSignIntegral(userSignRecordDTO.getSignIntegral());
                    signRecordBO.setSignFlag(2);
                    signRecordBO.setContinueDays(userSignRecordDTO.getContinueDays());
                    signRecordBO.setContinueSignIntegral(userSignRecordDTO.getContinueSignIntegral());
                }
                list.add(signRecordBO);
            }


        }

        return list;
    }

    /**
     * 生成未来月签到数据（例如：当前为2023年2月1日，那么生成的是2023年3月的数据）
     *
     * @param periodConfigList 周期配置信息
     * @param featureMonth 未来的月份
     * @param tomorrowContinueDay 本次处理开始的第一天为连续签到的第几天（1号开始）
     * @return
     */
    private List<GenerateUserSignRecordBO> generateFeatureMonth(List<IntegralPeriodConfigDTO> periodConfigList, int featureMonth, int tomorrowContinueDay) {
        // 未来月一共多少天
        int lengthOfMonth = DateUtil.lengthOfMonth(featureMonth, DateUtil.isLeapYear(DateUtil.year(new Date())));
        // 下一天为连续签到的第几天
        int tomorrowContinueDays = tomorrowContinueDay;
        // 今天
        Date today = DateUtil.beginOfDay(DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date()), 1));

        List<GenerateUserSignRecordBO> generateUserSignList = ListUtil.toList();
        for (int i=1; i <= lengthOfMonth; i++) {
            DateTime date = DateUtil.offsetDay(today, i-1);
            for (int j=0; j <= periodConfigList.size(); j++) {
                IntegralPeriodConfigDTO configDTO = periodConfigList.get(j);
                if (tomorrowContinueDays == configDTO.getDays()) {
                    GenerateUserSignRecordBO signRecordBO = new GenerateUserSignRecordBO();
                    signRecordBO.setSignTime(date);
                    signRecordBO.setSignIntegral(configDTO.getCurrentDayIntegral());
                    signRecordBO.setSignFlag(3);
                    signRecordBO.setContinueDays(configDTO.getDays());
                    signRecordBO.setContinueSignIntegral(configDTO.getContinuousReward());
                    generateUserSignList.add(signRecordBO);

                    tomorrowContinueDays++;
                    break;
                }
            }

            // 如果为最后一天，那么又重置为第一天
            if (tomorrowContinueDays == periodConfigList.size() + 1) {
                tomorrowContinueDays = 1;
            }
        }

        return generateUserSignList;
    }

    @Override
    public UserSignRecordDetailBO getSignDetail(Long giveRuleId, Integer platform, Long uid) {
        UserSignRecordDetailBO recordDetailBO = new UserSignRecordDetailBO();
        // 用户积分
        Integer userIntegral = userIntegralService.getUserIntegralByUid(uid, platform);
        recordDetailBO.setIntegralValue(userIntegral);
        // 今日是否已经签到
        boolean todaySignFlag = this.getTodaySignFlag(giveRuleId, platform, uid);
        recordDetailBO.setTodaySignFlag(!todaySignFlag);
        // 查询出昨日是连续签到的第几天，推出今日
        LambdaQueryWrapper<IntegralUserSignRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralUserSignRecordDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralUserSignRecordDO::getPlatform, platform);
        wrapper.eq(IntegralUserSignRecordDO::getUid, uid);
        wrapper.eq(IntegralUserSignRecordDO::getSignTime, DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), -1));
        wrapper.last("limit 1");
        IntegralUserSignRecordDO userSignRecordDO = this.getOne(wrapper);
        if (Objects.nonNull(userSignRecordDO)) {
            recordDetailBO.setContinueDays(userSignRecordDO.getContinueDays() + 1);
        } else {
            recordDetailBO.setContinueDays(1);
        }
        // 获取今日签到周期配置
        List<IntegralPeriodConfigDTO> periodConfigList = integralPeriodConfigService.getIntegralPeriodConfigList(giveRuleId);
        QueryUserSignRecordRequest recordRequest = new QueryUserSignRecordRequest();
        recordRequest.setPlatform(platform);
        recordRequest.setUid(uid);
        recordRequest.setGiveRuleId(giveRuleId);
        IntegralPeriodConfigDTO todayPeriodConfigDTO = this.getTodayPeriodConfigDTO(recordRequest, periodConfigList, DateUtil.beginOfDay(new Date()));
        if (Objects.nonNull(todayPeriodConfigDTO)) {
            recordDetailBO.setTodaySignIntegral(todayPeriodConfigDTO.getCurrentDayIntegral() + todayPeriodConfigDTO.getContinuousReward());
        }

        return recordDetailBO;
    }

    @Override
    public List<UserSignDetailBO> getSignDetailByRecordId(Long giveRecordId) {
        IntegralGiveUseRecordDO giveUseRecordDO = integralGiveUseRecordService.getById(giveRecordId);

        LambdaQueryWrapper<IntegralUserSignRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralUserSignRecordDO::getPlatform, giveUseRecordDO.getPlatform());
        wrapper.eq(IntegralUserSignRecordDO::getUid, giveUseRecordDO.getUid());
        wrapper.eq(IntegralUserSignRecordDO::getSignTime, DateUtil.beginOfDay(giveUseRecordDO.getOperTime()));
        IntegralUserSignRecordDO signRecordDO = this.getOne(wrapper);

        List<UserSignDetailBO> list = ListUtil.toList();
        if (Objects.nonNull(signRecordDO)) {
            IntegralGiveRuleDO giveRuleDO = integralGiveRuleService.getById(signRecordDO.getGiveRuleId());
            UserSignDetailBO signDetailBO = new UserSignDetailBO(signRecordDO.getGiveRuleId(), giveRuleDO.getName(), signRecordDO.getSignIntegral());
            list.add(signDetailBO);

            if (signRecordDO.getContinueSignIntegral() != 0) {
                UserSignDetailBO conSignDetailBO = new UserSignDetailBO(signRecordDO.getGiveRuleId(), giveRuleDO.getName(), signRecordDO.getContinueSignIntegral());
                list.add(conSignDetailBO);
            }
        }

        return list;
    }

}
