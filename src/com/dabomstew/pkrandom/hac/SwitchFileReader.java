package com.dabomstew.pkrandom.hac;

/*----------------------------------------------------------------------------*/
/*--  SwitchFileReader.java - WIP class for reading Switch files.           --*/
/*--                                                                        --*/
/*--  Part of "Universal Pokemon Randomizer ZX" by the UPR-ZX team          --*/
/*--  Pokemon and any associated names and the like are                     --*/
/*--  trademark and (C) Nintendo 1996-2020.                                 --*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.SysConstants;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class SwitchFileReader {
    private String extractedFilesPath;
    private String contentId;
    private boolean writingEnabled;
    private Map<String, SwitchRomfsFile> romfsFiles;
    private String tmpFolder;
    private boolean mainOpen, mainChanged;
    private byte[] mainRamstored;

    private static final int nso_header_size = 0x101;


    public SwitchFileReader(String extractedFilesPath) {
        this.extractedFilesPath = extractedFilesPath;
        this.romfsFiles = new HashMap<>();

        // TMP folder?
        // TODO: Actually give this a correct name other than "some_switch_game"
        String rawFilename = new File("some_switch_game").getName();
        String dataFolder = "tmp_" + rawFilename;
        // remove nonsensical chars
        dataFolder = dataFolder.replaceAll("[^A-Za-z0-9_]+", "");
        File tmpFolder = new File(SysConstants.ROOT_PATH + dataFolder);
        tmpFolder.mkdirs();
        if (tmpFolder.canWrite()) {
            writingEnabled = true;
            this.tmpFolder = SysConstants.ROOT_PATH + dataFolder + File.separator;
            tmpFolder.deleteOnExit();
        } else {
            writingEnabled = false;
        }
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTmpFolder() {
        return tmpFolder;
    }

    public String getExtractedFilesPath() {
        return extractedFilesPath;
    }

    public boolean isWritingEnabled() {
        return writingEnabled;
    }

    public boolean isMainCompressed(byte[] main) throws IOException {
        int flags = FileFunctions.readFullInt(main, 0xC);
        boolean textCompress = (flags & 1) != 0;
        boolean roCompress = (flags & 2) != 0;
        boolean dataCompress = (flags & 4) != 0;
        return textCompress || roCompress || dataCompress;
    }

    public byte[] decompressMain(byte[] compressed) {
        // Read offsets and sizes from uncompressed header
        int textOffset = FileFunctions.readFullInt(compressed, 0x10);
        int textCompressedSize = FileFunctions.readFullInt(compressed, 0x60);
        int textDecompressedSize = FileFunctions.readFullInt(compressed, 0x18);
        int roOffset = FileFunctions.readFullInt(compressed, 0x20);
        int roCompressedSize = FileFunctions.readFullInt(compressed, 0x64);
        int roDecompressedSize = FileFunctions.readFullInt(compressed, 0x28);
        int dataOffset = FileFunctions.readFullInt(compressed, 0x30);
        int dataCompressedSize = FileFunctions.readFullInt(compressed, 0x68);
        int dataDecompressedSize = FileFunctions.readFullInt(compressed, 0x38);
        LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();

        // Check to see which sections need decompressing
        int flags = FileFunctions.readFullInt(compressed, 0xC);
        boolean textCompress = (flags & 1) != 0;
        boolean roCompress = (flags & 2) != 0;
        boolean dataCompress = (flags & 4) != 0;

        // Decompress the .text, .rodata, and .data sections if needed
        byte[] text;
        byte[] compressedText = new byte[textCompressedSize];
        System.arraycopy(compressed, textOffset, compressedText, 0, compressedText.length);
        if (textCompress) {
            byte[] decompressedText = new byte[textDecompressedSize];
            decompressor.decompress(compressedText, decompressedText, textDecompressedSize);
            text = decompressedText;
        } else {
            text = compressedText;
        }

        byte[] ro;
        byte[] compressedRo = new byte[roCompressedSize];
        System.arraycopy(compressed, roOffset, compressedRo, 0, compressedRo.length);
        if (roCompress) {
            byte[] decompressedRo = new byte[roDecompressedSize];
            decompressor.decompress(compressedRo, decompressedRo, roDecompressedSize);
            ro = decompressedRo;
        } else {
            ro = compressedRo;
        }

        byte[] data;
        byte[] compressedData = new byte[dataCompressedSize];
        System.arraycopy(compressed, dataOffset, compressedData, 0, compressedData.length);
        if (dataCompress) {
            byte[] decompressedData = new byte[dataDecompressedSize];
            decompressor.decompress(compressedData, decompressedData, dataDecompressedSize);
            data = decompressedData;
        } else {
            data = compressedData;
        }

        // Copy the original header and modify as appropriate
        byte[] header = new byte[nso_header_size];
        System.arraycopy(compressed, 0, header, 0, nso_header_size);
        int newTextOffset = header.length;
        int newRoOffset = newTextOffset + text.length;
        int newDataOffset = newRoOffset + ro.length;
        FileFunctions.writeFullInt(header, 0xC, 0); // Clear out all flags (tells the Switch not to try decompressing anything, and don't check hashes).
        FileFunctions.writeFullInt(header, 0x10, newTextOffset);
        FileFunctions.writeFullInt(header, 0x20, newRoOffset);
        FileFunctions.writeFullInt(header, 0x30, newDataOffset);
        FileFunctions.writeFullInt(header, 0x60, text.length);
        FileFunctions.writeFullInt(header, 0x64, ro.length);
        FileFunctions.writeFullInt(header, 0x68, data.length);
        for (int i = 0xA0; i < 0x100; i++) {
            // Clear out hashes of .text/.rodata/.data, since we don't need them, and since the flags say not to look at this.
            header[i] = 0x00;
        }

        byte[] decompressed = new byte[header.length + text.length + ro.length + data.length];
        System.arraycopy(header, 0, decompressed, 0, header.length);
        System.arraycopy(text, 0, decompressed, newTextOffset, text.length);
        System.arraycopy(ro, 0, decompressed, newRoOffset, ro.length);
        System.arraycopy(data, 0, decompressed, newDataOffset, data.length);

        return decompressed;
    }

    // Retrieves a decompressed version of main (the game's executable).
    // The first time this is called, it will retrieve it straight from the
    // extracted folder. Future calls will rely on a cached version to speed
    // things up. If writing is enabled, it will cache the decompressed version
    // to the tmpFolder; otherwise, it will store it in RAM.
    public byte[] getMain() throws IOException {
        String mainPath = this.extractedFilesPath + File.separator + "exefs" + File.separator + "main";
        if (!mainOpen) {
            mainOpen = true;
            RandomAccessFile mainFile = new RandomAccessFile(mainPath, "r");
            byte[] main = new byte[(int)mainFile.length()];
            mainFile.readFully(main);

            if (isMainCompressed(main)) {
                main = decompressMain(main);
            }

            // Now actually make the copy or w/e
            if (writingEnabled) {
                File arm9file = new File(tmpFolder + "main");
                FileOutputStream fos = new FileOutputStream(arm9file);
                fos.write(main);
                fos.close();
                arm9file.deleteOnExit();
                this.mainRamstored = null;
                return main;
            } else {
                this.mainRamstored = main;
                byte[] newcopy = new byte[main.length];
                System.arraycopy(main, 0, newcopy, 0, main.length);
                return newcopy;
            }
        } else {
            if (writingEnabled) {
                return FileFunctions.readFileFullyIntoBuffer(tmpFolder + "main");
            } else {
                byte[] newcopy = new byte[this.mainRamstored.length];
                System.arraycopy(this.mainRamstored, 0, newcopy, 0, this.mainRamstored.length);
                return newcopy;
            }
        }
    }

    public void writeMain(byte[] main) throws IOException {
        if (!mainOpen) {
            getMain();
        }
        mainChanged = true;
        if (writingEnabled) {
            FileOutputStream fos = new FileOutputStream(new File(tmpFolder + "main"));
            fos.write(main);
            fos.close();
        } else {
            if (this.mainRamstored.length == main.length) {
                // copy new in
                System.arraycopy(main, 0, this.mainRamstored, 0, main.length);
            } else {
                // make new array
                this.mainRamstored = null;
                this.mainRamstored = new byte[main.length];
                System.arraycopy(main, 0, this.mainRamstored, 0, main.length);
            }
        }
    }

    // returns null if file doesn't exist
    public byte[] getFile(String filename) throws IOException {
        if (!romfsFiles.containsKey(filename)) {
            SwitchRomfsFile file = new SwitchRomfsFile(this);
            file.fullPath = filename;
            romfsFiles.put(filename, file);
        }
        return romfsFiles.get(filename).getContents();
    }

    public void writeFile(String filename, byte[] data) throws IOException {
        if (!romfsFiles.containsKey(filename)) {
            SwitchRomfsFile file = new SwitchRomfsFile(this);
            file.fullPath = filename;
            romfsFiles.put(filename, file);
        }
        romfsFiles.get(filename).writeOverride(data);
    }

    public void saveAsLayeredFS(String outputPath) throws IOException {
        String layeredFSRootPath = outputPath + File.separator + this.contentId + File.separator;
        File layeredFSRootDir = new File(layeredFSRootPath);
        if (!layeredFSRootDir.exists()) {
            layeredFSRootDir.mkdirs();
        } else {
            purgeDirectory(layeredFSRootDir);
        }
        String romfsRootPath = layeredFSRootPath + "romfs" + File.separator;
        File romfsDir = new File(romfsRootPath);
        if (!romfsDir.exists()) {
            romfsDir.mkdirs();
        }

        if (mainChanged) {
            // TODO: Is this what you do with main on Switch? I genuinely don't know
            byte[] main = getMain();
            FileOutputStream fos = new FileOutputStream(new File(layeredFSRootPath + "main"));
            fos.write(main);
            fos.close();
        }

        for (Map.Entry<String, SwitchRomfsFile> entry : romfsFiles.entrySet()) {
            SwitchRomfsFile file = entry.getValue();
            if (file.fileChanged) {
                writeRomfsFileToLayeredFS(file, romfsRootPath);
            }
        }
    }

    private void purgeDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

    private void writeRomfsFileToLayeredFS(SwitchRomfsFile file, String layeredFSRootPath) throws IOException {
        String[] romfsPathComponents = file.fullPath.split("/");
        StringBuffer buffer = new StringBuffer(layeredFSRootPath);
        for (int i = 0; i < romfsPathComponents.length - 1; i++) {
            buffer.append(romfsPathComponents[i]);
            buffer.append(File.separator);
            File currentDir = new File(buffer.toString());
            if (!currentDir.exists()) {
                currentDir.mkdirs();
            }
        }
        buffer.append(romfsPathComponents[romfsPathComponents.length - 1]);
        String romfsFilePath = buffer.toString();
        FileOutputStream fos = new FileOutputStream(new File(romfsFilePath));
        fos.write(file.getOverrideContents());
        fos.close();
    }
}
