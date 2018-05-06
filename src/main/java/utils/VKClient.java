package utils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import configs.Configuration;
import helper.VkHttpClient;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class VKClient {

    private VkApiClient client;
    private UserActor actor;

    public VKClient() {
        try {

            Configuration conf = Configuration.getInstance();
            TransportClient transportClient = new HttpTransportClient();
            if (conf.isUseProxy()) {
                HttpClient httpClient = VkHttpClient.httpClient();
                Field httpClientField = HttpTransportClient.class.getDeclaredField("httpClient");
                httpClientField.setAccessible(true);
                httpClientField.set(null, httpClient);
            }
            this.client = new VkApiClient(transportClient);
            this.actor = new UserActor(conf.getUserId(), conf.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VkApiClient getClient() {
        return client;
    }

    public UserActor getActor() {
        return actor;
    }
}
