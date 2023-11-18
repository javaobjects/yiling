/**
 * EASLoginProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.login;

/**
 * @author shuan
 */
public interface EASLoginProxy extends java.rmi.Remote {
    public com.yiling.open.webservice.login.WSContext loginByLtpaToken(String userName, String ltpaToken, String slnName, String dcName, String language, int dbType) throws java.rmi.RemoteException;
    public com.yiling.open.webservice.login.WSContext login(String userName, String password, String slnName, String dcName, String language, int dbType, String authPattern, int isEncodePwd) throws java.rmi.RemoteException;
    public com.yiling.open.webservice.login.WSContext login(String userName, String password, String slnName, String dcName, String language, int dbType) throws java.rmi.RemoteException;
    public com.yiling.open.webservice.login.WSContext login(String userName, String password, String slnName, String dcName, String language, int dbType, String authPattern) throws java.rmi.RemoteException;
    public boolean logout(String userName, String slnName, String dcName, String language) throws java.rmi.RemoteException;
}
