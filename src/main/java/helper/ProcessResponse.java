package helper;

import checker.Checker;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallPost;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.json.JSONArray;

import java.util.*;

public class ProcessResponse {

    public static List<HashMap<String, String>> process(GetResponse response, String domain) {
        List<WallPostFull> posts = response.getItems();
        List<HashMap<String, String>> listOfData = new ArrayList<>();

        System.out.println("step 2");
        if (posts != null) {
            posts
                    .stream()
                    .filter(s -> Checker.getInstance().isNew(domain, s.getDate()))
                    .sorted(Comparator.comparingInt(WallPost::getDate))
                    .forEach(v -> {
                        try {
                            String wallText = v.getText();
                            List<WallpostAttachment> attachments = v.getAttachments();
                            JSONArray jsonArray = new JSONArray();
                            for (WallpostAttachment attachment : attachments) {
                                if (attachment.getType() == WallpostAttachmentType.PHOTO) {
                                    Photo photo = attachment.getPhoto();
                                    String sPhoto = photo.getPhoto604() != null ? photo.getPhoto604() :
                                            photo.getPhoto807() != null ? photo.getPhoto807() :
                                                    photo.getPhoto1280();
                                    if (sPhoto != null && !sPhoto.isEmpty()) {
                                        jsonArray.put(sPhoto);
                                    }
                                }
                            }
                            if (jsonArray.length() == 0)
                                return;
                            HashMap<String, String> map = new HashMap<>();
                            map.put("data", jsonArray.toString());
                            map.put("date", "" + v.getDate());
                            if (wallText != null && !wallText.isEmpty() && wallText.length() < 200) {
                                map.put("wallText", wallText);
                            }
                            listOfData.add(map);
                            Checker.getInstance().setLastUpdate(domain, v.getDate());
                        } catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            return listOfData;
        }
        return null;
    }

}
