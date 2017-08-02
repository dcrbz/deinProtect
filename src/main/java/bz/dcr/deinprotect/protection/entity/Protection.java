package bz.dcr.deinprotect.protection.entity;

import java.util.*;

public abstract class Protection {

    private UUID owner;
    private Map<UUID, ProtectionMember> members;
    private Set<ProtectionPermission> publicPermissions;


    public Protection() {
        this.members = new HashMap<>();
        this.publicPermissions = new HashSet<>();
    }


    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public ProtectionMember getMember(UUID playerId) {
        return members.get(playerId);
    }

    public Map<UUID, ProtectionMember> getMembers() {
        return members;
    }

    public void setMembers(Map<UUID, ProtectionMember> members) {
        this.members = members;
    }

    public Set<ProtectionPermission> getPublicPermissions() {
        return publicPermissions;
    }

    public void setPublicPermissions(Set<ProtectionPermission> publicPermissions) {
        this.publicPermissions = publicPermissions;
    }

}
