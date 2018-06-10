package checker;

import configs.Configuration;
import org.json.JSONArray;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ImageSaver {

    private String path;

    public ImageSaver(String path) {
        this.path = path;
    }

    public void saveFile(List<HashMap<String, String>> list) {
        list
        .forEach(s -> {
            try {
                JSONArray jsonArray = new JSONArray(s.get("data"));
                String wallText = s.get("wallText");
                if (wallText != null && !wallText.isEmpty()) {
                    Path tPath = Paths.get(path + File.separator + s.get("date") + ".txt");
                    Files.write(tPath, Collections.singletonList(wallText), Charset.forName("UTF-8"));
                }
                if (jsonArray.length() > 1) {
                    saveFiles(s, jsonArray);
                    return;
                }
                Path iPath = Paths.get(path + File.separator + s.get("date") + ".jpg");

                Path p = Files.write(iPath, recoverImageFromUrl(jsonArray.getString(0)), StandardOpenOption.CREATE);
                System.out.println(p.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error with saving file");
                e.printStackTrace();
            }
        });
        list.clear();
    }

    private void saveFiles(HashMap<String, String> map, JSONArray jsonArray) throws Exception {
        File file = new File(path + File.separator + map.get("date"));
        file.mkdirs();
        for (int i = 0; i < jsonArray.length(); i++) {
            Files.write(Paths.get(file.getAbsolutePath() + File.separator + i + ".jpg"), recoverImageFromUrl(jsonArray.getString(i)),StandardOpenOption.CREATE);
        }

    }

    private byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int n;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }

}
