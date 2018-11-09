package com.webex.dap.data.httpclient_.v3_1;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.*;

/*
    This document provides an overview of how to use HttpClient safely from within a multi-threaded environment. It is broken down into the following main sections
        1. MultiThreadedHttpConnectionManager
        2. Connection Release

    MultiThreadedHttpConnectionManager
        The main reason for using multiple threads in HttpClient is to allow the execution of multiple methods at once()
        During execution each method uses an instance of an HttpConnection.

        This instance of HttpClient can now be used to execute multiple methods from multiple thread.

    Connection Release
        One main side effect of connection management is that connections must be manually released when no longer used.
        This is due to the fact that HttpClient cannot determine when a method is no longer using its connection.
        This occur because a method's response body is not read directly by HttpClient, but by the application using HttpClient.
        When
 */

public class HttpClientV31_ThreadingDemo {
    public static void main(String[] args){



        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

        /*
            step1: instanting httpclient
         */
        HttpClient client = new HttpClient(connectionManager);

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
        HttpMethod getMethod = new GetMethod("http://www.apache.org/");


        HttpMethod postMethod = new PostMethod("");
        final File file = null;
        ((PostMethod) postMethod).setRequestEntity(new RequestEntity() {
            @Override
            public boolean isRepeatable() {
                return true;
            }

            @Override
            public void writeRequest(OutputStream out) throws IOException {
                InputStream inputStream = new FileInputStream(file);
                try{
                    int length = 0;
                    byte[] buffer = new byte[1024];
                    while ((length = inputStream.read(buffer)) != -1){
                        out.write(buffer,0,length);
                    }
                }finally {
                    inputStream.close();
                }
            }

            @Override
            public long getContentLength() {
                return file.length();
            }

            @Override
            public String getContentType() {
                return "text/plain;charset=UTF-8";
            }
        });


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
            int statusCode = client.executeMethod(getMethod);
            Reader reader = new InputStreamReader(getMethod.getResponseBodyAsStream(),((GetMethod) getMethod).getResponseCharSet());

            //
            // consume the response entity.
        } catch (HttpException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            //
            getMethod.releaseConnection();
        }
    }
}
