package com.webex.dap.es.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * curl -XGET
 * "https://clsj1clw004.webex.com:9200/logs-clap_sj1-meeting-2017.08.13-am/_search"
 * -H 'Content-Type: application/json' -d' { "query": { "match": { "host": {
 * "query": "jsj6tc103.webex.com", "type": "phrase" } } } }'
 *
 * @author harry
 */
public class ESHttpCaller {
    public static void main(String[] args) throws Exception {
        // SSLContext sslContext = SSLContextBuilder.create()
        // .loadKeyMaterial(keyStore, "".toCharArray())
        // .loadTrustMaterial(new TrustSelfSignedStrategy()).build();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                System.out.println();
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                System.out.println();
            }
        };
        sslContext.init(new KeyManager[0], new TrustManager[]{tm}, new SecureRandom());

//        SSLContext sslContext2 = SSLContextBuilder.create().build();

//        RegistryBuilder.<ConnectionSocketFactory>create()
//                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                        .register("https", new SSLConnectionSocketFactory(sslContext2, new NoopHostnameVerifier()))
//                        .build();

//        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(RegistryBuilder
//                        .<ConnectionSocketFactory>create()
//                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                        .register("https", new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier()))
//                        .build());
//        poolConnManager.setMaxTotal(200);
//        poolConnManager.setDefaultMaxPerRoute(20);
        // poolConnManager.

//        poolConnManager.getTotalStats();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)// .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(10000)
                        .setConnectTimeout(10000).setSocketTimeout(10000).build())
                .build();

//        String host = "https://sj1-clapweb-s.webex.com/admin/metrics-clap_sj1-dap-*";
        String host = "https://clpsj-bts-dap.webex.com/admin/_search";
//        String host = "https://clpsj-bts-dap.webex.com/elasticsearch/_search";
//        String host = "https://clobt1clw002.webex.com:9200/_search";

        // Map<String, String> headers = new HashMap<String, String>();
        // headers.put("Authorization", "Basic Y2xwdGVzdDpjbHBwYXNz");
        // headers.put("x-qlik-xrfkey", "abcdefghijklmnop");
        // headers.put("X-Qlik-User", "UserDirectory= Internal; UserId=
        // sa_repository ");

//         HttpGet httpGet = new HttpGet(host);

        // List<BasicNameValuePair> xx = new ArrayList<>();
        // xx.add(new BasicNameValuePair("q", "host:\"jsj6tc107.webex.com\"
        // AND m_agent:\"Axis/1.4\" AND (m_resp_bytes:1642 OR
        // m_resp_bytes:1640)"));
        // xx.add(new BasicNameValuePair("from", "host:\"jsj6tc107.webex.com\"
        // AND m_agent:\"Axis/1.4\" AND (m_resp_bytes:1642 OR
        // m_resp_bytes:1640)"));

        // xx.add(new BasicNameValuePair("q", "type:qlklog AND error AND
        // beat.hostname:rpsjqlkhsn*"));

        // httpGet = new HttpGet(host + "?" + URLEncodedUtils.format(xx,
        // HTTP.UTF_8));
        // if (null != headers) {
        // for (String key : headers.keySet()) {
        // httpGet.addHeader(key, headers.get(key));
        // }
        // }
        // sendGet(httpclient, httpGet);

        HttpPost httpPost = new HttpPost(host);

        httpPost.addHeader("Authorization", "Basic aGV3ZXd1OldIVzIwMTMxMTAydGxoew==");
//        httpPost.addHeader("content-type","application/json; charset=UTF-8");
        // HttpPost httpPost = new
        // HttpPost("http://rpbt1rpd001.webex.com:7180/cmf/process/all/logs/search/api");
        // httpPost.setEntity(new StringEntity(
        // "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"message\":\"error\"}},{\"match_phrase_prefix\":{\"beat.hostname\":\"rpsjqlkhsn\"}}],\"filter\":[{\"term\":{\"type\":\"qlklog\"}},{\"range\":{\"@timestamp\":{\"gte\":\"now-10m\",\"lte\":\"now\"}}}]}}}",
        // ContentType.APPLICATION_JSON));

        httpPost.setEntity(new StringEntity("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"type\":{\"value\":\"eurtpgw\"}}}]}}}}"));
//         httpPost.setEntity(new StringEntity("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"type\":{\"value\":\"eurtpgw\"}}}],\"filter\":{\"range\":{\"timestamp\":{\"gte\":\"now-1m\",\"lte\":\"now\"}}}}}}"));
        //
        // if (null != headers) {
        // for (String key : headers.keySet()) {
        // httpPost.addHeader(key, headers.get(key));
        // }
        // }
        sendPost(httpclient, httpPost);

        // cm();
    }

    public static void cm() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext httpClientContext = HttpClientContext.adapt(new BasicHttpContext());

        HttpPost httpPost = new HttpPost("http://fancmeng:fancmeng@rpbt1rpd001.webex.com:7180/j_spring_security_check");

        response(httpclient.execute(httpPost, httpClientContext));

        HttpGet httpGet = new HttpGet("http://rpbt1rpd001.webex.com:7180/cmf/login?returnUrl=");
        response(httpclient.execute(httpGet, httpClientContext));

        HttpPost httpPost_x = new HttpPost("http://rpbt1rpd001.webex.com:7180/cmf/process/all/logs/search/api");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", "1510803016644"));
        params.add(new BasicNameValuePair("end", "1510803016644"));
        params.add(new BasicNameValuePair("offset", "0"));
        params.add(new BasicNameValuePair("num", "100"));
        params.add(new BasicNameValuePair("timeout", "60"));
        params.add(new BasicNameValuePair("level", "INFO"));
        params.add(new BasicNameValuePair("roleids", "616,503,617"));
        params.add(new BasicNameValuePair("requestStartTime", "1510804826176"));

        httpPost_x.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        response(httpclient.execute(httpPost_x, httpClientContext));
    }

    public static void sendGet(CloseableHttpClient httpclient, HttpGet httpGet) throws Exception {
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                System.out.printf("Request successfully.status={}", 200);
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    InputStream instream = responseEntity.getContent();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    System.out.println();
                    System.out.println("==============");
                    System.out.println(stringBuffer.toString());
                    System.out.println("==============");
                }
            } else {
                System.out.println("Request faild.status=" + statusLine.getStatusCode());
            }
        } finally {
            httpGet.releaseConnection();
        }
    }

    public static void sendPost(CloseableHttpClient httpclient, HttpPost httpPost) throws Exception {
        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                System.out.printf("Request successfully.status={}", 200);
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    InputStream instream = responseEntity.getContent();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    System.out.println();
                    System.out.println("==============");
                    System.out.println(stringBuffer.toString());
                    System.out.println("==============");
                }
            } else {
                System.out.println("Request faild.status=" + statusLine.getStatusCode());
            }
        } finally {
            httpPost.releaseConnection();
        }
    }

    public static void response(CloseableHttpResponse response) throws Exception {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            System.out.printf("Request successfully.status={}", 200);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                InputStream instream = responseEntity.getContent();
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                System.out.println();
                System.out.println("==============");
                System.out.println(stringBuffer.toString());
                System.out.println("==============");
            }
        } else {
            System.out.println("Request faild.status=" + statusLine.getStatusCode());
        }
    }
}
