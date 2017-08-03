package bz.dcr.deinprotect.protection.entity;

import bz.dcr.deinprotect.protection.ProtectionType;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class AbstractProtection {

    private UUID owner;
    private Map<UUID, ProtectionMember> members;
    private Set<ProtectionPermission> publicPermissions;
    private ProtectionType type;


    public AbstractProtection() {
        this.members = new HashMap<>();
        this.publicPermissions = new HashSet<>();
    }

    public AbstractProtection(UUID owner, ProtectionType type) {
        this.owner = owner;
        this.type = type;
    }


    public boolean isOwner(UUID playerId) {
        return owner.equals(playerId);
    }

    public boolean isOwner(Player player) {
        return isOwner(player.getUniqueId());
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

    public boolean hasPublicPermission(ProtectionPermission permission) {
        return publicPermissions.contains(permission);
    }

    public void setPublicPermissions(Set<ProtectionPermission> publicPermissions) {
        this.publicPermissions = publicPermissions;
    }

    public void addPublicPermission(ProtectionPermission permission) {
        publicPermissions.add(permission);
    }

    public void removePublicPermission(ProtectionPermission permission) {
        publicPermissions.remove(permission);
    }

    public ProtectionType getType() {
        return type;
    }

    public void setType(ProtectionType type) {
        this.type = type;
    }


    public boolean hasPermission(Player player, ProtectionPermission permission) {
        // Player is owner
        if (isOwner(player)) {
            return true;
        }

        // Get member
        final ProtectionMember member = getMember(player.getUniqueId());

        // Player is member
        if (member != null) {
            return member.hasPermission(permission);
        }

        // Check public permissions
        return publicPermissions.contains(permission);
    }

}