package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveUseRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.entity.IntegralGiveUseRecordDO;
import com.yiling.user.integral.dao.IntegralGiveUseRecordMapper;
import com.yiling.user.integral.enums.IntegralBehaviorEnum;
import com.yiling.user.integral.enums.IntegralRecordTypeEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分发放/扣减记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralGiveUseRecordServiceImpl extends BaseServiceImpl<IntegralGiveUseRecordMapper, IntegralGiveUseRecordDO> implements IntegralGiveUseRecordService {

    @Autowired
    StaffService staffService;
    @Autowired
    UserService userService;

    @Override
    public Page<IntegralGiveUseRecordBO> queryListPage(QueryIntegralRecordRequest request) {
        QueryWrapper<IntegralGiveUseRecordDO> wrapper = WrapperUtils.getWrapper(request);
        if (StrUtil.isNotEmpty(request.getMobile())) {
            Staff staff = staffService.getByMobile(request.getMobile());
            if (Objects.nonNull(staff)) {
                wrapper.lambda().eq(IntegralGiveUseRecordDO::getCreateUser, staff.getId());
            }
        }
        wrapper.lambda().orderByDesc(IntegralGiveUseRecordDO::getCreateTime, IntegralGiveUseRecordDO::getId);

        Page<IntegralGiveUseRecordDO> giveUseRecordDOPage = this.page(request.getPage(), wrapper);
        List<Long> userIdList = giveUseRecordDOPage.getRecords().stream().map(IntegralGiveUseRecordDO::getCreateUser).distinct().collect(Collectors.toList());

        Page<IntegralGiveUseRecordBO> recordBOPage = PojoUtils.map(giveUseRecordDOPage, IntegralGiveUseRecordBO.class);
        if (CollUtil.isNotEmpty(userIdList)) {
            Map<Long, UserDO> userMap = userService.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));
            recordBOPage.getRecords().forEach(integralGiveUseRecordBO -> integralGiveUseRecordBO.setMobile(userMap.getOrDefault(integralGiveUseRecordBO.getCreateUser(), new UserDO()).getMobile()));
        }

        return recordBOPage;
    }

    @Override
    public List<IntegralGiveUseRecordDTO> queryList(QueryIntegralGiveUseRecordRequest request) {
        QueryWrapper<IntegralGiveUseRecordDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.list(wrapper), IntegralGiveUseRecordDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntegralGiveUseRecordDO addRecord(AddIntegralRecordRequest request) {
        // 用户积分变更类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
        IntegralGiveUseRecordDO recordDO = PojoUtils.map(request, IntegralGiveUseRecordDO.class);
        recordDO.setOperTime(new Date());

        // 操作备注（订单送积分：订单号；签到送积分：天数；参与活动消耗：活动ID；退货扣减：订单号；兑换消耗：兑换单号）
        switch (Objects.requireNonNull(UserIntegralChangeTypeEnum.getByCode(request.getChangeType()))) {
            case ORDER_GIVE_INTEGRAL:
                recordDO.setType(1);
                recordDO.setOpRemark("订单号：" + request.getOpRemark());
                break;
            case SIGN_GIVE_INTEGRAL:
                recordDO.setType(1);
                if (Objects.nonNull(request.getContinueSignFlag()) && request.getContinueSignFlag()) {
                    recordDO.setOpRemark("第"+ request.getOpRemark() + "天（连签）");
                } else {
                    recordDO.setOpRemark("第"+ request.getOpRemark() + "天");
                }
                break;
            case JOIN_ACTIVITY_USE:
                recordDO.setType(2);
                recordDO.setIntegralValue(-request.getIntegralValue());
                recordDO.setOpRemark("活动ID："+ request.getOpRemark());
                break;
            case EXCHANGE_USE:
                recordDO.setType(2);
                recordDO.setIntegralValue(-request.getIntegralValue());
                recordDO.setOpRemark("兑换单号："+ request.getOpRemark());
                break;
            case REFUND_REDUCE:
                recordDO.setType(2);
                recordDO.setIntegralValue(-request.getIntegralValue());
                recordDO.setOpRemark("订单号："+ request.getOpRemark());
                break;
            case EXPIRED_INVALID:
                recordDO.setType(2);
                recordDO.setIntegralValue(-request.getIntegralValue());
            default:
                break;
        }

        this.save(recordDO);
        return recordDO;
    }

}
