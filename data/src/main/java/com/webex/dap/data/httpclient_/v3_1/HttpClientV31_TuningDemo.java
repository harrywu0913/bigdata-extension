package com.webex.dap.data.httpclient_.v3_1;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.*;

/*
    By default HttpClient is configured to provide maximum reliability and standards compliance rather than raw performance.
    There are several configuration options and optimization techniques which can significantly improve the performance of the HttpClient.

        1. Reuse the HttpClient instance
        2. Connection persistence
        3. Concurrent execution of HTTP methods
        4. Request/Response entity streaming
        5. Expect-continue handshake
        6. Stale connection check
        7. Cookie processing

    1. Reuse the HttpClient instance:
        Generally it is recommended to have a single instance of HttpClient per communiation component or even per application.

    2. Connection persistence
        HttpClient always does its best to reuse connections. Connection persistence is enabled by default and requires no configuration.
        Under the some situations this can be lead to leaked connections and therefore lost resources.
        The easient way to disable connection persistence is to provide or extend a connection manager that force-closes connections upon release in the releaseConncection method.

    3.
    4. Request/Response entity streaming
        HttpClient is capable of efficient request/response body streaming.Large enties may be submitted or received without being buffered in memory.
        This is especially critial if multiple HTTP methods may be executed concurrently.
        While there are conveniene methods to deal with entities such as strings/byte arrays. their use is discouraged.
        Unless used carefully they can easily lead to out of memory conditiions,since they imply buffering of the complete entity in memory.


 */

public class HttpClientV31_TuningDemo {
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
            getMethod.releaseConnection();
        }
    }
}
