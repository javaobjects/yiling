package com.yiling.open.webservice;

import com.yiling.open.webservice.login.EASLoginProxy;
import com.yiling.open.webservice.login.EASLoginProxyServiceLocator;
import com.yiling.open.webservice.login.WSContext;

/**
 *
 * @author: shuang.zhang
 * @date: 2021/7/26
 */
public class LoginClientTest {
  public static void main(String[] argv) {
      try {
          EASLoginProxyServiceLocator locator = new EASLoginProxyServiceLocator();
          locator.setEASLoginEndpointAddress("http://110.249.133.136:6890/ormrpc/services/EASLogin");
          EASLoginProxy service = locator.getEASLogin();
          WSContext wsContext= service.login("pop","Yiling@123","eas","POP","l2",2);
          System.out.println(wsContext.getSessionId());
      } catch (javax.xml.rpc.ServiceException ex) {
          ex.printStackTrace();
      } catch (java.rmi.RemoteException ex) {
          ex.printStackTrace();
      }  
  }
}
