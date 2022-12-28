package com.dabomstew.pkrandom.hac.unity;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.nio.ByteBuffer;

public class BundleFile {

    private LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
    public byte[] decompData;

    public BundleFile(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        BundleHeader header = new BundleHeader(bb);
        int totalDecompSize = 0;
        for (BlockInfo blockInfo: header.blockInfos) {
            totalDecompSize += blockInfo.decompSize;
        }
        decompData = new byte[totalDecompSize];
        int offset = 0;
        for (BlockInfo blockInfo: header.blockInfos) {
            byte[] compBlock = new byte[blockInfo.compSize];
            byte[] decompBlock = new byte[blockInfo.decompSize];
            bb.get(compBlock);
            if ((blockInfo.flags & 0x2) != 0) {
                decompressor.decompress(compBlock,decompBlock,blockInfo.decompSize);
            } else {
                decompBlock = compBlock;
            }
            System.arraycopy(decompBlock,0,decompData,offset,blockInfo.decompSize);
            offset += blockInfo.decompSize;
        }
    }

    private class BundleHeader {

        BlockInfo[] blockInfos;
        DirInfo[] dirInfos;

        private BundleHeader(ByteBuffer bb) {
            int compSize = bb.getInt(0x26);
            int decompSize = bb.getInt(0x2A);
            int flags = bb.getInt(0x2E);
            if ((flags & 0x80) != 0) {
                System.err.println("help");
            }
            byte[] compHeader = new byte[compSize];
            byte[] decompHeader = new byte[decompSize];
            bb.position(0x40);
            bb.get(compHeader);
            if ((flags & 0x2) != 0) {
                decompressor.decompress(compHeader,decompHeader,decompSize);
            } else {
                System.arraycopy(compHeader,0,decompHeader,0,compSize);
            }
            ByteBuffer headerBB = ByteBuffer.wrap(decompHeader);
            headerBB.position(0x10);
            int blockInfoCount = headerBB.getInt();
            blockInfos = new BlockInfo[blockInfoCount];
            for (int i = 0; i < blockInfoCount; i++) {
                blockInfos[i] = new BlockInfo(headerBB);
            }
            int nodesCount = headerBB.getInt();
            dirInfos = new DirInfo[nodesCount];
            for (int i = 0; i < nodesCount; i++) {
                dirInfos[i] = new DirInfo(headerBB);
            }
        }
    }

    private class BlockInfo {

        int decompSize, compSize, flags;
        private BlockInfo(ByteBuffer bb) {
            decompSize = bb.getInt();
            compSize = bb.getInt();
            flags = bb.getShort();
        }
    }

    private class DirInfo {

        long offset, size;
        int flags;
        String path;
        private DirInfo(ByteBuffer bb) {
            offset = bb.getLong();
            size = bb.getLong();
            flags = bb.getInt();
            int stringStart = bb.position();
            int stringLength = 0;
            while (bb.get() != 0) {
                stringLength++;
            }
            byte[] pathBytes = new byte[stringLength];
            bb.position(stringStart);
            bb.get(pathBytes);
            path = new String(pathBytes);
        }
    }
}
