package main;


import checker.ImageSaver;
import configs.Configuration;
import utils.VKClient;
import utils.WallProcessor;

import java.io.File;
import java.util.ArrayList;

public class Main {

//    private static int APP_ID = 6384672;
//    private static String CLIENT_SECRET = "5gOgcOmudwMX6Zmig7Do";
//    private static String REDIRECT_URI = "https://oauth.vk.com/blank.html";
//    private static String code = "44fe39a12cac3f32ae";


    public static void main(String[] args) throws Exception {

        File[] files = new File("./memeDownloaderProps").listFiles();
        ArrayList<Configuration> configs = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".properties")) {
                    configs.add(new Configuration(file));
                }
            }
        }

        for (Configuration config : configs) {
            VKClient vkClient = new VKClient(config);
            ImageSaver saver = new ImageSaver(config.getPath());
            String[] domains = config.getGroupDomains();

            for (String domain : domains) {
                new Thread(new WallProcessor(domain, vkClient, saver)).start();
                Thread.sleep(10000);
            }
        }
    }

}
