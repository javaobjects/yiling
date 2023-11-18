package com.yiling.bi.order;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.order.bo.OdsStjxcJddyhoutBO;
import com.yiling.bi.order.service.OdsStjxcJddyhoutService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/12/27
 */
@Slf4j
public class OdsStjxcJddyhoutTest extends BaseTest {

    @Autowired
    private OdsStjxcJddyhoutService odsStjxcJddyhoutService;

    @Test
    public void getByYearAndMonth() {
        List<OdsStjxcJddyhoutBO> odsStjxcJddyhoutBOList = odsStjxcJddyhoutService.getByYearAndMonth("2022", "7");
        System.out.println(odsStjxcJddyhoutBOList.size());
    }

    public static void main(String[] args) {
        Date monthDate = DateUtil.parse("2022-07", "yyyy-MM");
        Date lastMonth = DateUtil.offsetMonth(monthDate, -1);

        String dyear = String.valueOf(DateUtil.year(lastMonth));
        String dmonth = String.valueOf(DateUtil.month(lastMonth) + 1);
        System.out.println(dyear + "-" + dmonth);
    }

}
