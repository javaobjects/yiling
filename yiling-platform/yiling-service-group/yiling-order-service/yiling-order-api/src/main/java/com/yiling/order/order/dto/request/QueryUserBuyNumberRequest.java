package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 查询用户购买数量
 */
@Data
@Accessors(chain = true)
public class QueryUserBuyNumberRequest extends BaseRequest {

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 商品Id
     */
    private Long goodId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 查询规则
     */
    private SelectRuleEnum selectRuleEnum;


    private void  setTimeByDay () {
        setStartTime(DateUtil.beginOfDay(new Date()));
        setEndTime(DateUtil.endOfDay(new Date()));
    }

    private void setTimeByMonth() {

        setStartTime(DateUtil.beginOfMonth(new Date()));
        setEndTime(DateUtil.endOfMonth(new Date()));
    }

    private void setTimeByWeek() {
        setStartTime(DateUtil.beginOfWeek(new Date()));
        setEndTime(DateUtil.endOfWeek(new Date()));
    }

    private void setTimeByCustomized() {

        if (ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)) {

            throw new RuntimeException("自定义时间端为空!");
        }

        setStartTime(DateUtil.beginOfDay(this.startTime));
        setEndTime(DateUtil.endOfDay(this.endTime));
    }

    public void convert() {

        if (ObjectUtil.isNull(selectRuleEnum)) {

            throw new RuntimeException("转换规则为空!");
        }
        switch (this.selectRuleEnum) {
            case DAY: setTimeByDay(); break;
            case WEEK: setTimeByWeek(); break;
            case MONTH: setTimeByMonth(); break;
            case CUSTOMIZE: setTimeByCustomized(); break;
            default: break;
        }

    }


    /**
     * 查询规则
     */
    @Getter
    @AllArgsConstructor
    public enum SelectRuleEnum {


        DAY(2, "按照天"), WEEK(3, "按照周"), MONTH(4, "按照月"), CUSTOMIZE(1, "自定义"),
        ;

        private Integer code;

        private String name;

        public static QueryUserBuyNumberRequest.SelectRuleEnum getByCode(Integer code) {

            for (QueryUserBuyNumberRequest.SelectRuleEnum e : QueryUserBuyNumberRequest.SelectRuleEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
