package bz.dcr.deinprotect.protection.entity;

import bz.dcr.deinprotect.block.BlockLocation;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BaseProtection extends Protection {

    private List<BlockLocation> parts;


    // <editor-folding> Constructors
    public BaseProtection() {
        this.parts = new ArrayList<>();
    }

    public BaseProtection(Block block) {
        this();
        this.addPart(new BlockLocation(block));
    }

    public BaseProtection(Block... blocks) {
        this();
        for (int i = 0; i < blocks.length; i++) {
            this.addPart(new BlockLocation(blocks[i]));
        }
    }

    public BaseProtection(Collection<Block> blocks) {
        this();
        for (Block block : blocks) {
            this.addPart(new BlockLocation(block));
        }
    }
    // </editor-folding>


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
