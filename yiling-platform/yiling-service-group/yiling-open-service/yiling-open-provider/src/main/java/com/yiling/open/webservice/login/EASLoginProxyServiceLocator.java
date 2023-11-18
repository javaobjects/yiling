/**
 * EASLoginProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.login;

import org.springframework.stereotype.Service;

@Service("easLoginProxyServiceLocator")
public class EASLoginProxyServiceLocator extends org.apache.axis.client.Service implements EASLoginProxyService {

//    @Value("${eas.login.webservice.url}")
    private String loginUrl;

    public EASLoginProxyServiceLocator() {
    }


    public EASLoginProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EASLoginProxyServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EASLogin
//    private String EASLogin_address = "http://110.249.133.136:6890/ormrpc/services/EASLogin";

    public String getEASLoginAddress() {
        return loginUrl;
    }

    // The WSDD service name defaults to the port name.
    private String EASLoginWSDDServiceName = "EASLogin";

    public String getEASLoginWSDDServiceName() {
        return EASLoginWSDDServiceName;
    }

    public void setEASLoginWSDDServiceName(String name) {
        EASLoginWSDDServiceName = name;
    }

    public EASLoginProxy getEASLogin() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(loginUrl);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEASLogin(endpoint);
    }

    public EASLoginProxy getEASLogin(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            EASLoginSoapBindingStub _stub = new EASLoginSoapBindingStub(portAddress, this);
            _stub.setPortName(getEASLoginWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEASLoginEndpointAddress(String address) {
        loginUrl = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (EASLoginProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                EASLoginSoapBindingStub _stub = new EASLoginSoapBindingStub(new java.net.URL(loginUrl), this);
                _stub.setPortName(getEASLoginWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("EASLogin".equals(inputPortName)) {
            return getEASLogin();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/EASLogin", "EASLoginProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/EASLogin", "EASLogin"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("EASLogin".equals(portName)) {
            setEASLoginEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
