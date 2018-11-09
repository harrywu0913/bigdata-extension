package com.webex.dap.data.httpclient_.v3_1;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/*
    The headers of a HTTP request or response must be in US-ASCII format.

    The request/response body can be any encoding,but by default is ISO-8859-1. The encoding may be specified in the Content-Type header.
    ex1:
        Content-Type: text/html;charset=UTF-8
 */
public class HttpClientV31_CharsetDemo {
    public static void main(String[] args){
        /*
            step1: instanting httpclient
         */
        HttpClient client = new HttpClient();

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
