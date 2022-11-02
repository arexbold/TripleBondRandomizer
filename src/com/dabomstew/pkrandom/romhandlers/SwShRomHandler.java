package com.dabomstew.pkrandom.romhandlers;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.MiscTweak;
import com.dabomstew.pkrandom.RomFunctions;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.constants.*;
import com.dabomstew.pkrandom.exceptions.RandomizerIOException;
import com.dabomstew.pkrandom.generated.swsh.*;
import com.dabomstew.pkrandom.hac.AHTB;
import com.dabomstew.pkrandom.hac.BNTX;
import com.dabomstew.pkrandom.hac.GFPack;
import com.dabomstew.pkrandom.hac.SwitchFileReader;
import com.dabomstew.pkrandom.pokemon.*;
import pptxt.N3DSTxtHandler;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class SwShRomHandler extends AbstractSwitchRomHandler {

    public static class Factory extends RomHandler.Factory {

        @Override
        public SwShRomHandler create(Random random, PrintStream logStream) {
            return new SwShRomHandler(random, logStream);
        }

        public boolean isLoadable(String filename) {
            SwitchFileReader reader = new SwitchFileReader(filename);
            try {
                byte[] main = reader.getMain();
                long actualMainCRC32 = FileFunctions.getCRC32(main);
                return detectSwitchGameInner(actualMainCRC32);
            } catch (IOException e) {
                throw new RandomizerIOException(e);
            }

        }
    }

    public SwShRomHandler(Random random) {
        super(random, null);
    }

    public SwShRomHandler(Random random, PrintStream logStream) {
        super(random, logStream);
    }

    private static class RomEntry {
        private String name;
        private String contentId;
        private long mainCRC32;
        private Map<String, String> strings = new HashMap<>();
        private Map<String, Integer> numbers = new HashMap<>();
        private Map<String, int[]> arrayEntries = new HashMap<>();
        private Map<Integer, List<Integer>> linkedStaticOffsets = new HashMap<>();

        private int getInt(String key) {
            if (!numbers.containsKey(key)) {
                numbers.put(key, 0);
            }
            return numbers.get(key);
        }

        private String getString(String key) {
            if (!strings.containsKey(key)) {
                strings.put(key, "");
            }
            return strings.get(key);
        }
    }

    private static List<RomEntry> roms;

    static {
        loadROMInfo();
    }

    private static void loadROMInfo() {
        roms = new ArrayList<>();
        RomEntry current = null;
        try {
            Scanner sc = new Scanner(FileFunctions.openConfig("swsh_offsets.ini"), "UTF-8");
            while (sc.hasNextLine()) {
                String q = sc.nextLine().trim();
                if (q.contains("//")) {
                    q = q.substring(0, q.indexOf("//")).trim();
                }
                if (!q.isEmpty()) {
                    if (q.startsWith("[") && q.endsWith("]")) {
                        // New rom
                        current = new RomEntry();
                        current.name = q.substring(1, q.length() - 1);
                        roms.add(current);
                    } else {
                        String[] r = q.split("=", 2);
                        if (r.length == 1) {
                            System.err.println("invalid entry " + q);
                            continue;
                        }
                        if (r[1].endsWith("\r\n")) {
                            r[1] = r[1].substring(0, r[1].length() - 2);
                        }
                        r[1] = r[1].trim();
                        if (r[0].equals("ContentId")) {
                            current.contentId = r[1];
                        } else if (r[0].equals("MainCRC32")) {
                            current.mainCRC32 = parseRILong("0x" + r[1].trim());
                        } else if (r[0].equals("LinkedStaticEncounterOffsets")) {
                            String[] offsets = r[1].substring(1, r[1].length() - 1).split(",");
                            for (int i = 0; i < offsets.length; i++) {
                                String[] parts = offsets[i].split(":");
                                List<Integer> vals = Arrays.stream(Arrays.copyOfRange(parts,1,parts.length)).
                                        mapToInt(v -> Integer.parseInt(v.trim()))
                                        .boxed()
                                        .collect(Collectors.toList());
                                current.linkedStaticOffsets.put(Integer.parseInt(parts[0].trim()),vals);
                            }
                        } else if (r[0].endsWith("Offset") || r[0].endsWith("Count") || r[0].endsWith("Number")) {
                            int offs = parseRIInt(r[1]);
                            current.numbers.put(r[0], offs);
                        } else if (r[1].startsWith("[") && r[1].endsWith("]")) {
                            String[] offsets = r[1].substring(1, r[1].length() - 1).split(",");
                            if (offsets.length == 1 && offsets[0].trim().isEmpty()) {
                                current.arrayEntries.put(r[0], new int[0]);
                            } else {
                                int[] offs = new int[offsets.length];
                                int c = 0;
                                for (String off : offsets) {
                                    offs[c++] = parseRIInt(off);
                                }
                                current.arrayEntries.put(r[0], offs);
                            }
                        } else if (r[0].equals("CopyFrom")) {
                            for (RomEntry otherEntry : roms) {
                                if (r[1].equalsIgnoreCase(otherEntry.contentId)) {
                                    // copy from here
                                    current.linkedStaticOffsets.putAll(otherEntry.linkedStaticOffsets);
                                    current.arrayEntries.putAll(otherEntry.arrayEntries);
                                    current.numbers.putAll(otherEntry.numbers);
                                    current.strings.putAll(otherEntry.strings);
                                }
                            }
                        } else {
                            current.strings.put(r[0],r[1]);
                        }
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
    }

    private static int parseRIInt(String off) {
        int radix = 10;
        off = off.trim().toLowerCase();
        if (off.startsWith("0x") || off.startsWith("&h")) {
            radix = 16;
            off = off.substring(2);
        }
        try {
            return Integer.parseInt(off, radix);
        } catch (NumberFormatException ex) {
            System.err.println("invalid base " + radix + "number " + off);
            return 0;
        }
    }

    private static long parseRILong(String off) {
        int radix = 10;
        off = off.trim().toLowerCase();
        if (off.startsWith("0x") || off.startsWith("&h")) {
            radix = 16;
            off = off.substring(2);
        }
        try {
            return Long.parseLong(off, radix);
        } catch (NumberFormatException ex) {
            System.err.println("invalid base " + radix + "number " + off);
            return 0;
        }
    }

    @Override
    protected boolean detectSwitchGame(String filePath) throws IOException {
        byte[] main = this.readMain();
        long actualMainCRC32 = FileFunctions.getCRC32(main);
        return detectSwitchGameInner(actualMainCRC32);
    }

    private static boolean detectSwitchGameInner(long actualMainCRC32) {
        return entryFor(actualMainCRC32) != null;
    }

    private static RomEntry entryFor(long actualMainCRC32) {
        for (RomEntry re : roms) {
            if (re.mainCRC32 == actualMainCRC32) {
                return re;
            }
        }
        return null;
    }

    public class SwShItem {
        public int price;
        public int priceWatts;
        public int priceBPOrDyniteOre;
        public int sprite;

        public SwShItem(byte[] itemData, int offset) {
            price = FileFunctions.readFullInt(itemData, offset);
            priceWatts = FileFunctions.readFullInt(itemData, offset + 0x4);
            priceBPOrDyniteOre = FileFunctions.readFullInt(itemData, offset + 0x8);
            sprite = FileFunctions.read2ByteInt(itemData, offset + 0x1A);
        }

        public void outputToItemData(byte[] itemData, int offset) {
            FileFunctions.writeFullInt(itemData, offset, price);
            FileFunctions.writeFullInt(itemData, offset + 0x4, priceWatts);
            FileFunctions.writeFullInt(itemData, offset + 0x8, priceBPOrDyniteOre);
            FileFunctions.write2ByteInt(itemData, offset + 0x1A, sprite);
        }
    }

    // This ROM
    private RomEntry romEntry;
    private byte[] main;
    private Map<Integer,Pokemon> pokes;
    private Map<Integer,Map<Integer,Integer>> absolutePokeNumByBaseForme;
    private Map<Integer,Integer> dummyAbsolutePokeNums;
    private List<Pokemon> pokemonList;
    private List<Pokemon> pokemonListInclFormes;
    private List<String> abilityNames, itemNames;
    private Move[] moves;
    private Map<Long,Integer> itemHashToIndex;
    private List<Long> itemIndexToHash;
    private Map<Integer, SwShItem> itemTable;
    private GFPack placementPack;
    private AHTB placementAreaTable;
    private SwShGiftEncounterArchive giftEncounterArchive = null;

    @Override
    public boolean isRomValid() {
        return true;
    }

    @Override
    public List<Pokemon> getPokemon() {
        return pokemonList;
    }

    @Override
    public List<Pokemon> getPokemonInclFormes() {
        return pokemonListInclFormes;
    }

    @Override
    public List<Pokemon> getAltFormes() {

        return pokemonListInclFormes
                .stream()
                .filter(pk -> (pk != null && pk.number > SwShConstants.pokemonCount))
                .collect(Collectors.toList());
    }

    @Override
    public List<MegaEvolution> getMegaEvolutions() {
        return null;
    }

    @Override
    public Pokemon getAltFormeOfPokemon(Pokemon pk, int forme) {
        int pokeNum = absolutePokeNumByBaseForme.getOrDefault(pk.number,dummyAbsolutePokeNums).getOrDefault(forme,0);
        return pokeNum != 0 ? !pokes.get(pokeNum).actuallyCosmetic ? pokes.get(pokeNum) : pokes.get(pokeNum).baseForme : pk;
    }

    @Override
    public List<Pokemon> getIrregularFormes() {
        return null;
    }

    @Override
    public void removeEvosForPokemonPool() {

    }

    @Override
    public List<Pokemon> getStarters() {
        List<StaticEncounter> starters = new ArrayList<>();
        try {
            byte[] data = readFile(romEntry.getString("GiftPokemon"));
            SwShGiftEncounterArchive arc =
                    SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
            int[] starterGiftIndices = romEntry.arrayEntries.get("StarterGiftIndices");
            for (int i: starterGiftIndices) {
                SwShGiftEncounter starterGift = arc.giftEncounters(i);
                StaticEncounter se = new StaticEncounter();
                Pokemon pokemon = pokes.get(starterGift.species());
                int forme = starterGift.form();
                if (forme > pokemon.cosmeticForms && forme != 30 && forme != 31) {
                    pokemon = getAltFormeOfPokemon(pokemon, forme);
                }
                se.pkmn = pokemon;
                se.forme = forme;
                se.level = starterGift.level();
                se.heldItem = starterGift.heldItem();
                starters.add(se);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return starters.stream().map(pk -> pk.pkmn).collect(Collectors.toList());
    }

    @Override
    public boolean setStarters(List<Pokemon> newStarters) {
        try {
            // Need to rewrite this flatbuffer to make forms settable, might already have been done in setStaticPokemon
            if (giftEncounterArchive == null) {
                byte[] data = readFile(romEntry.getString("GiftPokemon"));
                SwShGiftEncounterArchive oldArc =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
                byte[] newData = constructNewGiftEncounterArchiveFlatBuffer(oldArc);
                giftEncounterArchive =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(newData));
            }
            int[] starterGiftIndices = romEntry.arrayEntries.get("StarterGiftIndices");
            int[] starterPlacementOutsideIndices = romEntry.arrayEntries.get("StarterPlacementOutsideIndices");
            int[] starterPlacementInsideIndices = romEntry.arrayEntries.get("StarterPlacementInsideIndices");
            Iterator<Pokemon> newStarterIterator = newStarters.iterator();

            byte[] starterPlacementOutsideData = placementPack.getDataFileName("a_0101.bin");
            byte[] starterPlacementInsideData = placementPack.getDataFileName("a_t0101_i0101.bin");

            // Fix placement vtable to allow forms to be set
            for (int vTableOffset: romEntry.arrayEntries.get("StarterPlacementOutsideVTableFormOffsets")) {
                starterPlacementOutsideData[vTableOffset] = 0x3C;
            }
            for (int vTableOffset: romEntry.arrayEntries.get("StarterPlacementInsideVTableFormOffsets")) {
                starterPlacementInsideData[vTableOffset] = 0x3C;
            }

            SwShPlacementAreaArchive starterPlacementOutsideArea =
                    SwShPlacementAreaArchive.getRootAsSwShPlacementAreaArchive(ByteBuffer.wrap(starterPlacementOutsideData));
            SwShPlacementZone starterPlacementZone1 = starterPlacementOutsideArea.placementZones(0);
            SwShPlacementAreaArchive starterPlacementInsideArea =
                    SwShPlacementAreaArchive.getRootAsSwShPlacementAreaArchive(ByteBuffer.wrap(starterPlacementInsideData));
            SwShPlacementZone starterPlacementZone2 = starterPlacementInsideArea.placementZones(0);
            // TODO look at what this actually changes, and figure out starter cries
//            byte[] townScript = readFile(
//                    String.format(
//                            romEntry.getString("ScriptTemplate"),
//                            romEntry.getString("PostwickOutdoorZone")));
//            AMX townAMX = new AMX(townScript);
//            int[] starterCryScriptOffsets = romEntry.arrayEntries.get("StarterCryScriptOffsets");

            for (int i = 0; i < 3; i++) {
                int giftIndex = starterGiftIndices[i];
                int placementOutsideIndex = starterPlacementOutsideIndices[i];
                int placementInsideIndex = starterPlacementInsideIndices[i];
                Pokemon starter = newStarterIterator.next();
                int forme = 0;
                boolean checkCosmetics = true;
                if (starter.formeNumber > 0) {
                    forme = starter.formeNumber;
                    starter = starter.baseForme;
                    checkCosmetics = false;
                }
                if (checkCosmetics && starter.cosmeticForms > 0) {
                    forme = starter.getCosmeticFormNumber(this.random.nextInt(starter.cosmeticForms));
                } else if (!checkCosmetics && starter.cosmeticForms > 0) {
                    forme += starter.getCosmeticFormNumber(this.random.nextInt(starter.cosmeticForms));
                }
                SwShGiftEncounter starterGift = giftEncounterArchive.giftEncounters(giftIndex);
                starterGift.mutateSpecies(starter.number);
                starterGift.mutateForm(forme);
                starterPlacementZone1.critters(placementOutsideIndex).mutateSpecies(starter.number);
                starterPlacementZone1.critters(placementOutsideIndex).mutateForm(forme);
                starterPlacementZone2.critters(placementInsideIndex).mutateSpecies(starter.number);
                starterPlacementZone2.critters(placementInsideIndex).mutateForm(forme);
//                FileFunctions.write2ByteInt(townAMX.decData,starterCryScriptOffsets[i],starter.number);
            }
            byte[] newStarterData = giftEncounterArchive.getByteBuffer().array();
            writeFile(romEntry.getString("GiftPokemon"),newStarterData);
            placementPack.setDataFileName("a_0101.bin",starterPlacementOutsideArea.getByteBuffer().array());
            placementPack.setDataFileName("a_t0101_i0101.bin",starterPlacementInsideArea.getByteBuffer().array());
//            writeFile(
//                    String.format(
//                            romEntry.getString("ScriptTemplate"),
//                            romEntry.getString("PostwickOutdoorZone")),
//                    townAMX.getBytes());
            return true;
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public boolean hasStarterAltFormes() {
        return true;
    }

    @Override
    public int starterCount() {
        return 3;
    }

    @Override
    public boolean supportsStarterHeldItems() {
        return true;
    }

    @Override
    public List<Integer> getStarterHeldItems() {
        List<Integer> starterHeldItems = new ArrayList<>();
        try {
            byte[] data = readFile(romEntry.getString("GiftPokemon"));
            SwShGiftEncounterArchive arc =
                    SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
            int[] starterGiftIndices = romEntry.arrayEntries.get("StarterGiftIndices");
            for (int i: starterGiftIndices) {
                SwShGiftEncounter starterGift = arc.giftEncounters(i);
                int heldItem = starterGift.heldItem();
                starterHeldItems.add(heldItem);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }

        return starterHeldItems;
    }

    @Override
    public void setStarterHeldItems(List<Integer> items) {
        try {
            if (giftEncounterArchive == null) {
                byte[] data = readFile(romEntry.getString("GiftPokemon"));
                SwShGiftEncounterArchive oldArc =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
                byte[] newData = constructNewGiftEncounterArchiveFlatBuffer(oldArc);
                giftEncounterArchive =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(newData));
            }
            int[] starterGiftIndices = romEntry.arrayEntries.get("StarterGiftIndices");
            Iterator<Integer> itemsIter = items.iterator();
            for (int i: starterGiftIndices) {
                int item = itemsIter.next();
                SwShGiftEncounter starterGift = giftEncounterArchive.giftEncounters(i);
                starterGift.mutateHeldItem(item);
            }
            byte[] newStarterData = giftEncounterArchive.getByteBuffer().array();
            writeFile(romEntry.getString("GiftPokemon"),newStarterData);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private byte[] constructNewGiftEncounterArchiveFlatBuffer(SwShGiftEncounterArchive arc) {
        // Need space for root object size (including its vtable), gift encounter vtable, and all gift encounters
        int sizeRequired = 0x94 + 0x3C + arc.giftEncountersLength() * 0x40;
        byte[] data = new byte[sizeRequired];

        // The following is the same as in the original flatbuffer
        data[0] = 0xC;  // Offset to root object
        data[6] = 6;    // Beginning of root object vtable - vtable size
        data[8] = 8;    // Root object inline size
        data[0xA] = 4;  // Offset to gift encounter array within root object
        data[0xC] = 6;  // Beginning of root object - offset to vtable (negative direction)
        data[0x10] = 4; // Pointer to gift encounter array
        FileFunctions.writeFullInt(data,0x14,arc.giftEncountersLength());  // Beginning of gift encounter array - element count

        // Add the offsets to the array objects, as well as the objects themselves
        for (int i = 0; i < arc.giftEncountersLength(); i++) {
            // In memory, the array objects are stored in reverse order because Google said so
            int pointerOffset = 0x18+(i*4);
            FileFunctions.writeFullInt(data,pointerOffset,(sizeRequired - (i+1) * 0x40) - pointerOffset);
            addGiftEncounterFlatBufferObject(data,arc.giftEncounters(i),0x94,sizeRequired - (i+1) * 0x40);
        }
        // Write vtable
        addGiftEncounterVTable(data,0x94);

        return data;
    }

    private void addGiftEncounterVTable(byte[] data, int offset) {
        FileFunctions.write2ByteInt(data,offset,0x3C);     // VTable length
        FileFunctions.write2ByteInt(data,offset+2,0x40);     // Object inline length
        FileFunctions.write2ByteInt(data,offset+4,0x0);      // Is Egg
        FileFunctions.write2ByteInt(data,offset+6,0x4);      // Form
        FileFunctions.write2ByteInt(data,offset+8,0x0);      // Dynamax Level
        FileFunctions.write2ByteInt(data,offset+0xA,0xC);    // Ball Item ID
        FileFunctions.write2ByteInt(data,offset+0xC,0x0);    // Field 4
        FileFunctions.write2ByteInt(data,offset+0xE,0x20);   // Hash 1
        FileFunctions.write2ByteInt(data,offset+0x10,0x3C);   // G-Max Factor
        FileFunctions.write2ByteInt(data,offset+0x12,0x38);  // Held Item
        FileFunctions.write2ByteInt(data,offset+0x14,0x5);   // Level
        FileFunctions.write2ByteInt(data,offset+0x16,0x10);  // Species
        FileFunctions.write2ByteInt(data,offset+0x18,0x0);   // Field A
        FileFunctions.write2ByteInt(data,offset+0x1A,0x0);   // Memory Code
        FileFunctions.write2ByteInt(data,offset+0x1C,0x0);   // Memory Data
        FileFunctions.write2ByteInt(data,offset+0x1E,0x0);   // Memory Feel
        FileFunctions.write2ByteInt(data,offset+0x20,0x0);   // Memory Level
        FileFunctions.write2ByteInt(data,offset+0x22,0x28);  // OT Name ID
        FileFunctions.write2ByteInt(data,offset+0x24,0x14);  // OT Gender
        FileFunctions.write2ByteInt(data,offset+0x26,0x18);  // Shiny Lock
        FileFunctions.write2ByteInt(data,offset+0x28,0x1C);  // Nature
        FileFunctions.write2ByteInt(data,offset+0x2A,0x0);   // Gender
        FileFunctions.write2ByteInt(data,offset+0x2C,0x6);   // IV Speed
        FileFunctions.write2ByteInt(data,offset+0x2E,0x7);   // IV Attack
        FileFunctions.write2ByteInt(data,offset+0x30,0x8);   // IV Defense
        FileFunctions.write2ByteInt(data,offset+0x32,0x9);   // IV HP
        FileFunctions.write2ByteInt(data,offset+0x34,0xA);   // IV Sp.Attack
        FileFunctions.write2ByteInt(data,offset+0x36,0xB);   // IV Sp.Defense
        FileFunctions.write2ByteInt(data,offset+0x38,0x30);   // Ability
        FileFunctions.write2ByteInt(data,offset+0x3A,0x34);   // Special Move
    }

    private void addGiftEncounterFlatBufferObject(byte[] data, SwShGiftEncounter enc, int vTableAbsOffset,
                                                  int objectAbsOffset) {
        FileFunctions.writeFullInt(data,objectAbsOffset,objectAbsOffset - vTableAbsOffset);
        data[objectAbsOffset+4] = (byte)enc.form();
        data[objectAbsOffset+5] = (byte)enc.level();
        data[objectAbsOffset+6] = enc.ivSpe();
        data[objectAbsOffset+7] = enc.ivAtk();
        data[objectAbsOffset+8] = enc.ivDef();
        data[objectAbsOffset+9] = enc.ivHp();
        data[objectAbsOffset+0xA] = enc.ivSpa();
        data[objectAbsOffset+0xB] = enc.ivSpd();
        FileFunctions.writeFullInt(data,objectAbsOffset+0xC,enc.ballItemId());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x10,enc.species());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x14,enc.otGender());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x18,enc.shinyLock());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x1C,enc.nature());
        FileFunctions.writeFullLong(data,objectAbsOffset+0x20,enc.hash1());
        FileFunctions.writeFullLong(data,objectAbsOffset+0x28,enc.otNameId());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x30,enc.ability());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x34,enc.specialMove());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x38,enc.heldItem());
        data[objectAbsOffset+0x3C] = enc.gmaxFactor() ? 1 : (byte)0;
    }

    @Override
    public Map<Integer, StatChange> getUpdatedPokemonStats(int generation) {
        return null;
    }

    @Override
    public int abilitiesPerPokemon() {
        return 3;
    }

    @Override
    public int highestAbilityIndex() {
        return SwShConstants.highestAbilityIndex;
    }

    @Override
    public String abilityName(int number) {
        return abilityNames.get(number);
    }

    @Override
    public List<Integer> getUselessAbilities() {
        return new ArrayList<>(SwShConstants.uselessAbilities);
    }

    @Override
    public List<Integer> getMovesBannedFromLevelup() {
        return SwShConstants.unusableMoves;
    }

    @Override
    public int maxTrainerNameLength() {
        return 10;
    }

    @Override
    public boolean altFormesCanHaveDifferentEvolutions() {
        return true;
    }

    @Override
    public Map<Integer, List<Integer>> getAbilityVariations() {
        return SwShConstants.abilityVariations;
    }

    @Override
    public boolean hasMegaEvolutions() {
        return false;
    }

    @Override
    public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
        List<EncounterSet> encounters = new ArrayList<>();
        try  {
            final int NUM_WEATHER_TABLES = 9;
            byte[] fieldData = this.readFile(romEntry.getString("FieldDataPack"));
            GFPack fieldDataPack = new GFPack(fieldData);

            byte[] wildData = fieldDataPack.getDataFileName(romEntry.getString("WildPokemonFile"));
            byte[] symbolData = fieldDataPack.getDataFileName(romEntry.getString("SymbolPokemonFile"));

            SwShWildEncounterArchive wildArchive = SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(wildData));
            SwShWildEncounterArchive symbolArchive = SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(symbolData));

            Map<String,Integer> symbolZoneNames = new HashMap<>();
            List<String> symbolZoneNamesWithWeather = new ArrayList<>();
            int internalOrder = 0;
            for (int i = 0; i < symbolArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable symbolTable = symbolArchive.encounterTables(i);
                String tableName = SwShConstants.getZoneName(symbolTable.zoneId());
                symbolZoneNames.put(tableName,internalOrder);
                if (symbolTable.subTablesLength() > 0 || symbolTable.subTablesLength() > 0) {
                    int j = 0;
                    if (isAllWeatherTablesIdentical(symbolTable, NUM_WEATHER_TABLES)) {
                        EncounterSet symbolEs = readEncounterSet(tableName + " (All Weather) (Visible)", symbolTable.subTables(0));
                        symbolEs.offset = internalOrder;
                        symbolEs.prettyOrder = internalOrder;
                        internalOrder++;

                        encounters.add(symbolEs);
                        symbolZoneNamesWithWeather.add(tableName + " (All Weather)");
                        j = NUM_WEATHER_TABLES;
                    }
                    while (j < symbolTable.subTablesLength()) {

                        EncounterSet symbolEs = readEncounterSet(
                                tableName + " (" + SwShConstants.encounterTypes.get(j) + ") (Visible)",
                                symbolTable.subTables(j));
                        symbolEs.offset = internalOrder;
                        symbolEs.prettyOrder = internalOrder;
                        internalOrder++;

                        encounters.add(symbolEs);
                        symbolZoneNamesWithWeather.add(tableName + " (" + SwShConstants.encounterTypes.get(j) + ")");
                        j++;
                    }
                }
            }
            for (int i = 0; i < wildArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable wildTable = wildArchive.encounterTables(i);
                String tableName = SwShConstants.getZoneName(wildTable.zoneId());
                if (wildTable.subTablesLength() > 0) {
                    int j = 0;
                    if (isAllWeatherTablesIdentical(wildTable, NUM_WEATHER_TABLES)) {
                        EncounterSet wildEs = readEncounterSet(tableName + " (All Weather)", wildTable.subTables(0));
                        wildEs.offset = internalOrder;
                        Integer sameZoneOffset = symbolZoneNamesWithWeather.indexOf(tableName + " (All Weather)");
                        if (sameZoneOffset >= 0) {
                            wildEs.prettyOrder = sameZoneOffset;
                        } else {
                            sameZoneOffset = symbolZoneNames.get(tableName);
                            if (sameZoneOffset != null) {
                                wildEs.prettyOrder = sameZoneOffset;
                            } else {
                                wildEs.prettyOrder = internalOrder;
                            }
                        }
                        internalOrder++;

                        encounters.add(wildEs);
                        j = NUM_WEATHER_TABLES;
                    }
                    while (j < wildTable.subTablesLength()) {
                        EncounterSet wildEs =
                                readEncounterSet(tableName + " (" + SwShConstants.encounterTypes.get(j) + ")", wildTable.subTables(j));
                        wildEs.offset = internalOrder;
                        Integer sameZoneOffset = symbolZoneNamesWithWeather.indexOf(tableName + " (" + SwShConstants.encounterTypes.get(j) + ")");
                        if (sameZoneOffset >= 0) {
                            wildEs.prettyOrder = sameZoneOffset;
                        } else {
                            sameZoneOffset = symbolZoneNames.get(tableName);
                            if (sameZoneOffset != null) {
                                wildEs.prettyOrder = sameZoneOffset;
                            } else {
                                wildEs.prettyOrder = internalOrder;
                            }
                        }
                        internalOrder++;

                        encounters.add(wildEs);
                        j++;
                    }
                }
            }

            // Wanderers
            byte[] staticEncounterData = readFile(romEntry.getString("StaticPokemon"));
            SwShStaticEncounterArchive staticArc =
                    SwShStaticEncounterArchive.getRootAsSwShStaticEncounterArchive(ByteBuffer.wrap(staticEncounterData));
            Map<Long,Encounter> wanderers = new HashMap<>();
            List<Integer> excludeIndices = Arrays.stream(romEntry.arrayEntries.get("RealStaticPokemon")).boxed().collect(Collectors.toList());
            for (int i = 0; i < staticArc.staticEncountersLength(); i++) {
                if (excludeIndices.contains(i)) {
                    continue;
                }
                SwShStaticEncounter enc = staticArc.staticEncounters(i);
                Encounter e = new Encounter();
                e.pokemon = getPokemonForEncounter(enc.species(), enc.form());
                e.formeNumber = enc.form();
                e.level = enc.level();
                e.wandererIndex = i;
                wanderers.put(enc.encounterId(),e);
            }
            for (String areaName: SwShConstants.areasWithWanderers) {
                byte[] arcData = placementPack.getDataFileName(areaName);
                SwShPlacementAreaArchive arc =
                        SwShPlacementAreaArchive.getRootAsSwShPlacementAreaArchive(ByteBuffer.wrap(arcData));
                for (int i = 0; i < arc.placementZonesLength(); i++) {
                    SwShPlacementZone zone = arc.placementZones(i);
                    String zoneName = SwShConstants.getZoneName(zone.meta().zoneId());
                    if (zoneName == null) continue;
                    EncounterSet[] encounterSets = new EncounterSet[10];
                    for (int j = 0; j < zone.staticObjectsLength(); j++) {
                        SwShPlacementStaticObject so = zone.staticObjects(j).object();
                        boolean allSame = true;
                        long firstID = so.spawns(0).spawnId();
                        for (int k = 1; k < so.spawnsLength(); k++) {
                            if (so.spawns(k).spawnId() != firstID) {
                                allSame = false;
                                break;
                            }
                        }
                        if (allSame) {
                            if (encounterSets[0] == null) {
                                encounterSets[0] = new EncounterSet();
                                encounterSets[0].displayName = zoneName + " (All Weather) (Wanderer)";
                            }
                            Encounter thisWanderer = wanderers.get(so.spawns(0).spawnId());
                            if (thisWanderer != null && !encounterSets[0].encounters.contains(thisWanderer)) {
                                encounterSets[0].encounters.add(thisWanderer);
                            }
                        } else {
                            for (int k = 0; k < so.spawnsLength(); k++) {
                                if (encounterSets[k+1] == null) {
                                    encounterSets[k+1] = new EncounterSet();
                                    encounterSets[k+1].displayName = zoneName + " (" + SwShConstants.encounterTypes.get(k) + ") (Wanderer)";
                                }
                                Encounter thisWanderer = wanderers.get(so.spawns(k).spawnId());
                                if (thisWanderer != null && !encounterSets[k+1].encounters.contains(thisWanderer)) {
                                    encounterSets[k+1].encounters.add(thisWanderer);
                                }
                            }
                        }
                    }
                    for (int j = 0; j < 10; j++) {
                        if (encounterSets[j] == null) continue;
                        encounterSets[j].offset = internalOrder;
                        Integer sameZoneOffset;
                        if (j == 0) {
                            sameZoneOffset = symbolZoneNamesWithWeather.indexOf(encounterSets[j] + " (All Weather)");
                        } else {
                            sameZoneOffset = symbolZoneNamesWithWeather.indexOf(encounterSets[j] + " (" + SwShConstants.encounterTypes.get(j-1) + ")");
                        }
                        if (sameZoneOffset >= 0) {
                            encounterSets[j].prettyOrder = sameZoneOffset;
                        } else {
                            sameZoneOffset = symbolZoneNames.get(zoneName);
                            if (sameZoneOffset != null) {
                                encounterSets[j].prettyOrder = sameZoneOffset;
                            } else {
                                encounterSets[j].prettyOrder = internalOrder;
                            }
                        }
                        encounters.add(encounterSets[j]);
                        internalOrder++;
                    }
                }
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }

        return encounters.stream().sorted(Comparator.comparingInt(es -> es.prettyOrder)).collect(Collectors.toList());
    }

    private EncounterSet readEncounterSet(String tableName, SwShWildEncounterSubTable subTable) {
        EncounterSet es = new EncounterSet();
        es.displayName = tableName;
        for (int k = 0; k < subTable.slotsLength(); k++) {
            SwShWildEncounterSlot slot = subTable.slots(k);
            if (slot.species() == 0) {
                continue;
            }
            Encounter e = new Encounter();
            e.pokemon = getPokemonForEncounter(slot.species(), slot.form());
            e.formeNumber = slot.form();
            e.probability = slot.probability();
            e.level = subTable.levelMin();
            e.maxLevel = subTable.levelMax();
            es.encounters.add(e);
        }

        return es;
    }

    private boolean isAllWeatherTablesIdentical(SwShWildEncounterTable table, int numWeatherTables) {
        if (table.subTablesLength() < numWeatherTables) {
            throw new IllegalArgumentException();
        }
        SwShWildEncounterSubTable firstSubTable = table.subTables(0);
        for (int i = 0; i < numWeatherTables; i++) {
            SwShWildEncounterSubTable currentSubTable = table.subTables(i);
            if (currentSubTable.levelMin() != firstSubTable.levelMin()) {
                return false;
            }
            if (currentSubTable.levelMax() != firstSubTable.levelMax()) {
                return false;
            }
            if (currentSubTable.slotsLength() != firstSubTable.slotsLength()) {
                return false;
            }
            for (int j = 0; j < currentSubTable.slotsLength(); j++) {
                if (currentSubTable.slots(j).species() != firstSubTable.slots(j).species()) {
                    return false;
                }
                if (currentSubTable.slots(j).form() != firstSubTable.slots(j).form()) {
                    return false;
                }
                if (currentSubTable.slots(j).probability() != firstSubTable.slots(j).probability()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Pokemon getPokemonForEncounter(int species, int forme) {
        Pokemon pokemon = pokes.get(species);

        // If the forme is purely cosmetic, just use the base forme as the Pokemon
        // for this encounter (the cosmetic forme will be stored in the encounter).
        if (forme <= pokemon.cosmeticForms || forme == 30 || forme == 31) {
            return pokemon;
        } else {
            int speciesWithForme = absolutePokeNumByBaseForme
                    .getOrDefault(species, dummyAbsolutePokeNums)
                    .getOrDefault(forme, 0);
            return pokes.get(speciesWithForme);
        }
    }

    @Override
    public void setEncounters(boolean useTimeOfDay, List<EncounterSet> encountersList) {
        Iterator<EncounterSet> encounters = encountersList
                .stream()
                .sorted(Comparator.comparingInt(es -> es.offset))
                .collect(Collectors.toList())
                .iterator();

        try  {
            final int NUM_WEATHER_TABLES = 9;
            byte[] fieldData = this.readFile(romEntry.getString("FieldDataPack"));
            GFPack fieldDataPack = new GFPack(fieldData);

            byte[] wildData = fieldDataPack.getDataFileName(romEntry.getString("WildPokemonFile"));
            byte[] symbolData = fieldDataPack.getDataFileName(romEntry.getString("SymbolPokemonFile"));
            byte[] staticEncounterData = readFile(romEntry.getString("StaticPokemon"));

            // Make forms settable
            for (int vTableOffset: romEntry.arrayEntries.get("WildPokemonVTableOffsets")) {
                wildData[vTableOffset] = 6;
            }
            for (int vTableOffset: romEntry.arrayEntries.get("SymbolPokemonVTableOffsets")) {
                symbolData[vTableOffset] = 6;
            }
            for (int i = 0; i < romEntry.arrayEntries.get("StaticPokemonVTables").length; i++) {
                int newFormOffset = romEntry.arrayEntries.get("StaticPokemonNewFormOffsets")[i];
                if (newFormOffset < 0) continue;
                int vTableOffset = romEntry.arrayEntries.get("StaticPokemonVTables")[i];
                staticEncounterData[vTableOffset + 0x14] = (byte)newFormOffset;
            }

            SwShWildEncounterArchive wildArchive =
                    SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(wildData));
            SwShWildEncounterArchive symbolArchive =
                    SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(symbolData));
            SwShStaticEncounterArchive staticArc =
                    SwShStaticEncounterArchive.getRootAsSwShStaticEncounterArchive(ByteBuffer.wrap(staticEncounterData));
            for (int i = 0; i < symbolArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable symbolTable = symbolArchive.encounterTables(i);
                if (symbolTable.subTablesLength() > 0) {
                    boolean distinctWeatherTables = true;
                    if (isAllWeatherTablesIdentical(symbolTable, NUM_WEATHER_TABLES)) {
                        distinctWeatherTables = false;
                    }
                    EncounterSet symbolEs = encounters.next();
                    for (int j = 0; j < symbolTable.subTablesLength(); j++) {

                        if (j >= NUM_WEATHER_TABLES || (j > 0 && distinctWeatherTables)) {
                            symbolEs = encounters.next();
                        }
                        SwShWildEncounterSubTable subTable = symbolTable.subTables(j);
                        for (int k = 0; k < subTable.slotsLength(); k++) {
                            SwShWildEncounterSlot slot = subTable.slots(k);
                            if (slot.species() == 0) {
                                continue;
                            }
                            Encounter e = symbolEs.encounters.get(k);
                            slot.mutateSpecies(e.pokemon.getBaseNumber());
                            slot.mutateForm(e.formeNumber);
                            if (k == 0) {
                                subTable.mutateLevelMin(e.level);
                                subTable.mutateLevelMax(e.maxLevel);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < wildArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable wildTable = wildArchive.encounterTables(i);
                if (wildTable.subTablesLength() > 0) {
                    boolean distinctWeatherTables = true;
                    if (isAllWeatherTablesIdentical(wildTable, NUM_WEATHER_TABLES)) {
                        distinctWeatherTables = false;
                    }
                    EncounterSet wildEs = encounters.next();
                    for (int j = 0; j < wildTable.subTablesLength(); j++) {

                        if (j >= NUM_WEATHER_TABLES || (j > 0 && distinctWeatherTables)) {
                            wildEs = encounters.next();
                        }
                        SwShWildEncounterSubTable subTable = wildTable.subTables(j);
                        for (int k = 0; k < subTable.slotsLength(); k++) {
                            SwShWildEncounterSlot slot = subTable.slots(k);
                            if (slot.species() == 0) {
                                continue;
                            }
                            Encounter e = wildEs.encounters.get(k);
                            slot.mutateSpecies(e.pokemon.getBaseNumber());
                            slot.mutateForm(e.formeNumber);
                            if (k == 0) {
                                subTable.mutateLevelMin(e.level);
                                subTable.mutateLevelMax(e.maxLevel);
                            }
                        }
                    }
                }
            }

            // Wanderers
            while (encounters.hasNext()) {
                EncounterSet wandererEs = encounters.next();
                for (Encounter e: wandererEs.encounters) {
                    if (e.modificationState == Encounter.DATA_WRITTEN) continue;
                    if (e.wandererIndex >= 0) {
                        SwShStaticEncounter enc = staticArc.staticEncounters(e.wandererIndex);
                        enc.mutateSpecies(e.pokemon.getBaseNumber());
                        enc.mutateForm(e.formeNumber);
                        enc.mutateLevel(e.level);
                        e.modificationState = Encounter.DATA_WRITTEN;
                    }
                }
            }
            fieldDataPack.setDataFileName(romEntry.getString("WildPokemonFile"),wildArchive.getByteBuffer().array());
            fieldDataPack.setDataFileName(romEntry.getString("SymbolPokemonFile"),symbolArchive.getByteBuffer().array());
            writeFile(romEntry.getString("FieldDataPack"),fieldDataPack.writePack());
            writeFile(romEntry.getString("StaticPokemon"),staticArc.getByteBuffer().array());
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public boolean hasWildAltFormes() {
        return true;
    }

    private byte[] constructNewStaticEncounterArchiveFlatBuffer(SwShGiftEncounterArchive arc) {
        // Need space for root object size (including its vtable), gift encounter vtable, and all gift encounters
        int sizeRequired = 0x94 + 0x3C + arc.giftEncountersLength() * 0x40;
        byte[] data = new byte[sizeRequired];

        // The following is the same as in the original flatbuffer
        data[0] = 0xC;  // Offset to root object
        data[6] = 6;    // Beginning of root object vtable - vtable size
        data[8] = 8;    // Root object inline size
        data[0xA] = 4;  // Offset to gift encounter array within root object
        data[0xC] = 6;  // Beginning of root object - offset to vtable (negative direction)
        data[0x10] = 4; // Pointer to gift encounter array
        FileFunctions.writeFullInt(data,0x14,arc.giftEncountersLength());  // Beginning of gift encounter array - element count

        // Add the offsets to the array objects, as well as the objects themselves
        for (int i = 0; i < arc.giftEncountersLength(); i++) {
            // In memory, the array objects are stored in reverse order because Google said so
            int pointerOffset = 0x18+(i*4);
            FileFunctions.writeFullInt(data,pointerOffset,(sizeRequired - (i+1) * 0x40) - pointerOffset);
            addGiftEncounterFlatBufferObject(data,arc.giftEncounters(i),0x94,sizeRequired - (i+1) * 0x40);
        }
        // Write vtable
        addStaticEncounterVTable(data,0x94);

        return data;
    }

    @Override
    public List<Trainer> getTrainers() {
        List<Trainer> allTrainers = new ArrayList<>();
        try {
            int trainernum = SwShConstants.trainerCount;
            List<String> tclasses = this.getTrainerClassNames();
            List<String> tnames = this.getTrainerNames();
            Map<Integer,String> tnamesMap = new TreeMap<>();
            for (int i = 0; i < tnames.size(); i++) {
                tnamesMap.put(i,tnames.get(i));
            }
            for (int i = 1; i < trainernum; i++) {
                byte[] trainer = readFile(String.format(romEntry.getString("TrainerDataTemplate"), i));
                byte[] trpoke = readFile(String.format(romEntry.getString("TrainerPokemonTemplate"), i));
                Trainer tr = new Trainer();
                tr.index = i;
                tr.trainerclass = trainer[0] & 0xFF;
                int battleType = trainer[2] & 0xFF;
                int numPokes = trainer[3] & 0xFF;
                int trainerAILevel = trainer[12] & 0xFF;
                boolean healer = trainer[15] != 0;
                int pokeOffs = 0;
                String trainerClass = tclasses.get(tr.trainerclass);
                String trainerName = tnamesMap.getOrDefault(i, "UNKNOWN");
                tr.fullDisplayName = trainerClass + " " + trainerName;

                for (int poke = 0; poke < numPokes; poke++) {
                    // Structure is
                    // IV SB LV LV SP SP FRM FRM
                    // (HI HI)
                    // (M1 M1 M2 M2 M3 M3 M4 M4)
                    // where SB = 0 0 Ab Ab 0 0 Fm Ml
                    // Ab Ab = ability number, 0 for random
                    // Fm = 1 for forced female
                    // Ml = 1 for forced male
                    // There's also a trainer flag to force gender, but
                    // this allows fixed teams with mixed genders.

                    // int secondbyte = trpoke[pokeOffs + 1] & 0xFF;
                    int abilityAndFlag = trpoke[pokeOffs];
                    TrainerPokemon tpk = new TrainerPokemon();
                    tpk.abilitySlot = (abilityAndFlag >>> 4) & 0xF;
                    tpk.forcedGenderFlag = (abilityAndFlag & 0xF);
                    tpk.nature = trpoke[pokeOffs + 1];
                    tpk.hpEVs = trpoke[pokeOffs + 2];
                    tpk.atkEVs = trpoke[pokeOffs + 3];
                    tpk.defEVs = trpoke[pokeOffs + 4];
                    tpk.spatkEVs = trpoke[pokeOffs + 5];
                    tpk.spdefEVs = trpoke[pokeOffs + 6];
                    tpk.speedEVs = trpoke[pokeOffs + 7];
                    tpk.dynamaxLevel = trpoke[pokeOffs + 8];
                    tpk.gmaxFactor = trpoke[pokeOffs + 9] != 0;
                    tpk.level = FileFunctions.read2ByteInt(trpoke, pokeOffs + 10);
                    int species = FileFunctions.read2ByteInt(trpoke, pokeOffs + 12);
                    tpk.pokemon = pokes.get(species);
                    int formnum = FileFunctions.read2ByteInt(trpoke, pokeOffs + 14);
                    tpk.forme = formnum;
                    tpk.formeSuffix = SwShConstants.getFormeSuffixByBaseForme(species,formnum);
                    tpk.heldItem = FileFunctions.read2ByteInt(trpoke, pokeOffs + 16);
                    for (int move = 0; move < 4; move++) {
                        tpk.moves[move] = FileFunctions.read2ByteInt(trpoke, pokeOffs + 18 + (move*2));
                    }
                    tpk.IVs = FileFunctions.readFullInt(trpoke, pokeOffs + 28);
                    pokeOffs += 32;
                    tr.pokemon.add(tpk);
                }
                allTrainers.add(tr);
            }
            SwShConstants.tagTrainers(allTrainers);
            SwShConstants.setForcedRivalStarterPositions(allTrainers);
            SwShConstants.setMultiBattleStatus(allTrainers);
        } catch (IOException ex) {
            throw new RandomizerIOException(ex);
        }
        return allTrainers;
    }

    @Override
    public List<Integer> getMainPlaythroughTrainers() {
        return null;
    }

    @Override
    public List<Integer> getEliteFourTrainers(boolean isChallengeMode) {
        return null;
    }

    @Override
    public void setTrainers(List<Trainer> trainerData, boolean doubleBattleMode) {
        Iterator<Trainer> allTrainers = trainerData.iterator();
        try {
            int trainernum = SwShConstants.trainerCount;
            // Get current movesets in case we need to reset them for certain
            // trainer mons.
            Map<Integer, List<MoveLearnt>> movesets = this.getMovesLearnt();
            for (int i = 1; i < trainernum; i++) {
                byte[] trainer = readFile(String.format(romEntry.getString("TrainerDataTemplate"), i));
                Trainer tr = allTrainers.next();
                int numPokes = tr.pokemon.size();
                trainer[3] = (byte) numPokes;

                if (doubleBattleMode) {
                    if (!tr.skipImportant()) {
                        if (trainer[2] == 0) {
                            trainer[2] = 1;
                            trainer[12] |= 0x8; // Flag that needs to be set for trainers not to attack their own pokes
                        }
                    }
                }

                int bytesNeeded = 32 * numPokes;
                byte[] trpoke = new byte[bytesNeeded];
                int pokeOffs = 0;
                Iterator<TrainerPokemon> tpokes = tr.pokemon.iterator();
                for (int poke = 0; poke < numPokes; poke++) {
                    TrainerPokemon tp = tpokes.next();
                    byte abilityAndFlag = (byte)((tp.abilitySlot << 4) | tp.forcedGenderFlag);
                    trpoke[pokeOffs] = abilityAndFlag;
                    trpoke[pokeOffs + 1] = tp.nature;
                    trpoke[pokeOffs + 2] = tp.hpEVs;
                    trpoke[pokeOffs + 3] = tp.atkEVs;
                    trpoke[pokeOffs + 4] = tp.defEVs;
                    trpoke[pokeOffs + 5] = tp.spatkEVs;
                    trpoke[pokeOffs + 6] = tp.spdefEVs;
                    trpoke[pokeOffs + 7] = tp.speedEVs;
                    FileFunctions.write2ByteInt(trpoke, pokeOffs + 10, tp.level);
                    FileFunctions.write2ByteInt(trpoke, pokeOffs + 12, tp.pokemon.number);
                    FileFunctions.write2ByteInt(trpoke, pokeOffs + 14, tp.forme);
                    FileFunctions.write2ByteInt(trpoke, pokeOffs + 16, tp.heldItem);
                    if (tp.resetMoves) {
                        int[] pokeMoves = RomFunctions.getMovesAtLevel(getAltFormeOfPokemon(tp.pokemon, tp.forme).number, movesets, tp.level);
                        for (int m = 0; m < 4; m++) {
                            FileFunctions.write2ByteInt(trpoke, pokeOffs + 18 + m * 2, pokeMoves[m]);
                        }
                    } else {
                        FileFunctions.write2ByteInt(trpoke, pokeOffs + 18, tp.moves[0]);
                        FileFunctions.write2ByteInt(trpoke, pokeOffs + 20, tp.moves[1]);
                        FileFunctions.write2ByteInt(trpoke, pokeOffs + 22, tp.moves[2]);
                        FileFunctions.write2ByteInt(trpoke, pokeOffs + 24, tp.moves[3]);
                    }
                    FileFunctions.writeFullInt(trpoke, pokeOffs + 28, tp.IVs);
                    pokeOffs += 32;
                }
                writeFile(String.format(romEntry.getString("TrainerDataTemplate"), i), trainer);
                writeFile(String.format(romEntry.getString("TrainerPokemonTemplate"), i), trpoke);
            }
        } catch (IOException ex) {
            throw new RandomizerIOException(ex);
        }
    }

    @Override
    public List<Move> getMoves() {
        return Arrays.asList(moves);
    }

    @Override
    public Map<Integer, List<MoveLearnt>> getMovesLearnt() {
        Map<Integer, List<MoveLearnt>> movesets = new TreeMap<>();
        try {
            byte[] movesLearnt = readFile(romEntry.getString("PokemonLearnsets"));
            int pokemonCount = SwShConstants.pokemonCount;
            int formeCount = SwShConstants.formeCount;
            for (int i = 1; i <= pokemonCount + formeCount; i++) {
                Pokemon pkmn = pokes.get(i);
                if (pkmn == null) {
                    continue;
                }
                int moveDataLoc = i * SwShConstants.learnsetEntrySize;
                List<MoveLearnt> learnt = new ArrayList<>();
                while (FileFunctions.read2ByteInt(movesLearnt, moveDataLoc) != 0xFFFF ||
                        FileFunctions.read2ByteInt(movesLearnt, moveDataLoc + 2) != 0xFFFF) {
                    int move = FileFunctions.read2ByteInt(movesLearnt, moveDataLoc);
                    int level = FileFunctions.read2ByteInt(movesLearnt, moveDataLoc + 2);
                    MoveLearnt ml = new MoveLearnt();
                    ml.level = level;
                    ml.move = move;
                    learnt.add(ml);
                    moveDataLoc += 4;
                }
                movesets.put(pkmn.number, learnt);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return movesets;
    }

    @Override
    public void setMovesLearnt(Map<Integer, List<MoveLearnt>> movesets) {
        try {
            byte[] movesLearnt = readFile(romEntry.getString("PokemonLearnsets"));
            int pokemonCount = SwShConstants.pokemonCount;
            int formeCount = SwShConstants.formeCount;
            for (int i = 1; i <= pokemonCount + formeCount; i++) {
                Pokemon pkmn = pokes.get(i);
                if (pkmn == null) {
                    continue;
                }
                List<MoveLearnt> learnt = movesets.get(pkmn.number);
                int moveDataLoc = i * SwShConstants.learnsetEntrySize;
                for (MoveLearnt ml: learnt) {
                    FileFunctions.write2ByteInt(movesLearnt, moveDataLoc, ml.move);
                    FileFunctions.write2ByteInt(movesLearnt, moveDataLoc + 2, ml.level);
                    moveDataLoc += 4;
                }
                while (moveDataLoc < (i+1) * SwShConstants.learnsetEntrySize) {
                    FileFunctions.writeFullInt(movesLearnt, moveDataLoc, 0xFFFFFFFF);
                    moveDataLoc += 4;
                }
            }
            // Save
            writeFile(romEntry.getString("PokemonLearnsets"), movesLearnt);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public Map<Integer, List<Integer>> getEggMoves() {
        Map<Integer, List<Integer>> eggMoves = new TreeMap<>();
        try {
            TreeMap<Pokemon, Integer> altFormeEggMoveFiles = new TreeMap<>();
            for (int i = 1; i <= SwShConstants.pokemonCount; i++) {
                Pokemon pkmn = pokes.get(i);
                if (pkmn == null) {
                    continue;
                }
                byte[] movedata = readFile(String.format(romEntry.getString("EggMoveTemplate"),i));
                int formeReference = FileFunctions.read2ByteInt(movedata, 0);
                if (formeReference != pkmn.number) {
                    altFormeEggMoveFiles.put(pkmn, formeReference);
                }
                int numberOfEggMoves = FileFunctions.read2ByteInt(movedata, 2);
                List<Integer> moves = new ArrayList<>();
                for (int j = 0; j < numberOfEggMoves; j++) {
                    int move = FileFunctions.read2ByteInt(movedata, 4 + (j * 2));
                    moves.add(move);
                }
                eggMoves.put(pkmn.number, moves);
            }
            Iterator<Pokemon> iter = altFormeEggMoveFiles.keySet().iterator();
            while (iter.hasNext()) {
                Pokemon originalForme = iter.next();
                int formeNumber = 1;
                int fileNumber = altFormeEggMoveFiles.get(originalForme);
                Pokemon altForme = getAltFormeOfPokemon(originalForme, formeNumber);
                while (!originalForme.equals(altForme)) {
                    byte[] movedata = readFile(String.format(romEntry.getString("EggMoveTemplate"),fileNumber));
                    int numberOfEggMoves = FileFunctions.read2ByteInt(movedata, 2);
                    List<Integer> moves = new ArrayList<>();
                    for (int j = 0; j < numberOfEggMoves; j++) {
                        int move = FileFunctions.read2ByteInt(movedata, 4 + (j * 2));
                        moves.add(move);
                    }
                    eggMoves.put(altForme.number, moves);
                    formeNumber++;
                    fileNumber++;
                    altForme = getAltFormeOfPokemon(originalForme, formeNumber);
                }
                iter.remove();
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return eggMoves;
    }

    @Override
    public void setEggMoves(Map<Integer, List<Integer>> eggMoves) {
        try {
            TreeMap<Pokemon, Integer> altFormeEggMoveFiles = new TreeMap<>();
            for (int i = 1; i <= SwShConstants.pokemonCount; i++) {
                Pokemon pkmn = pokes.get(i);
                if (pkmn == null) {
                    continue;
                }
                byte[] movedata = readFile(String.format(romEntry.getString("EggMoveTemplate"),i));
                int formeReference = FileFunctions.read2ByteInt(movedata, 0);
                if (formeReference != pkmn.number) {
                    altFormeEggMoveFiles.put(pkmn, formeReference);
                }
                List<Integer> moves = eggMoves.get(pkmn.number);
                for (int j = 0; j < moves.size(); j++) {
                    FileFunctions.write2ByteInt(movedata, 4 + (j * 2), moves.get(j));
                }
                // Save
                writeFile(String.format(romEntry.getString("EggMoveTemplate"),i), movedata);
            }
            Iterator<Pokemon> iter = altFormeEggMoveFiles.keySet().iterator();
            while (iter.hasNext()) {
                Pokemon originalForme = iter.next();
                int formeNumber = 1;
                int fileNumber = altFormeEggMoveFiles.get(originalForme);
                Pokemon altForme = getAltFormeOfPokemon(originalForme, formeNumber);
                while (!originalForme.equals(altForme)) {
                    byte[] movedata = readFile(String.format(romEntry.getString("EggMoveTemplate"),fileNumber));
                    List<Integer> moves = eggMoves.get(altForme.number);
                    for (int j = 0; j < moves.size(); j++) {
                        FileFunctions.write2ByteInt(movedata, 4 + (j * 2), moves.get(j));
                    }
                    // Save
                    writeFile(String.format(romEntry.getString("EggMoveTemplate"),fileNumber), movedata);
                    formeNumber++;
                    fileNumber++;
                    altForme = getAltFormeOfPokemon(originalForme, formeNumber);
                }
                iter.remove();
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public boolean supportsFourStartingMoves() {
        return true;
    }

    @Override
    public List<StaticEncounter> getStaticPokemon() {
        List<StaticEncounter> statics = new ArrayList<>();
        try {
            byte[] staticEncounterData = readFile(romEntry.getString("StaticPokemon"));
            SwShStaticEncounterArchive staticArc =
                    SwShStaticEncounterArchive.getRootAsSwShStaticEncounterArchive(ByteBuffer.wrap(staticEncounterData));
            List<Integer> includeIndices = Arrays.stream(romEntry.arrayEntries.get("RealStaticPokemon")).boxed().collect(Collectors.toList());
            for (int i = 0; i < staticArc.staticEncountersLength(); i++) {
                if (!includeIndices.contains(i)) continue;
                SwShStaticEncounter enc = staticArc.staticEncounters(i);
                StaticEncounter se = new StaticEncounter();
                int species = enc.species();
                Pokemon pokemon = pokes.get(species);
                int forme = enc.form();
                if (forme > pokemon.cosmeticForms && forme != 30 && forme != 31) {
                    int speciesWithForme = absolutePokeNumByBaseForme
                            .getOrDefault(species, dummyAbsolutePokeNums)
                            .getOrDefault(forme, 0);
                    pokemon = pokes.get(speciesWithForme);
                }
                se.pkmn = pokemon;
                se.forme = forme;
                se.level = enc.level();
                se.heldItem = enc.heldItem();
                statics.add(se);
            }

            byte[] data = readFile(romEntry.getString("GiftPokemon"));
            SwShGiftEncounterArchive giftArc =
                    SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
            List<Integer> skipGiftIndices =
                    Arrays.stream(romEntry.arrayEntries.get("StarterGiftIndices")).boxed().collect(Collectors.toList());
            // Gifts
            for (int i = 0; i < giftArc.giftEncountersLength(); i++) {
                if (skipGiftIndices.contains(i)) {
                    continue;
                }
                SwShGiftEncounter enc = giftArc.giftEncounters(i);
                StaticEncounter se = new StaticEncounter();
                int species = enc.species();
                Pokemon pokemon = pokes.get(species);
                int forme = enc.form();
                if (forme > pokemon.cosmeticForms && forme != 30 && forme != 31) {
                    int speciesWithForme = absolutePokeNumByBaseForme
                            .getOrDefault(species, dummyAbsolutePokeNums)
                            .getOrDefault(forme, 0);
                    pokemon = pokes.get(speciesWithForme);
                }
                se.pkmn = pokemon;
                se.forme = forme;
                se.level = enc.level();
                se.heldItem = enc.heldItem();
                se.isEgg = enc.isEgg() != 0;
                statics.add(se);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        consolidateLinkedEncounters(statics);
        return statics;
    }

    private void consolidateLinkedEncounters(List<StaticEncounter> statics) {
        List<StaticEncounter> encountersToRemove = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : romEntry.linkedStaticOffsets.entrySet()) {
            StaticEncounter baseEncounter = statics.get(entry.getKey());
            for (int v: entry.getValue()) {
                StaticEncounter linkedEncounter = statics.get(v);
                baseEncounter.linkedEncounters.add(linkedEncounter);
                encountersToRemove.add(linkedEncounter);
            }
        }
        for (StaticEncounter encounter : encountersToRemove) {
            statics.remove(encounter);
        }
    }

    @Override
    public boolean setStaticPokemon(List<StaticEncounter> staticPokemon) {
        try {
            unlinkStaticEncounters(staticPokemon);

            byte[] staticEncounterData = readFile(romEntry.getString("StaticPokemon"));
            // Make forms settable for most static Pokemon
            for (int i = 0; i < romEntry.arrayEntries.get("StaticPokemonVTables").length; i++) {
                int newFormOffset = romEntry.arrayEntries.get("StaticPokemonNewFormOffsets")[i];
                if (newFormOffset < 0) continue;
                int vTableOffset = romEntry.arrayEntries.get("StaticPokemonVTables")[i];
                staticEncounterData[vTableOffset + 0x14] = (byte)newFormOffset;
            }
            // Need extra space for new vtable + 4 static encounter objects
            byte[] newStaticEncounterData = new byte[staticEncounterData.length + 0x44 + 4*0x58];
            System.arraycopy(staticEncounterData,0,newStaticEncounterData,0,staticEncounterData.length);
            addStaticEncounterVTable(newStaticEncounterData,staticEncounterData.length);
            int movedStatics = 0;
            List<Integer> staticsToMove = Arrays.stream(romEntry.arrayEntries.get("StaticPokemonWithoutPadding"))
                    .boxed()
                    .collect(Collectors.toList());
            SwShStaticEncounterArchive staticArc =
                    SwShStaticEncounterArchive.getRootAsSwShStaticEncounterArchive(ByteBuffer.wrap(newStaticEncounterData));

            List<Integer> includeIndices = Arrays.stream(romEntry.arrayEntries.get("RealStaticPokemon"))
                    .boxed()
                    .collect(Collectors.toList());
            Iterator<StaticEncounter> staticIter = staticPokemon.iterator();
            for (int i = 0; i < staticArc.staticEncountersLength(); i++) {
                if (!includeIndices.contains(i)) continue;
                SwShStaticEncounter enc = staticArc.staticEncounters(i);
                // Make forms settable for static Pokemon where it could not be done the easy way
                if (staticsToMove.contains(i)) {
                    int objectAbsOffset = staticEncounterData.length + 0x44 + movedStatics*0x58;
                    addStaticEncounterFlatBufferObject(
                            newStaticEncounterData,
                            enc,
                            staticEncounterData.length,
                            objectAbsOffset);
                    FileFunctions.writeFullInt(newStaticEncounterData,0x18+i*4,(objectAbsOffset - (0x18+i*4)));
                    movedStatics++;
                    enc = staticArc.staticEncounters(i);
                }
                StaticEncounter se = staticIter.next();
                enc.mutateSpecies(se.pkmn.number);
                enc.mutateForm(se.forme);
                enc.mutateLevel(se.level);
                if (se.resetMoves) {
                    enc.mutateMove0(0);
                    enc.mutateMove1(0);
                    enc.mutateMove2(0);
                    enc.mutateMove3(0);
                }
                enc.mutateHeldItem(se.heldItem);
            }

            // Need to rewrite this flatbuffer to make forms settable, might already have been done in setStarters
            if (giftEncounterArchive == null) {
                byte[] data = readFile(romEntry.getString("GiftPokemon"));
                SwShGiftEncounterArchive oldArc =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(data));
                byte[] newData = constructNewGiftEncounterArchiveFlatBuffer(oldArc);
                giftEncounterArchive =
                        SwShGiftEncounterArchive.getRootAsSwShGiftEncounterArchive(ByteBuffer.wrap(newData));
            }
            List<Integer> skipGiftIndices =
                    Arrays.stream(romEntry.arrayEntries.get("StarterGiftIndices")).boxed().collect(Collectors.toList());
            // Gifts
            for (int i = 0; i < giftEncounterArchive.giftEncountersLength(); i++) {
                if (skipGiftIndices.contains(i)) {
                    continue;
                }
                SwShGiftEncounter enc = giftEncounterArchive.giftEncounters(i);
                StaticEncounter se = staticIter.next();
                enc.mutateSpecies(se.pkmn.number);
                enc.mutateForm(se.forme);
                enc.mutateLevel(se.level);
                enc.mutateHeldItem(se.heldItem);
            }

            writeFile(romEntry.getString("StaticPokemon"),staticArc.getByteBuffer().array());
            writeFile(romEntry.getString("GiftPokemon"),giftEncounterArchive.getByteBuffer().array());
            return true;
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void unlinkStaticEncounters(List<StaticEncounter> statics) {
        List<Integer> offsetsToInsert = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : romEntry.linkedStaticOffsets.entrySet()) {
            offsetsToInsert.addAll(entry.getValue());
        }
        Collections.sort(offsetsToInsert);
        for (Integer offsetToInsert : offsetsToInsert) {
            statics.add(offsetToInsert, new StaticEncounter());
        }
        for (Map.Entry<Integer, List<Integer>> entry : romEntry.linkedStaticOffsets.entrySet()) {
            StaticEncounter baseEncounter = statics.get(entry.getKey());
            Iterator<StaticEncounter> linkedIter = baseEncounter.linkedEncounters.iterator();
            for (int v: entry.getValue()) {
                statics.set(v, linkedIter.next());
            }
        }
    }

    private void addStaticEncounterVTable(byte[] data, int offset) {
        FileFunctions.write2ByteInt(data,offset,0x44);     // VTable length
        FileFunctions.write2ByteInt(data,offset+2,0x54);     // Object inline length
        FileFunctions.write2ByteInt(data,offset+4,0x14);      // Background far type ID
        FileFunctions.write2ByteInt(data,offset+6,0x1C);      // Background near type ID
        FileFunctions.write2ByteInt(data,offset+8,0x4);      // EV Speed
        FileFunctions.write2ByteInt(data,offset+0xA,0x5);    // EV Attack
        FileFunctions.write2ByteInt(data,offset+0xC,0);    // EV Defense
        FileFunctions.write2ByteInt(data,offset+0xE,0x6);   // EV HP
        FileFunctions.write2ByteInt(data,offset+0x10,0);  // EV Sp.Attack
        FileFunctions.write2ByteInt(data,offset+0x12,0);  // EV Sp.Defense
        FileFunctions.write2ByteInt(data,offset+0x14,0x7);   // Form
        FileFunctions.write2ByteInt(data,offset+0x16,0x8);  // Dynamax level
        FileFunctions.write2ByteInt(data,offset+0x18,0);   // Field 0A
        FileFunctions.write2ByteInt(data,offset+0x1A,0x24);   // Encounter ID
        FileFunctions.write2ByteInt(data,offset+0x1C,0x9);   // Field 0C
        FileFunctions.write2ByteInt(data,offset+0x1E,0);   // G-Max Factor
        FileFunctions.write2ByteInt(data,offset+0x20,0x2C);   // Held Item
        FileFunctions.write2ByteInt(data,offset+0x22,0xA);  // Level
        FileFunctions.write2ByteInt(data,offset+0x24,0x30);  // Encounter Scenario
        FileFunctions.write2ByteInt(data,offset+0x26,0x34);  // Species
        FileFunctions.write2ByteInt(data,offset+0x28,0x38);  // Shiny Lock
        FileFunctions.write2ByteInt(data,offset+0x2A,0x3C);   // Nature
        FileFunctions.write2ByteInt(data,offset+0x2C,0xB);   // Gender
        FileFunctions.write2ByteInt(data,offset+0x2E,0xC);   // IV Speed
        FileFunctions.write2ByteInt(data,offset+0x30,0xD);   // IV Attack
        FileFunctions.write2ByteInt(data,offset+0x32,0xE);   // IV Defense
        FileFunctions.write2ByteInt(data,offset+0x34,0xF);   // IV HP
        FileFunctions.write2ByteInt(data,offset+0x36,0x10);   // IV Sp.Attack
        FileFunctions.write2ByteInt(data,offset+0x38,0x11);   // IV Sp.Defense
        FileFunctions.write2ByteInt(data,offset+0x3A,0x40);   // Ability
        FileFunctions.write2ByteInt(data,offset+0x3C,0x44);   // Move 0
        FileFunctions.write2ByteInt(data,offset+0x3E,0x48);   // Move 1
        FileFunctions.write2ByteInt(data,offset+0x40,0x4C);   // Move 2
        FileFunctions.write2ByteInt(data,offset+0x42,0x50);   // Move 3
    }

    private void addStaticEncounterFlatBufferObject(byte[] data, SwShStaticEncounter enc, int vTableAbsOffset,
                                                    int objectAbsOffset) {
        FileFunctions.writeFullInt(data,objectAbsOffset,objectAbsOffset - vTableAbsOffset);
        data[objectAbsOffset+4] = (byte)enc.evSpe();
        data[objectAbsOffset+5] = (byte)enc.evAtk();
        data[objectAbsOffset+6] = (byte)enc.evHp();
        data[objectAbsOffset+7] = (byte)enc.form();
        data[objectAbsOffset+8] = (byte)enc.dynamaxLevel();
        data[objectAbsOffset+9] = (byte)enc.field0c();
        data[objectAbsOffset+0xA] = (byte)enc.level();
        data[objectAbsOffset+0xB] = (byte)enc.gender();
        data[objectAbsOffset+0xC] = enc.ivSpe();
        data[objectAbsOffset+0xD] = enc.ivAtk();
        data[objectAbsOffset+0xE] = enc.ivDef();
        data[objectAbsOffset+0xF] = enc.ivHp();
        data[objectAbsOffset+0x10] = enc.ivSpa();
        data[objectAbsOffset+0x11] = enc.ivSpd();
        FileFunctions.writeFullInt(data,objectAbsOffset+0x14,enc.heldItem());
        FileFunctions.writeFullLong(data,objectAbsOffset+0x18,enc.backgroundFarTypeId());
        FileFunctions.writeFullLong(data,objectAbsOffset+0x20,enc.backgroundNearTypeId());
        FileFunctions.writeFullLong(data,objectAbsOffset+0x28,enc.encounterId());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x30,enc.encounterScenario());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x34,enc.species());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x38,(int)enc.shinyLock());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x3C,(int)enc.nature());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x40,enc.ability());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x44,enc.move0());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x48,enc.move1());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x4C,enc.move2());
        FileFunctions.writeFullInt(data,objectAbsOffset+0x50,enc.move3());
    }

    @Override
    public boolean canChangeStaticPokemon() {
        return true;
    }

    @Override
    public boolean hasStaticAltFormes() {
        return true;
    }

    @Override
    public boolean hasMainGameLegendaries() {
        return false;
    }

    @Override
    public List<Integer> getMainGameLegendaries() {
        return null;
    }

    @Override
    public List<Integer> getSpecialMusicStatics() {
        return null;
    }

    @Override
    public void applyCorrectStaticMusic(Map<Integer, Integer> specialMusicStaticChanges) {

    }

    @Override
    public boolean hasStaticMusicFix() {
        return false;
    }

    @Override
    public List<TotemPokemon> getTotemPokemon() {
        return null;
    }

    @Override
    public void setTotemPokemon(List<TotemPokemon> totemPokemon) {

    }

    // TODO: Fix the log being really weird with this
    @Override
    public List<Integer> getTMMoves() {
        try {
            byte[] itemData = readFile(romEntry.getString("ItemData"));
            List<Integer> tms = new ArrayList<>();

            // TM and TR information is stored in numerical order in a simple table, where each entry looks like this:
            // - 2 byte item ID
            // - 2 byte move ID
            // We start at the very beginning of the table and simply work our way down, adding move IDs to the list.
            int currentOffset = SwShConstants.tmTrMoveTableOffset;
            for (int i = 0; i < SwShConstants.tmCount + SwShConstants.trCount; i++) {
                int move = FileFunctions.read2ByteInt(itemData, currentOffset + 2);
                tms.add(move);
                currentOffset += 4;
            }

            return tms;
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public List<Integer> getHMMoves() {
        return new ArrayList<>();
    }

    @Override
    public void setTMMoves(List<Integer> moveIndexes) {
        try {
            byte[] itemData = readFile(romEntry.getString("ItemData"));
            int currentOffset = SwShConstants.tmTrMoveTableOffset;
            for (int i = 0; i < SwShConstants.tmCount + SwShConstants.trCount; i++) {
                // Change what move the TM/TR teaches.
                FileFunctions.write2ByteInt(itemData, currentOffset + 2, moveIndexes.get(i));

                // Adjust the sprite to match the new move type.
                int itemIndex = FileFunctions.read2ByteInt(itemData, currentOffset);
                SwShItem item = itemTable.get(itemIndex);
                Move m = moves[moveIndexes.get(i)];
                int newSpriteNumber = i < SwShConstants.tmCount ? typeTMSpriteNumber(m.type) : typeTRSpriteNumber(m.type);
                item.sprite = newSpriteNumber;

                currentOffset += 4;
            }
            writeFile(romEntry.getString("ItemData"), itemData);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public int getTMCount() {
        return SwShConstants.tmCount + SwShConstants.trCount;
    }

    @Override
    public int getHMCount() {
        return 0;
    }

    @Override
    public Map<Pokemon, boolean[]> getTMHMCompatibility() {
        try {
            Map<Pokemon, boolean[]> compat = new TreeMap<>();
            byte[] personalInfo = this.readFile(romEntry.getString("PokemonStats"));
            for (Map.Entry<Integer, Pokemon> pokeEntry: pokes.entrySet()) {
                byte[] pokeData = new byte[SwShConstants.bsSize];
                int i = pokeEntry.getKey();
                System.arraycopy(personalInfo, i * SwShConstants.bsSize, pokeData, 0, SwShConstants.bsSize);
                Pokemon pkmn = pokes.get(i);
                boolean[] flags = new boolean[SwShConstants.tmCount + SwShConstants.trCount + 1];
                for (int j = 0; j < 13; j++) {
                    readByteIntoFlags(pokeData, flags, j * 8 + 1, SwShConstants.bsTMCompatOffset + j);
                }
                for (int j = 0; j < 13; j++) {
                    readByteIntoFlags(pokeData, flags, j * 8 + SwShConstants.tmCount + 1, SwShConstants.bsTRCompatOffset + j);
                }
                compat.put(pkmn, flags);
            }
            return compat;
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public void setTMHMCompatibility(Map<Pokemon, boolean[]> compatData) {
        try {
            byte[] personalInfo = this.readFile(romEntry.getString("PokemonStats"));
            for (Map.Entry<Integer, Pokemon> pokeEntry: pokes.entrySet()) {
                byte[] pokeData = new byte[SwShConstants.bsSize];
                int i = pokeEntry.getKey();
                System.arraycopy(personalInfo, i * SwShConstants.bsSize, pokeData, 0, SwShConstants.bsSize);
                Pokemon key = pokeEntry.getValue();
                boolean[] flags = compatData.get(key);
                for (int j = 0; j < 13; j++) {
                    pokeData[SwShConstants.bsTMCompatOffset + j] = getByteFromFlags(flags, j * 8 + 1);
                }
                for (int j = 0; j < 13; j++) {
                    pokeData[SwShConstants.bsTRCompatOffset + j] = getByteFromFlags(flags, j * 8 + SwShConstants.tmCount + 1);
                }
                System.arraycopy(pokeData, 0, personalInfo, i * SwShConstants.bsSize, SwShConstants.bsSize);
            }
            writeFile(romEntry.getString("PokemonStats"), personalInfo);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public boolean hasMoveTutors() {
        return false;
    }

    @Override
    public List<Integer> getMoveTutorMoves() {
        return null;
    }

    @Override
    public void setMoveTutorMoves(List<Integer> moves) {

    }

    @Override
    public Map<Pokemon, boolean[]> getMoveTutorCompatibility() {
        return null;
    }

    @Override
    public void setMoveTutorCompatibility(Map<Pokemon, boolean[]> compatData) {

    }

    @Override
    public boolean canChangeTrainerText() {
        return true;
    }

    @Override
    public List<String> getTrainerNames() {
        try {
            return getStrings(romEntry.getString("TrainerNames"));
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public void setTrainerNames(List<String> trainerNames) {

    }

    @Override
    public TrainerNameMode trainerNameMode() {
        return TrainerNameMode.MAX_LENGTH;
    }

    @Override
    public List<Integer> getTCNameLengthsByTrainer() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getTrainerClassNames() {
        try {
            return getStrings(romEntry.getString("TrainerClassNames"));
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public void setTrainerClassNames(List<String> trainerClassNames) {

    }

    @Override
    public boolean fixedTrainerClassNamesLength() {
        return false;
    }

    @Override
    public List<Integer> getDoublesTrainerClasses() {
        return null;
    }

    @Override
    public ItemList getAllowedItems() {
        ItemList allowedItems = SwShConstants.allowedItems.copy();
        for (int bannedItem: SwShConstants.bannedItems) {
            allowedItems.banSingles(bannedItem);
        }
        return allowedItems;
    }

    @Override
    public ItemList getNonBadItems() {
        ItemList nonBadItems = SwShConstants.nonBadItems.copy();
        for (int bannedItem: SwShConstants.bannedItems) {
            nonBadItems.banSingles(bannedItem);
        }
        return nonBadItems;
    }

    @Override
    public List<Integer> getEvolutionItems() {
        List<Integer> evolutionItems = new ArrayList<>(SwShConstants.evolutionItems);
        for (List<Integer> dupes: SwShConstants.duplicateEvolutionItems) {
            evolutionItems.add(dupes.get(this.random.nextInt(dupes.size())));
        }
        return evolutionItems;
    }

    @Override
    public List<Integer> getUniqueNoSellItems() {
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getRegularShopItems() {
        return SwShConstants.regularShopItems;
    }

    @Override
    public List<Integer> getOPShopItems() {
        return SwShConstants.opShopItems;
    }

    @Override
    public String[] getItemNames() {
        return itemNames.toArray(new String[0]);
    }

    @Override
    public List<Integer> getRequiredFieldTMs() {
        return SwShConstants.requiredFieldTMs;
    }

    private int tmFromIndex(int index) {

        if (index >= SwShConstants.tmBlockOneOffset
                && index < SwShConstants.tmBlockOneOffset + SwShConstants.tmBlockOneCount) {
            return index - (SwShConstants.tmBlockOneOffset - 1);
        } else if (index >= SwShConstants.tmBlockTwoOffset
                && index < SwShConstants.tmBlockTwoOffset + SwShConstants.tmBlockTwoCount) {
            return (index + SwShConstants.tmBlockOneCount) - (SwShConstants.tmBlockTwoOffset - 1);
        } else if (index >= SwShConstants.tmBlockThreeOffset
                && index < SwShConstants.tmBlockThreeOffset + SwShConstants.tmBlockThreeCount) {
            return (index + SwShConstants.tmBlockOneCount + SwShConstants.tmBlockTwoCount) - (SwShConstants.tmBlockThreeOffset - 1);
        } else {
            return 0;   // TM00
        }
    }

    private int indexFromTM(int tm) {
        if (tm >= 1 && tm <= SwShConstants.tmBlockOneCount) {
            return tm + (SwShConstants.tmBlockOneOffset - 1);
        } else if (tm > SwShConstants.tmBlockOneCount && tm <= SwShConstants.tmBlockOneCount + SwShConstants.tmBlockTwoCount) {
            return tm + (SwShConstants.tmBlockTwoOffset - 1 - SwShConstants.tmBlockOneCount);
        } else if (tm > SwShConstants.tmBlockOneCount + SwShConstants.tmBlockTwoCount && tm <= SwShConstants.tmBlockOneCount + SwShConstants.tmBlockTwoCount + SwShConstants.tmBlockThreeCount) {
            return tm + (SwShConstants.tmBlockThreeOffset - 1 - (SwShConstants.tmBlockOneCount + SwShConstants.tmBlockTwoCount));
        } else {
            return SwShConstants.tmBlockFourOffset;
        }
    }

    @Override
    public List<Integer> getCurrentFieldTMs() {
        List<Integer> fieldItems = this.getFieldItems();
        List<Integer> fieldTMs = new ArrayList<>();

        ItemList allowedItems = SwShConstants.allowedItems;
        for (int item : fieldItems) {
            if (allowedItems.isTM(item)) {
                fieldTMs.add(tmFromIndex(item));
            }
        }

        return fieldTMs.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public void setFieldTMs(List<Integer> fieldTMs) {
        List<Integer> fieldItems = this.getFieldItems();
        int fiLength = fieldItems.size();
        Iterator<Integer> iterTMs = fieldTMs.iterator();
        Map<Integer,Integer> tmMap = new HashMap<>();

        ItemList allowedItems = SwShConstants.allowedItems;
        for (int i = 0; i < fiLength; i++) {
            int oldItem = fieldItems.get(i);
            if (allowedItems.isTM(oldItem)) {
                if (tmMap.get(oldItem) != null) {
                    fieldItems.set(i,tmMap.get(oldItem));
                    continue;
                }
                int newItem = indexFromTM(iterTMs.next());
                fieldItems.set(i, newItem);
                tmMap.put(oldItem,newItem);
            }
        }

        this.setFieldItems(fieldItems);
    }

    @Override
    public List<Integer> getRegularFieldItems() {

        List<Integer> fieldItems = this.getFieldItems();
        List<Integer> fieldRegItems = new ArrayList<>();

        ItemList allowedItems = SwShConstants.allowedItems;
        for (int item : fieldItems) {
            if (allowedItems.isAllowed(item) && !(allowedItems.isTM(item))) {
                fieldRegItems.add(item);
            }
        }

        return fieldRegItems;
    }

    @Override
    public void setRegularFieldItems(List<Integer> items) {
        List<Integer> fieldItems = this.getFieldItems();
        int fiLength = fieldItems.size();
        Iterator<Integer> iterNewItems = items.iterator();

        ItemList allowedItems = SwShConstants.allowedItems;
        for (int i = 0; i < fiLength; i++) {
            int oldItem = fieldItems.get(i);
            if (!(allowedItems.isTM(oldItem)) && allowedItems.isAllowed(oldItem)) {
                int newItem = iterNewItems.next();
                fieldItems.set(i, newItem);
            }
        }

        this.setFieldItems(fieldItems);
    }

    @Override
    public boolean hasShopRandomization() {
        return true;
    }

    @Override
    public Map<Integer, Shop> getShopItems() {
        Map<Integer, Shop> shopItemsMap = new TreeMap<>();
        try {
            byte[] shopData = readFile(romEntry.getString("ShopItems"));
            SwShShopInventory shops = SwShShopInventory.getRootAsSwShShopInventory(ByteBuffer.wrap(shopData));
            for (int i = 0; i < shops.shop1Length(); i++) {
                SwShShop1 shop1 = shops.shop1(i);
                String shopName = SwShConstants.getShopName(shop1.hash());
                if (shopName == null) continue;
                SwShInventory inventory = shop1.inventory();
                Shop shop = new Shop();
                shop.name = shopName;
                shop.isMainGame = true;
                List<Integer> items = new ArrayList<>();
                for (int j = 0; j < inventory.itemsLength(); j++) {
                    items.add(inventory.items(j));
                }
                shop.items = items;
                shopItemsMap.put(i,shop);
            }
            return shopItemsMap;
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public void setShopItems(Map<Integer, Shop> shopItems) {
        try {
            byte[] shopData = readFile(romEntry.getString("ShopItems"));
            SwShShopInventory shops = SwShShopInventory.getRootAsSwShShopInventory(ByteBuffer.wrap(shopData));
            for (int shopIndex: shopItems.keySet()) {
                SwShInventory inventory = shops.shop1(shopIndex).inventory();
                Shop shop = shopItems.get(shopIndex);
                for (int i = 0; i < shop.items.size(); i++) {
                    inventory.mutateItems(i,shop.items.get(i));
                }
            }
            writeFile(romEntry.getString("ShopItems"),shops.getByteBuffer().array());
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public void setShopPrices() {
        for (int itemIndex: itemTable.keySet()) {
            Integer balancedItemPrice = SwShConstants.balancedItemPrices.get(itemIndex);
            if (balancedItemPrice == null) continue;
            itemTable.get(itemIndex).price = balancedItemPrice;
        }
    }

    @Override
    public List<PickupItem> getPickupItems() {
        List<PickupItem> pickupItems = new ArrayList<>();
        try {
            byte[] fieldData = this.readFile(romEntry.getString("FieldDataPack"));
            GFPack fieldDataPack = new GFPack(fieldData);
            byte[] pickupData = fieldDataPack.getDataFileName(romEntry.getString("PickupItemFile"));
            int numberOfPickupItems = FileFunctions.readFullInt(pickupData, 0x3C);
            for (int i = 0; i < numberOfPickupItems; i++) {
                int offsetOfOffset = 0x40 + (4 * i);
                int offset = FileFunctions.readFullInt(pickupData, offsetOfOffset) + 0x44 + (4 * i);
                int item = FileFunctions.readFullInt(pickupData, offset);
                PickupItem pickupItem = new PickupItem(item);
                for (int levelRange = 0; levelRange < 10; levelRange++) {
                    pickupItem.probabilities[levelRange] = FileFunctions.readFullInt(pickupData, offset + 12 + (levelRange * 4));
                }
                pickupItems.add(pickupItem);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pickupItems;
    }

    @Override
    public void setPickupItems(List<PickupItem> pickupItems) {
        try {
            byte[] fieldData = this.readFile(romEntry.getString("FieldDataPack"));
            GFPack fieldDataPack = new GFPack(fieldData);
            byte[] pickupData = fieldDataPack.getDataFileName(romEntry.getString("PickupItemFile"));
            for (int i = 0; i < pickupItems.size(); i++) {
                int offsetOfOffset = 0x40 + (4 * i);
                int offset = FileFunctions.readFullInt(pickupData, offsetOfOffset) + 0x44 + (4 * i);
                FileFunctions.writeFullInt(pickupData, offset, pickupItems.get(i).item);
            }
            fieldDataPack.setDataFileName(romEntry.getString("PickupItemFile"), pickupData);
            writeFile(romEntry.getString("FieldDataPack"), fieldDataPack.writePack());
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    public List<Integer> getFieldItems() {
        List<Integer> fieldItems = new ArrayList<>();

        for (String areaName: placementAreaTable.map.values()) {
            if (placementPack.getIndexFileName(areaName + ".bin") < 0) {
                continue;
            }
            byte[] areaData = placementPack.getDataFileName(areaName + ".bin");
            SwShPlacementAreaArchive arc =
                    SwShPlacementAreaArchive.getRootAsSwShPlacementAreaArchive(ByteBuffer.wrap(areaData));

            for (int i = 0; i < arc.placementZonesLength(); i++) {
                SwShPlacementZone zone = arc.placementZones(i);
                for (int j = 0; j < zone.fieldItemsLength(); j++) {
                    SwShPlacementFieldItem fldItem = zone.fieldItems(j).fieldItem();
                    for (int k = 0; k < fldItem.itemHashesLength(); k++) {
                        long hash = fldItem.itemHashes(k);
                        if (hash == SwShConstants.noItemHash) {
                            break;
                        }
                        int index = itemHashToIndex.get(hash);
                        fieldItems.add(index);
                    }
                }
                for (int j = 0; j < zone.hiddenItemsLength(); j++) {
                    SwShPlacementHiddenItem hiddenItem = zone.hiddenItems(j).hiddenItem();
                    for (int k = 0; k < hiddenItem.itemsLength(); k++) {
                        long hash = hiddenItem.items(k).itemHash();
                        if (hash == SwShConstants.noItemHash) {
                            break;
                        }
                        int index = itemHashToIndex.get(hash);
                        fieldItems.add(index);
                    }
                }
            }
        }
        return fieldItems;
    }

    public void setFieldItems(List<Integer> items) {
        Iterator<Integer> iterItems = items.iterator();
        for (String areaName: placementAreaTable.map.values()) {
            if (placementPack.getIndexFileName(areaName + ".bin") < 0) {
                continue;
            }
            byte[] areaData = placementPack.getDataFileName(areaName + ".bin");
            SwShPlacementAreaArchive arc =
                    SwShPlacementAreaArchive.getRootAsSwShPlacementAreaArchive(ByteBuffer.wrap(areaData));

            for (int i = 0; i < arc.placementZonesLength(); i++) {
                SwShPlacementZone zone = arc.placementZones(i);
                for (int j = 0; j < zone.fieldItemsLength(); j++) {
                    SwShPlacementFieldItem fldItem = zone.fieldItems(j).fieldItem();
                    for (int k = 0; k < fldItem.itemHashesLength(); k++) {
                        long hash = fldItem.itemHashes(k);
                        if (hash == SwShConstants.noItemHash) {
                            break;
                        }
                        fldItem.mutateItemHashes(k, itemIndexToHash.get(iterItems.next()));
                    }
                }
                for (int j = 0; j < zone.hiddenItemsLength(); j++) {
                    SwShPlacementHiddenItem hiddenItem = zone.hiddenItems(j).hiddenItem();
                    for (int k = 0; k < hiddenItem.itemsLength(); k++) {
                        long hash = hiddenItem.items(k).itemHash();
                        if (hash == SwShConstants.noItemHash) {
                            break;
                        }
                        hiddenItem.items(k).mutateItemHash(itemIndexToHash.get(iterItems.next()));
                    }
                }
            }
            placementPack.setDataFileName(areaName + ".bin", arc.getByteBuffer().array());
        }
    }

    @Override
    public List<IngameTrade> getIngameTrades() {
        List<IngameTrade> ingameTrades = new ArrayList<>();
        try {
            byte[] tradeData = readFile(romEntry.getString("TradePokemon"));
            SwShTradeEncounterArchive tradeEncounters =
                    SwShTradeEncounterArchive.getRootAsSwShTradeEncounterArchive(ByteBuffer.wrap(tradeData));
            List<String> tradeStrings = getStrings(romEntry.getString("TradePokemonStrings"));
            List<Integer> nickNameOffsets = SwShConstants.tradeNickNameOffsets;
            List<Integer> otOffsets = SwShConstants.tradeOTOffsets;
            for (int i = 0; i < tradeEncounters.tradeEncountersLength(); i++) {
                if (i == SwShConstants.duplicateTrade) {
                    continue;
                }
                SwShTradeEncounter tradeEncounter = tradeEncounters.tradeEncounters(i);
                IngameTrade trade = new IngameTrade();
                int givenSpecies = tradeEncounter.species();
                int requestedSpecies = tradeEncounter.requiredSpecies();
                Pokemon givenPokemon = pokes.get(givenSpecies);
                Pokemon requestedPokemon = pokes.get(requestedSpecies);
                int givenForme = tradeEncounter.form();
                if (givenForme > givenPokemon.cosmeticForms && givenForme != 30 && givenForme != 31) {
                    givenPokemon = getAltFormeOfPokemon(givenPokemon, givenForme);
                }
                int requestedForme = tradeEncounter.requiredForm();
                if (requestedForme > requestedPokemon.cosmeticForms && requestedForme != 30 && requestedForme != 31) {
                    requestedPokemon = getAltFormeOfPokemon(requestedPokemon, requestedForme);
                }
                trade.givenPokemon = givenPokemon;
                trade.requestedPokemon = requestedPokemon;
                trade.nickname = tradeStrings.get(nickNameOffsets.get(i));
                trade.otName = tradeStrings.get(otOffsets.get(i));
                trade.otId = tradeEncounter.trainerId();
                trade.ivs = new int[6];
                trade.ivs[0] = tradeEncounter.ivHp();
                trade.ivs[1] = tradeEncounter.ivAtk();
                trade.ivs[2] = tradeEncounter.ivDef();
                trade.ivs[3] = tradeEncounter.ivSpe();
                trade.ivs[4] = tradeEncounter.ivSpa();
                trade.ivs[5] = tradeEncounter.ivSpd();
                trade.item = tradeEncounter.heldItem();
                if (trade.item < 0) {
                    trade.item = 0;
                }
                ingameTrades.add(trade);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return ingameTrades;
    }

    @Override
    public void setIngameTrades(List<IngameTrade> trades) {
        try {
            List<IngameTrade> oldTrades = this.getIngameTrades();
            byte[] tradeData = readFile(romEntry.getString("TradePokemon"));
            SwShTradeEncounterArchive tradeEncounters =
                    SwShTradeEncounterArchive.getRootAsSwShTradeEncounterArchive(ByteBuffer.wrap(tradeData));
            List<String> tradeStrings = getStrings(romEntry.getString("TradePokemonStrings"));
            Map<Integer, List<Integer>> hardcodedTradeTextOffsets = SwShConstants.getHardcodedTradeTextOffsets();
            List<Integer> nickNameOffsets = SwShConstants.tradeNickNameOffsets;
            List<Integer> otOffsets = SwShConstants.tradeOTOffsets;
            for (int i = 0; i < tradeEncounters.tradeEncountersLength(); i++) {
                int j = i;
                if (i >= SwShConstants.duplicateTrade) {
                    j = i - 1;
                }
                IngameTrade trade = trades.get(j);
                SwShTradeEncounter tradeEncounter = tradeEncounters.tradeEncounters(i);
                Pokemon givenPokemon = trade.givenPokemon;
                int forme = 0;
                if (givenPokemon.formeNumber > 0) {
                    forme = givenPokemon.formeNumber;
                    givenPokemon = givenPokemon.baseForme;
                }
                tradeEncounter.mutateSpecies(givenPokemon.number);
                tradeEncounter.mutateForm(forme);
                tradeEncounter.mutateRequiredSpecies(trade.requestedPokemon.number);
                tradeStrings.set(nickNameOffsets.get(j),trade.nickname);
                tradeStrings.set(otOffsets.get(j),trade.otName);
                tradeEncounter.mutateTrainerId(trade.otId);
                tradeEncounter.mutateIvHp((byte)trade.ivs[0]);
                tradeEncounter.mutateIvAtk((byte)trade.ivs[1]);
                tradeEncounter.mutateIvDef((byte)trade.ivs[2]);
                tradeEncounter.mutateIvSpe((byte)trade.ivs[3]);
                tradeEncounter.mutateIvSpa((byte)trade.ivs[4]);
                tradeEncounter.mutateIvSpd((byte)trade.ivs[5]);
                tradeEncounter.mutateHeldItem(trade.item);

                List<Integer> hardcodedTextOffsetsForThisTrade = hardcodedTradeTextOffsets.get(i);
                if (hardcodedTextOffsetsForThisTrade != null) {
                    updateHardcodedTradeText(oldTrades.get(j), trade, tradeStrings, hardcodedTextOffsetsForThisTrade);
                }
            }
            writeFile(romEntry.getString("TradePokemon"),tradeEncounters.getByteBuffer().array());
            setStrings(romEntry.getString("TradePokemonStrings"), tradeStrings);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void updateHardcodedTradeText(IngameTrade oldTrade, IngameTrade newTrade, List<String> tradeStrings, List<Integer> hardcodedTextOffsets) {
        for (int offset : hardcodedTextOffsets) {
            String hardcodedText = tradeStrings.get(offset);
            String oldRequestedName = oldTrade.requestedPokemon.name;
            String oldGivenName = oldTrade.givenPokemon.name;
            String newRequestedName = newTrade.requestedPokemon.name;
            String newGivenName = newTrade.givenPokemon.name;
            hardcodedText = hardcodedText.replace("Galarian Pokémon", "a certain Pokémon");
            hardcodedText = hardcodedText.replace("a Galarian " + oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace("Galarian " + oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace(oldRequestedName + " in Galar", newRequestedName);
            hardcodedText = hardcodedText.replace("a " + oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace("an " + oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace("A " + oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace("Kantonian " + oldGivenName, newGivenName);
            hardcodedText = hardcodedText.replace("my " + oldGivenName, "my " + newGivenName);
            hardcodedText = hardcodedText.replace(oldGivenName + " I caught abroad", newGivenName + " I caught abroad");
            hardcodedText = hardcodedText.replace("super-rare " + oldGivenName, "super-rare " + newGivenName);
            hardcodedText = hardcodedText.replace("one from my region", newGivenName);
            hardcodedText = hardcodedText.replace(oldRequestedName, newRequestedName);
            hardcodedText = hardcodedText.replace(oldGivenName, newGivenName);
            tradeStrings.set(offset, hardcodedText);
        }
    }

    @Override
    public boolean hasDVs() {
        return false;
    }

    @Override
    public void removeImpossibleEvolutions(Settings settings) {
        boolean changeMoveEvos = !(settings.getMovesetsMod() == Settings.MovesetsMod.UNCHANGED);

        Map<Integer, List<MoveLearnt>> movesets = this.getMovesLearnt();
        Set<Evolution> extraEvolutions = new HashSet<>();
        for (Pokemon pkmn : pokes.values()) {
            if (pkmn != null) {
                extraEvolutions.clear();
                for (Evolution evo : pkmn.evolutionsFrom) {
                    if (changeMoveEvos && evo.type == EvolutionType.LEVEL_WITH_MOVE) {
                        // read move
                        int move = evo.extraInfo;
                        int levelLearntAt = 1;
                        for (MoveLearnt ml : movesets.get(evo.from.number)) {
                            if (ml.move == move) {
                                levelLearntAt = ml.level;
                                break;
                            }
                        }
                        if (levelLearntAt == 1) {
                            // override for piloswine
                            levelLearntAt = 45;
                        }
                        // change to pure level evo
                        evo.type = EvolutionType.LEVEL;
                        evo.extraInfo = levelLearntAt;
                        addEvoUpdateLevel(impossibleEvolutionUpdates, evo);
                    }
                    // Pure Trade
                    if (evo.type == EvolutionType.TRADE) {
                        // Replace w/ level 37
                        evo.type = EvolutionType.LEVEL;
                        evo.extraInfo = 37;
                        addEvoUpdateLevel(impossibleEvolutionUpdates, evo);
                    }
                    // Trade w/ Item
                    if (evo.type == EvolutionType.TRADE_ITEM) {
                        // Get the current item & evolution
                        int item = evo.extraInfo;
                        if (evo.from.number == Species.slowpoke) {
                            // Slowpoke is awkward - he already has a level evo
                            // So we can't do Level up w/ Held Item for him
                            // Put Water Stone instead
                            evo.type = EvolutionType.STONE;
                            evo.extraInfo = Items.waterStone;
                            addEvoUpdateStone(impossibleEvolutionUpdates, evo, itemNames.get(evo.extraInfo));
                        } else {
                            addEvoUpdateHeldItem(impossibleEvolutionUpdates, evo, itemNames.get(item));
                            // Replace, for this entry, w/
                            // Level up w/ Held Item at Day
                            evo.type = EvolutionType.LEVEL_ITEM_DAY;
                            // now add an extra evo for
                            // Level up w/ Held Item at Night
                            Evolution extraEntry = new Evolution(evo.from, evo.to, true,
                                    EvolutionType.LEVEL_ITEM_NIGHT, item);
                            extraEntry.forme = evo.forme;
                            extraEvolutions.add(extraEntry);
                        }
                    }
                    if (evo.type == EvolutionType.TRADE_SPECIAL) {
                        // This is the karrablast <-> shelmet trade
                        // Replace it with Level up w/ Other Species in Party
                        // (22)
                        // Based on what species we're currently dealing with
                        evo.type = EvolutionType.LEVEL_WITH_OTHER;
                        evo.extraInfo = (evo.from.number == Species.karrablast ? Species.shelmet : Species.karrablast);
                        addEvoUpdateParty(impossibleEvolutionUpdates, evo, pokes.get(evo.extraInfo).fullName());
                    }
                    // Finish one of the Towers of the Two Fists
                    if (evo.type == EvolutionType.TOWER) {
                        // This is impossible if you don't own the DLC, so change to level 40
                        evo.type = EvolutionType.LEVEL;
                        evo.extraInfo = 40;
                        addEvoUpdateLevel(impossibleEvolutionUpdates, evo);
                    }
                    // TBD: Pancham, Sliggoo? Sylveon?
                }

                pkmn.evolutionsFrom.addAll(extraEvolutions);
                for (Evolution ev : extraEvolutions) {
                    ev.to.evolutionsTo.add(ev);
                }
            }
        }
    }

    @Override
    public void makeEvolutionsEasier(Settings settings) {

    }

    @Override
    public void removeTimeBasedEvolutions() {

    }

    @Override
    public List<Integer> getFieldMoves() {
        // SwSh does not have field moves
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getEarlyRequiredHMMoves() {
        // SwSh does not have any HMs
        return new ArrayList<>();
    }

    @Override
    public int miscTweaksAvailable() {
        int available = 0;
        available |= MiscTweak.FASTEST_TEXT.getValue();
        available |= MiscTweak.RETAIN_ALT_FORMES.getValue();
        available |= MiscTweak.NO_FORCED_EXP_SHARE.getValue();
        return available;
    }

    @Override
    public void applyMiscTweak(MiscTweak tweak) {
        if (tweak == MiscTweak.FASTEST_TEXT) {
            applyFastestText();
        } else if (tweak == MiscTweak.RETAIN_ALT_FORMES) {
            patchFormeReversion();
        } else if (tweak == MiscTweak.NO_FORCED_EXP_SHARE) {
            disableForcedExpShare();
        }
    }

    private void applyFastestText() {
        int offset = find(main, SwShConstants.fastestTextPrefixes[0]);
        if (offset > 0) {
            offset += SwShConstants.fastestTextPrefixes[0].length() / 2; // because it was a prefix
            int patchedInstruction = createMovzInstruction(0, 3, false);
            FileFunctions.writeFullInt(main, offset, patchedInstruction);
        }
        offset = find(main, SwShConstants.fastestTextPrefixes[1]);
        if (offset > 0) {
            offset += SwShConstants.fastestTextPrefixes[1].length() / 2; // because it was a prefix
            int patchedInstruction = createMovzInstruction(0, 3, false);
            FileFunctions.writeFullInt(main, offset, patchedInstruction);
        }
    }

    private void patchFormeReversion() {
        // Upon loading a save, Darmanitan-Z, Darmanitan-GZ, Aegislash-B, Zygarde-C,
        // Wishiwashi-S, Eiscue-N, Zacian-C, Zamazenta-C, and Eternatus-E are all
        // reverted back to their base forme. This patches main such that this
        // reversion does not happen.
        int offset = find(main, SwShConstants.saveLoadFormeReversionPrefix);
        if (offset > 0) {
            offset += SwShConstants.saveLoadFormeReversionPrefix.length() / 2; // because it was a prefix

            // Stubs out the call to pml::pokepara::CoreParam::ChangeFormNo that reverts all these Pokemon back to base forme.
            FileFunctions.writeFullInt(main, offset, createNopInstruction());
        }

        // Additionally, upon completing a battle, Wishiwashi-S, Zacian-C, and
        // Zamazenta-C are forcibly returned to their base forme. This patches
        // main to prevent this from happening.
        offset = find(main, SwShConstants.afterBattleFormeReversionLocator);
        if (offset > 0) {
            // We can't just stub out the call to pml::pokepara::CoreParam::ChangeFormNo, since we *want* certain
            // post-battle forme reversions to happen (for example, we want Xerneas to exit its Active forme).
            // To get around this, we can deliberately break certain comparisons. The function checks to see if
            // the species ID is one of a few different values before reverting the forme; for our three desired
            // Pokemon, we can replace their comparisons with a nonsense species ID.
            FileFunctions.writeFullInt(main, offset, createCmpInstruction(20, 9999, false));
            FileFunctions.writeFullInt(main, offset + 32, createCmpInstruction(20, 9999, false));
            FileFunctions.writeFullInt(main, offset + 40, createCmpInstruction(20, 9999, false));
        }
    }

    private void disableForcedExpShare() {
        int offset = find(main, SwShConstants.expSharePrefix);
        if (offset > 0) {
            offset += SwShConstants.expSharePrefix.length() / 2; // because it was a prefix

            // There's a function in SwSh that controls whether EXP Share is enabled or disabled. However,
            // this is basically a leftover from the older games, and all it does is simply return 1. We
            // can disable this by simply returning 0 instead.
            int patchedInstruction = createMovzInstruction(0, 0, false);
            FileFunctions.writeFullInt(main, offset, patchedInstruction);
        }
    }

    @Override
    public void enableGuaranteedPokemonCatching() {
        int offset = find(main, SwShConstants.perfectOddsBranchLocator);
        if (offset > 0) {
            // The game checks to see if your odds are greater then or equal to 255 using the following
            // code. Note that they compare to 0xFF000 instead of 0xFF; it looks like all catching code
            // probabilities are shifted like this to avoid rounding errors.
            // cmp w20, #0xff, LSL #12
            // b.lt oddsLessThanOrEqualTo254
            // The below code just nops the branch out so it always acts like our odds are 255, and
            // Pokemon are automatically caught no matter what.
            FileFunctions.writeFullInt(main, offset, createNopInstruction());
        }
    }

    @Override
    public String getROMName() {
        return "Pokemon " + romEntry.name;
    }

    @Override
    public String getROMCode() {
        return null;
    }

    @Override
    public String getSupportLevel() {
        return "Partial";
    }

    @Override
    public String getDefaultExtension() {
        return null;
    }

    @Override
    public int internalStringLength(String string) {
        return 0;
    }

    @Override
    public void randomizeIntroPokemon() {

    }

    @Override
    public BufferedImage getMascotImage() {
        try {
            String randomIcon = SwShConstants.pokeIcons.get(this.random.nextInt(SwShConstants.pokeIcons.size()));
            byte[] iconData = readFile(String.format(romEntry.getString("PokeIconTemplate"), randomIcon));
            BNTX icon = new BNTX(iconData);
            return icon.getImage();
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public int generationOfPokemon() {
        return 8;
    }

    @Override
    public boolean isEffectivenessUpdated() {
        return false;
    }

    @Override
    public boolean hasFunctionalFormes() {
        return true;
    }

    @Override
    protected void loadedROM(String filename) {
        try {
            main = this.readMain();
            romEntry = entryFor(FileFunctions.getCRC32(main));

            loadPokemonStats();
            loadMoves();
            loadPlacement();

            pokemonListInclFormes = new ArrayList<>(pokes.values());
            pokemonListInclFormes.add(0,null);  // For compatibility with AbstractRomHandler; this will be removed later
            pokemonList = pokemonListInclFormes
                    .stream()
                    .filter(p -> (p == null || p.number <= SwShConstants.pokemonCount))
                    .collect(Collectors.toList());

            abilityNames = getStrings(romEntry.getString("AbilityNames"));
            itemNames = getStrings(romEntry.getString("ItemNames"));
            setupItemHashes();
            getFieldItems();
            populateItemTable();
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void loadPokemonStats() {
        try {
            byte[] personalInfo = this.readFile(romEntry.getString("PokemonStats"));
            String[] pokeNames = readPokemonNames();
            int pokemonCount = SwShConstants.pokemonCount;
            pokes = new TreeMap<>();
            Map<Integer,FormeInfo> formeMappings = new TreeMap<>();
            for (int i = 1; i <= pokemonCount; i++) {
                byte[] thisEntry = new byte[SwShConstants.bsSize];
                System.arraycopy(personalInfo, i * SwShConstants.bsSize, thisEntry, 0, SwShConstants.bsSize);
                if ((thisEntry[SwShConstants.bsPresentFlag] & SwShConstants.presentBitMask) == 0) {
                    continue;   // This Pokemon is not present in the game
                }
                Pokemon thisPokemon = new Pokemon();
                thisPokemon.number = i;
                loadBasicPokeStats(thisPokemon, thisEntry, formeMappings);
                thisPokemon.name = pokeNames[i];
                pokes.put(i,thisPokemon);
            }

            absolutePokeNumByBaseForme = new HashMap<>();
            dummyAbsolutePokeNums = new HashMap<>();
            dummyAbsolutePokeNums.put(255,0);

            int formNum = 1;
            int prevSpecies = 0;
            Map<Integer,Integer> currentMap = new HashMap<>();
            for (int k: formeMappings.keySet()) {
                Pokemon thisPokemon = new Pokemon();
                thisPokemon.number = k;
                byte[] thisEntry = new byte[SwShConstants.bsSize];
                System.arraycopy(personalInfo, k * SwShConstants.bsSize, thisEntry, 0, SwShConstants.bsSize);
                if ((thisEntry[SwShConstants.bsPresentFlag] & SwShConstants.presentBitMask) == 0) {
                    continue;   // This Pokemon is not present in the game
                }
                loadBasicPokeStats(thisPokemon, thisEntry, formeMappings);
                FormeInfo fi = formeMappings.get(k);
                int realBaseForme = pokes.get(fi.baseForme).baseForme == null ? fi.baseForme : pokes.get(fi.baseForme).baseForme.number;
                thisPokemon.name = pokeNames[realBaseForme];
                thisPokemon.baseForme = pokes.get(fi.baseForme);
                thisPokemon.formeNumber = fi.formeNumber;
                if (thisPokemon.actuallyCosmetic) {
                    thisPokemon.formeSuffix = thisPokemon.baseForme.formeSuffix;
                } else {
                    thisPokemon.formeSuffix = SwShConstants.getFormeSuffixByBaseForme(fi.baseForme,fi.formeNumber);
                }
                if (realBaseForme == prevSpecies) {
                    if (SwShConstants.altFormesWithCosmeticForms.containsKey(thisPokemon.baseForme.number)) {
                        formNum++;      // Special case for Power Construct Zygarde-10
                    } else {
                        formNum = fi.formeNumber;
                    }
                    currentMap.put(formNum,k);
                } else {
                    if (prevSpecies != 0) {
                        absolutePokeNumByBaseForme.put(prevSpecies,currentMap);
                    }
                    prevSpecies = realBaseForme;
                    formNum = fi.formeNumber;
                    currentMap = new HashMap<>();
                    currentMap.put(formNum,k);
                }
                pokes.put(k,thisPokemon);
            }
            if (prevSpecies != 0) {
                absolutePokeNumByBaseForme.put(prevSpecies,currentMap);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        populateEvolutions();
    }

    private void loadBasicPokeStats(Pokemon pkmn, byte[] stats, Map<Integer,FormeInfo> altFormes) {
        pkmn.hp = stats[SwShConstants.bsHPOffset] & 0xFF;
        pkmn.attack = stats[SwShConstants.bsAttackOffset] & 0xFF;
        pkmn.defense = stats[SwShConstants.bsDefenseOffset] & 0xFF;
        pkmn.speed = stats[SwShConstants.bsSpeedOffset] & 0xFF;
        pkmn.spatk = stats[SwShConstants.bsSpAtkOffset] & 0xFF;
        pkmn.spdef = stats[SwShConstants.bsSpDefOffset] & 0xFF;
        // Type
        pkmn.primaryType = SwShConstants.typeTable[stats[SwShConstants.bsPrimaryTypeOffset] & 0xFF];
        pkmn.secondaryType = SwShConstants.typeTable[stats[SwShConstants.bsSecondaryTypeOffset] & 0xFF];
        // Only one type?
        if (pkmn.secondaryType == pkmn.primaryType) {
            pkmn.secondaryType = null;
        }
        pkmn.catchRate = stats[SwShConstants.bsCatchRateOffset] & 0xFF;
        pkmn.growthCurve = ExpCurve.fromByte(stats[SwShConstants.bsGrowthCurveOffset]);

        pkmn.ability1 = FileFunctions.read2ByteInt(stats,SwShConstants.bsAbility1Offset);
        pkmn.ability2 = FileFunctions.read2ByteInt(stats,SwShConstants.bsAbility2Offset);
        pkmn.ability3 = FileFunctions.read2ByteInt(stats,SwShConstants.bsAbility3Offset);
        if (pkmn.ability1 == pkmn.ability2) {
            pkmn.ability2 = 0;
        }

        // Held Items?
        int item1 = FileFunctions.read2ByteInt(stats, SwShConstants.bsCommonHeldItemOffset);
        int item2 = FileFunctions.read2ByteInt(stats, SwShConstants.bsRareHeldItemOffset);

        if (item1 == item2) {
            // guaranteed
            pkmn.guaranteedHeldItem = item1;
            pkmn.commonHeldItem = 0;
            pkmn.rareHeldItem = 0;
            pkmn.darkGrassHeldItem = -1;
        } else {
            pkmn.guaranteedHeldItem = 0;
            pkmn.commonHeldItem = item1;
            pkmn.rareHeldItem = item2;
            pkmn.darkGrassHeldItem = -1;
        }

        int formeCount = stats[SwShConstants.bsFormeCountOffset] & 0xFF;
        if (formeCount > 1) {
            if (!altFormes.keySet().contains(pkmn.number)) {
                int firstFormeOffset = FileFunctions.read2ByteInt(stats, SwShConstants.bsFormeOffset);
                if (firstFormeOffset != 0) {
                    int j = 0;
                    int jMax = 0;
                    int theAltForme = 0;
                    Set<Integer> altFormesWithCosmeticForms = SwShConstants.altFormesWithCosmeticForms.keySet();
                    for (int i = 1; i < formeCount; i++) {
                        if (j == 0 || j > jMax) {
                            altFormes.put(firstFormeOffset + i - 1,new FormeInfo(pkmn.number,i,FileFunctions.read2ByteInt(stats,SwShConstants.bsFormeSpriteOffset))); // Assumes that formes are in memory in the same order as their numbers
                            if (SwShConstants.actuallyCosmeticForms.contains(firstFormeOffset+i-1)) {
                                if (!SwShConstants.ignoreForms.contains(firstFormeOffset+i-1)) { // Skip ignored forms (identical or confusing cosmetic forms)
                                    pkmn.cosmeticForms += 1;
                                    pkmn.realCosmeticFormNumbers.add(i);
                                }
                            }
                        } else {
                            altFormes.put(firstFormeOffset + i - 1,new FormeInfo(theAltForme,j,FileFunctions.read2ByteInt(stats,SwShConstants.bsFormeSpriteOffset)));
                            j++;
                        }
                        if (altFormesWithCosmeticForms.contains(firstFormeOffset + i - 1)) {
                            j = 1;
                            jMax = SwShConstants.altFormesWithCosmeticForms.get(firstFormeOffset + i - 1);
                            theAltForme = firstFormeOffset + i - 1;
                        }
                    }
                }
            } else {
                if (!SwShConstants.ignoreForms.contains(pkmn.number)) {
                    pkmn.cosmeticForms = SwShConstants.altFormesWithCosmeticForms.getOrDefault(pkmn.number,0);
                }
                if (SwShConstants.actuallyCosmeticForms.contains(pkmn.number)) {
                    pkmn.actuallyCosmetic = true;
                }
            }
        }
    }

    private String[] readPokemonNames() {
        int pokemonCount = SwShConstants.pokemonCount;
        String[] pokeNames = new String[pokemonCount + 1];
        try {
            List<String> nameList = getStrings(romEntry.getString("PokemonNames"));
            for (int i = 1; i <= pokemonCount; i++) {
                pokeNames[i] = nameList.get(i);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return pokeNames;
    }

    private void populateEvolutions() {
        for (Pokemon pkmn : pokes.values()) {
            if (pkmn != null) {
                pkmn.evolutionsFrom.clear();
                pkmn.evolutionsTo.clear();
            }
        }

        // Read files
        try {
            for (Map.Entry<Integer,Pokemon> pokeEntry: pokes.entrySet()) {
                Pokemon pk = pokeEntry.getValue();
                byte[] evoEntry = readFile(String.format(romEntry.getString("EvolutionEntryTemplate"), pokeEntry.getKey()));
                boolean skipNext = false;
                for (int evo = 0; evo < 9; evo++) {
                    int method = evoEntry[evo * 8];
                    int species = FileFunctions.read2ByteInt(evoEntry, evo * 8 + 4);
                    if (method >= 1 && method <= SwShConstants.evolutionMethodCount && species >= 1) {
                        EvolutionType et = EvolutionType.fromIndex(8, method);
                        if (et.skipSplitEvo()) continue; // Remove Feebas "split" evolution
                        if (skipNext) {
                            skipNext = false;
                            continue;
                        }
                        if (et == EvolutionType.LEVEL_GAME) {
                            skipNext = true;
                        }

                        int extraInfo = FileFunctions.read2ByteInt(evoEntry, evo * 8 + 2);
                        int forme = evoEntry[evo * 8 + 6];
                        int level = evoEntry[evo * 8 + 7];
                        Evolution evol = new Evolution(pk, getPokemonForEncounter(species,forme), true, et, extraInfo);
                        evol.forme = forme;
                        evol.level = level;
                        if (et.usesLevel()) {
                            evol.extraInfo = level;
                        }
                        switch (et) {
                            case LEVEL_GAME:
                                evol.type = EvolutionType.LEVEL;
                                evol.to = pokes.get(romEntry.getInt("CosmoemEvolutionNumber"));
                                break;
                            case LEVEL_DAY_GAME:
                                evol.type = EvolutionType.LEVEL_DAY;
                                break;
                            case LEVEL_NIGHT_GAME:
                                evol.type = EvolutionType.LEVEL_NIGHT;
                                break;
                            default:
                                break;
                        }
                        if (pk.baseForme != null && pk.baseForme.number == Species.rockruff && pk.formeNumber > 0) {
                            evol.from = pk.baseForme;
                            pk.baseForme.evolutionsFrom.add(evol);
                            pokes.get(absolutePokeNumByBaseForme.get(species).get(evol.forme)).evolutionsTo.add(evol);
                        }
                        if (!pk.evolutionsFrom.contains(evol)) {
                            pk.evolutionsFrom.add(evol);
                            if (!pk.actuallyCosmetic) {
                                if (evol.forme > 0) {
                                    // The forme number for the evolution might represent an actual alt forme, or it
                                    // might simply represent a cosmetic forme. If it represents an actual alt forme,
                                    // we'll need to figure out what the absolute species ID for that alt forme is
                                    // and update its evolutions. If it instead represents a cosmetic forme, then the
                                    // absolutePokeNumByBaseFormeMap will be null, since there's no secondary species
                                    // entry for this forme.
                                    Map<Integer, Integer> absolutePokeNumByBaseFormeMap = absolutePokeNumByBaseForme.get(species);
                                    if (absolutePokeNumByBaseFormeMap != null) {
                                        species = absolutePokeNumByBaseFormeMap.get(evol.forme);
                                    }
                                }
                                pokes.get(species).evolutionsTo.add(evol);
                            }
                        }
                    }
                }

                // Nincada's Shedinja evo is hardcoded into the game's executable,
                // so if the Pokemon is Nincada, then let's put it as one of its evolutions
                if (pk.number == Species.nincada) {
                    Pokemon shedinja = pokes.get(Species.shedinja);
                    Evolution evol = new Evolution(pk, shedinja, false, EvolutionType.LEVEL_IS_EXTRA, 20);
                    evol.forme = -1;
                    evol.level = 20;
                    pk.evolutionsFrom.add(evol);
                    shedinja.evolutionsTo.add(evol);
                }

                // Split evos shouldn't carry stats unless the evo is Nincada's
                // In that case, we should have Ninjask carry stats
                if (pk.evolutionsFrom.size() > 1) {
                    for (Evolution e : pk.evolutionsFrom) {
                        if (e.type != EvolutionType.LEVEL_CREATE_EXTRA) {
                            e.carryStats = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void loadMoves() {
        try {
            moves = new Move[SwShConstants.moveCount + 1];
            List<String> moveNames = getStrings(romEntry.getString("MoveNames"));
            for (int i = 1; i <= SwShConstants.moveCount; i++) {
                byte[] moveData = readFile(String.format(romEntry.getString("MoveDataTemplate"),i));
                SwShMove move = SwShMove.getRootAsSwShMove(ByteBuffer.wrap(moveData));
                moves[i] = new Move();
                moves[i].name = moveNames.get(i);
                moves[i].number = i;
                moves[i].internalId = i;
                moves[i].effectIndex = move.effectSequence();
                moves[i].hitratio = move.accuracy();
                moves[i].power = move.power();
                moves[i].pp = move.pp();
                moves[i].type = SwShConstants.typeTable[move.type() & 0xFF];
                moves[i].flinchPercentChance = move.flinch();
                moves[i].target = move.rawTarget();
                moves[i].category = SwShConstants.moveCategoryIndices[move.category() & 0xFF];
                moves[i].priority = move.priority();

                int critStages = move.critStage() & 0xFF;
                if (critStages == 6) {
                    moves[i].criticalChance = CriticalChance.GUARANTEED;
                } else if (critStages > 0) {
                    moves[i].criticalChance = CriticalChance.INCREASED;
                }

                int internalStatusType = move.inflict();
                moves[i].makesContact = move.flagMakesContact();
                moves[i].isChargeMove = move.flagCharge();
                moves[i].isRechargeMove = move.flagRecharge();
                moves[i].isPunchMove = move.flagPunch();
                moves[i].isSoundMove = move.flagSound();
                moves[i].isTrapMove = internalStatusType == 8;
                switch (moves[i].effectIndex) {
                    case SwShConstants.noDamageTargetTrappingEffect:
                    case SwShConstants.noDamageFieldTrappingEffect:
                    case SwShConstants.damageAdjacentFoesTrappingEffect:
                    case SwShConstants.damageTargetTrappingEffect:
                        moves[i].isTrapMove = true;
                        break;
                }

                int qualities = move.quality();
                int recoilOrAbsorbPercent = move.recoil();
                if (qualities == SwShConstants.damageAbsorbQuality) {
                    moves[i].absorbPercent = recoilOrAbsorbPercent;
                } else {
                    moves[i].recoilPercent = -recoilOrAbsorbPercent;
                }

                if (i == Moves.swift) {
                    perfectAccuracy = (int)moves[i].hitratio;
                }

                if (GlobalConstants.normalMultihitMoves.contains(i)) {
                    moves[i].hitCount = 19 / 6.0;
                } else if (GlobalConstants.doubleHitMoves.contains(i)) {
                    moves[i].hitCount = 2;
                } else if (GlobalConstants.tripleHitMoves.contains(i)) {
                    moves[i].hitCount = 3;
                } else if (i == Moves.tripleKick || i == Moves.tripleAxel) {
                    moves[i].hitCount = 2.71; // this assumes the first hit lands
                }

                switch (qualities) {
                    case SwShConstants.noDamageStatChangeQuality:
                    case SwShConstants.noDamageStatusAndStatChangeQuality:
                        // All Allies or Self
                        if (moves[i].target == 6 || moves[i].target == 7) {
                            moves[i].statChangeMoveType = StatChangeMoveType.NO_DAMAGE_USER;
                        } else if (moves[i].target == 2) {
                            moves[i].statChangeMoveType = StatChangeMoveType.NO_DAMAGE_ALLY;
                        } else if (moves[i].target == 8) {
                            moves[i].statChangeMoveType = StatChangeMoveType.NO_DAMAGE_ALL;
                        } else {
                            moves[i].statChangeMoveType = StatChangeMoveType.NO_DAMAGE_TARGET;
                        }
                        break;
                    case SwShConstants.damageTargetDebuffQuality:
                        moves[i].statChangeMoveType = StatChangeMoveType.DAMAGE_TARGET;
                        break;
                    case SwShConstants.damageUserBuffQuality:
                        moves[i].statChangeMoveType = StatChangeMoveType.DAMAGE_USER;
                        break;
                    default:
                        moves[i].statChangeMoveType = StatChangeMoveType.NONE_OR_UNKNOWN;
                        break;
                }

                moves[i].statChanges[0].type = StatChangeType.values()[move.stat1()];
                moves[i].statChanges[0].stages = move.stat1Stage();
                moves[i].statChanges[0].percentChance = move.stat1Percent();
                moves[i].statChanges[1].type = StatChangeType.values()[move.stat2()];
                moves[i].statChanges[1].stages = move.stat2Stage();
                moves[i].statChanges[1].percentChance = move.stat2Percent();
                moves[i].statChanges[2].type = StatChangeType.values()[move.stat3()];
                moves[i].statChanges[2].stages = move.stat3Stage();
                moves[i].statChanges[2].percentChance = move.stat3Percent();

                // Exclude status types that aren't in the StatusType enum.
                if (internalStatusType < 7) {
                    moves[i].statusType = StatusType.values()[internalStatusType];
                    if (moves[i].statusType == StatusType.POISON && (i == Moves.toxic || i == Moves.poisonFang)) {
                        moves[i].statusType = StatusType.TOXIC_POISON;
                    }
                    moves[i].statusPercentChance = move.inflictPercent();
                    switch (qualities) {
                        case SwShConstants.noDamageStatusQuality:
                        case SwShConstants.noDamageStatusAndStatChangeQuality:
                            moves[i].statusMoveType = StatusMoveType.NO_DAMAGE;
                            break;
                        case SwShConstants.damageStatusQuality:
                            moves[i].statusMoveType = StatusMoveType.DAMAGE;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void setupItemHashes() {
        try {
            byte[] itemHashToIndexData = readFile(romEntry.getString("ItemHashToIndex"));
            itemHashToIndex = new HashMap<>();
            itemIndexToHash = new ArrayList<>();
            int itemCount = FileFunctions.read2ByteInt(itemHashToIndexData, 0);
            for (int i = 0; i < itemCount; i++) {
                long hash = FileFunctions.readFullLong(itemHashToIndexData, i * 0x10 + 4);
                int index = FileFunctions.read2ByteInt(itemHashToIndexData, i * 0x10 + 0xC);
                if (index == 0) {
                    System.out.println(i);
                }
                itemHashToIndex.put(hash,index);
                itemIndexToHash.add(hash);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void loadPlacement() {
        try {
            byte[] placementData = readFile(romEntry.getString("Placement"));
            placementPack = new GFPack(placementData);
            placementAreaTable = new AHTB(placementPack.getDataFileName("AreaNameHashTable.tbl"));
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void populateItemTable() {
        try {
            byte[] itemData = readFile(romEntry.getString("ItemData"));
            itemTable = new HashMap<>();
            int numEntries = FileFunctions.read2ByteInt(itemData, 0);
            int maxEntryIndex = FileFunctions.read2ByteInt(itemData, 4);
            int entriesStart = FileFunctions.read2ByteInt(itemData, 0x40);
            for (int i = 0; i < numEntries; i++) {
                int entryIndex = FileFunctions.read2ByteInt(itemData, 0x44 + (2 * i));
                if (entryIndex >= maxEntryIndex) {
                    throw new RandomizerIOException("Invalid item data file.");
                }
                SwShItem item = new SwShItem(itemData, entriesStart + (entryIndex * SwShConstants.itemPropertiesEntryLength));
                itemTable.put(i, item);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    protected void savingROM() throws IOException {
        savePokemonStats();
        saveMoves();
        savePlacement();
        saveItemTable();
        writeMain(main);
    }

    private void savePokemonStats() {
        try {
            byte[] personalInfo = this.readFile(romEntry.getString("PokemonStats"));
            for (Map.Entry<Integer,Pokemon> pokeEntry: pokes.entrySet()) {
                byte[] pokeData = new byte[SwShConstants.bsSize];
                int i = pokeEntry.getKey();
                System.arraycopy(personalInfo, i * SwShConstants.bsSize, pokeData, 0, SwShConstants.bsSize);
                saveBasicPokeStats(pokeEntry.getValue(), pokeData);
                System.arraycopy(pokeData, 0, personalInfo, i * SwShConstants.bsSize, SwShConstants.bsSize);
            }
            writeFile(romEntry.getString("PokemonStats"), personalInfo);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }

        writeEvolutions();
    }

    private void saveBasicPokeStats(Pokemon pkmn, byte[] stats) {
        stats[SwShConstants.bsHPOffset] = (byte) pkmn.hp;
        stats[SwShConstants.bsAttackOffset] = (byte) pkmn.attack;
        stats[SwShConstants.bsDefenseOffset] = (byte) pkmn.defense;
        stats[SwShConstants.bsSpeedOffset] = (byte) pkmn.speed;
        stats[SwShConstants.bsSpAtkOffset] = (byte) pkmn.spatk;
        stats[SwShConstants.bsSpDefOffset] = (byte) pkmn.spdef;
        stats[SwShConstants.bsPrimaryTypeOffset] = SwShConstants.typeToByte(pkmn.primaryType);
        if (pkmn.secondaryType == null) {
            stats[SwShConstants.bsSecondaryTypeOffset] = stats[SwShConstants.bsPrimaryTypeOffset];
        } else {
            stats[SwShConstants.bsSecondaryTypeOffset] = SwShConstants.typeToByte(pkmn.secondaryType);
        }
        stats[SwShConstants.bsCatchRateOffset] = (byte) pkmn.catchRate;
        stats[SwShConstants.bsGrowthCurveOffset] = pkmn.growthCurve.toByte();

        stats[SwShConstants.bsAbility1Offset] = (byte) pkmn.ability1;
        stats[SwShConstants.bsAbility2Offset] = pkmn.ability2 != 0 ? (byte) pkmn.ability2 : (byte) pkmn.ability1;
        stats[SwShConstants.bsAbility3Offset] = (byte) pkmn.ability3;

        // Held items
        if (pkmn.guaranteedHeldItem > 0) {
            FileFunctions.write2ByteInt(stats, SwShConstants.bsCommonHeldItemOffset, pkmn.guaranteedHeldItem);
            FileFunctions.write2ByteInt(stats, SwShConstants.bsRareHeldItemOffset, pkmn.guaranteedHeldItem);
            FileFunctions.write2ByteInt(stats, SwShConstants.bsVeryRareHeldItemOffset, 0);
        } else {
            FileFunctions.write2ByteInt(stats, SwShConstants.bsCommonHeldItemOffset, pkmn.commonHeldItem);
            FileFunctions.write2ByteInt(stats, SwShConstants.bsRareHeldItemOffset, pkmn.rareHeldItem);
            FileFunctions.write2ByteInt(stats, SwShConstants.bsVeryRareHeldItemOffset, 0);
        }

        if (pkmn.fullName().equals("Meowstic")) {
            stats[SwShConstants.bsGenderOffset] = 0;
        } else if (pkmn.fullName().equals("Meowstic-F")) {
            stats[SwShConstants.bsGenderOffset] = (byte)0xFE;
        }
    }

    private void writeEvolutions() {
        try {
            for (Map.Entry<Integer,Pokemon> pokeEntry: pokes.entrySet()) {
                Pokemon pk = pokeEntry.getValue();
                byte[] evoEntry = readFile(String.format(romEntry.getString("EvolutionEntryTemplate"),pokeEntry.getKey()));
                if (pk.number == Species.nincada) {
                    writeShedinjaEvolution();
                }
                int evosWritten = 0;
                for (Evolution evo : pk.evolutionsFrom) {
                    Pokemon toPK = evo.to;
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8, evo.type.toIndex(8));
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 2, evo.type.usesLevel() ? 0 : evo.extraInfo);
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 4, toPK.getBaseNumber());
                    evoEntry[evosWritten * 8 + 6] = (byte)evo.forme;
                    evoEntry[evosWritten * 8 + 7] = evo.type.usesLevel() ? (byte)evo.extraInfo : (byte)evo.level;
                    evosWritten++;
                    // Milcery needs 9 identical evolutions
                    if (pk.number == Species.milcery) {
                        while (evosWritten < 9) {
                            FileFunctions.write2ByteInt(evoEntry, evosWritten * 8, evo.type.toIndex(8));
                            FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 2, evo.type.usesLevel() ? 0 : evo.extraInfo);
                            FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 4, toPK.getBaseNumber());
                            evoEntry[evosWritten * 8 + 6] = (byte)evo.forme;
                            evoEntry[evosWritten * 8 + 7] = evo.type.usesLevel() ? (byte)evo.extraInfo : (byte)evo.level;
                            evosWritten++;
                        }
                    }
                    if (evosWritten == 9) {
                        break;
                    }
                }
                while (evosWritten < 9) {
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8, 0);
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 2, 0);
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 4, 0);
                    FileFunctions.write2ByteInt(evoEntry, evosWritten * 8 + 6, 0);
                    evosWritten++;
                }
                writeFile(String.format(romEntry.getString("EvolutionEntryTemplate"),pokeEntry.getKey()),evoEntry);
            }

        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void writeShedinjaEvolution() {
        Pokemon nincada = pokes.get(Species.nincada);

        // When the "Limit Pokemon" setting is enabled and Gen 3 is disabled, or when
        // "Random Every Level" evolutions are selected, we end up clearing out Nincada's
        // vanilla evolutions. In that case, there's no point in even worrying about
        // Shedinja, so just return.
        if (nincada.evolutionsFrom.size() < 2) {
            return;
        }
        Pokemon primaryEvolution = nincada.evolutionsFrom.get(0).to;
        Pokemon extraEvolution = nincada.evolutionsFrom.get(1).to;

        // In the game's executable, there's a hardcoded check to see if the Pokemon
        // that just evolved is now a Ninjask after evolving; if it is, then we start
        // going down the path of creating a Shedinja. To accomplish this check, they
        // hardcoded Ninjask's species ID as a constant. We replace this constant
        // with the species ID of Nincada's new primary evolution; that way, evolving
        // Nincada will still produce an "extra" Pokemon like in older generations.
        int offset = find(main, SwShConstants.ninjaskSpeciesLocator);
        if (offset > 0) {
            int patchedInstruction = createCmpInstruction(0, primaryEvolution.getBaseNumber(), false);
            FileFunctions.writeFullInt(main, offset, patchedInstruction);
        }

        // In the game's executable, there's a hardcoded value to indicate what "extra"
        // Pokemon to create. It produces a Shedinja using the following instructions:
        // movz w1, #0x124
        // mov x0, x19
        // mov w2, wzr
        // The first instruction loads 0x124 (292 in decimal, which is Shedinja's species ID)
        // to act as the species ID, and the third instruction loads 0 to act as the forme ID.
        offset = find(main, SwShConstants.shedinjaLocator);
        if (offset > 0) {
            int patchedSpeciesInstruction = createMovzInstruction(1, extraEvolution.getBaseNumber(), false);
            FileFunctions.writeFullInt(main, offset, patchedSpeciesInstruction);
            if (extraEvolution.formeNumber != 0) {
                int patchedFormeInstruction = createMovzInstruction(2, extraEvolution.formeNumber, false);
                FileFunctions.writeFullInt(main, offset + 8, patchedFormeInstruction);
            }
        }

        // Now that we've handled the hardcoded Shedinja evolution, delete it so that
        // we do *not* handle it in WriteEvolutions
        nincada.evolutionsFrom.remove(1);
        extraEvolution.evolutionsTo.remove(0);
    }

    private void saveMoves() {
        try {
            int moveCount = SwShConstants.moveCount;
            for (int i = 1; i <= moveCount; i++) {
                byte[] moveData = readFile(String.format(romEntry.getString("MoveDataTemplate"),i));
                SwShMove move = SwShMove.getRootAsSwShMove(ByteBuffer.wrap(moveData));
                move.mutateCategory(SwShConstants.moveCategoryToByte(moves[i].category));
                move.mutatePower(moves[i].power);
                move.mutateType(SwShConstants.typeToByte(moves[i].type));
                int hitratio = (int) Math.round(moves[i].hitratio);
                if (hitratio < 0) {
                    hitratio = 0;
                }
                if (hitratio > 101) {
                    hitratio = 100;
                }
                move.mutateAccuracy(hitratio);
                move.mutatePp(moves[i].pp);
                writeFile(String.format(romEntry.getString("MoveDataTemplate"),i),move.getByteBuffer().array());
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void savePlacement() {
        try {
            writeFile(romEntry.getString("Placement"), placementPack.writePack());
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private void saveItemTable() {
        try {
            byte[] itemData = readFile(romEntry.getString("ItemData"));
            int numEntries = FileFunctions.read2ByteInt(itemData, 0);
            int maxEntryIndex = FileFunctions.read2ByteInt(itemData, 4);
            int entriesStart = FileFunctions.read2ByteInt(itemData, 0x40);
            for (int i = 0; i < numEntries; i++) {
                int entryIndex = FileFunctions.read2ByteInt(itemData, 0x44 + (2 * i));
                if (entryIndex >= maxEntryIndex) {
                    throw new RandomizerIOException("Invalid item data file.");
                }
                SwShItem item = itemTable.get(i);
                int startingOffset = entriesStart + (entryIndex * SwShConstants.itemPropertiesEntryLength);
                item.outputToItemData(itemData, startingOffset);
            }
            writeFile(romEntry.getString("ItemData"), itemData);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    private List<String> getStrings(String fileName) throws IOException {
        byte[] rawFile = this.readFile(fileName);
        // TODO handle romType better
        return new ArrayList<>(N3DSTxtHandler.readTexts(rawFile,true,100));
    }

    private void setStrings(String fileName, List<String> strings) {
        try {
            byte[] oldRawFile = this.readFile(fileName);
            // TODO handle romType better
            byte[] newRawFile = N3DSTxtHandler.saveEntry(oldRawFile, strings, 100);
            writeFile(fileName, newRawFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getGameAcronym() {
        return null;
    }

    @Override
    protected String getContentId() {
        return romEntry.contentId;
    }

    @Override
    protected boolean isGameUpdateSupported(int version) {
        return false;
    }

    @Override
    protected String getGameVersion() {
        return null;
    }

    private int typeTMSpriteNumber(Type t) {
        if (t == null) {
            return 357; // CURSE
        }
        switch (t) {
            case DARK:
                return 328;
            case DRAGON:
                return 329;
            case PSYCHIC:
                return 330;
            case NORMAL:
                return 332;
            case POISON:
                return 333;
            case ICE:
                return 334;
            case FIGHTING:
                return 335;
            case FIRE:
                return 338;
            case WATER:
                return 345;
            case FLYING:
                return 421;
            case GRASS:
                return 349;
            case ROCK:
                return 350;
            case ELECTRIC:
                return 351;
            case GROUND:
                return 353;
            case GHOST:
            default:
                return 357; // for CURSE
            case STEEL:
                return 401;
            case BUG:
                return 403;
            case FAIRY:
                return 693;
        }
    }

    private int typeTRSpriteNumber(Type t) {
        if (t == null) {
            return 1163; // CURSE
        }
        switch (t) {
            case DARK:
                return 1162;
            case DRAGON:
                return 1154;
            case PSYCHIC:
                return 1141;
            case NORMAL:
                return 1130;
            case POISON:
                return 1152;
            case ICE:
                return 1135;
            case FIGHTING:
                return 1137;
            case FIRE:
                return 1132;
            case WATER:
                return 1133;
            case FLYING:
                return 1196;
            case GRASS:
                return 1180;
            case ROCK:
                return 1193;
            case ELECTRIC:
                return 1138;
            case GROUND:
                return 1140;
            case GHOST:
            default:
                return 1163; // for CURSE
            case STEEL:
                return 1161;
            case BUG:
                return 1148;
            case FAIRY:
                return 1220;
        }
    }

    private int find(byte[] data, String hexString) {
        if (hexString.length() % 2 != 0) {
            return -3; // error
        }
        byte[] searchFor = new byte[hexString.length() / 2];
        for (int i = 0; i < searchFor.length; i++) {
            searchFor[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        List<Integer> found = RomFunctions.search(data, searchFor);
        if (found.size() == 0) {
            return -1; // not found
        } else if (found.size() > 1) {
            return -2; // not unique
        } else {
            return found.get(0);
        }
    }
}
