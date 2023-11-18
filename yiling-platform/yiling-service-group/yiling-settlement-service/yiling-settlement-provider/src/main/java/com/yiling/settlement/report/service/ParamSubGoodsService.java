package com.yiling.settlement.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.bo.LadderGoodsInfoBO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryParamSubGoodsPageListRequest;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;

/**
 * <p>
 * 报表子参数商品关联表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
public interface ParamSubGoodsService extends BaseService<ParamSubGoodsDO> {

    /**
     * 分页查询子参属下的商品列表
     *
     * @param request
     * @return
     */
    Page<ReportParamSubGoodsDTO> queryParamSubGoodsPageList(QueryParamSubGoodsPageListRequest request);

    /**
     * 查询商品分类信息
     *
     * @param queryGoodsCategoryRequest
     * @return
     */
    List<ReportYlGoodsCategoryDTO> queryCategoryByYlGoodsIds(QueryGoodsCategoryRequest queryGoodsCategoryRequest);

    /**
     * 查询商品阶梯信息
     *
     * @param request
     * @return
     */
    List<LadderGoodsInfoBO> queryLadderInfo(QueryGoodsInfoRequest request);

    /**
     * 查询商品活动信息
     *
     * @param request
     * @return
     */
    List<ReportParamSubGoodsDTO> queryActivityGoodsInfo(QueryGoodsInfoRequest request);

    /**
     * 新增或修改子参属下的商品（商品分类）
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubCategoryGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 新增或修改 活动商品
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubActivityGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 新增或修改阶梯商品
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubLadderGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 更新企业下参数商品的ylGoodsId
     *
     * @param request
     * @return
     */
    Boolean updateYlGoodsId(UpdateYlGoodsIdRequest request);

    /**
     * 删除子参数商品
     *
     * @param id
     * @param opUser
     * @return
     */
    Boolean deleteParSubGoods(Long id, Long opUser);
}
