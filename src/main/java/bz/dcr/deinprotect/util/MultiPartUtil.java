package bz.dcr.deinprotect.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Door;
import org.bukkit.material.Openable;

import java.util.ArrayList;
import java.util.List;

public class MultiPartUtil {

    /**
     * Get all parts remaining of a block
     * @param block The block to search all remaining parts for
     * @return List of all remaining parts of the given block
     */
    public static List<Block> getParts(Block block) {
        List<Block> parts = new ArrayList<>();

        if (block.getState().getData() instanceof Openable) {
            parts.addAll(getDoorParts(block));
        } else if (block.getState() instanceof InventoryHolder) {
            parts.addAll(getContainerParts(block));
        } else {
            parts.add(block);
        }

        return parts;
    }

    /**
     * Get the second part of a door
     * @param block The first part of the door. This can be the upper or the lower part.
     * @return List containing the second part of the given first part of the door
     */
    public static List<Block> getDoorParts(Block block) {
        List<Block> parts = new ArrayList<>();

        parts.add(block);

        if (block.getState().getData() instanceof Door) {
            final Door door = (Door) block.getState().getData();

            parts.add(block);

            if (door.isTopHalf()) {
                parts.add(block.getRelative(BlockFace.DOWN));
            } else {
                parts.add(block.getRelative(BlockFace.UP));
            }
        } else if (block.getState().getData() instanceof Openable) {
            parts.add(block);
        }

        return parts;
    }

    /**
     * Get the second part of a chest
     * @param block The first part of the chest. I doesn't matter which part of the chest this is.
     * @return List containing the second part of the given first part of the chest
     */
    public static List<Block> getContainerParts(Block block) {
        List<Block> parts = new ArrayList<>();

        parts.add(block);

        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            final Material chestType = block.getType();

            Block secondPart;

            if ((secondPart = block.getRelative(BlockFace.NORTH)).getType() == chestType) {
                parts.add(secondPart);
            } else if ((secondPart = block.getRelative(BlockFace.EAST)).getType() == chestType) {
                parts.add(secondPart);
            } else if ((secondPart = block.getRelative(BlockFace.SOUTH)).getType() == chestType) {
                parts.add(secondPart);
            } else if ((secondPart = block.getRelative(BlockFace.WEST)).getType() == chestType) {
                parts.add(secondPart);
            }
        }

        return parts;
    }

}
