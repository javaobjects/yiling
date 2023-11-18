package com.yiling.order;/**
 * @author: ray
 * @date: 2021/5/21
 */

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;
import com.yiling.order.order.service.TicketDiscountRecordService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gxl
 * @description:
 * @date: Created in 8:44 2021/5/21
 * @modified By:
 */
@Slf4j
public class AgreementServiceTest extends BaseTest {


    @Autowired
	TicketDiscountRecordService ticketDiscountRecordService;

    @Test
    public void test1(){
		AddTicketDiscountRecordRequest request=new AddTicketDiscountRecordRequest();
		request.setId(3L);
		request.setEid(1L);
		request.setEname("北京以岭");
		request.setAvailableAmount(new BigDecimal("10"));
		request.setCustomerErpCode("1500001");

		TicketDiscountRecordDTO ticketDiscountRecordDTO = ticketDiscountRecordService.saveOrUpdate(request);
		System.err.println(ticketDiscountRecordDTO);
    }

    @Test
    public void test(){
		QueryTicketDiscountPageListRequest request=new QueryTicketDiscountPageListRequest();
		request.setCurrent(1);
		request.setSize(1);
		request.setStartTime(DateUtil.parseDate("2021-08-02 00:00:00"));
		request.setEndTime(DateUtil.parseDate("2021-08-03 00:00:00"));
		Page<TicketDiscountRecordDTO> thirdAgreementEntListItemBOPage = ticketDiscountRecordService.queryPageListByUseTime(request);
		System.err.println(thirdAgreementEntListItemBOPage);
    }


}
