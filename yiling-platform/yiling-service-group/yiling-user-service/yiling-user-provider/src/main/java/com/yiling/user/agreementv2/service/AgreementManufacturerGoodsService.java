package com.yiling.user.agreementv2.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.entity.AgreementManufacturerGoodsDO;

/**
 * <p>
 * 厂家商品表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
public interface AgreementManufacturerGoodsService extends BaseService<AgreementManufacturerGoodsDO> {

    /**
     * 添加厂家商品
     * @param request
     * @return
     */
    Boolean addManufacturerGoods(List<AddAgreementManufacturerGoodsRequest> request);

    /**
     * 厂家商品分页列表
     * @param request
     * @return
     */
    Page<AgreementManufacturerGoodsDTO> queryManufacturerGoodsListPage(QueryAgreementManufacturerGoodsRequest request);

    /**
     * 删除厂家商品
     * @param idList
     * @param opUserId
     * @return
     */
    Boolean deleteManufacturerGoods(List<Long> idList, Long opUserId);

    /**
     * 根据规格和类型获取厂家商品
     *
     * @param specificationGoodsId
     * @param type
     * @return
     */
    String getNameBySpecificationAndType(Long specificationGoodsId, Integer type);

    /**
     * 根据规格ID集合获取厂家商品信息
     *
     * @param specificationGoodsIdList
     * @return
     */
    Map<Long, List<AgreementManufacturerGoodsDTO>> getGoodsBySpecificationId(List<Long> specificationGoodsIdList);

    /**
     * 获取厂家企业商品数量
     *
     * @param eid
     * @return
     */
    Integer getManufactureGoodsNumberByEid(Long eid);

    /**
     * 根据企业ID，获取厂家企业商品
     *
     * @param eid
     * @return
     */
    List<AgreementManufacturerGoodsDTO> getManufactureGoodsListByEid(Long eid);
}
