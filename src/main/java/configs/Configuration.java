package configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private String token;
    private Integer userId;
    private String[] groupDomains;
    private String[] groupIds;
    private String path;

    public Configuration(File props) throws IOException, NumberFormatException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(props)) {
            properties.load(is);
        }
        this.token = properties.getProperty("token");
        String userId = properties.getProperty("userId");
        if (!userId.isEmpty()) {
            this.userId = Integer.parseInt(properties.getProperty("userId"));
        }
        String domains = properties.getProperty("groupDomains");
        if (!domains.isEmpty()) {
            if (domains.contains(";")) {
                this.groupDomains = domains.split(";");
            } else {
                this.groupDomains = new String[]{domains};
            }
        }
        String ids = properties.getProperty("groupIds");
        if (!ids.isEmpty()) {
            if (domains.contains(";")) {
                this.groupIds = domains.split(";");
            } else {
                this.groupIds = new String[]{domains};
            }
        }
        this.path = properties.getProperty("path");
        File file = new File(path);
        file.mkdirs();
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public String[] getGroupDomains() {
        return groupDomains;
    }

    public String[] getGroupIds() {
        return groupIds;
    }

    public String getPath() {
        return path;
    }

}
