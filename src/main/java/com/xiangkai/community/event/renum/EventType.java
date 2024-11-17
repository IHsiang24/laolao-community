package com.xiangkai.community.event.renum;

public enum EventType {
    COMMENT("COMMENT"),
    LIKE("LIKE"),
    FOLLOW("FOLLOW"),
    PUBLISH("PUBLISH"),
    DELETE("DELETE"),
    REGISTER("REGISTER");

    private String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
