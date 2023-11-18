package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.marketing.common.enums.PromotionErrorCode;
import com.yiling.marketing.promotion.enums.PromotionBearTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionEffectTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionPermittedTypeCode;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动保存form
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionSaveForm", description = "促销活动保存form")
public class PromotionSaveForm extends BaseForm {

    @ApiModelProperty(value = "促销活动id")
    private Long                                   id;

    @ApiModelProperty(value = "促销活动信息")
    @NotNull(message = "促销活动信息不能为空")
    private PromotionActivitySaveForm              activity;

    @ApiModelProperty(value = "促销活动-秒杀&特价信息")
    private PromotionSecKillSpecialSaveForm        secKillSpecial;

    @ApiModelProperty(value = "促销活动企业")
    @NotNull(message = "促销活动企业不能为空")
    private List<PromotionEnterpriseLimitSaveForm> enterpriseLimitList;

    @ApiModelProperty(value = "促销活动商品")
    @NotNull(message = "促销活动商品不能为空")
    private List<PromotionGoodsLimitSaveForm>      goodsLimitList;

    @ApiModelProperty(value = "促销活动赠品")
    private List<PromotionGoodsGiftLimitSaveForm>  goodsGiftLimit;

    @ApiModelProperty(value = "商家赠品名称")
    private String                                 goodsGiftName;

    @ApiModelProperty(value = "组合包基本信息")
    private PromotionCombinationPackageSaveForm  combinationPackage;

    /**
     * 参数校验
     */
    public void check() {

        // 1、校验销售渠道
        if (CollUtil.isEmpty(this.activity.getPlatformSelected())) {
            throw new BusinessException(PromotionErrorCode.PLATFORM_SELECTED);
        }

        // 2、校验参与活动企业
        if (CollectionUtils.isEmpty(this.enterpriseLimitList)) {
            throw new BusinessException(PromotionErrorCode.ENTERPRISE_NOT_ACTIVITY);
        }

        // 3、校验企业 -> 满赠只能有一个企业
        if (PromotionTypeEnum.isFullGift(this.activity.getType()) && this.enterpriseLimitList.size() > 1) {
            throw new BusinessException(PromotionErrorCode.ENTERPRISE_ONLY_ONE);
        }

        // 4、校验满赠 -> 赠品
        if (PromotionTypeEnum.FULL_GIFT.getType().equals(this.activity.getType()) && CollUtil.isEmpty(this.goodsGiftLimit)
            && StringUtils.isEmpty(this.goodsGiftName)) {
            throw new BusinessException(PromotionErrorCode.GIFT_NOT_ACTIVITY);
        }

        // 5、校验允许购买企业类型
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(this.activity.getType())
            && PromotionPermittedTypeCode.PART.getCode().equals(this.secKillSpecial.getPermittedEnterpriseType())
            && CollUtil.isEmpty(this.secKillSpecial.getPermittedEnterpriseDetail())) {
            throw new BusinessException(PromotionErrorCode.PERMITTED_ENTERPRISE_DETAIL);
        }

        // 6、校验活动编码
        if (StringUtils.isNotBlank(this.activity.getPromotionCode()) && this.activity.getPromotionCode().length() > 20) {
            throw new BusinessException(PromotionErrorCode.PROMOTION_CODE_TOO_LONG);
        }

        // 7、校验分摊填写完整
        if (PromotionBearTypeEnum.SHARE.getType().equals(this.activity.getBear())
            && (Objects.isNull(this.activity.getPlatformPercent()) || Objects.isNull(this.activity.getMerchantPercent()))) {
            throw new BusinessException(PromotionErrorCode.BEAR_NOT_COMPLETE);
        }

        // 8、校验合是否100%
        if (PromotionBearTypeEnum.SHARE.getType().equals(this.activity.getBear())) {
            BigDecimal platformPercent = this.activity.getPlatformPercent();
            BigDecimal merchantPercent = this.activity.getMerchantPercent();
            if (platformPercent.add(merchantPercent).compareTo(BigDecimal.valueOf(100)) != 0) {
                throw new BusinessException(PromotionErrorCode.BEAR_NOT_100);
            }
        }

