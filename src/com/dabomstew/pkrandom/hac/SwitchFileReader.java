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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class SwitchFileReader {
    private String extractedFilesPath;
    private boolean writingEnabled;
    private Map<String, SwitchRomfsFile> romfsFiles;
    private String tmpFolder;
    private boolean mainOpen, mainChanged;
    private byte[] mainRamstored;

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

    public String getTmpFolder() {
        return tmpFolder;
    }

    public String getExtractedFilesPath() {
        return extractedFilesPath;
    }

    public boolean isWritingEnabled() {
        return writingEnabled;
    }

    // Retrieves main (the game's executable). There is no decompression yet.
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
        String layeredFSRootPath = outputPath + File.separator + "switch_game" + File.separator;
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
