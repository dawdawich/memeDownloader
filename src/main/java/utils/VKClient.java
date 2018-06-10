package utils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import configs.Configuration;

public class VKClient {

    private VkApiClient client;
    private UserActor actor;

    public VKClient(Configuration conf) {
        try {

            TransportClient transportClient = new HttpTransportClient();
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
