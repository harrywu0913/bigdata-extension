package com.webex.dap.es.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;

/**
 * Created by harry on 6/6/18.
 */
public class LowerRestClientDemo {


    static RestClient restClient;

    static {
        restClient = RestClient.builder(new HttpHost("10.225.17.135", 9200, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {

                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("clptest", "clppass"));
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                        try {
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
                            httpClientBuilder.setSSLContext(sslContext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return httpClientBuilder;
                    }
                })
                .build();
    }

    public static void postData() throws IOException {
        Map<String, String> params = Collections.emptyMap();
        HttpEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON);
        restClient.performRequest("PUT","/post1/doc/1",params,entity);
    }

    public static void main(String[] args) throws IOException {

        Response response = restClient.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));

        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        restClient.close();
    }
}
