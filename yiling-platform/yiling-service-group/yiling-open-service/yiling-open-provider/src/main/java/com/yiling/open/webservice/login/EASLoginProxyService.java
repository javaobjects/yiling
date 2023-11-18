/**
 * EASLoginProxyService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.login;

public interface EASLoginProxyService extends javax.xml.rpc.Service {
    public String getEASLoginAddress();

    public com.yiling.open.webservice.login.EASLoginProxy getEASLogin() throws javax.xml.rpc.ServiceException;

    public com.yiling.open.webservice.login.EASLoginProxy getEASLogin(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
