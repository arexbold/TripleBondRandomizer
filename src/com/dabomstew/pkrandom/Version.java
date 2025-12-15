package com.dabomstew.pkrandom;

/*----------------------------------------------------------------------------*/
/*--  Version.java - contains information about the randomizer's versions   --*/
/*--                                                                        --*/
/*--  Part of "Universal Pokemon Randomizer ZX" by the UPR-ZX team          --*/
/*--  Originally part of "Universal Pokemon Randomizer" by Dabomstew        --*/
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

import java.util.HashMap;
import java.util.Map;

public class Version {
    public static final int VERSION = 324; // Increment by 1 for new version. Updated for TripleBond 0.4.2
    public static final String VERSION_STRING = "TripleBond 0.4.2";

    public static final Map<Integer,String> oldVersions = setupVersionsMap();

    private static Map<Integer,String> setupVersionsMap() {
        Map<Integer,String> map = new HashMap<>();

        // Original UPR-ZX version forked from
        map.put(322, "4.6.1");
        
        // TripleBond versions
        map.put(323, "TripleBond 0.4.1");

        // Latest version - when version is updated, add the old version as an explicit put
        map.put(VERSION, VERSION_STRING);

        return map;
    }

    public static boolean isReleaseVersionNewer(String releaseVersion) {
        if (VERSION_STRING.contains("dev")) {
            return false;
        }
        // Chop off leading "v" from release version
        try {
            String releaseVersionTrimmed = releaseVersion.substring(1);
            String[] thisVersionPieces = VERSION_STRING.split("\\.");
            String[] releaseVersionPieces = releaseVersionTrimmed.split("\\.");
            int smallestLength = Math.min(thisVersionPieces.length, releaseVersionPieces.length);
            for (int i = 0; i < smallestLength; i++) {
                int thisVersionPiece = Integer.parseInt(thisVersionPieces[i]);
                int releaseVersionPiece = Integer.parseInt(releaseVersionPieces[i]);
                if (thisVersionPiece < releaseVersionPiece) {
                    return true;
                } else if (thisVersionPiece > releaseVersionPiece) {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            // Really not a big deal if we fail at this, probably because we can't connect to Github.
            return false;
        }
    }
}
