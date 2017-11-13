package bz.dcr.deinprotect.protection.entity;

public enum ProtectionPermission {

    INTERACT("Protection-Permission.Interact", "deinprotect.bypass.interact"),
    MANAGE("Protection-Permission.Manage", "deinprotect.bypass.manage"),
    BREAK("Protection-Permission.Break", "deinprotect.bypass.break"),

    CONTAINER_OPEN("Protection-Permission.Container-Open", "deinprotect.bypass.open"),
    CONTAINER_PUT_ITEM("Protection-Permission.Container-Put-Item", "deinprotect.bypass.putitem"),
    CONTAINER_TAKE_ITEM("Protection-Permission.Container-Take-Item", "deinprotect.bypass.takeitem"),

    DOOR_OPEN("Protection-Permission.Door-Open", "deinprotect.bypass.opendoor"),
    DOOR_CLOSE("Protection-Permission.Door-Close", "deinprotect.bypass.closedoor");


    private String langKey;
    private String bypassPermission;

    ProtectionPermission(String langKey, String bypassPermission) {
        this.langKey = langKey;
        this.bypassPermission = bypassPermission;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

}
