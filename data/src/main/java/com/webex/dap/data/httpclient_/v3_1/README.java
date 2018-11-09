package com.webex.dap.data.httpclient_.v3_1;

/*
    Concepts

    The general process for using HttpClient consists of a number of steps:
        1. Create an instance of HttpClient
        2. Create an instance of one of the methods(GetMethon in this case). The URL to connect is passed in to the method constructor
        3. Tell HttpClient to execute the method
        4. Read the response
        5. Release the connection
        6. Deal with teh response

    HTTP1.1 allows multiple requests to use the same connection by simply sending the requests one after the other.
    Obviously, If we donot read the entire response to the first request, the left over data will get in the way of the second response.

 */
public class README {
}
