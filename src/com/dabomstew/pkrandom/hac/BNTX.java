package com.dabomstew.pkrandom.hac;

import com.dabomstew.pkrandom.FileFunctions;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BNTX {

    private final int BYTES_PER_PIXEL = 8;
    private final int BLOCK_WIDTH = 4;
    private final int BLOCK_HEIGHT = 4;
    private int width, height;
    private int[] decImageData;

    public BNTX(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        NXHeader NXHeader = new NXHeader(bb);
        BRTIInfo info = new BRTIInfo(bb,bb.getInt(NXHeader.infoPtrAddr));
        width = info.width;
        height = info.height;
        int dataAddr = bb.getInt(info.ptrsAddr);
        byte[] imageData = new byte[info.imageSize];
        bb.position(dataAddr);
        bb.get(imageData);

        int deSwizzledSize =
                divideRoundUp(info.width,BLOCK_WIDTH) * divideRoundUp(info.height,BLOCK_HEIGHT) * BYTES_PER_PIXEL;
        byte[] deSwizzledResult = deSwizzle(info.width,info.height,info.alignment,info.sizeRange,imageData);
        byte[] realResult = new byte[deSwizzledSize];
        System.arraycopy(deSwizzledResult,0,realResult,0,realResult.length);
        decImageData = decompress(realResult,info.width,info.height);
    }

    private class NXHeader {

        int magic;
        int count;
        int infoPtrAddr, dataBlkAddr, dictAddr;
        int strDictSize;

        private NXHeader(ByteBuffer bb) {
            magic = bb.getInt(0x20);
            count = bb.getInt(0x24);
            infoPtrAddr = bb.getInt(0x28);
            dataBlkAddr = bb.getInt(0x30);
            dictAddr = bb.getInt(0x38);
            strDictSize = bb.getInt(0x40);
        }
    }

    private class BRTIInfo {

        int width, height, sizeRange, imageSize, alignment, compSel, ptrsAddr;
        private BRTIInfo(ByteBuffer bb, int brtiOffset) {
            width = bb.getInt(brtiOffset + 0x24);
            height = bb.getInt(brtiOffset + 0x28);
            sizeRange = bb.getInt(brtiOffset + 0x34);
            imageSize = bb.getInt(brtiOffset + 0x50);
            alignment = bb.getInt(brtiOffset + 0x54);
            compSel = bb.getInt(brtiOffset + 0x58);
            ptrsAddr = bb.getInt(brtiOffset + 0x70);
        }
    }

    private int[] decompress(byte[] data, int width, int height) {
        int[] out = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte[] pixels = fetch2DTexel(width, data, x, y);
                int pos = (y * width + x);
                out[pos] = ((pixels[3] & 0xFF) |
                        ((pixels[2] & 0xFF) << 8) |
                        ((pixels[1] & 0xFF) << 16) |
                        ((pixels[0] & 0xFF) << 24));
            }
        }
        return out;
    }

    private byte[] fetch2DTexel(int width, byte[] data, int i, int j) {
        byte[] out = new byte[4];
        int blkSrc = (((width + 3) / 4) * (j / 4) + (i / 4)) * 8;
        int color0 = FileFunctions.read2ByteInt(data, blkSrc);
        int color1 = FileFunctions.read2ByteInt(data, blkSrc + 2);
        int bits = FileFunctions.readFullInt(data, blkSrc + 4);
        int bitPos = 2 * ((j & 3) * 4 + (i & 3));
        int code = (bits >>> bitPos) & 3;

        out[0] = (byte)0xFF;
        switch (code) {
            case 0:
                out[1] = (byte)getRValue(color0);
                out[2] = (byte)getGValue(color0);
                out[3] = (byte)getBValue(color0);
                break;
            case 1:
                out[1] = (byte)getRValue(color1);
                out[2] = (byte)getGValue(color1);
                out[3] = (byte)getBValue(color1);
                break;
            case 2:
                if (color0 > color1) {
                    out[1] = (byte)((getRValue(color0) * 2 + getRValue(color1)) / 3);
                    out[2] = (byte)((getGValue(color0) * 2 + getGValue(color1)) / 3);
                    out[3] = (byte)((getBValue(color0) * 2 + getBValue(color1)) / 3);
                } else {
                    out[1] = (byte)((getRValue(color0) + getRValue(color1)) / 2);
                    out[2] = (byte)((getGValue(color0) + getGValue(color1)) / 2);
                    out[3] = (byte)((getBValue(color0) + getBValue(color1)) / 2);
                }
                break;
            case 3:
                if (color0 > color1) {
                    out[1] = (byte)((getRValue(color0) + getRValue(color1) * 2) / 3);
                    out[2] = (byte)((getGValue(color0) + getGValue(color1) * 2) / 3);
                    out[3] = (byte)((getBValue(color0) + getBValue(color1) * 2) / 3);
                } else {
                    out[0] = 0;
                    out[1] = 0;
                    out[2] = 0;
                    out[3] = 0;
                }
                break;
            default:
                break;
        }

        return out;
    }

    private int getRValue(int packedCol) {
        return (((packedCol >>> 8) & 0xF8) | ((packedCol >>> 13) & 0x7));
    }

    private int getGValue(int packedCol) {
        return (((packedCol >>> 3) & 0xFC) | ((packedCol >>> 9) & 0x3));
    }

    private int getBValue(int packedCol) {
        return (((packedCol << 3) & 0xF8) | ((packedCol >>> 2) & 0x7));
    }

    private int divideRoundUp(int num, int denom) {
        return (int) Math.ceil((double)num / denom);
    }

    private int roundUp(int a, int b) {
        return ((a - 1) | (b - 1)) + 1;
    }

    private byte[] deSwizzle(int width, int height, int alignment, int sizeRange, byte[] data) {
        int blockHeight = 1 << sizeRange;
        width = divideRoundUp(width,BLOCK_WIDTH);
        height = divideRoundUp(height,BLOCK_HEIGHT);
        int pitch = roundUp(width * BYTES_PER_PIXEL, 0x40);
        int surfSize = roundUp(pitch * roundUp(height, blockHeight * 8), alignment);

        byte[] result = new byte[surfSize];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pos = getAddrBlockLinear(x,y,width,0,blockHeight);
                int pos2 = (y * width + x) * BYTES_PER_PIXEL;
                if (pos + BYTES_PER_PIXEL < surfSize) {
                    System.arraycopy(data,pos,result,pos2,BYTES_PER_PIXEL);
                }
            }
        }

        return result;
    }

    private int getAddrBlockLinear(int x, int y, int imageWidth, int baseAddr, int blockHeight) {
        int imageWidthInGobs = divideRoundUp(imageWidth * BYTES_PER_PIXEL, 64);
        int gobAddr = baseAddr +
                (y / (8 * blockHeight)) * 512 * blockHeight * imageWidthInGobs +
                (x * BYTES_PER_PIXEL / 64) * 512 * blockHeight +
                (y % (8 * blockHeight) / 8) * 512;
        x *= BYTES_PER_PIXEL;
        return gobAddr + ((x % 64) / 32) * 256 + ((y % 8) / 2) * 64 + ((x % 32) / 16) * 32 + (y % 2) * 16 + (x % 16);
    }

    public BufferedImage getImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pos = (y * width + x);
                int color = decImageData[pos];
                image.setRGB(x, y, color);
            }
        }
        return image;
    }
}
