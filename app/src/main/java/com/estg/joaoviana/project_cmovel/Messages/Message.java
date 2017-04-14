package com.estg.joaoviana.project_cmovel.Messages;

/**
 * Created by PJ on 11/04/2017.
 */

public class Message {
    //private int id;
    private String name_other;
    private String title;
    private String body;
    //private int id_author;
    //private int id_reciver;


    public Message(String name_other, String title, String body ) {
        this.title = title;
        this.body = body;
        this.name_other = name_other;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName_other() {
        return name_other;
    }

    public void setName_other(String name_other) {
        this.name_other = name_other;
    }
}
