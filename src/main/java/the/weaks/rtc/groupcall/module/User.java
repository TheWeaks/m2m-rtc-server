package the.weaks.rtc.groupcall.module;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tzh on 2017/8/8.
 *
 * @author tzh
 * @since 1.7
 */
public class User implements Serializable {
    private String prefix;
    private String suffix;
    private String name;
    private Integer id;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String id, String name, String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.name = name;
        this.id = Integer.valueOf(id);
    }

    public User(String id) {
        this.prefix = UUID.randomUUID().toString();
        this.suffix = UUID.randomUUID().toString();
        this.name = UUID.randomUUID().toString();
        this.id = Integer.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public Number getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    public JsonObject toJson() {
        JsonObject user = new JsonObject();
        user.addProperty("userId", this.id);
        user.addProperty("name", this.getName());
        user.addProperty("prefix", this.getPrefix());
        user.addProperty("suffix", this.getSuffix());
        return user;
    }

    @Override
    public String toString() {
        return "User:" + toJson().toString();
    }
}
