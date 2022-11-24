package com.dabomstew.pkrandom.romhandlers;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.Utils;
import com.dabomstew.pkrandom.constants.SwShConstants;
import com.dabomstew.pkrandom.exceptions.RandomizerIOException;
import com.dabomstew.pkrandom.hac.SwitchFileReader;
import com.dabomstew.pkrandom.pokemon.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class BDSPRomHandler extends AbstractSwitchRomHandler {

    public static class Factory extends RomHandler.Factory {

        @Override
        public BDSPRomHandler create(Random random, PrintStream logStream) {
            return new BDSPRomHandler(random, logStream);
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

    public BDSPRomHandler(Random random) {
        super(random, null);
    }

    public BDSPRomHandler(Random random, PrintStream logStream) {
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

    private static void loadROMInfo() {
        roms = new ArrayList<>();
        RomEntry current = null;
        try {
            Scanner sc = new Scanner(FileFunctions.openConfig("bdsp_offsets.ini"), "UTF-8");
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
                            current.mainCRC32 = Utils.parseRILong("0x" + r[1].trim());
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
                            int offs = Utils.parseRIInt(r[1]);
                            current.numbers.put(r[0], offs);
                        } else if (r[1].startsWith("[") && r[1].endsWith("]")) {
                            String[] offsets = r[1].substring(1, r[1].length() - 1).split(",");
                            if (offsets.length == 1 && offsets[0].trim().isEmpty()) {
                                current.arrayEntries.put(r[0], new int[0]);
                            } else {
                                int[] offs = new int[offsets.length];
                                int c = 0;
                                for (String off : offsets) {
                                    offs[c++] = Utils.parseRIInt(off);
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

    private static List<RomEntry> roms;

    static {
        loadROMInfo();
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
    private byte[] main;

    @Override
    public boolean isRomValid() {
        return true;
    }

    @Override
    public List<Pokemon> getPokemon() {
        return new ArrayList<>();
    }

    @Override
    public List<Pokemon> getPokemonInclFormes() {
        return new ArrayList<>();
    }

    @Override
    public List<Pokemon> getAltFormes() {
        return null;
    }

    @Override
    public List<MegaEvolution> getMegaEvolutions() {
        return null;
    }

    @Override
    public Pokemon getAltFormeOfPokemon(Pokemon pk, int forme) {
        return null;
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
    public boolean supportsStarterHeldItems() {
        return false;
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
        return 0;
    }

    @Override
    public int highestAbilityIndex() {
        return 0;
    }

    @Override
    public Map<Integer, List<Integer>> getAbilityVariations() {
        return null;
    }

    @Override
    public boolean hasMegaEvolutions() {
        return false;
    }

    @Override
    public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
        return null;
    }

    @Override
    public void setEncounters(boolean useTimeOfDay, List<EncounterSet> encounters) {

    }

    @Override
    public boolean hasWildAltFormes() {
        return false;
    }

    @Override
    public List<Trainer> getTrainers() {
        return null;
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
        return new String[0];
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
    public void enableGuaranteedPokemonCatching() {
        // do nothing for now
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
        return 0;
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
            main = this.readMain();
            romEntry = entryFor(FileFunctions.getCRC32(main));
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    protected void savingROM() throws IOException {

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
