package com.dabomstew.pkrandom.constants;

import com.dabomstew.pkrandom.pokemon.ItemList;
import com.dabomstew.pkrandom.pokemon.MoveCategory;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.Type;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SwShConstants {
    public static final int pokemonCount = 898; // includes unused pokemon
    public static final int formeCount = 293;   // includes unused formes
    public static final int moveCount = 826;
    public static final int highestAbilityIndex = Abilities.asOneGrimNeigh;

    public static final int bsHPOffset = 0, bsAttackOffset = 1, bsDefenseOffset = 2, bsSpeedOffset = 3,
            bsSpAtkOffset = 4, bsSpDefOffset = 5, bsPrimaryTypeOffset = 6, bsSecondaryTypeOffset = 7,
            bsCatchRateOffset = 8, bsCommonHeldItemOffset = 12, bsRareHeldItemOffset = 14,
            bsVeryRareHeldItemOffset = 16, bsGenderOffset = 18, bsGrowthCurveOffset = 21, bsAbility1Offset = 24,
            bsAbility2Offset = 26, bsAbility3Offset = 28, bsFormeOffset = 30, bsFormeSpriteOffset = 30, // ???
            bsFormeCountOffset = 32, bsPresentFlag = 33, bsTMHMCompatOffset = 40, bsSpecialMTCompatOffset = 56,
            bsMTCompatOffset = 60;

    public static final int presentBitMask = 0x40;

    public static final int bsSize = 0xB0;

    public static final int evolutionMethodCount = 48;

    public static final int trainerCount = 437;         // Includes Trainer 0 which is not real

    public static final int learnsetEntrySize = 0x104;

    public static final int duplicateTrade = 9;

    public static final int tmCount = 100, trCount = 100, tmBlockOneCount = 92, tmBlockTwoCount = 3, tmBlockThreeCount = 4,
            tmBlockFourCount = 1, tmBlockOneOffset = Items.tm01, tmBlockTwoOffset = Items.tm93,
            tmBlockThreeOffset = Items.tm96, tmBlockFourOffset = Items.tm00;

    public static final int tmTrMoveTableOffset = 0xCD4;

    public static final int itemPropertiesEntryLength = 0x30;

    public static final String ninjaskSpeciesLocator = "1F8C047141", shedinjaLocator = "81248052E00313AAE2";

    public static final String[] fastestTextPrefixes = new String[]{"40F915F940F9B50000B4E00315AA088C44F8080940F900013FD6E00315AA", "14002945F9"};

    public static final String saveLoadFormeReversionPrefix = "D3E00315AAE2031FAA", afterBattleFormeReversionLocator = "9FAA0B71";

    public static final Type[] typeTable = constructTypeTable();

    private static Type[] constructTypeTable() {
        Type[] table = new Type[256];
        table[0x00] = Type.NORMAL;
        table[0x01] = Type.FIGHTING;
        table[0x02] = Type.FLYING;
        table[0x03] = Type.POISON;
        table[0x04] = Type.GROUND;
        table[0x05] = Type.ROCK;
        table[0x06] = Type.BUG;
        table[0x07] = Type.GHOST;
        table[0x08] = Type.STEEL;
        table[0x09] = Type.FIRE;
        table[0x0A] = Type.WATER;
        table[0x0B] = Type.GRASS;
        table[0x0C] = Type.ELECTRIC;
        table[0x0D] = Type.PSYCHIC;
        table[0x0E] = Type.ICE;
        table[0x0F] = Type.DRAGON;
        table[0x10] = Type.DARK;
        table[0x11] = Type.FAIRY;
        return table;
    }

    public static byte typeToByte(Type type) {
        if (type == null) {
            return 0x00; // normal?
        }
        switch (type) {
            case NORMAL:
                return 0x00;
            case FIGHTING:
                return 0x01;
            case FLYING:
                return 0x02;
            case POISON:
                return 0x03;
            case GROUND:
                return 0x04;
            case ROCK:
                return 0x05;
            case BUG:
                return 0x06;
            case GHOST:
                return 0x07;
            case FIRE:
                return 0x09;
            case WATER:
                return 0x0A;
            case GRASS:
                return 0x0B;
            case ELECTRIC:
                return 0x0C;
            case PSYCHIC:
                return 0x0D;
            case ICE:
                return 0x0E;
            case DRAGON:
                return 0x0F;
            case STEEL:
                return 0x08;
            case DARK:
                return 0x10;
            case FAIRY:
                return 0x11;
            default:
                return 0; // normal by default
        }
    }

    public static List<Integer> actuallyCosmeticForms = Arrays.asList(
            Species.SwShFormes.cherrimCosmetic1,
            Species.SwShFormes.shellosCosmetic1,
            Species.SwShFormes.gastrodonCosmetic1,
            Species.SwShFormes.keldeoCosmetic1,
            Species.SwShFormes.pumpkabooCosmetic1, Species.SwShFormes.pumpkabooCosmetic2,
            Species.SwShFormes.pumpkabooCosmetic3,
            Species.SwShFormes.gourgeistCosmetic1, Species.SwShFormes.gourgeistCosmetic2,
            Species.SwShFormes.gourgeistCosmetic3,
            Species.SwShFormes.mimikyuCosmetic1,
            Species.SwShFormes.zygarde10Cosmetic1, Species.SwShFormes.zygardeCosmetic1,
            Species.SwShFormes.magearnaCosmetic1,
            Species.SwShFormes.pikachuCosmetic1, Species.SwShFormes.pikachuCosmetic2, Species.SwShFormes.pikachuCosmetic3,
            Species.SwShFormes.pikachuCosmetic4, Species.SwShFormes.pikachuCosmetic5, Species.SwShFormes.pikachuCosmetic6,
            Species.SwShFormes.pikachuCosmetic7, Species.SwShFormes.pikachuCosmetic8, Species.SwShFormes.pikachuCosmetic9, // Pikachu With Funny Hats
            Species.SwShFormes.rockruffCosmetic1,
            Species.SwShFormes.cramorantCosmetic1, Species.SwShFormes.cramorantCosmetic2,
            Species.SwShFormes.alcremieCosmetic1, Species.SwShFormes.alcremieCosmetic2,
            Species.SwShFormes.alcremieCosmetic3, Species.SwShFormes.alcremieCosmetic4,
            Species.SwShFormes.alcremieCosmetic5, Species.SwShFormes.alcremieCosmetic6,
            Species.SwShFormes.alcremieCosmetic7, Species.SwShFormes.alcremieCosmetic8,
            Species.SwShFormes.genesectCosmetic1, Species.SwShFormes.genesectCosmetic2,
            Species.SwShFormes.genesectCosmetic3, Species.SwShFormes.genesectCosmetic4,
            Species.SwShFormes.morpekoCosmetic1,
            Species.SwShFormes.sinisteaCosmetic1,
            Species.SwShFormes.polteageistCosmetic1,
            Species.SwShFormes.toxtricityCosmetic1,
            Species.SwShFormes.zarudeCosmetic1,
            Species.SwShFormes.xerneasCosmetic1,
            Species.SwShFormes.silvallyCosmetic1, Species.SwShFormes.silvallyCosmetic2,
            Species.SwShFormes.silvallyCosmetic3, Species.SwShFormes.silvallyCosmetic4,
            Species.SwShFormes.silvallyCosmetic5, Species.SwShFormes.silvallyCosmetic6,
            Species.SwShFormes.silvallyCosmetic7, Species.SwShFormes.silvallyCosmetic8,
            Species.SwShFormes.silvallyCosmetic9, Species.SwShFormes.silvallyCosmetic10,
            Species.SwShFormes.silvallyCosmetic11, Species.SwShFormes.silvallyCosmetic12,
            Species.SwShFormes.silvallyCosmetic13, Species.SwShFormes.silvallyCosmetic14,
            Species.SwShFormes.silvallyCosmetic15, Species.SwShFormes.silvallyCosmetic16,
            Species.SwShFormes.silvallyCosmetic17

    );

    public static Map<Integer,Integer> altFormesWithCosmeticForms = setupAltFormesWithCosmeticForms();

    private static Map<Integer,Integer> setupAltFormesWithCosmeticForms() {
        Map<Integer,Integer> map = new HashMap<>();

        map.put(Species.SwShFormes.zygarde10,1); // 1 form (Power Construct)

        return map;
    }

    // TODO make sure that ignored forms still get base stats etc propagated from base forme
    public static List<Integer> ignoreForms = Arrays.asList(
            Species.SwShFormes.pikachuCosmetic8,
            Species.SwShFormes.slowbroMega,
            Species.SwShFormes.rayquazaMega,
            Species.SwShFormes.cherrimCosmetic1,
            Species.SwShFormes.zygarde10Cosmetic1,
            Species.SwShFormes.zygardeCosmetic1,
            Species.SwShFormes.mimikyuCosmetic1,
            Species.SwShFormes.xerneasCosmetic1,
            Species.SwShFormes.toxtricityCosmetic1,
            Species.SwShFormes.cramorantCosmetic1,
            Species.SwShFormes.cramorantCosmetic2,
            Species.SwShFormes.rockruffCosmetic1,
            Species.SwShFormes.genesectCosmetic1,
            Species.SwShFormes.genesectCosmetic2,
            Species.SwShFormes.genesectCosmetic3,
            Species.SwShFormes.genesectCosmetic4,
            Species.SwShFormes.silvallyCosmetic1,
            Species.SwShFormes.silvallyCosmetic2,
            Species.SwShFormes.silvallyCosmetic3,
            Species.SwShFormes.silvallyCosmetic4,
            Species.SwShFormes.silvallyCosmetic5,
            Species.SwShFormes.silvallyCosmetic6,
            Species.SwShFormes.silvallyCosmetic7,
            Species.SwShFormes.silvallyCosmetic8,
            Species.SwShFormes.silvallyCosmetic9,
            Species.SwShFormes.silvallyCosmetic10,
            Species.SwShFormes.silvallyCosmetic11,
            Species.SwShFormes.silvallyCosmetic12,
            Species.SwShFormes.silvallyCosmetic13,
            Species.SwShFormes.silvallyCosmetic14,
            Species.SwShFormes.silvallyCosmetic15,
            Species.SwShFormes.silvallyCosmetic16,
            Species.SwShFormes.silvallyCosmetic17
    );

    private static List<Integer> speciesWithAlolanForms = Arrays.asList(
            Species.raichu, Species.sandshrew, Species.sandslash, Species.vulpix, Species.ninetales, Species.diglett,
            Species.dugtrio, Species.persian, Species.exeggutor, Species.marowak
    );

    private static List<Integer> speciesWithGalarianForms = Arrays.asList(
            Species.ponyta, Species.rapidash, Species.slowpoke, Species.farfetchd, Species.weezing, Species.mrMime,
            Species.articuno, Species.zapdos, Species.moltres, Species.slowking, Species.corsola, Species.zigzagoon,
            Species.linoone, Species.darumaka, Species.yamask, Species.stunfisk
    );


    private static final Map<Integer,String> dummyFormeSuffixes = setupDummyFormeSuffixes();
    private static final Map<Integer,Map<Integer,String>> formeSuffixesByBaseForme = setupFormeSuffixesByBaseForme();

    public static String getFormeSuffixByBaseForme(int baseForme, int formNum) {
        return formeSuffixesByBaseForme.getOrDefault(baseForme,dummyFormeSuffixes).getOrDefault(formNum,"");
    }

    public static final List<Integer> irregularFormes = Arrays.asList(
            Species.SwShFormes.darmanitanZ,
            Species.SwShFormes.darmanitanGZ,
            Species.SwShFormes.kyuremW,
            Species.SwShFormes.kyuremB,
            Species.SwShFormes.aegislashB,
            Species.SwShFormes.wishiwashiS,
            Species.SwShFormes.zygardeC,
            Species.SwShFormes.necrozmaDM,
            Species.SwShFormes.necrozmaDW
    );

    private static Map<Integer,Map<Integer,String>> setupFormeSuffixesByBaseForme() {
        Map<Integer,Map<Integer,String>> map = new HashMap<>();

        Map<Integer,String> meowthMap = new HashMap<>();
        meowthMap.put(1,"-A");
        meowthMap.put(2,"-G");
        map.put(Species.meowth, meowthMap);

        Map<Integer,String> slowbroMap = new HashMap<>();
        slowbroMap.put(2,"-G");
        map.put(Species.slowbro, slowbroMap);

        Map<Integer,String> giratinaMap = new HashMap<>();
        giratinaMap.put(1,"-O");
        map.put(Species.giratina, giratinaMap);

        Map<Integer,String> rotomMap = new HashMap<>();
        rotomMap.put(1,"-H");
        rotomMap.put(2,"-W");
        rotomMap.put(3,"-Fr");
        rotomMap.put(4,"-Fa");
        rotomMap.put(5,"-M");
        map.put(Species.rotom, rotomMap);

        Map<Integer,String> basculinMap = new HashMap<>();
        basculinMap.put(1,"-B");
        map.put(Species.basculin, basculinMap);

        Map<Integer,String> darmanitanMap = new HashMap<>();
        darmanitanMap.put(1,"-Z");
        darmanitanMap.put(2,"-G");
        darmanitanMap.put(3,"-GZ");
        map.put(Species.darmanitan, darmanitanMap);

        Map<Integer,String> kyuremMap = new HashMap<>();
        kyuremMap.put(1,"-W");
        kyuremMap.put(2,"-B");
        map.put(Species.kyurem, kyuremMap);

        Map<Integer,String> tornadusMap = new HashMap<>();
        tornadusMap.put(1,"-T");
        map.put(Species.tornadus, tornadusMap);

        Map<Integer,String> thundurusMap = new HashMap<>();
        thundurusMap.put(1,"-T");
        map.put(Species.thundurus, thundurusMap);

        Map<Integer,String> landorusMap = new HashMap<>();
        landorusMap.put(1,"-T");
        map.put(Species.landorus, landorusMap);

        Map<Integer,String> meowsticMap = new HashMap<>();
        meowsticMap.put(1,"-F");
        map.put(Species.meowstic, meowsticMap);

        Map<Integer,String> aegislashMap = new HashMap<>();
        aegislashMap.put(1,"-B");
        map.put(Species.aegislash, aegislashMap);

        Map<Integer,String> pumpkabooMap = new HashMap<>();
        pumpkabooMap.put(1,"-M");
        pumpkabooMap.put(2,"-L");
        pumpkabooMap.put(3,"-XL");
        map.put(Species.pumpkaboo, pumpkabooMap);

        Map<Integer,String> gourgeistMap = new HashMap<>();
        gourgeistMap.put(1,"-M");
        gourgeistMap.put(2,"-L");
        gourgeistMap.put(3,"-XL");
        map.put(Species.gourgeist, gourgeistMap);

        Map<Integer,String> wishiwashiMap = new HashMap<>();
        wishiwashiMap.put(1,"-S");
        map.put(Species.wishiwashi, wishiwashiMap);

        Map<Integer,String> lycanrocMap = new HashMap<>();
        lycanrocMap.put(1,"-M");
        lycanrocMap.put(2,"-D");
        map.put(Species.lycanroc, lycanrocMap);

        for (int species: speciesWithAlolanForms) {
            Map<Integer,String> alolanMap = new HashMap<>();
            alolanMap.put(1,"-A");
            map.put(species, alolanMap);
        }

        for (int species: speciesWithGalarianForms) {
            Map<Integer,String> galarianMap = new HashMap<>();
            galarianMap.put(1,"-G");
            map.put(species, galarianMap);
        }

        Map<Integer,String> zygardeMap = new HashMap<>();
        zygardeMap.put(1,"-10");
        zygardeMap.put(4,"-C");
        map.put(Species.zygarde, zygardeMap);

        Map<Integer,String> necrozmaMap = new HashMap<>();
        necrozmaMap.put(1,"-DM");
        necrozmaMap.put(2,"-DW");
        map.put(Species.necrozma, necrozmaMap);

        Map<Integer,String> eiscueMap = new HashMap<>();
        eiscueMap.put(1,"-N");
        map.put(Species.eiscue, eiscueMap);

        Map<Integer,String> indeedeeMap = new HashMap<>();
        indeedeeMap.put(1,"-F");
        map.put(Species.indeedee, indeedeeMap);

        Map<Integer,String> zacianMap = new HashMap<>();
        zacianMap.put(1,"-C");
        map.put(Species.zacian, zacianMap);

        Map<Integer,String> zamazentaMap = new HashMap<>();
        zamazentaMap.put(1,"-C");
        map.put(Species.zamazenta, zamazentaMap);

        Map<Integer,String> eternatusMap = new HashMap<>();
        eternatusMap.put(1,"-E");
        map.put(Species.eternatus, eternatusMap);

        Map<Integer,String> urshifuMap = new HashMap<>();
        urshifuMap.put(1,"-R");
        map.put(Species.urshifu, urshifuMap);

        Map<Integer,String> calyrexMap = new HashMap<>();
        calyrexMap.put(1,"-I");
        calyrexMap.put(2,"-S");
        map.put(Species.calyrex, calyrexMap);

        return map;
    }

    private static Map<Integer,String> setupDummyFormeSuffixes() {
        Map<Integer,String> m = new HashMap<>();
        m.put(0,"");
        return m;
    }

    public static final Map<Integer,List<Integer>> abilityVariations = setupAbilityVariations();

    private static Map<Integer,List<Integer>> setupAbilityVariations() {
        Map<Integer,List<Integer>> map = new HashMap<>();
        map.put(Abilities.insomnia, Arrays.asList(Abilities.insomnia, Abilities.vitalSpirit));
        map.put(Abilities.clearBody, Arrays.asList(Abilities.clearBody, Abilities.whiteSmoke, Abilities.fullMetalBody));
        map.put(Abilities.hugePower, Arrays.asList(Abilities.hugePower, Abilities.purePower));
        map.put(Abilities.battleArmor, Arrays.asList(Abilities.battleArmor, Abilities.shellArmor));
        map.put(Abilities.cloudNine, Arrays.asList(Abilities.cloudNine, Abilities.airLock));
        map.put(Abilities.filter, Arrays.asList(Abilities.filter, Abilities.solidRock, Abilities.prismArmor));
        map.put(Abilities.roughSkin, Arrays.asList(Abilities.roughSkin, Abilities.ironBarbs));
        map.put(Abilities.moldBreaker, Arrays.asList(Abilities.moldBreaker, Abilities.turboblaze, Abilities.teravolt));
        map.put(Abilities.wimpOut, Arrays.asList(Abilities.wimpOut, Abilities.emergencyExit));
        map.put(Abilities.queenlyMajesty, Arrays.asList(Abilities.queenlyMajesty, Abilities.dazzling));
        map.put(Abilities.gooey, Arrays.asList(Abilities.gooey, Abilities.tanglingHair));
        map.put(Abilities.receiver, Arrays.asList(Abilities.receiver, Abilities.powerOfAlchemy));
        map.put(Abilities.multiscale, Arrays.asList(Abilities.multiscale, Abilities.shadowShield));
        map.put(Abilities.protean, Arrays.asList(Abilities.protean, Abilities.libero));
        map.put(Abilities.propellerTail, Arrays.asList(Abilities.propellerTail, Abilities.stalwart));

        return map;
    }

    public static final List<Integer> uselessAbilities = Arrays.asList(Abilities.forecast, Abilities.multitype,
            Abilities.flowerGift, Abilities.zenMode, Abilities.stanceChange, Abilities.shieldsDown, Abilities.schooling,
            Abilities.disguise, Abilities.battleBond, Abilities.powerConstruct, Abilities.rksSystem,
            Abilities.gulpMissile, Abilities.iceFace, Abilities.hungerSwitch);

    public static final MoveCategory[] moveCategoryIndices = { MoveCategory.STATUS, MoveCategory.PHYSICAL,
            MoveCategory.SPECIAL };

    public static byte moveCategoryToByte(MoveCategory cat) {
        switch (cat) {
            case PHYSICAL:
                return 1;
            case SPECIAL:
                return 2;
            case STATUS:
            default:
                return 0;
        }
    }

    public static final int noDamageTargetTrappingEffect = 106, noDamageFieldTrappingEffect = 354,
            damageAdjacentFoesTrappingEffect = 373, damageTargetTrappingEffect = 384;

    public static final int noDamageStatusQuality = 1, noDamageStatChangeQuality = 2, damageStatusQuality = 4,
            noDamageStatusAndStatChangeQuality = 5, damageTargetDebuffQuality = 6, damageUserBuffQuality = 7,
            damageAbsorbQuality = 8;

    public static final List<Integer> unusableMoves = Arrays.asList(
            Moves.karateChop, Moves.doubleSlap, Moves.cometPunch, Moves.razorWind, Moves.jumpKick, Moves.rollingKick,
            Moves.twineedle, Moves.sonicBoom, Moves.dragonRage, Moves.meditate, Moves.rage, Moves.barrier, Moves.bide,
            Moves.mirrorMove, Moves.eggBomb, Moves.boneClub, Moves.clamp, Moves.spikeCannon, Moves.constrict, Moves.barrage,
            Moves.bubble, Moves.dizzyPunch, Moves.flash, Moves.psywave, Moves.hyperFang, Moves.sharpen, Moves.sketch,
            Moves.spiderWeb, Moves.nightmare, Moves.feintAttack, Moves.foresight, Moves.returnTheMoveNotTheKeyword,
            Moves.frustration, Moves.magnitude, Moves.pursuit, Moves.hiddenPower, Moves.smellingSalts, Moves.assist,
            Moves.refresh, Moves.snatch, Moves.secretPower, Moves.camouflage, Moves.tailGlow, Moves.mudSport, Moves.iceBall,
            Moves.needleArm, Moves.odorSleuth, Moves.silverWind, Moves.grassWhistle, Moves.signalBeam, Moves.skyUppercut,
            Moves.waterSport, Moves.psychoBoost, Moves.miracleEye, Moves.wakeUpSlap, Moves.naturalGift, Moves.embargo,
            Moves.trumpCard, Moves.healBlock, Moves.wringOut, Moves.luckyChant, Moves.meFirst, Moves.punishment,
            Moves.heartSwap, Moves.mudBomb, Moves.mirrorShot, Moves.rockClimb, Moves.magnetBomb, Moves.captivate,
            Moves.chatter, Moves.judgment, Moves.healOrder, Moves.darkVoid, Moves.seedFlare, Moves.ominousWind,
            Moves.telekinesis, Moves.flameBurst, Moves.synchronoise, Moves.chipAway, Moves.skyDrop, Moves.bestow,
            Moves.heartStamp, Moves.steamroller, Moves.relicSong, Moves.rototiller, Moves.ionDeluge, Moves.hyperspaceHole,
            Moves.powder, Moves.lightOfRuin, Moves.hyperspaceFury, Moves.breakneckBlitzPhysical, Moves.breakneckBlitzSpecial,
            Moves.allOutPummelingPhysical, Moves.allOutPummelingSpecial, Moves.supersonicSkystrikePhysical,
            Moves.supersonicSkystrikeSpecial, Moves.acidDownpourPhysical, Moves.acidDownpourSpecial,
            Moves.tectonicRagePhysical, Moves.tectonicRageSpecial, Moves.continentalCrushPhysical,
            Moves.continentalCrushSpecial, Moves.savageSpinOutPhysical, Moves.savageSpinOutSpecial,
            Moves.neverEndingNightmarePhysical, Moves.neverEndingNightmareSpecial, Moves.corkscrewCrashPhysical,
            Moves.corkscrewCrashSpecial, Moves.infernoOverdrivePhysical, Moves.infernoOverdriveSpecial,
            Moves.hydroVortexPhysical, Moves.hydroVortexSpecial, Moves.bloomDoomPhysical, Moves.bloomDoomSpecial,
            Moves.gigavoltHavocPhysical, Moves.gigavoltHavocSpecial, Moves.shatteredPsychePhysical,
            Moves.shatteredPsycheSpecial, Moves.subzeroSlammerPhysical, Moves.subzeroSlammerSpecial,
            Moves.devastatingDrakePhysical, Moves.devastatingDrakeSpecial, Moves.blackHoleEclipsePhysical,
            Moves.blackHoleEclipseSpecial, Moves.twinkleTacklePhysical, Moves.twinkleTackleSpecial, Moves.catastropika,
            Moves.iceHammer, Moves.spotlight, Moves.toxicThread, Moves.revelationDance, Moves.beakBlast,
            Moves.sinisterArrowRaid, Moves.maliciousMoonsault, Moves.oceanicOperetta, Moves.guardianOfAlola,
            Moves.soulStealing7StarStrike, Moves.stokedSparksurfer, Moves.pulverizingPancake, Moves.extremeEvoboost,
            Moves.genesisSupernova, Moves.tenMillionVoltThunderbolt, Moves.lightThatBurnsTheSky, Moves.searingSunrazeSmash,
            Moves.menacingMoonrazeMaelstrom, Moves.letsSnuggleForever, Moves.splinteredStormshards,
            Moves.clangorousSoulblaze, Moves.zippyZap, Moves.splishySplash, Moves.floatyFall, Moves.pikaPapow,
            Moves.bouncyBubble, Moves.buzzyBuzz, Moves.sizzlySlide, Moves.glitzyGlow, Moves.baddyBad, Moves.sappySeed,
            Moves.freezyFrost, Moves.sparklySwirl, Moves.veeveeVolley
    );

    public static Map<Integer, List<Integer>> getHardcodedTradeTextOffsets() {
        Map<Integer, List<Integer>> hardcodedTradeTextOffsets = new HashMap<>();
        hardcodedTradeTextOffsets.put(0, Collections.singletonList(127));
        hardcodedTradeTextOffsets.put(1, Collections.singletonList(128));
        hardcodedTradeTextOffsets.put(2, Collections.singletonList(129));
        hardcodedTradeTextOffsets.put(3, Collections.singletonList(130));
        hardcodedTradeTextOffsets.put(4, Collections.singletonList(131));
        hardcodedTradeTextOffsets.put(5, Collections.singletonList(132));
        hardcodedTradeTextOffsets.put(6, Collections.singletonList(133));
        hardcodedTradeTextOffsets.put(7, Collections.singletonList(134));
        hardcodedTradeTextOffsets.put(8, Collections.singletonList(135));
        hardcodedTradeTextOffsets.put(10, Collections.singletonList(136));
        hardcodedTradeTextOffsets.put(11, Collections.singletonList(137));
        hardcodedTradeTextOffsets.put(12, Arrays.asList(0, 3, 4));
        hardcodedTradeTextOffsets.put(13, Arrays.asList(8, 11, 12));
        hardcodedTradeTextOffsets.put(14, Arrays.asList(16, 17, 20));
        hardcodedTradeTextOffsets.put(15, Arrays.asList(24, 25, 26, 27, 28));
        hardcodedTradeTextOffsets.put(16, Collections.singletonList(32));
        hardcodedTradeTextOffsets.put(17, Collections.singletonList(40));
        hardcodedTradeTextOffsets.put(18, Arrays.asList(48, 49, 52));
        hardcodedTradeTextOffsets.put(19, Arrays.asList(56, 59, 60));
        hardcodedTradeTextOffsets.put(20, Arrays.asList(64, 67, 68));
        hardcodedTradeTextOffsets.put(21, Arrays.asList(72, 73, 75));
        hardcodedTradeTextOffsets.put(22, Arrays.asList(80, 81, 84));
        return hardcodedTradeTextOffsets;
    }

    public static final List<Integer> tradeNickNameOffsets = Arrays.asList(142, 143, 144, 145, 146, 147, 148, 149, 150,
            150, 151, 152, 6, 14, 22, 30, 38, 46, 54, 62, 70, 78, 86);

    public static final List<Integer> tradeOTOffsets = Arrays.asList(153, 153, 153, 153, 153, 153, 153, 153, 153, 153,
            153, 153, 7, 15, 23, 31, 39, 47, 55, 63, 71, 79, 87);

    public static String getZoneName(long hash) {
        return zoneNames.get(hash);
    }

    private static final Map<Long,String> zoneNames = setupZoneNames();

    private static Map<Long, String> setupZoneNames() {
        Map<Long,String> zoneNames = new HashMap<>();
        zoneNames.put(0x078BC1FF1A657844L, "Route 1");
        zoneNames.put(0x10355EFF1F4DB0B5L, "Route 2");
        zoneNames.put(0x776776717EA4483EL, "Rolling Fields");
        zoneNames.put(0x776777717EA449F1L, "Dappled Grove");
        zoneNames.put(0x776778717EA44BA4L, "Watchtower Ruins");
        zoneNames.put(0x776779717EA44D57L, "East Lake Axewell");
        zoneNames.put(0x77677A717EA44F0AL, "West Lake Axewell");
        zoneNames.put(0x77677B717EA450BDL, "Axew's Eye");
        zoneNames.put(0x77676C717EA43740L, "South Lake Miloch");
        zoneNames.put(0x77676D717EA438F3L, "Giant's Seat");
        zoneNames.put(0x776AFA717EA75E61L, "North Lake Miloch");
        zoneNames.put(0x194B97FF2492111AL, "Route 3");
        zoneNames.put(0x776E81717EAA799DL, "Motostoke Riverbank");
        zoneNames.put(0x776E7E717EAA7484L, "Bridge Field");
        zoneNames.put(0xDBCF5CFF0180B073L, "Route 4");
        zoneNames.put(0x8F67CD45F405D66EL, "Slumbering Weald (Low Level)");
        zoneNames.put(0xE0D6E5E78C91F4A7L, "City of Motostoke");
        zoneNames.put(0xE4E595FF06C510D8L, "Route 5");
        zoneNames.put(0x1C7150C0594994E5L, "Town of Hulbury");
        zoneNames.put(0x7D3B7A45E97D4A51L, "Galar Mine No. 2");
        zoneNames.put(0x75D83E45E5AA7953L, "Galar Mine");
        zoneNames.put(0x7D3B7745E97D4538L, "Motostoke Outskirts");
        zoneNames.put(0xA88AC04602050B95L, "Glimwood Tangle");
        zoneNames.put(0xEDFC32FF0C0A1B29L, "Route 6");
        zoneNames.put(0xF55F6BFF0FDCE70EL, "Route 7");
        zoneNames.put(0x449AE0FF3D19D777L, "Route 8");
        zoneNames.put(0x4BFDF9FF40EC6CFCL, "Route 8 (on Steamdrift Way)");
        zoneNames.put(0x4BFDFCFF40EC7215L, "Route 9");
        zoneNames.put(0x4BFDF6FF40EC67E3L, "Route 9 (in Circhester Bay)");
        zoneNames.put(0x4BFDFBFF40EC7062L, "Route 9 (in Outer Spikemuth)");
        zoneNames.put(0xB332930807F9D48AL, "Route 10 (Near Station)");
        zoneNames.put(0x7771E5717EAD5960L, "Stony Wilderness");
        zoneNames.put(0x7771E8717EAD5E79L, "Dusty Bowl");
        zoneNames.put(0x7771E7717EAD5CC6L, "Giant's Mirror");
        zoneNames.put(0x7771EA717EAD61DFL, "Hammerlocke Hills");
        zoneNames.put(0x7771E9717EAD602CL, "Giant's Cap");
        zoneNames.put(0x7771EC717EAD6545L, "Lake of Outrage");
        zoneNames.put(0x10355BFF1F4DAB9CL, "Route 2 (High Level)");
        zoneNames.put(0xB332920807F9D2D7L, "Route 10");
        zoneNames.put(0x8F67CB45F405D308L, "Slumbering Weald (High Level)");

        zoneNames.put(0xCD6E4FBCE1466F32L, "Route 1");
        zoneNames.put(0xDF686EC613544BD1L, "Route 2");
        zoneNames.put(0xD602B2A66C268F7CL, "Rolling Fields");
        zoneNames.put(0x458C9CA2C0087385L, "Dappled Grove");
        zoneNames.put(0xE20E6AE30AAA57D2L, "Watchtower Ruins");
        zoneNames.put(0xEEEEAC06BAC8D0B3L, "East Lake Axewell");
        zoneNames.put(0xF8D1E527F7B21FA0L, "West Lake Axewell");
        zoneNames.put(0xB6CFE90E0378FD79L, "Axew's Eye");
        zoneNames.put(0x520D8DD522E9A4C6L, "South Lake Miloch");
        zoneNames.put(0xBC7237A0392D8837L, "Giant's Seat");
        zoneNames.put(0xB67C706F5BAE9E35L, "North Lake Miloch");
        zoneNames.put(0xDA910F69A1B92FEDL, "West Lake Axewell (Surfing)");
        zoneNames.put(0x7C17DB1B430F9543L, "South Lake Miloch (Surfing)");
        zoneNames.put(0xCC0F8A437312B8ACL, "East Lake Axewell (Surfing)");
        zoneNames.put(0x8BE2F6160986FB8EL, "North Lake Miloch (Surfing)");
        zoneNames.put(0x0E8392C0A57D5830L, "Route 3");
        zoneNames.put(0x82A7A328A26B9057L, "Galar Mine");
        zoneNames.put(0x5B2BC38E044EC2B7L, "Route 4");
        zoneNames.put(0x8D68276C03A332BEL, "Route 5");
        zoneNames.put(0x16D2FC4840A658A5L, "Galar Mine No. 2");
        zoneNames.put(0x3D6D58A96894575EL, "Motostoke Outskirts");
        zoneNames.put(0x6AA652641154B119L, "Motostoke Riverbank");
        zoneNames.put(0x36A5DC94335E1E72L, "Bridge Field");
        zoneNames.put(0xE503416A1C05765DL, "Route 6");
        zoneNames.put(0x201EF8E9D2A32D71L, "Glimwood Tangle");
        zoneNames.put(0x42312695C904658CL, "Route 7");
        zoneNames.put(0x1B95A78295F6F213L, "Route 8");
        zoneNames.put(0xAADAC3CB6A1DFE8AL, "Route 8 (on Steamdrift Way)");
        zoneNames.put(0x9116B224702CDCF1L, "Route 9");
        zoneNames.put(0xCDD3B5660D2E5E67L, "Route 9 (in Circhester Bay)");
        zoneNames.put(0x5A3B8F8147272058L, "Route 9 (in Outer Spikemuth)");
        zoneNames.put(0xA93101EA38598995L, "Route 9  (Surfing)");
        zoneNames.put(0x0181225223DE5420L, "Route 10 (Near Station)");
        zoneNames.put(0x1F0F1AE1818C4326L, "Stony Wilderness");
        zoneNames.put(0xAD11B3F3B2AC662DL, "Dusty Bowl");
        zoneNames.put(0xCD9719B2E64F2AA4L, "Giant's Mirror");
        zoneNames.put(0xCD48625EDC10CBFBL, "Hammerlocke Hills");
        zoneNames.put(0x712F3056573E23FAL, "Giant's Cap");
        zoneNames.put(0x593196758BA16B61L, "Lake of Outrage");
        zoneNames.put(0xF79DE930E6F50533L, "Route 10");
        zoneNames.put(0xA26A4595F72EDAEAL, "Route 2 (High Level)");
        zoneNames.put(0x56580C94EDFCE664L, "Route 3 (Garbage)");
        zoneNames.put(0xCB38FEA3F71C3958L, "Rolling Fields (Flying)");
        zoneNames.put(0x1F174D36062B8C38L, "Rolling Fields (Ground)");
        zoneNames.put(0x23017513039A78E7L, "Rolling Fields (2)");
        zoneNames.put(0xF1BA4AAD9AAB2C1AL, "Watchtower Ruins (Flying)");
        zoneNames.put(0x3D2E746F9D3F5CB5L, "East Lake Axewell (Flying)");
        zoneNames.put(0x6E121A9CE4F58F1EL, "East Lake Axewell (Flying)");
        zoneNames.put(0x3171A0C61793816EL, "South Lake Miloch (Flying)");
        zoneNames.put(0x198E4023A1B2DDEFL, "South Lake Miloch (2)");
        zoneNames.put(0xFAB1C08E70C0F1CAL, "Motostoke Riverbank (Surfing)");
        zoneNames.put(0xB9F76CEE459CEC07L, "Bridge Field (Surfing)");
        zoneNames.put(0x5F4E0AB29FD3F13AL, "Bridge Field (Flying)");
        zoneNames.put(0xF603DEA4177200EAL, "Stony Wilderness (2)");
        zoneNames.put(0x76EE4E28DD28374EL, "Stony Wilderness (Flying)");
        zoneNames.put(0x3F264B6FCB5647B4L, "Giant's Mirror (Flying)");
        zoneNames.put(0x2D887A1CA9B1B99AL, "Dusty Bowl (Flying)");
        zoneNames.put(0x2BE7E6A8901ECC20L, "Giant's Mirror (Ground)");
        zoneNames.put(0x39F0170769BF4524L, "Dusty Bowl and Giant's Mirror (Surfing)");
        zoneNames.put(0xB2067FBCF8D5C7BAL, "Giant's Cap (Ground)");
        zoneNames.put(0x48B9525945EE48B5L, "Stony Wilderness (3)");
        zoneNames.put(0xB5756B87989661E1L, "Giant's Cap (2)");
        zoneNames.put(0x7AB83D18C831DDEBL, "Giant's Cap (3)");
        zoneNames.put(0xDBEF8A8593377AAAL, "Giant's Cap (Lunatone/Solrock)");
        zoneNames.put(0x066F97F8765BC22DL, "Hammerlocke Hills (Flying)");
        zoneNames.put(0x87A97AFF94BC6CF2L, "Lake of Outrage (Surfing)");
        zoneNames.put(0x94289204B628522CL, "Slumbering Weald (Low Level)");
        zoneNames.put(0x5D02F15C043B872EL, "Slumbering Weald (High Level)");
        zoneNames.put(0xA4945486A2B97DFFL, "Route 2 (Surfing)");
        zoneNames.put(0xAC1187E9EC166853L, "Route 9 (in Circhester Bay) (Surfing)");

        // DLC 1 - Isle of Armor
        zoneNames.put(0x908A64718CA374E6L, "Fields of Honor");
        zoneNames.put(0x908A63718CA37333L, "Soothing Wetlands");
        zoneNames.put(0x908A62718CA37180L, "Forest of Focus");
        zoneNames.put(0x908A69718CA37D65L, "Challenge Beach");
        zoneNames.put(0x908A68718CA37BB2L, "Brawlers' Cave");
        zoneNames.put(0x908A67718CA379FFL, "Challenge Road");
        zoneNames.put(0x908A66718CA3784CL, "Courageous Cavern");
        zoneNames.put(0x908A6D718CA38431L, "Loop Lagoon");
        zoneNames.put(0x908A6C718CA3827EL, "Training Lowlands");
        zoneNames.put(0x90875F718CA13690L, "Warm-Up Tunnel");
        zoneNames.put(0x908760718CA13843L, "Potbottom Desert");
        zoneNames.put(0x909170718CA9A7F8L, "Workout Sea");
        zoneNames.put(0x909173718CA9AD11L, "Stepping-Stone Sea");
        zoneNames.put(0x909172718CA9AB5EL, "Insular Sea");
        zoneNames.put(0x909175718CA9B077L, "Honeycalm Sea");
        zoneNames.put(0x908DEC718CA691D5L, "Honeycalm Island");

        zoneNames.put(0x525D03DF0309D804L, "Fields of Honor");
        zoneNames.put(0xB0621052994A5089L, "Fields of Honor (Surfing)");
        zoneNames.put(0x91B1D1436BAF5871L, "Fields of Honor (Beach)");
        zoneNames.put(0xC449DFAB894F632CL, "Loop Lagoon (Beach)");
        zoneNames.put(0x273693DD91D7BD10L, "Challenge Beach (Beach)");
        zoneNames.put(0xD61582D408C39E60L, "Challenge Beach (Surfing - River)");
        zoneNames.put(0xBECC9623CD3E8C77L, "Soothing Wetlands");
        zoneNames.put(0x1C051CB6F97C2068L, "Soothing Wetlands (Puddles)");
        zoneNames.put(0xBC028EF260AD9406L, "Forest of Focus");
        zoneNames.put(0x32AB88FC9797DC83L, "Forest of Focus (Surfing)");
        zoneNames.put(0x39D078468AA0DCC1L, "Challenge Beach");
        zoneNames.put(0x3BFB22D0FB5B42D2L, "Challenge Beach (Surfing - Ocean)");
        zoneNames.put(0x2B1DF6E85F9BAE28L, "Brawlers' Cave");
        zoneNames.put(0x36FE81B956D0DCB5L, "Brawlers' Cave (Surfing)");
        zoneNames.put(0xBBAA199D0705405BL, "Challenge Road");
        zoneNames.put(0xFB9A7FD6D979C6DAL, "Courageous Cavern");
        zoneNames.put(0xBC0E1701C0276FCFL, "Courageous Cavern (Surfing)");
        zoneNames.put(0xAC2ED08E980FCFC5L, "Loop Lagoon");
        zoneNames.put(0x7D2E205E8E300EE1L, "Loop Lagoon (Surfing)");
        zoneNames.put(0x67E3FF10EB64FB79L, "Training Lowlands (Beach)");
        zoneNames.put(0x85E286D82C666BBCL, "Training Lowlands");
        zoneNames.put(0x95E125D2EE3ED656L, "Warm-up Tunnel");
        zoneNames.put(0xA7F495799F209587L, "Potbottom Desert");
        zoneNames.put(0x30AAD92559FCE81EL, "Workout Sea");
        zoneNames.put(0x6F748A46C8E3802CL, "Workout Sea (Surfing)");
        zoneNames.put(0x97A3E0687E3C5B01L, "Stepping-Stone Sea (Surfing)");
        zoneNames.put(0xDDDFF88957FD5B5CL, "Insular Sea");
        zoneNames.put(0xF3036CD294CE9365L, "Stepping-Stone Sea");
        zoneNames.put(0xFB9BB438425D58DAL, "Insular Sea (Surfing)");
        zoneNames.put(0xC16C1E2A1B5FFE87L, "Honeycalm Sea (Surfing)");
        zoneNames.put(0x081D7EF6A1C192B1L, "Honeycalm Island");
        zoneNames.put(0x86EFBF49516B5555L, "Honeycalm Island (Surfing)");
        zoneNames.put(0x39AB700A9F1AB71FL, "Training Lowlands (Surfing)");
        zoneNames.put(0x96C6A2A36131F383L, "Stepping-Stone Sea (Sharpedo)");
        zoneNames.put(0xC92D06352150C78AL, "Insular Sea (Sharpedo)");
        zoneNames.put(0xED1F9772AA35C3CDL, "Workout Sea (Sharpedo)");
        zoneNames.put(0x9C0049D3E6129924L, "Honeycalm Sea (Sharpedo)");

        // DLC 2 - Crown Tundra
        zoneNames.put(0x87E14B7187BC1CC1L, "Slippery Slope");
        zoneNames.put(0x87E1487187BC17A8L, "Freezington");
        zoneNames.put(0x87E1497187BC195BL, "Frostpoint Field");
        zoneNames.put(0x87E14E7187BC21DAL, "Giant's Bed");
        zoneNames.put(0x87E14F7187BC238DL, "Old Cemetery");
        zoneNames.put(0x87E14C7187BC1E74L, "Snowslide Slope");
        zoneNames.put(0x87E14D7187BC2027L, "Tunnel to the Top");
        zoneNames.put(0x87E1427187BC0D76L, "Path to the Peak");
        zoneNames.put(0x87E1437187BC0F29L, "Crown Shrine");
        zoneNames.put(0x87E4507187BE5B17L, "Giant's Foot");
        zoneNames.put(0x87E44F7187BE5964L, "Roaring-Sea Caves");
        zoneNames.put(0x87E4527187BE5E7DL, "Frigid Sea");
        zoneNames.put(0x87E4517187BE5CCAL, "Three-Point Pass");
        zoneNames.put(0x87DA3F7187B5E9AFL, "Ballimere Lake");
        zoneNames.put(0x87DA407187B5EB62L, "Lakeside Cave");
        zoneNames.put(0x87DA417187B5ED15L, "Dyna Tree Hill");

        zoneNames.put(0xD6EA3DE40B009E55L, "Slippery Slope");
        zoneNames.put(0xADF616908BD308DFL, "Frostpoint Field");
        zoneNames.put(0x308C5EB6A846D1F0L, "Giant's Bed");
        zoneNames.put(0x50E781F91B97C049L, "Old Cemetery");
        zoneNames.put(0xC303110BF1EC3322L, "Snowslide Slope");
        zoneNames.put(0xB768660B0BF4C0C3L, "Tunnel to the Top");
        zoneNames.put(0xFCB78AFCCECAF094L, "Path to the Peak");
        zoneNames.put(0xA345459C03EA6673L, "Giant's Foot");
        zoneNames.put(0xE4A982819ACF7292L, "Roaring-Sea Caves");
        zoneNames.put(0x18AAF85178C7B839L, "Frigid Sea");
        zoneNames.put(0x3EC6FCDC0C77D460L, "Three-Point Pass");
        zoneNames.put(0xE5225F9325CCA74BL, "Ballimere Lake");
        zoneNames.put(0x2F1B41507D695958L, "Lakeside Cave");

        zoneNames.put(0xF8A59FCA719D1EAEL, "Giant's Bed / Giant's Foot (Surfing)");
        zoneNames.put(0x55D8F226A42368B7L, "Roaring-Sea Caves (Surfing)");
        zoneNames.put(0x78536116469DC44DL, "Frigid Sea (Surfing)");
        zoneNames.put(0x9BDD6D11FFBEDA3FL, "Ballimere Lake (Surfing)");

        zoneNames.put(0x5C46B1DEE67BD29AL, "Route 4");
        zoneNames.put(0xDE85BF46AE1556E0L, "Route 5");
        zoneNames.put(0x47C72206348B3AEDL, "Route 3");
        zoneNames.put(0xD8EDCE41159D0455L, "Route 2");
        zoneNames.put(0x12421E663E65107DL, "Route 2");
        zoneNames.put(0x24421F03ACB98746L, "Route 2 (High Level)");
        zoneNames.put(0x6F6B85098D378F1BL, "Route 2 (High Level)");
        zoneNames.put(0xF91FDFCF10FCCADBL, "Route 2 (High Level)");
        zoneNames.put(0x3A854984141B8E52L, "Glimwood Tangle");
        zoneNames.put(0x5FCD67E6C2A42CCAL, "Slumbering Weald (High Level)");
        zoneNames.put(0x0B9D02DA8CC54F42L, "Slumbering Weald (High Level)");
        zoneNames.put(0x5FF6CE58D2A39754L, "Galar Mine No. 2");
        zoneNames.put(0x9D9CE333B1019A45L, "Galar Mine No. 2");
        zoneNames.put(0x2891058AE8EBD691L, "Galar Mine");
        zoneNames.put(0xB89E7ABAB607531EL, "Galar Mine");
        zoneNames.put(0xE6E733FFA830D110L, "Route 10");
        zoneNames.put(0xE6E736FFA830D629L, "Route 10");
        zoneNames.put(0xCC02A458BE623642L, "Route 9");
        zoneNames.put(0x56E55D05E7E14A84L, "Route 9 (in Circhester Bay)");
        zoneNames.put(0x81D830EFAED8E2ECL, "Rolling Fields");
        zoneNames.put(0x78C51BEFA996F58AL, "Rolling Fields");
        zoneNames.put(0x78C51AEFA996F3D7L, "Rolling Fields");
        zoneNames.put(0x81D82CEFAED8DC20L, "Rolling Fields");
        zoneNames.put(0x81D82FEFAED8E139L, "Rolling Fields");
        zoneNames.put(0x78C515EFA996EB58L, "Rolling Fields");
        zoneNames.put(0x78C517EFA996EEBEL, "Rolling Fields");
        zoneNames.put(0x78C516EFA996ED0BL, "Rolling Fields");
        zoneNames.put(0x78C51CEFA996F73DL, "Rolling Fields");
        zoneNames.put(0x81D4BFEFAED5ED12L, "Rolling Fields");
        zoneNames.put(0x78CC08EFA99CF3EFL, "Rolling Fields");
        zoneNames.put(0x78CC07EFA99CF23CL, "Rolling Fields");
        zoneNames.put(0x81D82EEFAED8DF86L, "Rolling Fields");
        zoneNames.put(0x78C519EFA996F224L, "Rolling Fields");
        zoneNames.put(0x78C510EFA996E2D9L, "Rolling Fields");
        zoneNames.put(0x78C50FEFA996E126L, "Rolling Fields");
        zoneNames.put(0x81D4C3EFAED5F3DEL, "Rolling Fields");
        zoneNames.put(0x81D831EFAED8E49FL, "Rolling Fields");
        zoneNames.put(0x81B9B3EFAEBF10F6L, "Rolling Fields");
        zoneNames.put(0x81D832EFAED8E652L, "Dappled Grove");
        zoneNames.put(0x81D835EFAED8EB6BL, "Dappled Grove");
        zoneNames.put(0x81D833EFAED8E805L, "Dappled Grove");
        zoneNames.put(0x78C89EEFA99A09FAL, "Dappled Grove");
        zoneNames.put(0x78C89CEFA99A0694L, "Dappled Grove");
        zoneNames.put(0x78C89FEFA99A0BADL, "Dappled Grove");
        zoneNames.put(0x81B9B4EFAEBF12A9L, "Dappled Grove");
        zoneNames.put(0x81D834EFAED8E9B8L, "Dappled Grove");
        zoneNames.put(0x81D4C8EFAED5FC5DL, "Watchtower Ruins");
        zoneNames.put(0x78C89AEFA99A032EL, "Watchtower Ruins");
        zoneNames.put(0x78C89BEFA99A04E1L, "Watchtower Ruins");
        zoneNames.put(0x81D4C7EFAED5FAAAL, "Watchtower Ruins");
        zoneNames.put(0x78C89DEFA99A0847L, "Watchtower Ruins");
        zoneNames.put(0x81B9B1EFAEBF0D90L, "Watchtower Ruins");
        zoneNames.put(0x81CDBEEFAECFCCB1L, "East Lake Axewell");
        zoneNames.put(0x78D30FEFA9A31E82L, "East Lake Axewell");
        zoneNames.put(0x81CDBCEFAECFC94BL, "East Lake Axewell");
        zoneNames.put(0x81CDC0EFAECFD017L, "East Lake Axewell");
        zoneNames.put(0x81D4C1EFAED5F078L, "East Lake Axewell");
        zoneNames.put(0x78CC04EFA99CED23L, "East Lake Axewell");
        zoneNames.put(0x78C897EFA999FE15L, "East Lake Axewell");
        zoneNames.put(0x78CC03EFA99CEB70L, "East Lake Axewell");
        zoneNames.put(0x81CDBDEFAECFCAFEL, "East Lake Axewell");
        zoneNames.put(0x81CDC1EFAECFD1CAL, "East Lake Axewell");
        zoneNames.put(0x81D4C4EFAED5F591L, "East Lake Axewell");
        zoneNames.put(0x78CC05EFA99CEED6L, "East Lake Axewell");
        zoneNames.put(0x81D138EFAED2D1D6L, "East Lake Axewell");
        zoneNames.put(0x81B9B2EFAEBF0F43L, "East Lake Axewell");
        zoneNames.put(0x81D4C6EFAED5F8F7L, "West Lake Axewell");
        zoneNames.put(0x78C899EFA99A017BL, "West Lake Axewell");
        zoneNames.put(0x78C898EFA999FFC8L, "West Lake Axewell");
        zoneNames.put(0x78C896EFA999FC62L, "West Lake Axewell");
        zoneNames.put(0x81D4C5EFAED5F744L, "West Lake Axewell");
        zoneNames.put(0x81CDBBEFAECFC798L, "West Lake Axewell");
        zoneNames.put(0x81CDBFEFAECFCE64L, "West Lake Axewell");
        zoneNames.put(0x81B9BFEFAEBF255AL, "West Lake Axewell");
        zoneNames.put(0x81E5C5EFAEE46A77L, "Axew's Eye");
        zoneNames.put(0x78D30EEFA9A31CCFL, "Axew's Eye");
        zoneNames.put(0x78D30DEFA9A31B1CL, "Axew's Eye");
        zoneNames.put(0x81B9C0EFAEBF270DL, "Axew's Eye");
        zoneNames.put(0x81D141EFAED2E121L, "South Lake Miloch");
        zoneNames.put(0x78CC06EFA99CF089L, "South Lake Miloch");
        zoneNames.put(0x78CC0AEFA99CF755L, "South Lake Miloch");
        zoneNames.put(0x81D13FEFAED2DDBBL, "South Lake Miloch");
        zoneNames.put(0x78CC09EFA99CF5A2L, "South Lake Miloch");
        zoneNames.put(0x78CC0CEFA99CFABBL, "South Lake Miloch");
        zoneNames.put(0x81D140EFAED2DF6EL, "South Lake Miloch");
        zoneNames.put(0x81CDC2EFAECFD37DL, "South Lake Miloch");
        zoneNames.put(0x81CDB3EFAECFBA00L, "South Lake Miloch");
        zoneNames.put(0x81CDB4EFAECFBBB3L, "South Lake Miloch");
        zoneNames.put(0x81BCB4EFAEC14880L, "South Lake Miloch");
        zoneNames.put(0x78AA0CEFA9801455L, "South Lake Miloch");
        zoneNames.put(0x81D4C0EFAED5EEC5L, "South Lake Miloch");
        zoneNames.put(0x78C212EFA994B068L, "South Lake Miloch");
        zoneNames.put(0x78D310EFA9A32035L, "South Lake Miloch");
        zoneNames.put(0x81D13EEFAED2DC08L, "Giant's Seat");
        zoneNames.put(0x81D142EFAED2E2D4L, "Giant's Seat");
        zoneNames.put(0x81D139EFAED2D389L, "Giant's Seat");
        zoneNames.put(0x78CF93EFA9A015F7L, "Giant's Seat");
        zoneNames.put(0x78CF92EFA9A01444L, "Giant's Seat");
        zoneNames.put(0x81D143EFAED2E487L, "Giant's Seat");
        zoneNames.put(0x78CF8DEFA9A00BC5L, "Giant's Seat");
        zoneNames.put(0x78CF87EFA9A00193L, "Giant's Seat");
        zoneNames.put(0x78CF86EFA99FFFE0L, "Giant's Seat");
        zoneNames.put(0x78CF88EFA9A00346L, "Giant's Seat");
        zoneNames.put(0x78CF89EFA9A004F9L, "Giant's Seat");
        zoneNames.put(0x78C213EFA994B21BL, "Giant's Seat");
        zoneNames.put(0x81D144EFAED2E63AL, "North Lake Miloch");
        zoneNames.put(0x78CF8AEFA9A006ACL, "North Lake Miloch");
        zoneNames.put(0x78CC0BEFA99CF908L, "North Lake Miloch");
        zoneNames.put(0x78CF8BEFA9A0085FL, "North Lake Miloch");
        zoneNames.put(0x78CF8CEFA9A00A12L, "North Lake Miloch");
        zoneNames.put(0x81D4C2EFAED5F22BL, "North Lake Miloch");
        zoneNames.put(0x81D145EFAED2E7EDL, "North Lake Miloch");
        zoneNames.put(0x78C214EFA994B3CEL, "North Lake Miloch");
        zoneNames.put(0x81E5C7EFAEE46DDDL, "Motostoke Riverbank");
        zoneNames.put(0x78D30BEFA9A317B6L, "Motostoke Riverbank");
        zoneNames.put(0x81E5C1EFAEE463ABL, "Motostoke Riverbank");
        zoneNames.put(0x78D30AEFA9A31603L, "Motostoke Riverbank");
        zoneNames.put(0x81E5C6EFAEE46C2AL, "Motostoke Riverbank");
        zoneNames.put(0x81E5C0EFAEE461F8L, "Motostoke Riverbank");
        zoneNames.put(0x81E5C4EFAEE468C4L, "Motostoke Riverbank");
        zoneNames.put(0x78D30CEFA9A31969L, "Motostoke Riverbank");
        zoneNames.put(0x81E5C3EFAEE46711L, "Motostoke Riverbank");
        zoneNames.put(0x78D309EFA9A31450L, "Motostoke Riverbank");
        zoneNames.put(0x81E5C2EFAEE4655EL, "Motostoke Riverbank");
        zoneNames.put(0x78C215EFA994B581L, "Motostoke Riverbank");
        zoneNames.put(0x81E23DEFAEE14D88L, "Bridge Field");
        zoneNames.put(0x78D61BEFA9A568BDL, "Bridge Field");
        zoneNames.put(0x81E241EFAEE15454L, "Bridge Field");
        zoneNames.put(0x81E5BCEFAEE45B2CL, "Bridge Field");
        zoneNames.put(0x78D317EFA9A32C1AL, "Bridge Field");
        zoneNames.put(0x78D616EFA9A5603EL, "Bridge Field");
        zoneNames.put(0x78D617EFA9A561F1L, "Bridge Field");
        zoneNames.put(0x81E23EEFAEE14F3BL, "Bridge Field");
        zoneNames.put(0x78D618EFA9A563A4L, "Bridge Field");
        zoneNames.put(0x81E5BDEFAEE45CDFL, "Bridge Field");
        zoneNames.put(0x81E23FEFAEE150EEL, "Bridge Field");
        zoneNames.put(0x78D318EFA9A32DCDL, "Bridge Field");
        zoneNames.put(0x81E243EFAEE157BAL, "Bridge Field");
        zoneNames.put(0x78D61AEFA9A5670AL, "Bridge Field");
        zoneNames.put(0x78D60FEFA9A55459L, "Bridge Field");
        zoneNames.put(0x78D60EEFA9A552A6L, "Bridge Field");
        zoneNames.put(0x78D99CEFA9A879C7L, "Bridge Field");
        zoneNames.put(0x81E240EFAEE152A1L, "Bridge Field");
        zoneNames.put(0x78D614EFA9A55CD8L, "Bridge Field");
        zoneNames.put(0x78D615EFA9A55E8BL, "Bridge Field");
        zoneNames.put(0x81E244EFAEE1596DL, "Bridge Field");
        zoneNames.put(0x78D619EFA9A56557L, "Bridge Field");
        zoneNames.put(0x81E242EFAEE15607L, "Bridge Field");
        zoneNames.put(0x78C216EFA994B734L, "Bridge Field");
        zoneNames.put(0x81E238EFAEE14509L, "Stony Wilderness");
        zoneNames.put(0x81DEB8EFAEDE35B2L, "Stony Wilderness");
        zoneNames.put(0x78D99BEFA9A87814L, "Stony Wilderness");
        zoneNames.put(0x81DEB9EFAEDE3765L, "Stony Wilderness");
        zoneNames.put(0x81E237EFAEE14356L, "Stony Wilderness");
        zoneNames.put(0x81DEB5EFAEDE3099L, "Stony Wilderness");
        zoneNames.put(0x78D99EEFA9A87D2DL, "Stony Wilderness");
        zoneNames.put(0x81DEB7EFAEDE33FFL, "Stony Wilderness");
        zoneNames.put(0x81DEB6EFAEDE324CL, "Stony Wilderness");
        zoneNames.put(0x78C217EFA994B8E7L, "Stony Wilderness");
        zoneNames.put(0x81DEC1EFAEDE44FDL, "Dusty Bowl");
        zoneNames.put(0x78D994EFA9A86C2FL, "Dusty Bowl");
        zoneNames.put(0x81DBB4EFAEDBF90FL, "Dusty Bowl");
        zoneNames.put(0x78D993EFA9A86A7CL, "Dusty Bowl");
        zoneNames.put(0x81DBB5EFAEDBFAC2L, "Dusty Bowl");
        zoneNames.put(0x78A682EFA97CF400L, "Dusty Bowl");
        zoneNames.put(0x81DEB2EFAEDE2B80L, "Dusty Bowl");
        zoneNames.put(0x78D999EFA9A874AEL, "Dusty Bowl");
        zoneNames.put(0x81DBB3EFAEDBF75CL, "Dusty Bowl");
        zoneNames.put(0x81DBB6EFAEDBFC75L, "Dusty Bowl");
        zoneNames.put(0x81DEB4EFAEDE2EE6L, "Dusty Bowl");
        zoneNames.put(0x78D998EFA9A872FBL, "Dusty Bowl");
        zoneNames.put(0x78D99DEFA9A87B7AL, "Dusty Bowl");
        zoneNames.put(0x78D997EFA9A87148L, "Dusty Bowl");
        zoneNames.put(0x78D99AEFA9A87661L, "Dusty Bowl");
        zoneNames.put(0x81DEB3EFAEDE2D33L, "Dusty Bowl");
        zoneNames.put(0x78C218EFA994BA9AL, "Dusty Bowl");
        zoneNames.put(0x81DEC0EFAEDE434AL, "Dusty Bowl");
        zoneNames.put(0x81DBAFEFAEDBF090L, "Giant's Mirror");
        zoneNames.put(0x81DBB0EFAEDBF243L, "Giant's Mirror");
        zoneNames.put(0x78A683EFA97CF5B3L, "Giant's Mirror");
        zoneNames.put(0x78A684EFA97CF766L, "Giant's Mirror");
        zoneNames.put(0x81DBB2EFAEDBF5A9L, "Giant's Mirror");
        zoneNames.put(0x78A686EFA97CFACCL, "Giant's Mirror");
        zoneNames.put(0x81DBB1EFAEDBF3F6L, "Giant's Mirror");
        zoneNames.put(0x78A685EFA97CF919L, "Giant's Mirror");
        zoneNames.put(0x78C219EFA994BC4DL, "Giant's Mirror");
        zoneNames.put(0x81BCC3EFAEC161FDL, "Hammerlocke Hills");
        zoneNames.put(0x78AA07EFA9800BD6L, "Hammerlocke Hills");
        zoneNames.put(0x81DBBBEFAEDC04F4L, "Hammerlocke Hills");
        zoneNames.put(0x81BCC2EFAEC1604AL, "Hammerlocke Hills");
        zoneNames.put(0x78AA06EFA9800A23L, "Hammerlocke Hills");
        zoneNames.put(0x78AA05EFA9800870L, "Hammerlocke Hills");
        zoneNames.put(0x81BCC0EFAEC15CE4L, "Hammerlocke Hills");
        zoneNames.put(0x81BCB5EFAEC14A33L, "Hammerlocke Hills");
        zoneNames.put(0x78C20AEFA994A2D0L, "Hammerlocke Hills");
        zoneNames.put(0x81BCBCEFAEC15618L, "Giant's Cap");
        zoneNames.put(0x78A68BEFA97D034BL, "Giant's Cap");
        zoneNames.put(0x78A689EFA97CFFE5L, "Giant's Cap");
        zoneNames.put(0x78A68AEFA97D0198L, "Giant's Cap");
        zoneNames.put(0x81BCBDEFAEC157CBL, "Giant's Cap");
        zoneNames.put(0x81BCBFEFAEC15B31L, "Giant's Cap");
        zoneNames.put(0x81BCBEEFAEC1597EL, "Giant's Cap");
        zoneNames.put(0x81DBBCEFAEDC06A7L, "Giant's Cap");
        zoneNames.put(0x78A687EFA97CFC7FL, "Giant's Cap");
        zoneNames.put(0x78A688EFA97CFE32L, "Giant's Cap");
        zoneNames.put(0x81BCC1EFAEC15E97L, "Giant's Cap");
        zoneNames.put(0x78AA08EFA9800D89L, "Giant's Cap");
        zoneNames.put(0x78C20BEFA994A483L, "Giant's Cap");
        zoneNames.put(0x81B9B8EFAEBF1975L, "Lake of Outrage");
        zoneNames.put(0x78AA10EFA9801B21L, "Lake of Outrage");
        zoneNames.put(0x78AA0AEFA98010EFL, "Lake of Outrage");
        zoneNames.put(0x78AA09EFA9800F3CL, "Lake of Outrage");
        zoneNames.put(0x78AA0BEFA98012A2L, "Lake of Outrage");
        zoneNames.put(0x81B9B5EFAEBF145CL, "Lake of Outrage");
        zoneNames.put(0x939802EFB90574F6L, "Lake of Outrage");
        zoneNames.put(0x78AA0FEFA980196EL, "Lake of Outrage");
        zoneNames.put(0x939803EFB90576A9L, "Lake of Outrage");
        zoneNames.put(0x939800EFB9057190L, "Lake of Outrage");
        zoneNames.put(0x939801EFB9057343L, "Lake of Outrage");
        zoneNames.put(0x939807EFB9057D75L, "Lake of Outrage");
        zoneNames.put(0x939806EFB9057BC2L, "Lake of Outrage");
        zoneNames.put(0x81B9B7EFAEBF17C2L, "Lake of Outrage");
        zoneNames.put(0x78C518EFA996F071L, "Lake of Outrage");
        zoneNames.put(0x244F30A98851012DL, "Route 8");
        zoneNames.put(0xAA34F5FBE82AD5B0L, "Route 8");
        zoneNames.put(0x178A982BCB2C2847L, "Fields of Honor");
        zoneNames.put(0x178A992BCB2C29FAL, "Fields of Honor");
        zoneNames.put(0x1787132BCB291071L, "Fields of Honor");
        zoneNames.put(0x178A932BCB2C1FC8L, "Fields of Honor");
        zoneNames.put(0x178A912BCB2C1C62L, "Fields of Honor");
        zoneNames.put(0x1791882BCB322BC5L, "Fields of Honor");
        zoneNames.put(0x178E002BCB2F0ED6L, "Fields of Honor");
        zoneNames.put(0x178A952BCB2C232EL, "Fields of Honor");
        zoneNames.put(0x17870C2BCB29048CL, "Fields of Honor");
        zoneNames.put(0x178A962BCB2C24E1L, "Fields of Honor");
        zoneNames.put(0x178A942BCB2C217BL, "Fields of Honor");
        zoneNames.put(0x1787142BCB291224L, "Fields of Honor");
        zoneNames.put(0x178A972BCB2C2694L, "Fields of Honor");
        zoneNames.put(0x1791872BCB322A12L, "Fields of Honor");
        zoneNames.put(0x17918E2BCB3235F7L, "Fields of Honor");
        zoneNames.put(0x596BD708CEAFF8CCL, "Soothing Wetlands");
        zoneNames.put(0x1787152BCB2913D7L, "Soothing Wetlands");
        zoneNames.put(0x1787102BCB290B58L, "Soothing Wetlands");
        zoneNames.put(0x1787172BCB29173DL, "Soothing Wetlands");
        zoneNames.put(0x1787122BCB290EBEL, "Soothing Wetlands");
        zoneNames.put(0x178DFF2BCB2F0D23L, "Soothing Wetlands");
        zoneNames.put(0x1791842BCB3224F9L, "Soothing Wetlands");
        zoneNames.put(0x178A922BCB2C1E15L, "Soothing Wetlands");
        zoneNames.put(0x1787162BCB29158AL, "Soothing Wetlands");
        zoneNames.put(0x17918D2BCB323444L, "Soothing Wetlands");
        zoneNames.put(0x1791852BCB3226ACL, "Soothing Wetlands");
        zoneNames.put(0x178E012BCB2F1089L, "Soothing Wetlands");
        zoneNames.put(0x1791862BCB32285FL, "Soothing Wetlands");
        zoneNames.put(0x178DFE2BCB2F0B70L, "Soothing Wetlands");
        zoneNames.put(0x17870D2BCB29063FL, "Soothing Wetlands");
        zoneNames.put(0x178E042BCB2F15A2L, "Forest of Focus");
        zoneNames.put(0x177D0A2BCB20ACA1L, "Forest of Focus");
        zoneNames.put(0x178E022BCB2F123CL, "Forest of Focus");
        zoneNames.put(0x17797F2BCB1D8A99L, "Forest of Focus");
        zoneNames.put(0x178E092BCB2F1E21L, "Forest of Focus");
        zoneNames.put(0x178E082BCB2F1C6EL, "Forest of Focus");
        zoneNames.put(0x1791832BCB322346L, "Forest of Focus");
        zoneNames.put(0x1779852BCB1D94CBL, "Forest of Focus");
        zoneNames.put(0x17797C2BCB1D8580L, "Forest of Focus");
        zoneNames.put(0x177D052BCB20A422L, "Forest of Focus");
        zoneNames.put(0x177D022BCB209F09L, "Forest of Focus");
        zoneNames.put(0x177D032BCB20A0BCL, "Forest of Focus");
        zoneNames.put(0x177D042BCB20A26FL, "Forest of Focus");
        zoneNames.put(0x177CFF2BCB2099F0L, "Forest of Focus");
        zoneNames.put(0x178E032BCB2F13EFL, "Forest of Focus");
        zoneNames.put(0x17797D2BCB1D8733L, "Forest of Focus");
        zoneNames.put(0x177D092BCB20AAEEL, "Forest of Focus");
        zoneNames.put(0x1791812BCB321FE0L, "Forest of Focus");
        zoneNames.put(0x177D062BCB20A5D5L, "Forest of Focus");
        zoneNames.put(0x1779802BCB1D8C4CL, "Forest of Focus");
        zoneNames.put(0x17797E2BCB1D88E6L, "Forest of Focus");
        zoneNames.put(0x178E052BCB2F1755L, "Forest of Focus");
        zoneNames.put(0x177D012BCB209D56L, "Forest of Focus");
        zoneNames.put(0x177D002BCB209BA3L, "Forest of Focus");
        zoneNames.put(0x0E8F6A2BC5FEA24BL, "Forest of Focus");
        zoneNames.put(0x05CAC12BC0FF8DBEL, "Forest of Focus");
        zoneNames.put(0x0E92D82BC601930CL, "Forest of Focus");
        zoneNames.put(0x0E8F672BC5FE9D32L, "Forest of Focus");
        zoneNames.put(0x17840F2BCB26D3CEL, "Challenge Beach");
        zoneNames.put(0x1784122BCB26D8E7L, "Challenge Beach");
        zoneNames.put(0x1784102BCB26D581L, "Challenge Beach");
        zoneNames.put(0x1780872BCB23B6DFL, "Challenge Beach");
        zoneNames.put(0x17A60A2BCB43B1B5L, "Challenge Beach");
        zoneNames.put(0x1780882BCB23B892L, "Challenge Beach");
        zoneNames.put(0x1784132BCB26DA9AL, "Challenge Beach");
        zoneNames.put(0x1780902BCB23C62AL, "Challenge Beach");
        zoneNames.put(0x1784112BCB26D734L, "Challenge Beach");
        zoneNames.put(0x1784062BCB26C483L, "Challenge Beach");
        zoneNames.put(0x1780862BCB23B52CL, "Challenge Beach");
        zoneNames.put(0x1780832BCB23B013L, "Challenge Beach");
        zoneNames.put(0x1780852BCB23B379L, "Challenge Beach");
        zoneNames.put(0x1784052BCB26C2D0L, "Challenge Beach");
        zoneNames.put(0x1780822BCB23AE60L, "Challenge Beach");
        zoneNames.put(0x17A6092BCB43B002L, "Challenge Beach");
        zoneNames.put(0x17A6082BCB43AE4FL, "Challenge Beach");
        zoneNames.put(0x1780912BCB23C7DDL, "Challenge Beach");
        zoneNames.put(0x17A6072BCB43AC9CL, "Challenge Beach");
        zoneNames.put(0x17A6052BCB43A936L, "Challenge Beach");
        zoneNames.put(0x1780892BCB23BA45L, "Challenge Beach");
        zoneNames.put(0x1779812BCB1D8DFFL, "Challenge Beach");
        zoneNames.put(0x17840D2BCB26D068L, "Challenge Beach");
        zoneNames.put(0x1779842BCB1D9318L, "Challenge Beach");
        zoneNames.put(0x17A6042BCB43A783L, "Challenge Beach");
        zoneNames.put(0x17A6122BCB43BF4DL, "Challenge Beach");
        zoneNames.put(0x17A6032BCB43A5D0L, "Challenge Beach");
        zoneNames.put(0x0E8F682BC5FE9EE5L, "Challenge Beach");
        zoneNames.put(0x0E92D42BC6018C40L, "Challenge Beach");
        zoneNames.put(0x0E92D52BC6018DF3L, "Challenge Beach");
        zoneNames.put(0x0E92D62BC6018FA6L, "Challenge Beach");
        zoneNames.put(0x0E92D92BC60194BFL, "Challenge Beach");
        zoneNames.put(0x0E92D72BC6019159L, "Challenge Beach");
        zoneNames.put(0x0E92DA2BC6019672L, "Challenge Beach");
        zoneNames.put(0x17A6112BCB43BD9AL, "Brawlers' Cave");
        zoneNames.put(0x17A2842BCB40982CL, "Brawlers' Cave");
        zoneNames.put(0x17A2852BCB4099DFL, "Brawlers' Cave");
        zoneNames.put(0x17A2862BCB409B92L, "Brawlers' Cave");
        zoneNames.put(0x17A2872BCB409D45L, "Brawlers' Cave");
        zoneNames.put(0x17A2802BCB409160L, "Brawlers' Cave");
        zoneNames.put(0x17A2812BCB409313L, "Challenge Road");
        zoneNames.put(0x17A2822BCB4094C6L, "Challenge Road");
        zoneNames.put(0x17A2832BCB409679L, "Challenge Road");
        zoneNames.put(0x17A28D2BCB40A777L, "Challenge Road");
        zoneNames.put(0x0E745F2BC5E7C7E2L, "Challenge Road");
        zoneNames.put(0x0E74602BC5E7C995L, "Challenge Road");
        zoneNames.put(0x0E745D2BC5E7C47CL, "Challenge Road");
        zoneNames.put(0x0E745E2BC5E7C62FL, "Challenge Road");
        zoneNames.put(0x0E745C2BC5E7C2C9L, "Challenge Road");
        zoneNames.put(0x0E745B2BC5E7C116L, "Challenge Road");
        zoneNames.put(0x0E74592BC5E7BDB0L, "Challenge Road");
        zoneNames.put(0x0E74672BC5E7D57AL, "Challenge Road");
        zoneNames.put(0x0E77E52BC5EAE16BL, "Challenge Road");
        zoneNames.put(0x0E77E72BC5EAE4D1L, "Challenge Road");
        zoneNames.put(0x0E74682BC5E7D72DL, "Challenge Road");
        zoneNames.put(0x17A28C2BCB40A5C4L, "Challenge Road");
        zoneNames.put(0x0E745A2BC5E7BF63L, "Challenge Road");
        zoneNames.put(0x1780842BCB23B1C6L, "Challenge Road");
        zoneNames.put(0x0E77E42BC5EADFB8L, "Challenge Road");
        zoneNames.put(0x0E77E62BC5EAE31EL, "Courageous Cavern");
        zoneNames.put(0x05CABB2BC0FF838CL, "Courageous Cavern");
        zoneNames.put(0x0E77E82BC5EAE684L, "Courageous Cavern");
        zoneNames.put(0x0E77EB2BC5EAEB9DL, "Courageous Cavern");
        zoneNames.put(0x0E77EA2BC5EAE9EAL, "Courageous Cavern");
        zoneNames.put(0x0E6DD62BC5E27369L, "Loop Lagoon");
        zoneNames.put(0x0E77DC2BC5EAD220L, "Loop Lagoon");
        zoneNames.put(0x0E6DD52BC5E271B6L, "Loop Lagoon");
        zoneNames.put(0x0E6DD32BC5E26E50L, "Loop Lagoon");
        zoneNames.put(0x0E6DD82BC5E276CFL, "Loop Lagoon");
        zoneNames.put(0x0E6DD92BC5E27882L, "Loop Lagoon");
        zoneNames.put(0x0E6DDA2BC5E27A35L, "Loop Lagoon");
        zoneNames.put(0x0E6DDB2BC5E27BE8L, "Loop Lagoon");
        zoneNames.put(0x0E6DDC2BC5E27D9BL, "Loop Lagoon");
        zoneNames.put(0x0E70D92BC5E4AE59L, "Loop Lagoon");
        zoneNames.put(0x0E70D82BC5E4ACA6L, "Loop Lagoon");
        zoneNames.put(0x0E70D62BC5E4A940L, "Loop Lagoon");
        zoneNames.put(0x0E70D72BC5E4AAF3L, "Loop Lagoon");
        zoneNames.put(0x0E70DD2BC5E4B525L, "Loop Lagoon");
        zoneNames.put(0x0E70DC2BC5E4B372L, "Loop Lagoon");
        zoneNames.put(0x0E70DB2BC5E4B1BFL, "Loop Lagoon");
        zoneNames.put(0x0E70DA2BC5E4B00CL, "Training Lowlands");
        zoneNames.put(0x0E70E12BC5E4BBF1L, "Training Lowlands");
        zoneNames.put(0x0E81D52BC5F31AC0L, "Training Lowlands");
        zoneNames.put(0x0E70E02BC5E4BA3EL, "Training Lowlands");
        zoneNames.put(0x0E85652BC5F64547L, "Training Lowlands");
        zoneNames.put(0x0E7B6E2BC5EE000DL, "Training Lowlands");
        zoneNames.put(0x0E81D82BC5F31FD9L, "Training Lowlands");
        zoneNames.put(0x0E81D62BC5F31C73L, "Training Lowlands");
        zoneNames.put(0x0E81DA2BC5F3233FL, "Training Lowlands");
        zoneNames.put(0x0E81DF2BC5F32BBEL, "Training Lowlands");
        zoneNames.put(0x0E81DC2BC5F326A5L, "Training Lowlands");
        zoneNames.put(0x0E81DB2BC5F324F2L, "Training Lowlands");
        zoneNames.put(0x0E81D92BC5F3218CL, "Training Lowlands");
        zoneNames.put(0x0E855D2BC5F637AFL, "Training Lowlands");
        zoneNames.put(0x0E855F2BC5F63B15L, "Training Lowlands");
        zoneNames.put(0x0E855E2BC5F63962L, "Training Lowlands");
        zoneNames.put(0x0E85642BC5F64394L, "Training Lowlands");
        zoneNames.put(0x0E85582BC5F62F30L, "Training Lowlands");
        zoneNames.put(0x0E81E02BC5F32D71L, "Training Lowlands");
        zoneNames.put(0x0E7B692BC5EDF78EL, "Training Lowlands");
        zoneNames.put(0x0E7B682BC5EDF5DBL, "Training Lowlands");
        zoneNames.put(0x0E7B6D2BC5EDFE5AL, "Training Lowlands");
        zoneNames.put(0x0E855B2BC5F63449L, "Training Lowlands");
        zoneNames.put(0x0E7B672BC5EDF428L, "Training Lowlands");
        zoneNames.put(0x0E855A2BC5F63296L, "Training Lowlands");
        zoneNames.put(0x0E81D72BC5F31E26L, "Training Lowlands");
        zoneNames.put(0x0E85592BC5F630E3L, "Training Lowlands");
        zoneNames.put(0x0E7B6B2BC5EDFAF4L, "Training Lowlands");
        zoneNames.put(0x0E7B6C2BC5EDFCA7L, "Training Lowlands");
        zoneNames.put(0x0E855C2BC5F635FCL, "Training Lowlands");
        zoneNames.put(0x0E7B632BC5EDED5CL, "Potbottom Desert");
        zoneNames.put(0x0E7B6A2BC5EDF941L, "Potbottom Desert");
        zoneNames.put(0x0E7B642BC5EDEF0FL, "Potbottom Desert");
        zoneNames.put(0x0E7E712BC5F03AFDL, "Potbottom Desert");
        zoneNames.put(0x0E8F692BC5FEA098L, "Potbottom Desert");
        zoneNames.put(0x0E7E702BC5F0394AL, "Potbottom Desert");
        zoneNames.put(0x0E7E6F2BC5F03797L, "Potbottom Desert");
        zoneNames.put(0x0E7E6E2BC5F035E4L, "Potbottom Desert");
        zoneNames.put(0x0E7E6B2BC5F030CBL, "Potbottom Desert");
        zoneNames.put(0x0E7E6D2BC5F03431L, "Potbottom Desert");
        zoneNames.put(0x0E7E6C2BC5F0327EL, "Potbottom Desert");
        zoneNames.put(0x0E7E6A2BC5F02F18L, "Workout Sea");
        zoneNames.put(0x0E7E692BC5F02D65L, "Workout Sea");
        zoneNames.put(0x05CAC52BC0FF948AL, "Workout Sea");
        zoneNames.put(0x05CAC62BC0FF963DL, "Workout Sea");
        zoneNames.put(0x05CAC32BC0FF9124L, "Workout Sea");
        zoneNames.put(0x05CAC42BC0FF92D7L, "Workout Sea");
        zoneNames.put(0x0E92DC2BC60199D8L, "Workout Sea");
        zoneNames.put(0x0E92DD2BC6019B8BL, "Workout Sea");
        zoneNames.put(0x0E8F6F2BC5FEAACAL, "Insular Sea");
        zoneNames.put(0x0E8F702BC5FEAC7DL, "Insular Sea");
        zoneNames.put(0x0E8F6D2BC5FEA764L, "Insular Sea");
        zoneNames.put(0x0E8F6E2BC5FEA917L, "Insular Sea");
        zoneNames.put(0x05CABC2BC0FF853FL, "Insular Sea");
        zoneNames.put(0x05CABF2BC0FF8A58L, "Honeycalm Sea");
        zoneNames.put(0x05CAC02BC0FF8C0BL, "Honeycalm Sea");
        zoneNames.put(0x0E8F6B2BC5FEA3FEL, "Honeycalm Island");
        zoneNames.put(0x0E8F6C2BC5FEA5B1L, "Honeycalm Island");
        zoneNames.put(0xA0FFFB5BFC2B229DL, "Slippery Slope");
        zoneNames.put(0xA0FFF45BFC2B16B8L, "Slippery Slope");
        zoneNames.put(0xA103685BFC2E11ABL, "Slippery Slope");
        zoneNames.put(0xA0FFF85BFC2B1D84L, "Slippery Slope");
        zoneNames.put(0xA0FFF35BFC2B1505L, "Slippery Slope");
        zoneNames.put(0xA1035F5BFC2E0260L, "Slippery Slope");
        zoneNames.put(0xA0F8EE5BFC24EDD8L, "Slippery Slope");
        zoneNames.put(0xA0FFF75BFC2B1BD1L, "Slippery Slope");
        zoneNames.put(0xA0FFF55BFC2B186BL, "Slippery Slope");
        zoneNames.put(0xA0FFF25BFC2B1352L, "Slippery Slope");
        zoneNames.put(0xA103635BFC2E092CL, "Slippery Slope");
        zoneNames.put(0xA0FFF65BFC2B1A1EL, "Slippery Slope");
        zoneNames.put(0xA103655BFC2E0C92L, "Slippery Slope");
        zoneNames.put(0xA103675BFC2E0FF8L, "Slippery Slope");
        zoneNames.put(0xAA089E5C0163F9C4L, "Slippery Slope");
        zoneNames.put(0xA103605BFC2E0413L, "Slippery Slope");
        zoneNames.put(0xA103625BFC2E0779L, "Slippery Slope");
        zoneNames.put(0xA103645BFC2E0ADFL, "Slippery Slope");
        zoneNames.put(0xAA089F5C0163FB77L, "Slippery Slope");
        zoneNames.put(0xAA08955C0163EA79L, "Slippery Slope");
        zoneNames.put(0xAA05185C0160E03BL, "Slippery Slope");
        zoneNames.put(0xAA08945C0163E8C6L, "Frostpoint Field");
        zoneNames.put(0xAA05175C0160DE88L, "Frostpoint Field");
        zoneNames.put(0xA103665BFC2E0E45L, "Frostpoint Field");
        zoneNames.put(0xA0FFF95BFC2B1F37L, "Frostpoint Field");
        zoneNames.put(0xAA05195C0160E1EEL, "Frostpoint Field");
        zoneNames.put(0xA103615BFC2E05C6L, "Frostpoint Field");
        zoneNames.put(0x58098D686DF1879AL, "Giant's Bed");
        zoneNames.put(0xA0F8EF5BFC24EF8BL, "Giant's Bed");
        zoneNames.put(0xA0F8F25BFC24F4A4L, "Giant's Bed");
        zoneNames.put(0xA0F8F35BFC24F657L, "Giant's Bed");
        zoneNames.put(0xA0F8F15BFC24F2F1L, "Giant's Bed");
        zoneNames.put(0xA0FC745BFC280761L, "Giant's Bed");
        zoneNames.put(0xA0FC6B5BFC27F816L, "Giant's Bed");
        zoneNames.put(0xA10D6A5BFC366996L, "Giant's Bed");
        zoneNames.put(0xA0FC765BFC280AC7L, "Giant's Bed");
        zoneNames.put(0xA10D735BFC3678E1L, "Giant's Bed");
        zoneNames.put(0xA0FC735BFC2805AEL, "Giant's Bed");
        zoneNames.put(0xA0FC715BFC280248L, "Giant's Bed");
        zoneNames.put(0xA10D705BFC3673C8L, "Giant's Bed");
        zoneNames.put(0xA110F75BFC398F04L, "Giant's Bed");
        zoneNames.put(0xA0F8F45BFC24F80AL, "Giant's Bed");
        zoneNames.put(0xA10D765BFC367DFAL, "Giant's Bed");
        zoneNames.put(0xA10D775BFC367FADL, "Giant's Bed");
        zoneNames.put(0xA10D755BFC367C47L, "Giant's Bed");
        zoneNames.put(0xA0F8F55BFC24F9BDL, "Giant's Bed");
        zoneNames.put(0xA0FC725BFC2803FBL, "Giant's Bed");
        zoneNames.put(0xA10D6B5BFC366B49L, "Giant's Bed");
        zoneNames.put(0xA0F8E65BFC24E040L, "Giant's Bed");
        zoneNames.put(0xA0F8E75BFC24E1F3L, "Giant's Bed");
        zoneNames.put(0xA0F8F05BFC24F13EL, "Giant's Bed");
        zoneNames.put(0xA10D745BFC367A94L, "Giant's Bed");
        zoneNames.put(0xA10D715BFC36757BL, "Giant's Bed");
        zoneNames.put(0xA0FC775BFC280C7AL, "Giant's Bed");
        zoneNames.put(0xA0FC755BFC280914L, "Giant's Bed");
        zoneNames.put(0xA10D725BFC36772EL, "Giant's Bed");
        zoneNames.put(0xA0FC785BFC280E2DL, "Giant's Bed");
        zoneNames.put(0xA0FC6C5BFC27F9C9L, "Giant's Bed");
        zoneNames.put(0xA110F85BFC3990B7L, "Giant's Bed");
        zoneNames.put(0xAA199D5C01726B44L, "Giant's Bed");
        zoneNames.put(0xAA199E5C01726CF7L, "Giant's Bed");
        zoneNames.put(0xAA19935C01725A46L, "Giant's Bed");
        zoneNames.put(0xAA051A5C0160E3A1L, "Giant's Bed");
        zoneNames.put(0xA110FA5BFC39941DL, "Old Cemetery");
        zoneNames.put(0xA110F95BFC39926AL, "Old Cemetery");
        zoneNames.put(0xAA08A05C0163FD2AL, "Old Cemetery");
        zoneNames.put(0xA110F45BFC3989EBL, "Snowslide Slope");
        zoneNames.put(0xA110F35BFC398838L, "Snowslide Slope");
        zoneNames.put(0xA110F55BFC398B9EL, "Snowslide Slope");
        zoneNames.put(0xA106E45BFC311A36L, "Snowslide Slope");
        zoneNames.put(0xA106E95BFC3122B5L, "Snowslide Slope");
        zoneNames.put(0xA106E35BFC311883L, "Snowslide Slope");
        zoneNames.put(0xA106EE5BFC312B34L, "Snowslide Slope");
        zoneNames.put(0xA109E85BFC3356D9L, "Snowslide Slope");
        zoneNames.put(0xA110F65BFC398D51L, "Snowslide Slope");
        zoneNames.put(0xA106E75BFC311F4FL, "Snowslide Slope");
        zoneNames.put(0xA109EC5BFC335DA5L, "Snowslide Slope");
        zoneNames.put(0xA109E95BFC33588CL, "Snowslide Slope");
        zoneNames.put(0xA106E85BFC312102L, "Snowslide Slope");
        zoneNames.put(0xA106E55BFC311BE9L, "Snowslide Slope");
        zoneNames.put(0xA110F05BFC39831FL, "Snowslide Slope");
        zoneNames.put(0xA109EA5BFC335A3FL, "Snowslide Slope");
        zoneNames.put(0xA110EF5BFC39816CL, "Snowslide Slope");
        zoneNames.put(0xA106E25BFC3116D0L, "Snowslide Slope");
        zoneNames.put(0xA106EF5BFC312CE7L, "Snowslide Slope");
        zoneNames.put(0xA109EB5BFC335BF2L, "Snowslide Slope");
        zoneNames.put(0xA106E65BFC311D9CL, "Snowslide Slope");
        zoneNames.put(0xA109E75BFC335526L, "Tunnel to the Top");
        zoneNames.put(0xA109E55BFC3351C0L, "Tunnel to the Top");
        zoneNames.put(0xA109F45BFC336B3DL, "Tunnel to the Top");
        zoneNames.put(0xA109F35BFC33698AL, "Tunnel to the Top");
        zoneNames.put(0xA0E4EA5BFC143E02L, "Tunnel to the Top");
        zoneNames.put(0xA0E4EB5BFC143FB5L, "Tunnel to the Top");
        zoneNames.put(0xA109E65BFC335373L, "Tunnel to the Top");
        zoneNames.put(0xA0E4E85BFC143A9CL, "Path to the Peak");
        zoneNames.put(0xA0E4E95BFC143C4FL, "Path to the Peak");
        zoneNames.put(0xA0E4E65BFC143736L, "Giant's Foot");
        zoneNames.put(0xA0E4E75BFC1438E9L, "Giant's Foot");
        zoneNames.put(0xA0E4F35BFC144D4DL, "Giant's Foot");
        zoneNames.put(0xA0E4F25BFC144B9AL, "Giant's Foot");
        zoneNames.put(0xA0E4E45BFC1433D0L, "Giant's Foot");
        zoneNames.put(0xA0E7F05BFC167E0BL, "Giant's Foot");
        zoneNames.put(0xA0E7EF5BFC167C58L, "Giant's Foot");
        zoneNames.put(0xA0E7F15BFC167FBEL, "Giant's Foot");
        zoneNames.put(0xA0E4E55BFC143583L, "Giant's Foot");
        zoneNames.put(0xAA089D5C0163F811L, "Giant's Foot");
        zoneNames.put(0xAA19945C01725BF9L, "Giant's Foot");
        zoneNames.put(0xA0E7E85BFC167073L, "Roaring-Sea Caves");
        zoneNames.put(0xA0E7F55BFC16868AL, "Roaring-Sea Caves");
        zoneNames.put(0xA0E7F65BFC16883DL, "Roaring-Sea Caves");
        zoneNames.put(0xAA12915C016C3832L, "Roaring-Sea Caves");
        zoneNames.put(0xAA12905C016C367FL, "Roaring-Sea Caves");
        zoneNames.put(0x41A2BE472A662FB9L, "Frigid Sea");
        zoneNames.put(0xAA16145C016F4CA2L, "Frigid Sea");
        zoneNames.put(0xA0E7E75BFC166EC0L, "Frigid Sea");
        zoneNames.put(0xAA16135C016F4AEFL, "Frigid Sea");
        zoneNames.put(0xA0E7F25BFC168171L, "Frigid Sea");
        zoneNames.put(0xA0E7F45BFC1684D7L, "Frigid Sea");
        zoneNames.put(0xAA16125C016F493CL, "Frigid Sea");
        zoneNames.put(0xAA160F5C016F4423L, "Frigid Sea");
        zoneNames.put(0xAA16155C016F4E55L, "Frigid Sea");
        zoneNames.put(0xAA16115C016F4789L, "Frigid Sea");
        zoneNames.put(0xAA16105C016F45D6L, "Frigid Sea");
        zoneNames.put(0xAA160E5C016F4270L, "Frigid Sea");
        zoneNames.put(0xAA161D5C016F5BEDL, "Three-Point Pass");
        zoneNames.put(0xAA128F5C016C34CCL, "Three-Point Pass");
        zoneNames.put(0x51E02AA2144333CDL, "Ballimere Lake");
        zoneNames.put(0x75C70155BAC59C9FL, "Ballimere Lake");
        zoneNames.put(0xAA12925C016C39E5L, "Ballimere Lake");
        zoneNames.put(0xAA128B5C016C2E00L, "Ballimere Lake");
        zoneNames.put(0xAA128C5C016C2FB3L, "Ballimere Lake");
        zoneNames.put(0xAA128D5C016C3166L, "Ballimere Lake");
        zoneNames.put(0xAA128E5C016C3319L, "Ballimere Lake");
        zoneNames.put(0xAA12975C016C4264L, "Ballimere Lake");
        zoneNames.put(0xAA1D205C01757FB4L, "Ballimere Lake");
        zoneNames.put(0xAA1D235C017584CDL, "Ballimere Lake");
        zoneNames.put(0xAA1D225C0175831AL, "Ballimere Lake");
        zoneNames.put(0xAA12985C016C4417L, "Ballimere Lake");
        zoneNames.put(0xAA1D215C01758167L, "Ballimere Lake");
        zoneNames.put(0xAA1D1D5C01757A9BL, "Ballimere Lake");
        zoneNames.put(0xAA1D1C5C017578E8L, "Ballimere Lake");
        zoneNames.put(0xAA1D1F5C01757E01L, "Ballimere Lake");
        zoneNames.put(0xAA1D1E5C01757C4EL, "Ballimere Lake");
        zoneNames.put(0xAA1D195C017573CFL, "Ballimere Lake");
        zoneNames.put(0xAA199C5C01726991L, "Ballimere Lake");
        zoneNames.put(0xAA19A05C0172705DL, "Ballimere Lake");
        zoneNames.put(0xAA1D185C0175721CL, "Ballimere Lake");
        zoneNames.put(0xAA199B5C017267DEL, "Ballimere Lake");
        zoneNames.put(0xAA089C5C0163F65EL, "Ballimere Lake");
        zoneNames.put(0xAA089B5C0163F4ABL, "Ballimere Lake");
        zoneNames.put(0xAA089A5C0163F2F8L, "Ballimere Lake");
        zoneNames.put(0xAA08A15C0163FEDDL, "Ballimere Lake");
        zoneNames.put(0x7E538435D82A9219L, "Lakeside Cave");
        zoneNames.put(0xAA19995C01726478L, "Lakeside Cave");
        zoneNames.put(0xAA199A5C0172662BL, "Lakeside Cave");
        zoneNames.put(0xAA199F5C01726EAAL, "Lakeside Cave");

        return zoneNames;
    }

    public static final List<String> encounterTypes = Arrays.asList(
            "Normal Weather",
            "Overcast",
            "Raining",
            "Thunderstorm",
            "Intense Sun",
            "Snowing",
            "Snowstorm",
            "Sandstorm",
            "Heavy Fog",
            "Shaking Trees",
            "Fishing"
    );

    public static final List<String> areasWithWanderers = Arrays.asList("a_0301.bin", "a_0201.bin", "a_0101.bin",
            "a_d0401.bin", "a_d0101.bin", "a_d0302.bin", "a_d0201.bin", "a_1201.bin", "a_0901.bin", "a_wr0101.bin",
            "a_0802.bin", "a_wr0201.bin", "a_wr0301.bin");

    public static void tagTrainers(List<Trainer> trs) {

        // Gym Trainers
        tag(trs,"GYM1", 29, 30, 31);
        tag(trs,"GYM2",45, 46, 47);
        tag(trs,"GYM3",161, 162, 163);
        tag(trs,"GYM4", 62, 63, 64, 65, 66, 67);
        tag(trs,"GYM5", 113, 114, 115);
        tag(trs,"GYM6", 79, 80, 81, 82, 83, 84, 85, 86);
        tag(trs,"GYM7", 101, 102, 103, 104, 105, 106);
        tag(trs,"GYM8", 150, 151, 152);

        // Gym Leaders
        tag(trs,"GYM1-LEADER", 32, 218, 258, 288, 289, 363, 365, 387, 413);     // Milo
        tag(trs,"GYM2-LEADER",36, 210, 219, 259, 345, 388);                     // Nessa
        tag(trs,"GYM3-LEADER",37, 220, 260, 344, 389);                          // Kabu
        tag(trs,"GYM4-LEADER", 77, 78, 211, 212, 261, 262, 347, 349, 390, 391); // Bea and Allister
        tag(trs,"GYM5-LEADER", 108, 364, 134, 231, 269, 341, 397);              // Opal and Bede
        tag(trs,"GYM6-LEADER", 135, 136, 264, 265, 357, 358, 392, 393);         // Gordie and Melony
        tag(trs,"GYM7-LEADER", 107, 266, 346, 394, 268, 342, 396);              // Piers and Marnie
        tag(trs,"GYM8-LEADER", 144, 213, 267, 348, 395);                        // Raihan
        tag(trs,"GYM9-LEADER", 359, 375);                                       // Avery
        tag(trs,"GYM10-LEADER", 360, 377);                                      // Klara

        tag(trs,"RIVAL2-0", 149);
        tag(trs,"RIVAL2-1", 189);
        tag(trs,"RIVAL2-2", 190);
        tagRival(trs,"RIVAL3",249);
        tag(trs,"RIVAL4-X",343);
        tagRival(trs,"RIVAL5",348);
        tagRival(trs,"RIVAL6",378);

        tagRival(trs,"FRIEND1", 4);
        tagRival(trs,"FRIEND2", 7);
        tagRival(trs,"FRIEND3", 197);
        tagRival(trs,"FRIEND4", 191);
        tagRival(trs,"FRIEND5", 121);
        tagRival(trs,"FRIEND6", 156);
        tagRival(trs,"FRIEND7", 124);
        tagRival(trs,"FRIEND8", 127);
        tagRival(trs,"FRIEND9", 202);
        tagRival(trs,"FRIEND10", 130);
        tagRival(trs,"FRIEND11", 312);
        tagRival(trs,"FRIEND12", 153);
        tagRival(trs,"FRIEND13", 214);
        tagRival(trs,"FRIEND14", 228);
        tagRival(trs,"FRIEND15", 225);
        tag(trs,"FRIEND16-0", 234, 235);
        tag(trs,"FRIEND16-1", 236, 237);
        tag(trs,"FRIEND16-2", 238, 239);
        tag(trs,"FRIEND17-0", 252, 253);
        tag(trs,"FRIEND17-1", 254, 255);
        tag(trs,"FRIEND17-2", 256, 257);
        tag(trs,"FRIEND18-0", 351, 352);
        tag(trs,"FRIEND18-1", 353, 354);
        tag(trs,"FRIEND18-2", 355, 356);
        tag(trs,"FRIEND19-0", 381, 382);
        tag(trs,"FRIEND19-1", 383, 384);
        tag(trs,"FRIEND19-2", 385, 386);

        tag(trs,"THEMED:BEDE-STRONG", 133, 195, 240);
        tag(trs,"THEMED:MARNIE-STRONG", 138, 145, 196, 248, 431);
        tag(trs,"THEMED:OLEANA-STRONG", 143);
        tag(trs,"THEMED:SORDWARD-STRONG", 221, 223, 232, 362);
        tag(trs,"THEMED:SHIELBERT-STRONG", 222, 224, 233, 350);
        tag(trs,"THEMED:AVERY-STRONG", 315, 317, 319, 374, 414, 417, 419);
        tag(trs,"THEMED:KLARA-STRONG", 316, 318, 320, 376, 415, 418, 420);
        tag(trs,"THEMED:MUSTARD-STRONG", 312, 322, 323, 328, 329, 339, 340, 429, 430, 432, 433);
        tag(trs,"THEMED:HONEY-STRONG", 324, 325, 326, 327);
        tag(trs,"THEMED:PEONY-STRONG", 330, 361, 434);
        tag(trs,"THEMED:ERIC-STRONG", 285, 286, 287);
        tag(trs,"THEMED:ROSE-LEADER", 175);
    }

    private static void tagRival(List<Trainer> allTrainers, String tag, int offset) {
        allTrainers.get(offset - 1).tag = tag + "-0";
        allTrainers.get(offset).tag = tag + "-1";
        allTrainers.get(offset + 1).tag = tag + "-2";

    }

    private static void tag(List<Trainer> allTrainers, String tag, int... numbers) {
        for (int num : numbers) {
            if (allTrainers.size() > (num - 1)) {
                allTrainers.get(num - 1).tag = tag;
            }
        }
    }

    public static void setMultiBattleStatus(List<Trainer> trs) {
        // All Double Battles in Gen 8 are internally treated as a Multi Battle
        // 38 & 39: Interviewers Gillian and Cam (Route 5)
        // 52 & 53: Music Crew Owen and Andrea
        // 60 & 61: Daring Couple Robert and Jacqueline
        // 74 & 75: Colleagues Jordan and Alison
        // 99 & 100: Interviewers Gillian and Cam (Route 10)
        // 105 & 106: Team Yell Grunt & Gym Trainer Joshua
        // 140 & 309: Macro Cosmos's Mateo & Jane
        // 141 & 310: Macro Cosmos's Kevin & Carla
        // 142 & 311: Macro Cosmos's Adalyn & Justin
        // 159 & 160: Team Yell Grunts in Galar Mine No. 2
        // 185 & 186: Medical Team Iwan and Evelyn
        // 223 & 224: Sordward & Shielbert
        setMultiBattleStatus(trs, 38, 39, 52, 53, 60, 61, 74, 75, 99, 100, 105, 106, 140, 141, 142, 159, 160,
                185, 186, 223, 224, 309, 310, 311
        );
    }

    private static void setMultiBattleStatus(List<Trainer> allTrainers, int... numbers) {
        for (int num : numbers) {
            if (allTrainers.size() > (num - 1)) {
                allTrainers.get(num - 1).multiBattleStatus = Trainer.MultiBattleStatus.ALWAYS;
            }
        }
    }

    public static void setForcedRivalStarterPositions(List<Trainer> allTrainers) {

        // Hop 16
        allTrainers.get(234 - 1).forceStarterPosition = 4;
        allTrainers.get(235 - 1).forceStarterPosition = 4;
        allTrainers.get(236 - 1).forceStarterPosition = 4;
        allTrainers.get(237 - 1).forceStarterPosition = 4;
        allTrainers.get(238 - 1).forceStarterPosition = 4;
        allTrainers.get(239 - 1).forceStarterPosition = 4;

        // Hop 17
        allTrainers.get(252 - 1).forceStarterPosition = 2;
        allTrainers.get(253 - 1).forceStarterPosition = 2;
        allTrainers.get(254 - 1).forceStarterPosition = 2;
        allTrainers.get(255 - 1).forceStarterPosition = 2;
        allTrainers.get(256 - 1).forceStarterPosition = 2;
        allTrainers.get(257 - 1).forceStarterPosition = 2;

        // Hop 19
        allTrainers.get(381 - 1).forceStarterPosition = 2;
        allTrainers.get(382 - 1).forceStarterPosition = 2;
        allTrainers.get(383 - 1).forceStarterPosition = 2;
        allTrainers.get(384 - 1).forceStarterPosition = 2;
        allTrainers.get(385 - 1).forceStarterPosition = 2;
        allTrainers.get(386 - 1).forceStarterPosition = 2;

        // Leon 1
        allTrainers.get(149 - 1).forceStarterPosition = 4;
        allTrainers.get(189 - 1).forceStarterPosition = 4;
        allTrainers.get(190 - 1).forceStarterPosition = 4;

        // Leon 2
        allTrainers.get(249 - 1).forceStarterPosition = 3;
        allTrainers.get(250 - 1).forceStarterPosition = 3;
        allTrainers.get(251 - 1).forceStarterPosition = 3;

        // Leon 3
        allTrainers.get(378 - 1).forceStarterPosition = 3;
        allTrainers.get(379 - 1).forceStarterPosition = 3;
        allTrainers.get(380 - 1).forceStarterPosition = 3;
    }

    public static final long noItemHash = 0xCBF29CE484222645L;

    public static ItemList allowedItems, nonBadItems;
    public static List<Integer> regularShopItems, opShopItems;
    public static final List<Integer> bannedItems = Arrays.asList(Items.expCandyM, Items.expCandyL,
            Items.expCandyXL);

    static {
        setupAllowedItems();
    }

    private static void setupAllowedItems() {
        allowedItems = new ItemList(Items.reinsofUnity);
        // Key items + version exclusives
        allowedItems.banRange(Items.explorerKit, 57);
        allowedItems.banRange(Items.dataCard01, 32);
        allowedItems.banRange(Items.xtransceiverMale, 7);
        allowedItems.banSingles(Items.expShare, Items.libertyPass, Items.propCase, Items.dragonSkull,
                Items.lightStone, Items.darkStone);
        // Unknown blank items or version exclusives
        allowedItems.banRange(Items.tea, 3);
        allowedItems.banRange(Items.unused120, 14);

        // Items that were removed in Sw/Sh
        allowedItems.banSingles(Items.sacredAsh);
        allowedItems.banRange(Items.fluffyTail, 12);
        allowedItems.banSingles(Items.heartScale);
        allowedItems.banRange(Items.growthMulch, 11);
        allowedItems.banSingles(Items.oddKeystone);
        allowedItems.banRange(Items.mail1, 12);
        allowedItems.banRange(Items.razzBerry, 5);
        allowedItems.banRange(Items.cornnBerry, 9);
        allowedItems.banSingles(Items.expShare);
        allowedItems.banRange(Items.deepSeaTooth, 2);
        allowedItems.banSingles(Items.luckyPunch);
        allowedItems.banRange(Items.redScarf, 5);
        allowedItems.banSingles(Items.razorFang);
        allowedItems.banRange(Items.photoAlbum, 3);
        allowedItems.banRange(Items.fireGem, 17);
        allowedItems.banRange(Items.coverFossil, 4);
        allowedItems.banRange(Items.pokeToy, 3);
        allowedItems.banRange(Items.relicCopper, 7);
        allowedItems.banRange(Items.direHit2, 26);
        allowedItems.banSingles(Items.permit);
        allowedItems.banRange(Items.plasmaCard, 5);
        allowedItems.banRange(Items.pokeFlute, 35);
        allowedItems.banSingles(Items.tm100);
        allowedItems.banRange(Items.redNectar, 4);

        // TMs & HMs - tms cant be held in gen7
        allowedItems.tmRange(Items.tm01, 92);
        allowedItems.tmRange(Items.tm93, 3);
        allowedItems.banRange(Items.tm01, 100);
        allowedItems.banRange(Items.tm93, 3);
        // Battle Launcher exclusives
        allowedItems.banRange(Items.direHit2, 24);


        // Key items (Gen 6)
        allowedItems.banRange(Items.holoCasterMale,3);
        allowedItems.banSingles(Items.sprinklotad);
        allowedItems.banRange(Items.powerPlantPass,13);
        allowedItems.banRange(Items.elevatorKey,4);
        allowedItems.banRange(Items.lensCase,3);
        allowedItems.banRange(Items.lookerTicket,3);
        allowedItems.banRange(Items.megaCharm,2);

        // TMs (Gen 6)
        allowedItems.tmRange(Items.tm96,5);
        allowedItems.banRange(Items.tm96,5);

        // Gen 6/7 key items, Z-Crystals
        allowedItems.banRange(Items.jawFossil,85);
        allowedItems.banRange(Items.zRing, 49);

        // Key Items (Gen 7)
        allowedItems.banSingles(Items.zRing, Items.sparklingStone, Items.zygardeCube, Items.ridePager,
                Items.sunFlute, Items.moonFlute, Items.enigmaticCard);
        allowedItems.banRange(Items.forageBag,3);

        // Unused
        allowedItems.banSingles(Items.unused848, Items.unused859);
        allowedItems.banRange(Items.unused837,4);
        allowedItems.banRange(Items.silverRazzBerry,18);
        allowedItems.banRange(Items.stretchySpring,18);

        // Z-Crystals
        allowedItems.banRange(Items.solganiumZBag,12);

        // Key Items
        allowedItems.banRange(Items.zPowerRing,16);

        // ROTO LOTO
        allowedItems.banRange(Items.rotoHatch,11);

        // Candy, unused items
        allowedItems.banRange(Items.healthCandy, 114);

        // Gen 8 Key items, unused items
        allowedItems.banRange(Items.endorsement, 10);
        allowedItems.banRange(Items.campingGear, 3);
        allowedItems.banSingles(Items.hiTechEarbuds);
        allowedItems.banRange(Items.wishingChip, 14);
        allowedItems.banRange(Items.styleCard, 5);
        allowedItems.banRange(Items.markCharm, 3);
        allowedItems.banRange(Items.legendaryClue1, 11);
        allowedItems.banSingles(Items.carrotSeeds, Items.reinsofUnity);

        // TRs and TM 00
        allowedItems.tmRange(Items.tm00, 1);
        allowedItems.banSingles(Items.tm00);

        // non-bad items
        // ban specific pokemon hold items, berries, apricorns, mail
        nonBadItems = allowedItems.copy();

        nonBadItems.banSingles(Items.oddKeystone, Items.griseousOrb, Items.soulDew, Items.lightBall,
                Items.oranBerry, Items.quickPowder, Items.passOrb, Items.discountCoupon, Items.strangeSouvenir,
                Items.festivalTicket, Items.galaricaTwig);
        nonBadItems.banRange(Items.growthMulch, 4); // mulch
        nonBadItems.banRange(Items.adamantOrb, 2); // orbs
        nonBadItems.banRange(Items.mail1, 12); // mails
        nonBadItems.banRange(Items.figyBerry, 25); // berries without useful battle effects
        nonBadItems.banRange(Items.luckyPunch, 4); // pokemon specific
        nonBadItems.banRange(Items.redScarf, 5); // contest scarves
        nonBadItems.banRange(Items.richMulch,4); // more mulch
        nonBadItems.banRange(Items.gengarite, 30); // Mega Stones, part 1
        nonBadItems.banRange(Items.swampertite, 13); // Mega Stones, part 2
        nonBadItems.banRange(Items.cameruptite, 4); // Mega Stones, part 3
        nonBadItems.banRange(Items.fightingMemory,17); // Memories
        nonBadItems.banRange(Items.relicCopper,7); // relic items
        nonBadItems.banSingles(Items.shoalSalt, Items.shoalShell); // Shoal items; have no purpose and sell for $10.
        nonBadItems.banRange(Items.blueFlute, 5); // Flutes; have no purpose and sell for $10.
        nonBadItems.banRange(Items.starAnd458, 300); // Dynamax crystals
        nonBadItems.banRange(Items.redApricorn, 7); // Apricorns
        nonBadItems.banRange(Items.sausages, 16); // Curry items
        nonBadItems.banRange(Items.fruitBunch, 9); // Curry items

        regularShopItems = new ArrayList<>();

        regularShopItems.addAll(IntStream.rangeClosed(Items.ultraBall, Items.pokeBall).boxed().collect(Collectors.toList()));
        regularShopItems.addAll(IntStream.rangeClosed(Items.potion, Items.revive).boxed().collect(Collectors.toList()));
        regularShopItems.addAll(IntStream.rangeClosed(Items.superRepel, Items.repel).boxed().collect(Collectors.toList()));
        regularShopItems.add(Items.pokeDoll);

        opShopItems = new ArrayList<>();

        // "Money items" etc
        opShopItems.add(Items.lavaCookie);
        opShopItems.add(Items.berryJuice);
        opShopItems.add(Items.rareCandy);
        opShopItems.add(Items.oldGateau);
        opShopItems.addAll(IntStream.rangeClosed(Items.tinyMushroom, Items.nugget).boxed().collect(Collectors.toList()));
        opShopItems.add(Items.rareBone);
        opShopItems.addAll(IntStream.rangeClosed(Items.lansatBerry, Items.rowapBerry).boxed().collect(Collectors.toList()));
        opShopItems.add(Items.luckyEgg);
        opShopItems.add(Items.prettyFeather);
        opShopItems.addAll(IntStream.rangeClosed(Items.balmMushroom, Items.casteliacone).boxed().collect(Collectors.toList()));
    }

    public static final List<Integer> requiredFieldTMs = Arrays.asList(
            26, 51, 64, 22, 58, 84, 95, 81, 39, 75, 73, 65, 98, 93, 53, 49, 29, 30, 15, 74, 57, 69, 56, 24, 43, 96, 21, 2, 54, 37, 7, 11, 97,
            31, 82, 86
    );

    public static final List<Integer> evolutionItems = Arrays.asList(Items.sunStone, Items.moonStone, Items.fireStone,
            Items.thunderStone, Items.waterStone, Items.leafStone, Items.shinyStone, Items.duskStone, Items.dawnStone,
            Items.ovalStone, Items.kingsRock, Items.deepSeaTooth, Items.deepSeaScale, Items.metalCoat, Items.dragonScale,
            Items.upgrade, Items.protector, Items.electirizer, Items.magmarizer, Items.dubiousDisc, Items.reaperCloth,
            Items.razorClaw, Items.razorFang, Items.prismScale, Items.whippedDream, Items.sachet, Items.iceStone);

    public static final List<List<Integer>> duplicateEvolutionItems = setupDuplicateEvolutionItems();

    private static List<List<Integer>> setupDuplicateEvolutionItems() {
        List<List<Integer>> duplicateEvolutionItems = new ArrayList<>();

        List<Integer> sweetsList = Arrays.asList(Items.strawberrySweet, Items.loveSweet, Items.berrySweet,
                Items.cloverSweet, Items.flowerSweet, Items.starSweet, Items.ribbonSweet);
        List<Integer> potsList = Arrays.asList(Items.crackedPot, Items.chippedPot);

        duplicateEvolutionItems.add(sweetsList);
        duplicateEvolutionItems.add(potsList);

        return duplicateEvolutionItems;
    }
}
