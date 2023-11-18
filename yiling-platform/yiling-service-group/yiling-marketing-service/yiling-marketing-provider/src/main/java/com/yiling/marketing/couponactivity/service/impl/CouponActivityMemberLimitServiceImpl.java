package com.yiling.marketing.couponactivity.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityMemberLimitDO;
import com.yiling.marketing.couponactivity.dao.CouponActivityMemberLimitMapper;
import com.yiling.marketing.couponactivity.service.CouponActivityMemberLimitService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetEnterpriseLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 优惠券活动关联vip会员表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-08-10
 */
@Service
@Slf4j
public class CouponActivityMemberLimitServiceImpl extends BaseServiceImpl<CouponActivityMemberLimitMapper, CouponActivityMemberLimitDO> implements CouponActivityMemberLimitService {
}
