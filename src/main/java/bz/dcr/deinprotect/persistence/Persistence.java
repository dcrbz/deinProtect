package bz.dcr.deinprotect.persistence;

import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;

import java.io.Closeable;

public interface Persistence extends Closeable {

    void saveProtection(Protection protection);

    void deleteProtection(Protection protection);

    Protection getProtection(BlockLocation location);

}
