package utils;

import checker.ImageSaver;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import helper.ProcessResponse;

import javax.annotation.Nonnull;
import java.util.*;

public class WallProcessor implements Runnable {

    private String domain;
    private int id = 0;
    private VKClient client;
    private boolean isDomain;
    private ImageSaver saver;

    public WallProcessor(@Nonnull String domain, @Nonnull VKClient client, ImageSaver saver) {
        this.domain = domain;
        this.client = client;
        this.isDomain = true;
        this.saver = saver;
    }

    @Override
    public void run() {
        while (true) {
            try {
                GetResponse getResponse;
                if (domain != null && !domain.isEmpty() && isDomain) {
                    getResponse = client.getClient().wall().get(client.getActor()).domain(domain).count(4).execute();
                } else if (id != 0) {
                    getResponse = client.getClient().wall().get(client.getActor()).ownerId(id).count(4).execute();
                } else {
                    //log that dont have id or domain
                    System.out.println("Dont have id or domain.");
                    return;
                }

                System.out.println("step 1");

                if (isDomain) {
                    List<HashMap<String, String>> data = ProcessResponse.process(getResponse, domain);
                    saver.saveFile(data);
                } else {
                    List<HashMap<String, String>> data = ProcessResponse.process(getResponse, "" + id);
                    saver.saveFile(data);
                }


                System.out.println("Current hour: " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                System.out.println("Current minute: " + Calendar.getInstance().get(Calendar.MINUTE));

                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    System.out.println("Interact wall processor wait");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Error in wall processor");
                System.out.println(e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
