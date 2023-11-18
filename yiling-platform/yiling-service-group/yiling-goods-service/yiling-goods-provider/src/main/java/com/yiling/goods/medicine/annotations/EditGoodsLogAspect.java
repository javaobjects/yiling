package com.yiling.goods.medicine.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.medicine.service.GoodsLogService;
import com.yiling.goods.medicine.service.GoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-05-25
 */
@Aspect
@Component
@Slf4j
public class EditGoodsLogAspect {

    @Autowired
    private GoodsLogService goodsLogService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private InventoryService inventoryService;

    /**
     * @EditGoodsLog 注解为切点
     */
    @Pointcut("@annotation(com.yiling.goods.medicine.annotations.EditGoodsLog)")
    public void editGoodsLog() {

    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @Around("editGoodsLog()")
    public Object doAfter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        //插入数据库的值
//        List<GoodsLogDO> logDOList = new ArrayList<>();
//        //调用参数
//        SaveOrUpdateGoodsRequest request = (SaveOrUpdateGoodsRequest) proceedingJoinPoint.getArgs()[0];
//
//        if(request.getId()==null){
//            request.setId(0L);
//        }
//        GoodsInfoDTO goodsInfoDTO = goodsService.queryInfo(request.getId());
//        InventoryDO inventoryDO=inventoryService.getBySkuId(request.getId());
//
        //执行方法
        Object result = proceedingJoinPoint.proceed();
//
//        //记录日志
//        //参数模板
//        GoodsLogDO goodsLogDO = new GoodsLogDO();
//        goodsLogDO.setGid((Long)result);
//        goodsLogDO.setOpUserId(request.getOpUserId());
//
//        //修改商品
//        if (goodsInfoDTO != null) {
//            goodsLogDO.setLicenseNo(goodsInfoDTO.getLicenseNo());
//            goodsLogDO.setName(goodsInfoDTO.getName());
////            //修改上下架状态
////            if (request.getGoodsStatus()!=null&&!goodsInfoDTO.getGoodsStatus().equals(request.getGoodsStatus())) {
////                goodsLogDO.setModifyColumn(GoodsLogEnum.EDIT_STATUS.getCode());
////                goodsLogDO.setBeforeValue(GoodsStatusEnum.getByCode(goodsInfoDTO.getGoodsStatus()).getName());
////                goodsLogDO.setAfterValue(GoodsStatusEnum.getByCode(request.getGoodsStatus()).getName());
////                //修改库存
////            } else
//                if (request.getQty()!=null&&inventoryDO!=null&&!inventoryDO.getQty().equals(request.getQty())) {
//                goodsLogDO.setModifyColumn(GoodsLogEnum.EDIT_QTY.getCode());
//                goodsLogDO.setBeforeValue(String.valueOf(inventoryDO.getQty()));
//                goodsLogDO.setAfterValue(String.valueOf(request.getQty()));
//                //修改价格
//            } else if (request.getPrice()!=null&&!goodsInfoDTO.getPrice().equals(request.getPrice())) {
//                goodsLogDO.setModifyColumn(GoodsLogEnum.EDIT_PRICE.getCode());
//                goodsLogDO.setBeforeValue(String.valueOf(goodsInfoDTO.getPrice()));
//                goodsLogDO.setAfterValue(String.valueOf(request.getPrice()));
//            }
//            logDOList.add(goodsLogDO);
//        }else {
//            //新增商品
//            goodsLogDO.setBeforeValue("0");
//            goodsLogDO.setLicenseNo(request.getLicenseNo());
//            goodsLogDO.setName(request.getName());
//            //上下架
//            GoodsLogDO statusLog=new GoodsLogDO();
//            BeanUtils.copyProperties(goodsLogDO,statusLog);
//            if (request.getGoodsStatus()==null){
//                request.setGoodsStatus(GoodsStatusEnum.UN_SHELF.getCode());
//            }
//            statusLog.setAfterValue(String.valueOf(request.getGoodsStatus()));
//            statusLog.setModifyColumn(GoodsLogEnum.EDIT_STATUS.getCode());
//            //库存
//            GoodsLogDO qtyLog=new GoodsLogDO();
//            BeanUtils.copyProperties(goodsLogDO,qtyLog);
//            qtyLog.setAfterValue(String.valueOf(request.getQty()));
//            qtyLog.setModifyColumn(GoodsLogEnum.EDIT_QTY.getCode());
//            //价格
//            GoodsLogDO priceLog=new GoodsLogDO();
//            BeanUtils.copyProperties(goodsLogDO,priceLog);
//            priceLog.setAfterValue(String.valueOf(request.getPrice()));
//            priceLog.setModifyColumn(GoodsLogEnum.EDIT_PRICE.getCode());
//
//            logDOList.add(statusLog);
//            logDOList.add(qtyLog);
//            logDOList.add(priceLog);
//
//        }
//        if (logDOList != null) {
//            //存盘
//            boolean canSave = goodsLogService.saveBatch(logDOList);
//            if (!canSave) {
//                log.error("修改商品信息保存日志失败" + JSON.toJSONString(logDOList));
//            }
//        }
        return result;
    }

}
