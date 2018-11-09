package com.webex.dap.es.rest;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by harry on 6/6/18.
 */
public class HigherRestClientDemo {


    public static void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest();
    }

    public static void bulkRequest() {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("posts", "doc", "1").source(XContentType.JSON, "field", "foo"));
    }

    public static void searchRequest(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    }

    public static void main(String[] args) {
        RestClient restClient = RestClient.builder(new HttpHost("10.225.17.135", 9200, "https"))
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
                }).build();

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClient);


//        restHighLevelClient.searchScroll()


//        restClient.performRequest()
    }
}
