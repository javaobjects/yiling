package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.dto.AgreementApplyGoodsDTO;
import com.yiling.open.erp.dto.AgreementApplyLocationDTO;
import com.yiling.open.erp.dto.ValidAgreementApplyImportDTO;
import com.yiling.open.erp.dto.request.ValidExcelRequest;

/**
 * 调用三方系统api
 *
 * @author dexi.yao
 * @date 2021-08-09
 */
public interface CallThirdSystemApi {


	/**
	 * 冲红系统---校验导入返利申请的参数
	 *
	 * @param request
	 * @return
	 */
	ValidAgreementApplyImportDTO validAgreementApplyImport(List<ValidExcelRequest> request);

	/**
	 * 冲红系统---查询返利申请商品
	 *
	 * @return
	 */
	List<AgreementApplyGoodsDTO> queryAgreementApplyGoods();

	/**
	 * 冲红系统---查询地址
	 *
	 * @return
	 */
	List<AgreementApplyLocationDTO> queryAgreementApplyLocation();
}
