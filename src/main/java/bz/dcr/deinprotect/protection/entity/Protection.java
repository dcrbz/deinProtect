package bz.dcr.deinprotect.protection.entity;

import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.ProtectionType;
import org.bson.types.ObjectId;
import org.bukkit.block.Block;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Protection extends AbstractProtection {

    @Id
    private ObjectId id;

    private List<BlockLocation> parts;


    // <editor-folding> Constructors
    protected Protection() {
        super();
        this.parts = new ArrayList<>();
    }

    public Protection(UUID owner, ProtectionType type, List<BlockLocation> parts) {
        super(owner, type);
        this.parts = parts;
    }
    // </editor-folding>


    public ObjectId getId() {
        return id;
    }

    public List<Block> getBlocks() {
        return getParts().stream()
                .map(BlockLocation::getBlock)
                .collect(Collectors.toList());
    }

    public List<BlockLocation> getParts() {
        return parts;
    }

    public void setParts(List<BlockLocation> parts) {
        this.parts = parts;
    }

    public void addPart(BlockLocation location) {
        this.parts.add(location);
    }

    public void removePart(BlockLocation location) {
        this.parts.remove(location);
    }

    public boolean hasPart(BlockLocation location) {
        return parts.contains(location);
    }

}
