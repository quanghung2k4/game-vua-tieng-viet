package baitaplon.nhom4.client.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String content;
    private Serializable data;

    public MessageModel() {
    }

    public MessageModel(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public MessageModel(String type, Serializable data) {
        this.type = type;
        this.data = data;
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

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }
}