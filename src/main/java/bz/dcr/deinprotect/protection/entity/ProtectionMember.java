package bz.dcr.deinprotect.protection.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProtectionMember {

    private UUID playerId;
    private Set<ProtectionPermission> permissions;


    public ProtectionMember(UUID playerId) {
        this.playerId = playerId;
        this.permissions = new HashSet<>();
    }

    public ProtectionMember(UUID playerId, Set<ProtectionPermission> permissions) {
        this.playerId = playerId;
        this.permissions = permissions;
    }


    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Set<ProtectionPermission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(ProtectionPermission permission) {
        return permissions.contains(permission);
    }

    public void setPermissions(Set<ProtectionPermission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(ProtectionPermission permission) {
        permissions.add(permission);
    }

    public void removePermission(ProtectionPermission permission) {
        permissions.remove(permission);
    }

}