        // 9、校验商品是否为空
        if (CollectionUtils.isEmpty(this.goodsLimitList)) {
            throw new BusinessException(PromotionErrorCode.GOODS_EMPTY);
        }

        // 10、如果特价、秒杀 & 立即生效 -> 判断持续时间
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(this.activity.getType())
            && PromotionEffectTypeEnum.NOW.getCode().equals(this.activity.getEffectType())) {
            if (Objects.isNull(this.activity.getLastTime()) || this.activity.getLastTime() <= 0) {
                throw new BusinessException(PromotionErrorCode.LAST_TIME_ERROR);
            }
        }

        // 11、如果特价、秒杀 & 立即生效 -> 设置活动开始、结束时间
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(this.activity.getType())
            && PromotionEffectTypeEnum.NOW.getCode().equals(this.activity.getEffectType())) {
            Calendar instance = Calendar.getInstance();
            this.activity.setBeginTime(instance.getTime());
            instance.add(Calendar.HOUR_OF_DAY, this.activity.getLastTime());
            this.activity.setEndTime(instance.getTime());
        }

        // 12、满赠 -> 活动库存是否合法
        if (PromotionTypeEnum.isFullGift(this.activity.getType())) {
            boolean result = this.goodsGiftLimit.stream().anyMatch(item -> item.getPromotionStock() <= 0);
            if (result) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_STOCK_ERROR);
            }
        }

        // 13、秒杀&特价 -> 允许购买数量
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(this.activity.getType())) {
            boolean result = this.goodsLimitList.stream().anyMatch(item -> item.getAllowBuyCount() <= 0);
            if (result) {
                throw new BusinessException(PromotionErrorCode.ALLOW_BUY_COUNT_ERROR);
            }
        }

        // 14、满赠金额不能重复
        if (PromotionTypeEnum.isFullGift(this.activity.getType())) {
            List<BigDecimal> collect = this.goodsGiftLimit.stream().map(PromotionGoodsGiftLimitSaveForm::getPromotionAmount).distinct()
                .collect(Collectors.toList());
            if (collect.size() != this.goodsGiftLimit.size()) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_AMOUNT_REPEAT);
            }
        }

        // 15、满赠金额大于0
        if (PromotionTypeEnum.isFullGift(this.activity.getType())) {
            boolean result = this.goodsGiftLimit.stream().anyMatch(item -> BigDecimal.ZERO.compareTo(item.getPromotionAmount()) >= 0);
            if (result) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_AMOUNT_ERROR);
            }
        }

        // 16、促销渠道
        if (CollUtil.isEmpty(this.activity.getPlatformSelected())) {
            throw new BusinessException(PromotionErrorCode.PLATFORM_ERROR);
        }

        // 17、秒杀&特价 -> 活动价不允许大于销售价
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(this.activity.getType())) {
            boolean result = this.goodsLimitList.stream().anyMatch(item -> item.getPromotionPrice().compareTo(item.getPrice()) > 0);
            if (result) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_PRICE_ERROR);
            }
        }

        // 18、组合包产品数量在2到4之间
        if (PromotionTypeEnum.isCombinationPackage(this.activity.getType())) {
            int size = goodsLimitList.size();
            if (size < 2 || size > 4) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_GOODS_TOO_MUCH_ERROR);
            }
        }

        // 赋默认值逻辑
        if (PromotionBearTypeEnum.PLATFORM.getType().equals(this.activity.getBear())) {
            this.activity.setPlatformPercent(BigDecimal.valueOf(100));
        }
        if (PromotionBearTypeEnum.MERCHANT.getType().equals(this.activity.getBear())) {
            this.activity.setMerchantPercent(BigDecimal.valueOf(100));
        }


    }

}
