package main;


import configs.Configuration;
import utils.VKClient;
import utils.WallProcessor;

public class Main {

//    private static int APP_ID = 6384672;
//    private static String CLIENT_SECRET = "5gOgcOmudwMX6Zmig7Do";
//    private static String REDIRECT_URI = "https://oauth.vk.com/blank.html";
//    private static String code = "44fe39a12cac3f32ae";


    public static void main(String[] args) throws Exception {

        String[] domains = Configuration.getInstance().getGroupDomains();
        VKClient vkClient = new VKClient();

        for (String domain : domains) {
            new Thread(new WallProcessor(domain, vkClient)).start();
            Thread.sleep(10000);
        }



    }

}
