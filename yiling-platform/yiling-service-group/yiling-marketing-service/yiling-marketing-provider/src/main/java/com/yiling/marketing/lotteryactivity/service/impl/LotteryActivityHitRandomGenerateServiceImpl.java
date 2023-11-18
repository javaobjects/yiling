package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityListRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityHitRandomGenerateDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityHitRandomGenerateMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityProgressEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityHitRandomGenerateService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.util.GenerateNameUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动随机生成中奖名单表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
@Slf4j
@Service
public class LotteryActivityHitRandomGenerateServiceImpl extends BaseServiceImpl<LotteryActivityHitRandomGenerateMapper, LotteryActivityHitRandomGenerateDO> implements LotteryActivityHitRandomGenerateService {

    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateHitTask() {
        log.info("抽奖活动随机生成用户昵称数据开始执行");
        // 获取C端的抽奖活动
        QueryLotteryActivityListRequest request = new QueryLotteryActivityListRequest();
        request.setStatus(EnableStatusEnum.ENABLED.getCode());
        request.setProgress(LotteryActivityProgressEnum.GOING.getCode());
        List<LotteryActivityDTO> activityDTOList = lotteryActivityService.queryList(request).stream().filter(lotteryActivityDTO -> !lotteryActivityDTO.getPlatform().equals(LotteryActivityPlatformEnum.B2B.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(activityDTOList)) {
            return;
        }

        // 循环所有C端进行中的抽奖活动
        activityDTOList.forEach(lotteryActivityDTO -> {

            // 生成随机中奖数量：生成的数量范围0-20，分两批生成，一批中文一批英文
            List<String> nameList = ListUtil.toList();
            int randomHit = RandomUtil.randomInt(0, 10);
            if (randomHit > 0) {
                for (int i=0; i< randomHit; i++) {
                    String nickName = GenerateNameUtil.getNickName();
                    nameList.add(nickName);
                }
            }
            int randomEnHit = RandomUtil.randomInt(0, 10);
            if (randomEnHit > 0) {
                for (int i=0; i< randomEnHit; i++) {
                    String nickName = GenerateNameUtil.getEnglishRandom(RandomUtil.randomInt(8, 15));
                    nameList.add(nickName);
                }
            }

            // 获取该抽奖活动的实物奖品信息
            List<LotteryActivityRewardSettingDTO> rewardSettingDTOS = lotteryActivityRewardSettingService.getByLotteryActivityId(lotteryActivityDTO.getId())
                    .stream().filter(rewardSettingDTO -> rewardSettingDTO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())).collect(Collectors.toList());

            // 将随机生成的中奖者数据入库
            List<LotteryActivityHitRandomGenerateDO> randomGenerateDOList = ListUtil.toList();
            if (CollUtil.isNotEmpty(nameList) && CollUtil.isNotEmpty(rewardSettingDTOS)) {
                nameList.forEach(name -> {
                    // 随机取一个实物奖品
                    LotteryActivityRewardSettingDTO rewardSettingDTO = rewardSettingDTOS.get(RandomUtil.randomInt(0, rewardSettingDTOS.size()));

                    LotteryActivityHitRandomGenerateDO randomGenerateDO = PojoUtils.map(rewardSettingDTO, LotteryActivityHitRandomGenerateDO.class);
                    randomGenerateDO.setUname(name);
                    randomGenerateDO.setLotteryActivityId(lotteryActivityDTO.getId());
                    randomGenerateDO.setPlatformType(2);
                    randomGenerateDOList.add(randomGenerateDO);
                });
                log.info("抽奖活动ID={} 随机生成用户昵称数量={}", lotteryActivityDTO.getId(), nameList.size());
            }

            if (CollUtil.isNotEmpty(randomGenerateDOList)) {
                this.saveBatch(randomGenerateDOList);
            }

        });
        log.info("抽奖活动随机生成用户昵称数据结束 共有{}个抽奖活动随机生成昵称", activityDTOList.size());

    }

    @Override
    public List<LotteryActivityHitRandomGenerateDTO> getCurrentGenerate(Long activityId) {
        Date now = new Date();
        int hour = DateTime.now().hour(true);
        DateTime begin = DateUtil.offsetHour(DateUtil.beginOfDay(now), hour);
        DateTime end = DateUtil.offsetSecond(DateUtil.offsetHour(DateUtil.beginOfDay(now), hour + 1), -1);

        LambdaQueryWrapper<LotteryActivityHitRandomGenerateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityHitRandomGenerateDO::getLotteryActivityId, activityId);
        wrapper.ge(LotteryActivityHitRandomGenerateDO::getCreateTime, begin);
        wrapper.lt(LotteryActivityHitRandomGenerateDO::getCreateTime, end);
        return PojoUtils.map(this.list(wrapper), LotteryActivityHitRandomGenerateDTO.class);
    }
}
