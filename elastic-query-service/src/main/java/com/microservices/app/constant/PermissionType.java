package com.microservices.app.constant;

public enum PermissionType {

    READ("READ"), WRITE("WRITE"), ADMIN("ADMIN");

    private final String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
