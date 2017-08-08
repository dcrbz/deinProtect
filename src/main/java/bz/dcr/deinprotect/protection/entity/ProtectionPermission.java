package bz.dcr.deinprotect.protection.entity;

public enum ProtectionPermission {

    INTERACT("Protection-Permission.Interact"),
    MANAGE("Protection-Permission.Manage"),
    BREAK("Protection-Permission.Break"),

    CONTAINER_OPEN("Protection-Permission.Container-Open"),
    CONTAINER_PUT_ITEM("Protection-Permission.Container-Put-Item"),
    CONTAINER_TAKE_ITEM("Protection-Permission.Container-Take-Item"),

    DOOR_OPEN("Protection-Permission.Door-Open"),
    DOOR_CLOSE("Protection-Permission.Door-Close");


    private String langKey;

    ProtectionPermission(String langKey) {
        this.langKey = langKey;
    }

    public String getLangKey() {
        return langKey;
    }

}
