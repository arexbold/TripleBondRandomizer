package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  AbstractSwitchRomHandler.java - a base class for Switch rom handlers  --*/
/*--                                  which standardizes common Switch      --*/
/*--                                  functions.                            --*/
/*--                                                                        --*/
/*--  Part of "Universal Pokemon Randomizer ZX" by the UPR-ZX team          --*/
/*--  Pokemon and any associated names and the like are                     --*/
/*--  trademark and (C) Nintendo 1996-2020.                                 --*/
/*--                                                                        --*/
/*--  The custom code written here is licensed under the terms of the GPL:  --*/
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

import com.dabomstew.pkrandom.ctr.RomfsFile;
import com.dabomstew.pkrandom.exceptions.RandomizerIOException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class AbstractSwitchRomHandler extends AbstractRomHandler {

    private String loadedFN;
    private Map<String, byte[]> changedFiles;
    private byte[] main;
    private boolean mainChanged;

    public AbstractSwitchRomHandler(Random random, PrintStream logStream) {
        super(random, logStream);
    }

    @Override
    public boolean loadRom(String filename) {
        if (!this.detectSwitchGame(filename)) {
            return false;
        }
        loadedFN = filename;
        changedFiles = new HashMap<>();
        this.loadedROM(filename);
        return true;
    }

    protected abstract boolean detectSwitchGame(String filePath);

    @Override
    public String loadedFilename() {
        return loadedFN;
    }

    protected abstract void loadedROM(String filename);

    protected abstract void savingROM() throws IOException;

    protected abstract String getGameAcronym();

    @Override
    public boolean saveRomFile(String filename, long seed) {
        try {
            savingROM();
            // TODO: Make this unsupported for Switch games
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return true;
    }

    @Override
    public boolean saveRomDirectory(String filename) {
        try {
            savingROM();
            this.writeLayeredFS(filename);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return true;
    }

    // TODO: Make this better, probably want a class for reading/writing Switch stuff
    private void writeLayeredFS(String filename) throws IOException {
        String layeredFSRootPath = filename + File.separator + "switch game" + File.separator;
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
            FileOutputStream fos = new FileOutputStream(new File(layeredFSRootPath + "main"));
            fos.write(main);
            fos.close();
        }

        for (Map.Entry<String, byte[]> entry : changedFiles.entrySet()) {
            byte[] file = entry.getValue();
            writeRomfsFileToLayeredFS(file, romfsRootPath);
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

    private void writeRomfsFileToLayeredFS(String path, byte[] file, String layeredFSRootPath) throws IOException {
        String[] romfsPathComponents = path.split("/");
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
        fos.write(file);
        fos.close();
    }

    protected abstract boolean isGameUpdateSupported(int version);

    @Override
    public boolean hasGameUpdateLoaded() {
        return false;
    }

    @Override
    public boolean loadGameUpdate(String filename) {
        return true;
    }

    @Override
    public void removeGameUpdate() {
    }

    protected abstract String getGameVersion();

    @Override
    public String getGameUpdateVersion() {
        return getGameVersion();
    }

    @Override
    public void printRomDiagnostics(PrintStream logStream) {
    }

    @Override
    public boolean hasPhysicalSpecialSplit() {
        // Default value for Gen4+.
        // Handlers can override again in case of ROM hacks etc.
        return true;
    }

    // Doesn't decompress this for now, so you're on you're own.
    protected byte[] readMain() throws IOException {
        String mainPath = loadedFN + File.separator + "exefs" + File.separator + "main";
        RandomAccessFile mainFile = new RandomAccessFile(mainPath, "r");
        main = new byte[(int)mainFile.length()];
        mainFile.readFully(main);
        return main;
    }

    protected void writeMain(byte[] data) throws IOException {
        main = data;
    }

    protected byte[] readFile(String location) throws IOException {
        String filePath = loadedFN + File.separator + "romfs" + File.separator + location;
        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
        byte[] file = new byte[(int)randomAccessFile.length()];
        randomAccessFile.readFully(file);
        return file;
    }

    protected void writeFile(String location, byte[] data, int offset, int length) throws IOException {
        if (offset != 0 || length != data.length) {
            byte[] newData = new byte[length];
            System.arraycopy(data, offset, newData, 0, length);
            data = newData;
        }
        changedFiles.put(location, data);
    }

    protected void writeFile(String location, byte[] data) throws IOException {
        writeFile(location, data, 0, data.length);
    }
}
