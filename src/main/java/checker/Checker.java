package checker;


import java.util.HashMap;

public class Checker {

    private HashMap<String, Long> lastUpdate = new HashMap<>();
    private static Checker instance;

    public boolean isNew(String key, long time) {
        if (lastUpdate.get(key) == null) {
            lastUpdate.put(key, 0L);
        }
        return time > lastUpdate.get(key);
    }

    private Checker(String key, long lastUpdate) {
        this.lastUpdate.put(key, lastUpdate);
    }

    private Checker()
    {
    }

    public long getLastUpdate(String key) {
        return lastUpdate.get(key);
    }

    public void setLastUpdate(String key, long lastUpdate) {
        this.lastUpdate.put(key, lastUpdate);
    }

    public static Checker getInstance() {

        if (instance == null) {
            instance = new Checker();
        }
        return instance;

    }

}
