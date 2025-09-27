package baitaplon.nhom4.client.model;

import java.io.Serializable;

public class MessageModel implements Serializable{
    private String type;
    private String content;

    public MessageModel(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
}
