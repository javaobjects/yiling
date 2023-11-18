package com.yiling.user.agreement.bo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/7
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsAgreementDTO implements Serializable {

    private static final long serialVersionUID = -9044217574008714L;

    /**
     * 供应商eid
     */
    private Long eid;

    /**
     * 年度协议idList
     */
    private List<Long> yearAgreementList;

    /**
     * 临时协议idList
     */
    private List<Long> tempAgreementList;

	/**
	 * 临时协议的三方协议idList(不区分乙丙方)
	 */
	private List<Long> thirdAgreementsList;

    /**
     * 发生三方三方协议的eidList(只有queryEid为null时才统计)
     */
    private List<Long> thirdAgreementsCustomerList;
}
