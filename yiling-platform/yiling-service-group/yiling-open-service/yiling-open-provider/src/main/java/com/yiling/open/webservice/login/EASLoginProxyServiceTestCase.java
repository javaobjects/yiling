/**
 * EASLoginProxyServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.login;

public class EASLoginProxyServiceTestCase extends junit.framework.TestCase {
    public EASLoginProxyServiceTestCase(String name) {
        super(name);
    }

    public void testEASLoginWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLoginAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1EASLoginLoginByLtpaToken() throws Exception {
        EASLoginSoapBindingStub binding;
        try {
            binding = (EASLoginSoapBindingStub)
                          new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLogin();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        com.yiling.open.webservice.login.WSContext value = null;
        value = binding.loginByLtpaToken(new String(), new String(), new String(), new String(), new String(), 0);
        // TBD - validate results
    }

    public void test2EASLoginLogin() throws Exception {
        EASLoginSoapBindingStub binding;
        try {
            binding = (EASLoginSoapBindingStub)
                          new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLogin();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        com.yiling.open.webservice.login.WSContext value = null;
        value = binding.login(new String(), new String(), new String(), new String(), new String(), 0, new String(), 0);
        // TBD - validate results
    }

    public void test3EASLoginLogin() throws Exception {
        EASLoginSoapBindingStub binding;
        try {
            binding = (EASLoginSoapBindingStub)
                          new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLogin();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        com.yiling.open.webservice.login.WSContext value = null;
        value = binding.login(new String(), new String(), new String(), new String(), new String(), 0);
        // TBD - validate results
    }

    public void test4EASLoginLogin() throws Exception {
        EASLoginSoapBindingStub binding;
        try {
            binding = (EASLoginSoapBindingStub)
                          new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLogin();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        com.yiling.open.webservice.login.WSContext value = null;
        value = binding.login(new String(), new String(), new String(), new String(), new String(), 0, new String());
        // TBD - validate results
    }

    public void test5EASLoginLogout() throws Exception {
        EASLoginSoapBindingStub binding;
        try {
            binding = (EASLoginSoapBindingStub)
                          new com.yiling.open.webservice.login.EASLoginProxyServiceLocator().getEASLogin();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        boolean value = false;
        value = binding.logout(new String(), new String(), new String(), new String());
        // TBD - validate results
    }

}
