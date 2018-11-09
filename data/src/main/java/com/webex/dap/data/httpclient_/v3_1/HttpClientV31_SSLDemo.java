package com.webex.dap.data.httpclient_.v3_1;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.*;

/*
    HttpClient provides full support for HTTP over Secure Sockets Layer(SSL) or IETF Transport Layer Security(TLS) protocols by JSSE

    Standard SSL in HttpClient

    Customizing SSL in HttpClient
        1. Ability to accept self-signed or untrusted SSL certificates.
            This is highlighted by an SSLException with the message Unrecognized SSL handshake(or simialr) being thrown when a connection attempt is made.

        2. You want to use a third party SSL library instead of Sun's default implementation

        Implementation of a custom protocol involves the following steps:
            1. Provide a custom socket facotry that implements `org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory`
                The socket factory is responsible for opening a socket to the target server using either the standard or a third party SSL library and performing any required intialization such as performing the connection handshake.

            2. Instantiate an object of type `org.apache.commons.httpclient.protocol.Protocol.`
                Protocol myhttps = new Protocol("https",new MySSLSocketFactory(),443);
            3.
            4.
 */
public class HttpClientV31_SSLDemo {
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
