package helper;

import configs.Configuration;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class VkHttpClient {

    public static HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
        SSLContextBuilder contextBuilder = new SSLContextBuilder();
        contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(contextBuilder.build());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        String proxyHost = Configuration.getInstance().getProxyHost();
        int proxyPort = Configuration.getInstance().getProxyPort();
        HttpClientBuilder clientBuilder = HttpClients.custom();

        if (Configuration.getInstance().isProxyAuth()) {
            String proxyUsername = "dawdawich";
            String proxyPass = "A9g2YcG";
            credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
                    new UsernamePasswordCredentials(proxyUsername, proxyPass));
            clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
        }

        clientBuilder.setSSLSocketFactory(socketFactory);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        clientBuilder.setDefaultRequestConfig(requestConfig);



        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(300);
        connectionManager.setDefaultMaxPerRoute(300);
        clientBuilder.setConnectionManager(connectionManager);

        BasicCookieStore cookieStore = new BasicCookieStore();
        clientBuilder.setDefaultCookieStore(cookieStore);
        clientBuilder.setUserAgent("Java VK SDK/0.5.9");


        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        clientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

            private long lastApiCallTime;

            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                long cooldown = System.currentTimeMillis() - lastApiCallTime;
                if (cooldown < 1000) {
                    try {
                        Thread.sleep(1000 - cooldown);
                    } catch (InterruptedException ignored) {
                    }
                }
                lastApiCallTime = System.currentTimeMillis();
            }
        });

        return clientBuilder.build();
    }

}
