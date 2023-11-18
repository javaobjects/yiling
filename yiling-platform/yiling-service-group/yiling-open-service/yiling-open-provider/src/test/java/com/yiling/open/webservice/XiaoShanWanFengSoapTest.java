package com.yiling.open.webservice;

import cn.hutool.json.JSONUtil;

public class XiaoShanWanFengSoapTest {

    public static void main(String[] args) {
        //        Map<String, Object> params = new HashMap<>();
        //
        //        params.put("yzid", "石家庄以岭");
        //        params.put("pwd", "111111");
        //        params.put("intertype", "QueryStockEnterInfo");
        //
        //        Map<String, Object> inputParamMap = new HashMap<>();
        //        inputParamMap.put("startdate", "2022-04-23");
        //        inputParamMap.put("enddate", "2022-06-23");
        //        inputParamMap.put("currentpage", 2);
        //
        //        Map<String, Object> inputStrMap = new HashMap<>();
        //        inputStrMap.put("inputparas", inputParamMap);
        //
        //        String xmlStr = JSONUtil.toXmlStr(JSONUtil.parse(inputStrMap));
        //        System.out.println(xmlStr);
        //
        //        params.put("inputstr", xmlStr);
        //
        //        String url = "http://115.236.184.130:8071/service.asmx";
        //
        //        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "http://tempuri.org/")
        //                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
        //                .setMethod("DownLoadData").setParams(params, false).setConnectionTimeout(1000*60*5);
        //
        //        String msg =  client.getMsgStr(true);
        //        System.out.println("request = " + msg);
        //        String str = client.send();
        //
        //        System.out.println("response = " + str);
        //        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        //
        //        System.out.println(JSONUtil.toJsonStr(jsonObject));

        // Test2
        //        String jsonStr = "{\n" + "\t\"soap:Envelope\": {\n" + "\t\t\"xmlns:soap\": \"http://schemas.xmlsoap.org/soap/envelope/\",\n" + "\t\t\"xmlns:xsi\": \"http://www.w3.org/2001/XMLSchema-instance\",\n" + "\t\t\"xmlns:xsd\": \"http://www.w3.org/2001/XMLSchema\",\n" + "\t\t\"soap:Body\": {\n" + "\t\t\t\"DownLoadDataResponse\": {\n" + "\t\t\t\t\"xmlns\": \"http://tempuri.org/\",\n" + "\t\t\t\t\"DownLoadDataResult\": \"<output><retcode>-2</retcode><retmsg>未查询到满足条件的数据</retmsg><data></data></output>\"\n" + "\t\t\t}\n" + "\t\t}\n" + "\t}\n" + "}";
        //
        //        String value = getResponseJson(JSONUtil.parseObj(jsonStr));
        //        System.out.println(value);

        // TEST 3
        String xmlStr = "<output>\n" + "    <retcode>999</retcode>\n" + "    <retmsg>成功</retmsg>\n" + "    <data>\n" + "        <dataitem>\n" + "            <billid>205512</billid>\n" + "            <itemid>1</itemid>\n" + "            <billcode>T0023441            </billcode>\n" + "            <makername></makername>\n" + "            <billtype>1</billtype>\n" + "            <maketime>2022-05-03 10:20:11</maketime>\n" + "            <clientid>3410</clientid>\n" + "            <clientno>11030010       </clientno>\n" + "            <clientname>华润衢州医药有限公司</clientname>\n" + "            <goodsid>16938</goodsid>\n" + "            <goodscode>04000880</goodscode>\n" + "            <goodsname>氢溴酸右美沙芬片</goodsname>\n" + "            <spec>15mg*24s</spec>\n" + "            <unit>盒</unit>\n" + "            <medicaltype>片剂</medicaltype>\n" + "            <productor>石家庄以岭药业股份有限公司</productor>\n" + "            <amount>-31.00000000</amount>\n" + "            <price>5.20000000</price>\n" + "            <sums>-161.2000</sums>\n" + "            <batchid>0</batchid>\n" + "            <batchno>A2110009</batchno>\n" + "            <producedate></producedate>\n" + "            <validdate>202303</validdate>\n" + "            <memo>滞销</memo>\n" + "            <linenum>1</linenum>\n" + "            <totalpage>1</totalpage>\n" + "        </dataitem>\n" + "        <dataitem>\n" + "            <billid>208316</billid>\n" + "            <itemid>1</itemid>\n" + "            <billcode>J0178275            </billcode>\n" + "            <makername></makername>\n" + "            <billtype>0</billtype>\n" + "            <maketime>2022-06-09 14:03:47</maketime>\n" + "            <clientid>3410</clientid>\n" + "            <clientno>11030010       </clientno>\n" + "            <clientname>华润衢州医药有限公司</clientname>\n" + "            <goodsid>25751</goodsid>\n" + "            <goodscode>05000874</goodscode>\n" + "            <goodsname>通心络胶囊/禁退</goodsname>\n" + "            <spec>0.26g*40s</spec>\n" + "            <unit>瓶</unit>\n" + "            <medicaltype>胶囊剂</medicaltype>\n" + "            <productor>石家庄以岭药业股份有限公司</productor>\n" + "            <amount>320.00000000</amount>\n" + "            <price>32.70000000</price>\n" + "            <sums>10464.0000</sums>\n" + "            <batchid>0</batchid>\n" + "            <batchno>A2111049</batchno>\n" + "            <producedate></producedate>\n" + "            <validdate>202410</validdate>\n" + "            <memo></memo>\n" + "            <linenum>2</linenum>\n" + "            <totalpage>1</totalpage>\n" + "        </dataitem>\n" + "    </data>\n" + "</output>";

        System.out.println(JSONUtil.toJsonStr(JSONUtil.xmlToJson(xmlStr)));
    }
}
