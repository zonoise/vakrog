package com.example.models.kanban;


/**
 * Created by zonoise on 2017/08/29.
 */
public enum  KanbanAxis {

    none("none"),
    user("user"),
    status("status"),
    milestone("milestone");

    KanbanAxis(final String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return code;
    }
//    public static KanbanAxis valueOf(String code){
//        for (KanbanAxis d : values()) {
//            if (d.getCode() == code) {
//                return d;
//            }
//        }
//        return NONE;
//    }
}
