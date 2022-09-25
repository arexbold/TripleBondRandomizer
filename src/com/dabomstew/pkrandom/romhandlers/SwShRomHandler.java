package com.dabomstew.pkrandom.romhandlers;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.constants.Gen7Constants;
import com.dabomstew.pkrandom.constants.Species;
import com.dabomstew.pkrandom.constants.SwShConstants;
import com.dabomstew.pkrandom.exceptions.RandomizerIOException;
import com.dabomstew.pkrandom.generated.swsh.*;
import com.dabomstew.pkrandom.hac.GFPack;
import com.dabomstew.pkrandom.hac.SwitchFileReader;
import com.dabomstew.pkrandom.pokemon.*;
import jdk.internal.org.objectweb.asm.signature.SignatureWriter;
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

    // This ROM
    private RomEntry romEntry;
    private Map<Integer,Pokemon> pokes;
    private Map<Integer,Map<Integer,Integer>> absolutePokeNumByBaseForme;
    private Map<Integer,Integer> dummyAbsolutePokeNums;
    private List<Pokemon> pokemonList;
    private List<Pokemon> pokemonListInclFormes;
    private List<String> abilityNames, itemNames;

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
        return null;
    }

    @Override
    public boolean setStarters(List<Pokemon> newStarters) {
        return false;
    }

    @Override
    public boolean hasStarterAltFormes() {
        return false;
    }

    @Override
    public int starterCount() {
        return 0;
    }

    @Override
    public List<Integer> getStarterHeldItems() {
        return null;
    }

    @Override
    public void setStarterHeldItems(List<Integer> items) {

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
            byte[] wildData = this.readFile(romEntry.getString("WildPokemonPack"));
            GFPack wildPack = new GFPack(wildData);

            byte[] shieldData = wildPack.getDataFileName(romEntry.getString("WildPokemonFile"));

            SwShWildEncounterArchive wildArchive = SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(shieldData));
            for (int i = 0; i < wildArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable table = wildArchive.encounterTables(i);
                String tableName = SwShConstants.getZoneName(table.zoneId());
                if (table.subTablesLength() > 0) {
                    int j = 0;
                    if (isAllWeatherTablesIdentical(table, NUM_WEATHER_TABLES)) {
                        SwShWildEncounterSubTable subTable = table.subTables(0);
                        EncounterSet es = new EncounterSet();
                        es.displayName = tableName + " (All Weather)";
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
                        encounters.add(es);
                        j = NUM_WEATHER_TABLES;
                    }
                    while (j < table.subTablesLength()) {
                        SwShWildEncounterSubTable subTable = table.subTables(j);
                        EncounterSet es = new EncounterSet();
                        es.displayName = tableName + " (" + SwShConstants.encounterTypes.get(j) + ")";
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
                        encounters.add(es);
                        j++;
                    }
                }
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }

        return encounters;
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
        Iterator<EncounterSet> encounters = encountersList.iterator();

        try  {
            final int NUM_WEATHER_TABLES = 9;
            byte[] wildData = this.readFile(romEntry.getString("WildPokemonPack"));
            GFPack wildPack = new GFPack(wildData);

            byte[] shieldData = wildPack.getDataFileName(romEntry.getString("WildPokemonFile"));

            SwShWildEncounterArchive wildArchive = SwShWildEncounterArchive.getRootAsSwShWildEncounterArchive(ByteBuffer.wrap(shieldData));
            for (int i = 0; i < wildArchive.encounterTablesLength(); i++) {
                SwShWildEncounterTable table = wildArchive.encounterTables(i);
                if (table.subTablesLength() > 0) {
                    boolean distinctWeatherTables = true;
                    if (isAllWeatherTablesIdentical(table, NUM_WEATHER_TABLES)) {
                        distinctWeatherTables = false;
                    }
                    EncounterSet es = encounters.next();
                    for (int j = 0; j < table.subTablesLength(); j++) {

                        if (j >= NUM_WEATHER_TABLES || (j > 0 && distinctWeatherTables)) {
                            es = encounters.next();
                        }
                        SwShWildEncounterSubTable subTable = table.subTables(j);
                        for (int k = 0; k < subTable.slotsLength(); k++) {
                            SwShWildEncounterSlot slot = subTable.slots(k);
                            if (slot.species() == 0) {
                                continue;
                            }
                            Encounter e = es.encounters.get(k);
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
            byte[] newShieldData = wildArchive.getByteBuffer().array();
            wildPack.setDataFileName("encount_t.bin",newShieldData);
            byte[] newWildData = wildPack.writePack();
            writeFile("bin/archive/field/resident/data_table.gfpak",newWildData);
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    public boolean hasWildAltFormes() {
        return false;
    }

    @Override
    public List<Trainer> getTrainers() {
        return new ArrayList<>();
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

    }

    @Override
    public List<Move> getMoves() {
        return null;
    }

    @Override
    public Map<Integer, List<MoveLearnt>> getMovesLearnt() {
        return null;
    }

    @Override
    public void setMovesLearnt(Map<Integer, List<MoveLearnt>> movesets) {

    }

    @Override
    public Map<Integer, List<Integer>> getEggMoves() {
        return null;
    }

    @Override
    public void setEggMoves(Map<Integer, List<Integer>> eggMoves) {

    }

    @Override
    public boolean supportsFourStartingMoves() {
        return false;
    }

    @Override
    public List<StaticEncounter> getStaticPokemon() {
        return null;
    }

    @Override
    public boolean setStaticPokemon(List<StaticEncounter> staticPokemon) {
        return false;
    }

    @Override
    public boolean canChangeStaticPokemon() {
        return false;
    }

    @Override
    public boolean hasStaticAltFormes() {
        return false;
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

    @Override
    public List<Integer> getTMMoves() {
        return null;
    }

    @Override
    public List<Integer> getHMMoves() {
        return null;
    }

    @Override
    public void setTMMoves(List<Integer> moveIndexes) {

    }

    @Override
    public int getTMCount() {
        return 0;
    }

    @Override
    public int getHMCount() {
        return 0;
    }

    @Override
    public Map<Pokemon, boolean[]> getTMHMCompatibility() {
        return null;
    }

    @Override
    public void setTMHMCompatibility(Map<Pokemon, boolean[]> compatData) {

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
        return false;
    }

    @Override
    public List<String> getTrainerNames() {
        return null;
    }

    @Override
    public void setTrainerNames(List<String> trainerNames) {

    }

    @Override
    public TrainerNameMode trainerNameMode() {
        return null;
    }

    @Override
    public List<Integer> getTCNameLengthsByTrainer() {
        return null;
    }

    @Override
    public List<String> getTrainerClassNames() {
        return null;
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
        return null;
    }

    @Override
    public ItemList getNonBadItems() {
        return null;
    }

    @Override
    public List<Integer> getEvolutionItems() {
        return null;
    }

    @Override
    public List<Integer> getUniqueNoSellItems() {
        return null;
    }

    @Override
    public List<Integer> getRegularShopItems() {
        return null;
    }

    @Override
    public List<Integer> getOPShopItems() {
        return null;
    }

    @Override
    public String[] getItemNames() {
        return itemNames.toArray(new String[0]);
    }

    @Override
    public List<Integer> getRequiredFieldTMs() {
        return null;
    }

    @Override
    public List<Integer> getCurrentFieldTMs() {
        return null;
    }

    @Override
    public void setFieldTMs(List<Integer> fieldTMs) {

    }

    @Override
    public List<Integer> getRegularFieldItems() {
        return null;
    }

    @Override
    public void setRegularFieldItems(List<Integer> items) {

    }

    @Override
    public boolean hasShopRandomization() {
        return false;
    }

    @Override
    public Map<Integer, Shop> getShopItems() {
        return null;
    }

    @Override
    public void setShopItems(Map<Integer, Shop> shopItems) {

    }

    @Override
    public void setShopPrices() {

    }

    @Override
    public List<IngameTrade> getIngameTrades() {
        return null;
    }

    @Override
    public void setIngameTrades(List<IngameTrade> trades) {

    }

    @Override
    public boolean hasDVs() {
        return false;
    }

    @Override
    public void removeImpossibleEvolutions(Settings settings) {

    }

    @Override
    public void makeEvolutionsEasier(Settings settings) {

    }

    @Override
    public void removeTimeBasedEvolutions() {

    }

    @Override
    public List<Integer> getFieldMoves() {
        return null;
    }

    @Override
    public List<Integer> getEarlyRequiredHMMoves() {
        return null;
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
        return "None";
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
        return null;
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
        return false;
    }

    @Override
    protected void loadedROM(String filename) {
        try {
            byte[] main = this.readMain();
            byte[] test = this.readFile("bin/appli/autosave/bin/autosave_00.arc");
            this.romEntry = entryFor(FileFunctions.getCRC32(main));

            loadPokemonStats();

            pokemonListInclFormes = new ArrayList<>(pokes.values());
            pokemonListInclFormes.add(0,null);  // For compatibility with AbstractRomHandler; this will be removed later
            pokemonList = pokemonListInclFormes
                    .stream()
                    .filter(p -> (p == null || p.number <= SwShConstants.pokemonCount))
                    .collect(Collectors.toList());

            abilityNames = getStrings(romEntry.getString("AbilityNames"));
            itemNames = getStrings(romEntry.getString("ItemNames"));
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
                loadBasicPokeStats(thisPokemon,thisEntry,formeMappings);
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
                loadBasicPokeStats(thisPokemon, thisEntry,formeMappings);
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
                // so if the Pokemon is Nincada, then let's and put it as one of its evolutions
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

    @Override
    protected void savingROM() throws IOException {
        savePokemonStats();
    }

    private void savePokemonStats() {

        try {
            byte[] personalInfo = this.readFile("bin/pml/personal/personal_total.bin");
            for (Map.Entry<Integer,Pokemon> pokeEntry: pokes.entrySet()) {
                byte[] pokeData = new byte[SwShConstants.bsSize];
                int i = pokeEntry.getKey();
                System.arraycopy(personalInfo, i * SwShConstants.bsSize, pokeData, 0, SwShConstants.bsSize);
                saveBasicPokeStats(pokeEntry.getValue(), pokeData);
                System.arraycopy(pokeData, 0, personalInfo, i * SwShConstants.bsSize, SwShConstants.bsSize);
            }
            writeFile("bin/pml/personal/personal_total.bin", personalInfo);
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
                // TODO shedinja bullshit
//                if (pk.number == Species.nincada) {
//                    writeShedinjaEvolution();
//                }
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
                writeFile(String.format("bin/pml/evolution/evo_%03d.bin",pokeEntry.getKey()),evoEntry);
            }

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
            // TODO save file
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
}
