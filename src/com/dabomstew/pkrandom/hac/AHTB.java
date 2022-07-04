package com.dabomstew.pkrandom.hac;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class AHTB {
    private AHTBEntry[] entries;
    private static final int magic = 0x42544841;
    public Map<Long,String> map = new HashMap<>();

    public AHTB(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.getInt();    // Magic
        int count = bb.getInt();
        entries = new AHTBEntry[count];
        for (int i = 0; i < count; i++) {
            AHTBEntry entry = new AHTBEntry(bb);
            entries[i] = entry;
            if (!map.containsKey(entry.hash)) {
                map.put(entry.hash, entry.name);
            }
        }
    }

    private class AHTBEntry {
        private long hash;
        private short nameLength;
        private String name;

        private AHTBEntry(ByteBuffer bb) {
            hash = bb.getLong();
            nameLength = bb.getShort();
            byte[] nameBytes = new byte[nameLength - 1];
            bb.get(nameBytes);
            name = new String(nameBytes);
            bb.get();   // Null byte
        }

        private byte[] getBytes() {
            ByteBuffer bb = ByteBuffer.allocate(12 + nameLength);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putLong(hash);
            bb.putShort(nameLength);
            bb.put(name.getBytes());
            bb.put((byte)0);
            return bb.array();
        }
    }
}
