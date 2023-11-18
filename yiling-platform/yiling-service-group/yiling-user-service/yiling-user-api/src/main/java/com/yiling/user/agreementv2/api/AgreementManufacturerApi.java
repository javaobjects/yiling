package com.yiling.user.agreementv2.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.dto.AgreementManufacturerDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;

/**
 * 厂家管理 API
 *
 * @author: lun.yu
 * @date: 2022/2/22
 */
public interface AgreementManufacturerApi {

    /**
     * 分页列表
     *
     * @param request
     * @return
     */
    Page<AgreementManufacturerDTO> queryManufacturerListPage(QueryAgreementManufacturerRequest request);

    /**
     * 新增厂家
     *
     * @param request
     * @return
     */
    Boolean addManufacturer(AddAgreementManufacturerRequest request);

    /**
     * 根据厂家ID批量查询
     *
     * @param ids
     * @return
     */
    List<AgreementManufacturerDTO> listByIds(List<Long> ids);

    /**
     * 根据规格和类型获取厂家名称
     *
     * @param specificationGoodsId
     * @param type
     * @return
     */
    String getNameBySpecificationAndType(Long specificationGoodsId, Integer type);

    /**
     * 根据厂家ID查询
     *
     * @param id
     * @return
     */
    AgreementManufacturerDTO listById(Long id);

    /**
     * 根据企业ID查询厂家信息
     * @param eid
     * @return
     */
    AgreementManufacturerDTO getByEid(Long eid);

    /**
     * 删除厂家
     *
     * @param id 厂家ID
     * @param opUserId 操作人ID
     * @return
     */
    Boolean deleteManufacturer(Long id, Long opUserId);

    /**
     * 添加厂家商品
     *
     * @param request
     * @return
     */
    Boolean addManufacturerGoods(List<AddAgreementManufacturerGoodsRequest> request);

    /**
     * 厂家商品分页列表
     *
     * @param request
     * @return
     */
    Page<AgreementManufacturerGoodsDTO> queryManufacturerGoodsListPage(QueryAgreementManufacturerGoodsRequest request);

    /**
     * 删除厂家商品
     *
     * @param idList
     * @param opUserId
     * @return
     */
    Boolean deleteManufacturerGoods(List<Long> idList, Long opUserId);

    /**
     * 根据规格ID批量查询厂家商品
     *
     * @param specificationId
     * @return
     */
    Map<Long, List<AgreementManufacturerGoodsDTO>> getGoodsBySpecificationId(List<Long> specificationId);

    /**
     * 获取厂家企业商品数量
     *
     * @param eid
     * @return
     */
    Integer getManufactureGoodsNumberByEid(Long eid);

}
