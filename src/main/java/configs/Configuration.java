package configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private String token;
    private Integer userId;
    private boolean useProxy;
    private boolean proxyAuth;
    private String proxyLogin;
    private String proxyPass;
    private String proxyHost;
    private Integer proxyPort;
    private String[] groupDomains;
    private String[] groupIds;
    private String path;

    private static Configuration instance;

    private Configuration() throws IOException, NumberFormatException {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(is);
        }
        this.token = properties.getProperty("token");
        String userId = properties.getProperty("userId");
        if (!userId.isEmpty()) {
            this.userId = Integer.parseInt(properties.getProperty("userId"));
        }
        this.useProxy = Boolean.parseBoolean(properties.getProperty("useProxy"));
        this.proxyAuth = Boolean.parseBoolean(properties.getProperty("proxyAuth"));
        this.proxyLogin = properties.getProperty("proxyLogin");
        this.proxyPass = properties.getProperty("proxyPass");
        this.proxyHost = properties.getProperty("proxyHost");
        String proxyPort = properties.getProperty("proxyPort");
        if (!proxyPort.isEmpty()) {
            this.proxyPort = Integer.parseInt(proxyPort);
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

    public boolean isUseProxy() {
        return useProxy;
    }

    public boolean isProxyAuth() {
        return proxyAuth;
    }

    public String getProxyLogin() {
        return proxyLogin;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
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

    public static Configuration getInstance() throws IOException {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
