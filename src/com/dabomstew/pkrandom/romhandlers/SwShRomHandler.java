package com.dabomstew.pkrandom.romhandlers;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.constants.Species;
import com.dabomstew.pkrandom.constants.SwShConstants;
import com.dabomstew.pkrandom.exceptions.RandomizerIOException;
import com.dabomstew.pkrandom.generated.swsh.*;
import com.dabomstew.pkrandom.hac.GFPack;
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
            return detectSwitchGameInner(filename);
        }
    }

    public SwShRomHandler(Random random) {
        super(random, null);
    }

    public SwShRomHandler(Random random, PrintStream logStream) {
        super(random, logStream);
    }

    @Override
    protected boolean detectSwitchGame(String filePath) {
        return detectSwitchGameInner(filePath);
    }

    private static boolean detectSwitchGameInner(String filePath) {
        // No other Pokemon games on Switch have camping (currently), so it's a signal that this is SwSh.
        String pokeCampFoodstuffTablePath = filePath + File.separator + "romfs" + File.separator + "bin" + File.separator + "pokecamp" +
                File.separator + "foodstuff" + File.separator + "pokecamp_foodstuff_table.prmb";
        File pokeCampFoodstuffTableFile = new File(pokeCampFoodstuffTablePath);
        return (pokeCampFoodstuffTableFile.exists() && pokeCampFoodstuffTableFile.isFile());
    }

    private Map<Integer,Pokemon> pokes;
    private Map<Integer,FormeInfo> formeMappings = new TreeMap<>();
    private Map<Integer,Map<Integer,Integer>> absolutePokeNumByBaseForme;
    private Map<Integer,Integer> dummyAbsolutePokeNums;
    private List<Pokemon> pokemonList;
    private List<Pokemon> pokemonListInclFormes;

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
        List<EncounterSet> encounters = new ArrayList<>();
        try  {
            final int NUM_WEATHER_TABLES = 9;
            // TODO move file name to offsets.ini file
            byte[] wildData = this.readFile("bin/archive/field/resident/data_table.gfpak");
            GFPack wildPack = new GFPack(wildData);

            // TODO read from encount_k for sword
            byte[] shieldData = wildPack.getDataFileName("encount_t.bin");

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
            // TODO move file name to offsets.ini file
            byte[] wildData = this.readFile("bin/archive/field/resident/data_table.gfpak");
            GFPack wildPack = new GFPack(wildData);

            // TODO read from encount_k for sword
            byte[] shieldData = wildPack.getDataFileName("encount_t.bin");

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
    public String getROMName() {
        return "Pokemon SwSh";
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

            loadPokemonStats();

            pokemonListInclFormes = new ArrayList<>(pokes.values());
            pokemonListInclFormes.add(0,null);  // For compatibility with AbstractRomHandler; this will be removed later
            pokemonList = pokemonListInclFormes
                    .stream()
                    .filter(p -> (p == null || p.number <= SwShConstants.pokemonCount))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
    }

    @Override
    protected void savingROM() throws IOException {

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


    private void loadPokemonStats() {
        try {
            // TODO move file name to offsets.ini file
            byte[] personalInfo = this.readFile("bin/pml/personal/personal_total.bin");
            String[] pokeNames = readPokemonNames();
            int pokemonCount = SwShConstants.pokemonCount;
            pokes = new TreeMap<>();
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
//
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
                    formNum++;
                    currentMap.put(formNum,k);
                } else {
                    if (prevSpecies != 0) {
                        absolutePokeNumByBaseForme.put(prevSpecies,currentMap);
                    }
                    prevSpecies = realBaseForme;
                    formNum = 1;
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
        //populateEvolutions();
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
            System.out.println("Forms: " + formeCount);
            System.out.printf("Index: %04X\n",FileFunctions.read2ByteInt(stats, SwShConstants.bsFormeOffset));
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
            // TODO move file name to offsets.ini file
            List<String> nameList = getStrings("bin/message/English/common/monsname.dat");
            for (int i = 1; i <= pokemonCount; i++) {
                pokeNames[i] = nameList.get(i);
            }
        } catch (IOException e) {
            throw new RandomizerIOException(e);
        }
        return pokeNames;
    }
    @Override
    protected String getGameAcronym() {
        return null;
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
