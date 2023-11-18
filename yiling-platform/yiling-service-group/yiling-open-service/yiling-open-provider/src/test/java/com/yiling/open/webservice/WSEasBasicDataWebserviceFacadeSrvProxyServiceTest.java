package com.yiling.open.webservice;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.kingdee.bos.openapi.third.OpenApi;
import com.kingdee.bos.openapi.third.OpenApiFactory;
import com.kingdee.bos.openapi.third.OpenApiInfo;
import com.kingdee.bos.openapi.third.ctx.CommonLogin;
import com.kingdee.bos.openapi.third.exception.BizException;
import com.kingdee.bos.openapi.third.exception.InvokeNetworkException;
import com.kingdee.bos.openapi.third.exception.LoginException;
import com.kingdee.bos.openapi.third.login.EASLoginContext;

import cn.hutool.core.date.DateUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/7/26
 */
public class WSEasBasicDataWebserviceFacadeSrvProxyServiceTest {
//
//    public static void main(String[] args) throws ServiceException, RemoteException, LoginException, BizException, InvokeNetworkException {
//        //IP地址
//        String ip = "222.223.229.34";
//        //端口
//        int port = 6896;
//        //用户名
//        String userName = "PMS";
//        //密码
//        String password = "Yiling@123";
//        //数据中心编码
//        String dcName = "yl01";
//        String language = "l2";
//
//        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, port,
//                new CommonLogin.Builder(userName, password, dcName, language)
//                        .build()).https(false)
//                .build();
//
//        OpenApiInfo info = new OpenApiInfo();
//        //调用api的方法名
//        info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
//        //调用参数，格式是数组形式
//        String date = DateUtil.format(DateUtil.parse("2021-09-06 15:50:00"), "yyyy-MM-dd HH:mm:ss");
//        info.setData("[\"01\",\"[0531.JZT]\",\"[02.03.001.004]\",\"1970-01-01 00:00:00\"]");
//        OpenApi openApi = OpenApiFactory.getService(loginCtx);
//        //返回数据
//        String result = openApi.invoke(info);
//        System.out.println(result);
//    }


        public static void main(String[] args) throws ServiceException, RemoteException, LoginException, BizException, InvokeNetworkException {
        //IP地址
        String ip = "110.249.133.136";
        //端口
        int port = 6890;
        //用户名
        String userName = "pop";
        //密码
        String password = "Yiling@123";
        //数据中心编码
        String dcName = "pop";
        String language = "l2";

        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, port,
                new CommonLogin.Builder(userName, password, dcName, language)
                        .build()).https(false)
                .build();

        OpenApiInfo info = new OpenApiInfo();
        //调用api的方法名
        info.setApi("EasBasicDataWebserviceFacade-InventoryQuery");
        //调用参数，格式是数组形式
        info.setData("[\"[01]\", \"1970-01-01 00:00:00\", \"[02.01.004.004]\"]");
        OpenApi openApi = OpenApiFactory.getService(loginCtx);
        //返回数据
        String result = openApi.invoke(info);
        System.out.println(result);
    }

}
