package cz.zk;

import lombok.Data;

@Data
public class AbtMessage {

    private String Name;
    private String Body;

    public AbtMessage(String name, String body) {
        this.Name = name;
        this.Body = body;
    }
}
