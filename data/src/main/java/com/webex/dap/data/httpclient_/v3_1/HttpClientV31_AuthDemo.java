package com.webex.dap.data.httpclient_.v3_1;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.io.IOException;

/*
    The HttpClient supports three different types of http authentication shcemes:
        Basic/Digest/NTML


 */
public class HttpClientV31_AuthDemo {
    public static void main(String[] args){
        /*
            step1: instanting httpclient
         */
        HttpClient client = new HttpClient();

        client.getParams().setAuthenticationPreemptive(true);



        /*
            credentials是client级别的，而同一个client可以访问不同URL，所以需要定义AuthScope来区分验证。
         */



        Credentials defaultCredentials = new UsernamePasswordCredentials("username","password");
        client.getState().setCredentials(new AuthScope("myhost",80,AuthScope.ANY_REALM),defaultCredentials);


//        CredentialsProvider credentialsProvider = new CredentialsProvider() {
//            @Override
//            public Credentials getCredentials(AuthScheme scheme, String host, int port, boolean proxy) throws CredentialsNotAvailableException {
//                return null;
//            }
//        };
//        client.getParams().setParameter(CredentialsProvider.PROVIDER,credentialsProvider);

        /*
            step2: creating a method
         */
        HttpMethod method = new GetMethod("http://www.apache.org/");


        /*
            step3: execute the method
                The actual executing of the method is performed by calling executeMethod on the client and passing in the method to execute.
                Since networks connections are unreliable, we also need to deal with any errors that occur.

                There are two kinds of exceptions that could be thrown by executeMethod, HttpException/IOException.

                The other useful piece of information is the status code that is returned by the server.

                HttpException
                IOException

                Per default HttpClient will automaticeally attempt to recover from the not-fatal errors.
                that is ,when a plain IOException is thrown. HttpClient will retry the method three times provided that the request has never been fully transmitted to the target server.

                ex1:
                    client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());

                ex2:
                    DefaultMethodRetryHandler retryHandler = new DefaultMethodRetryHandler(10,true);
                    client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,retryHandler);
         */

        try {
            int statusCode = client.executeMethod(method);

            byte[] responseBody = method.getResponseBody();

            System.out.println(new String(responseBody,"UTF-8"));
        } catch (HttpException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            method.releaseConnection();
        }
    }
}
