package com.dabomstew.pkrandom.hac;

/*----------------------------------------------------------------------------*/
/*--  RomfsFile.java - an entry in the Switch romfs filesystem              --*/
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SwitchRomfsFile {
    private SwitchFileReader parent;
    public String fullPath;
    private Extracted status = Extracted.NOT;
    private String extFilename;
    public byte[] data;
    public boolean fileChanged = false;
    public long originalCRC;

    public SwitchRomfsFile(SwitchFileReader parent) {
        this.parent = parent;
    }

    public byte[] getContents() throws IOException {
        if (this.status == Extracted.NOT) {
            // extract file
            String filePath = parent.getExtractedFilesPath() + File.separator + "romfs" + File.separator + fullPath;
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            byte[] file = new byte[(int)randomAccessFile.length()];
            randomAccessFile.readFully(file);
            originalCRC = FileFunctions.getCRC32(file);
            if (parent.isWritingEnabled()) {
                // make a file
                String tmpDir = parent.getTmpFolder();
                this.extFilename = fullPath.replaceAll("[^A-Za-z0-9_\\.]+", "");
                File tmpFile = new File(tmpDir + extFilename);
                FileOutputStream fos = new FileOutputStream(tmpFile);
                fos.write(file);
                fos.close();
                tmpFile.deleteOnExit();
                this.status = Extracted.TO_FILE;
                this.data = null;
                return file;
            } else {
                this.status = Extracted.TO_RAM;
                this.data = file;
                byte[] newcopy = new byte[file.length];
                System.arraycopy(file, 0, newcopy, 0, file.length);
                return newcopy;
            }
        } else if (this.status == Extracted.TO_RAM) {
            byte[] newcopy = new byte[this.data.length];
            System.arraycopy(this.data, 0, newcopy, 0, this.data.length);
            return newcopy;
        } else {
            String tmpDir = parent.getTmpFolder();
            return FileFunctions.readFileFullyIntoBuffer(tmpDir + this.extFilename);
        }
    }

    public void writeOverride(byte[] data) throws IOException {
        if (status == Extracted.NOT) {
            // temp extract
            getContents();
        }
        fileChanged = true;
        if (status == Extracted.TO_FILE) {
            String tmpDir = parent.getTmpFolder();
            FileOutputStream fos = new FileOutputStream(new File(tmpDir + this.extFilename));
            fos.write(data);
            fos.close();
        } else {
            if (this.data.length == data.length) {
                // copy new in
                System.arraycopy(data, 0, this.data, 0, data.length);
            } else {
                // make new array
                this.data = null;
                this.data = new byte[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }
    }

    // returns null if no override
    public byte[] getOverrideContents() throws IOException {
        if (status == Extracted.NOT) {
            return null;
        }
        return getContents();
    }

    private enum Extracted {
        NOT, TO_FILE, TO_RAM
    }
}
