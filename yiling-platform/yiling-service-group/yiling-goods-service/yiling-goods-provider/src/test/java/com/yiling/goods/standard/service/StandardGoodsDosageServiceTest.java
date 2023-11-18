package com.yiling.goods.standard.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.BaseTest;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.entity.StandardInstructionsGoodsDO;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
public class StandardGoodsDosageServiceTest extends BaseTest {

    private static final String path="D:\\标准库商品导入5.15.xlsx";

    @Autowired
    private StandardGoodsService standardGoodsService;

    @Autowired
    private StandardInstructionsGoodsService standardInstructionsGoodsService;

    @Autowired
    private StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Test
    public void queryStandardGoodsSpecification(){
        List<StandardGoodsSpecificationDO> list = standardGoodsSpecificationService.getStandardGoodsSpecificationBySpecificationOrBarcode(null, "100ml", "2342342342342");
        System.out.println(list);
    }
    @Test
    public void test(){
        StandardGoodsInfoRequest request = new StandardGoodsInfoRequest();
        request.setStandardId(0L);
        Page<StandardGoodsInfoDTO> standardGoodsInfo = standardGoodsService.getStandardGoodsInfo(request);
        System.out.println(standardGoodsInfo);
    }

    @Test
    public void getYilingStandardGoodsPage(){
        StandardGoodsInfoRequest request = new StandardGoodsInfoRequest();
        request.setCurrent(1);
        request.setSize(10);
        request.setName("小半夏");
        Page<StandardGoodsInfoDTO> goodsInfo = standardGoodsService.getYilingStandardGoodsInfo(request);
        System.out.println(goodsInfo);
    }
    @Test
    public void insertStandardGoods(){
        String URL = "jdbc:mysql://nacos.comm.yl.local:3306/reaclan_temp?characterEncoding=utf-8";
        String USER = "yiling_dev";
        String PASSWORD = "yiling_dev@123";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获得数据库链接
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
            Integer id=1;
            //预编译
            String sql="select *  from (   select * from rk_standard_goods where goods_type =? )  s " +
                    "inner join " +
                    "(select id as id1 ,standard_id,drug_details,indications,usage_dosage,adverse_events,contraindication,note_events,interreaction," +
                    "storage_conditions,packing_instructions,shelf_life,executive_standard ,special_composition from rk_goods_instructions) m" +
                    " on m.standard_id = s.id limit 1";
            /*String sql= "SELECT\n" +
                    "\t* \n" +
                    "FROM\n" +
                    "\t( SELECT * FROM rk_standard_goods WHERE goods_type = ? ) s\n" +
                    "\tINNER JOIN (\n" +
                    "SELECT\n" +
                    "\tr.standard_id, r.id as id1, \n" +
                    "\tr.license_no,\n" +
                    "\tr.name,\n" +
                    "\tr.manufacturer,\n" +
                    "\tr.sell_specifications,\n" +
                    "\tr.unit \n" +
                    "FROM\n" +
                    "\trk_sell_specifications r \n" +
                    "\t) m ON s.id = m.standard_id and m.standard_id= 170692";*/
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,1);
            //statement.setInt(2, 0);
            //statement.setInt(3, 1);
            ResultSet rs = statement.executeQuery();
            List<StandardGoodsDO> list = new ArrayList<>();
            List<StandardInstructionsGoodsDO> instructionsList = new ArrayList<>();
            System.out.println("sql执行完成");
            while (rs.next()) {
                StandardGoodsDO goodsDO = new StandardGoodsDO();
                goodsDO.setId(rs.getLong("id"));
                goodsDO.setGoodsType(rs.getInt("goods_type"));
                goodsDO.setName(rs.getString("name"));
                goodsDO.setCommonName(rs.getString("common_name"));
                goodsDO.setLicenseNo(rs.getString("license_no"));
                goodsDO.setManufacturer(rs.getString("manufacturer"));
                goodsDO.setManufacturerAddress(rs.getString("manufacturer_address"));
                goodsDO.setStandardCategoryId1(rs.getLong("gc_id1"));
                goodsDO.setStandardCategoryId2(rs.getLong("gc_id2"));
                goodsDO.setAliasName(rs.getString("alias_name"));
                goodsDO.setGdfName(rs.getString("gdf_name"));
                goodsDO.setGdfSpecifications(rs.getString("specifications"));
                goodsDO.setOtcType(rs.getInt("otc_type"));
                goodsDO.setIsYb(rs.getInt("is_yb"));
                goodsDO.setControlType(rs.getInt("control_type"));
                goodsDO.setGoodsCode(rs.getString("goods_code"));
                goodsDO.setIngredient(rs.getString("ingredient"));
                goodsDO.setStandardCategoryName2(rs.getString("gc_name2"));
                goodsDO.setStandardCategoryName1(rs.getString("gc_name1"));
                goodsDO.setSpecialComposition(rs.getInt("special_composition"));
                goodsDO.setQualityType(rs.getInt("quality_type"));

                standardGoodsService.saveOrUpdate(goodsDO);

                StandardInstructionsGoodsDO instructionsGoodsDO = new StandardInstructionsGoodsDO();
                instructionsGoodsDO.setId(rs.getLong("id1"));
                instructionsGoodsDO.setStandardId(goodsDO.getId());
                instructionsGoodsDO.setDrugDetails(rs.getString("drug_details"));
                instructionsGoodsDO.setIndications(rs.getString("indications"));
                instructionsGoodsDO.setUsageDosage(rs.getString("usage_dosage"));
                instructionsGoodsDO.setAdverseEvents(rs.getString("adverse_events"));
                instructionsGoodsDO.setContraindication(rs.getString("contraindication"));
                instructionsGoodsDO.setNoteEvents(rs.getString("note_events"));
                instructionsGoodsDO.setInterreaction(rs.getString("interreaction"));
                instructionsGoodsDO.setStorageConditions(rs.getString("storage_conditions"));
                instructionsGoodsDO.setPackingInstructions(rs.getString("packing_instructions"));
                instructionsGoodsDO.setShelfLife(rs.getString("shelf_life"));
                instructionsGoodsDO.setExecutiveStandard(rs.getString("executive_standard"));
                standardInstructionsGoodsService.saveOrUpdate(instructionsGoodsDO);
                //list.add(goodsDO);
                /*StandardGoodsSpecificationDO standardGoodsSpecificationDO = new StandardGoodsSpecificationDO();
                standardGoodsSpecificationDO.setId(rs.getLong("id1"));
                standardGoodsSpecificationDO.setLicenseNo(rs.getString("license_no"));
                standardGoodsSpecificationDO.setManufacturer(rs.getString("manufacturer"));
                standardGoodsSpecificationDO.setStandardId(rs.getLong("standard_id"));
                standardGoodsSpecificationDO.setName(rs.getString("name"));
                standardGoodsSpecificationDO.setSellSpecifications(rs.getString("sell_specifications"));
                standardGoodsSpecificationDO.setUnit(rs.getString("unit"));
                standardGoodsSpecificationService.saveOrUpdate(standardGoodsSpecificationDO);
                System.out.println("sql插入中");*/
                //standardGoodsService.saveOrUpdate(goodsDO);

            }
            //standardInstructionsGoodsService.saveBatch(instructionsList);
            //standardGoodsService.saveBatch(list);

            rs.close();
            statement.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
