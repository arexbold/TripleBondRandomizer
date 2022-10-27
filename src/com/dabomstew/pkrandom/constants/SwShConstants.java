package com.dabomstew.pkrandom.constants;

import com.dabomstew.pkrandom.pokemon.ItemList;
import com.dabomstew.pkrandom.pokemon.MoveCategory;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.Type;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            bsFormeCountOffset = 32, bsPresentFlag = 33, bsTMCompatOffset = 40, bsSpecialMTCompatOffset = 56,
            bsTRCompatOffset = 60, bsMTCompatOffset = 168;

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

    public static final String expSharePrefix = "1FD67F0200F9FD7B42A9F44F41A9F50743F8C0035FD6";

    public static final String perfectOddsBranchLocator = "0B010054E803003268";

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
        allowedItems.banRange(Items.fireGem, 16);
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
        opShopItems.add(Items.armoriteOre);
        opShopItems.add(Items.dyniteOre);
        opShopItems.add(Items.expCandyXS);
        opShopItems.add(Items.expCandyS);
    }

    public static final List<Integer> requiredFieldTMs = Arrays.asList(
            26, 51, 64, 22, 58, 84, 95, 81, 39, 75, 73, 65, 98, 93, 53, 49, 29, 30, 15, 74, 57, 69, 56, 24, 43, 96, 21, 2, 54, 37, 7, 11, 97,
            31, 82, 86
    );

    public static final List<Integer> evolutionItems = Arrays.asList(Items.sunStone, Items.moonStone, Items.fireStone,
            Items.thunderStone, Items.waterStone, Items.leafStone, Items.shinyStone, Items.duskStone, Items.dawnStone,
            Items.ovalStone, Items.kingsRock, Items.metalCoat, Items.dragonScale, Items.upgrade, Items.protector,
            Items.electirizer, Items.magmarizer, Items.dubiousDisc, Items.reaperCloth, Items.razorClaw,
            Items.prismScale, Items.whippedDream, Items.sachet, Items.iceStone, Items.sweetApple,
            Items.tartApple, Items.galaricaCuff, Items.galaricaWreath);

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

    public static String getShopName(long hash) {
        return shopNames.get(hash);
    }

    private static final Map<Long,String> shopNames = setupShopNames();

    private static Map<Long,String> setupShopNames() {
        Map<Long,String> map = new HashMap<>();
        map.put(0xCBD67969D873539BL, "Motostoke [Lower Tier, Miscellaneous]");
        map.put(0xCBD67869D87351E8L, "Hammerlocke [South, Miscellaneous]");
        map.put(0xCBD67B69D8735701L, "Wyndon [South, Miscellaneous]");
        map.put(0x04D7046DA09D3C78L, "Hulbury [Herb Shop]");
        map.put(0x4B2F9E98DDCB0707L, "Hulbury [Incense Shop]");
        map.put(0xE379CDF67A297070L, "Wedgehurst [Berry Shop]");
        return map;
    }
    public static final Map<Integer,Integer> balancedItemPrices = Stream.of(new Integer[][] {
            // Skip item index 0.
            {Items.masterBall, 3000},
            {Items.safariBall, 500},
            {Items.luxuryBall, 1000},
            {Items.premierBall, 200},
            {Items.cherishBall, 200},
            {Items.ether, 3000},
            {Items.maxEther, 4500},
            {Items.elixir, 15000},
            {Items.maxElixir, 18000},
            {Items.ppMax, 25000},
            {Items.griseousOrb, 10000},
            {Items.douseDrive, 1000},
            {Items.shockDrive, 1000},
            {Items.burnDrive, 1000},
            {Items.chillDrive, 1000},
            {Items.sweetHeart, 150},
            {Items.adamantOrb, 10000},
            {Items.lustrousOrb, 10000},
            {Items.cheriBerry, 200},
            {Items.chestoBerry, 200},
            {Items.pechaBerry, 200},
            {Items.rawstBerry, 200},
            {Items.aspearBerry, 200},
            {Items.leppaBerry, 3000},
            {Items.oranBerry, 50},
            {Items.persimBerry, 200},
            {Items.lumBerry, 500},
            {Items.sitrusBerry, 500},
            {Items.figyBerry, 100},
            {Items.wikiBerry, 100},
            {Items.magoBerry, 100},
            {Items.aguavBerry, 100},
            {Items.iapapaBerry, 100},
            {Items.pomegBerry, 500},
            {Items.kelpsyBerry, 500},
            {Items.qualotBerry, 500},
            {Items.hondewBerry, 500},
            {Items.grepaBerry, 500},
            {Items.tamatoBerry, 500},
            {Items.occaBerry, 1000},
            {Items.passhoBerry, 1000},
            {Items.wacanBerry, 1000},
            {Items.rindoBerry, 1000},
            {Items.yacheBerry, 1000},
            {Items.chopleBerry, 1000},
            {Items.kebiaBerry, 1000},
            {Items.shucaBerry, 1000},
            {Items.cobaBerry, 1000},
            {Items.payapaBerry, 1000},
            {Items.tangaBerry, 1000},
            {Items.chartiBerry, 1000},
            {Items.kasibBerry, 1000},
            {Items.habanBerry, 1000},
            {Items.colburBerry, 1000},
            {Items.babiriBerry, 1000},
            {Items.chilanBerry, 1000},
            {Items.liechiBerry, 1000},
            {Items.ganlonBerry, 1000},
            {Items.salacBerry, 1000},
            {Items.petayaBerry, 1000},
            {Items.apicotBerry, 1000},
            {Items.lansatBerry, 1000},
            {Items.starfBerry, 1000},
            {Items.enigmaBerry, 1000},
            {Items.micleBerry, 1000},
            {Items.custapBerry, 1000},
            {Items.jabocaBerry, 1000},
            {Items.rowapBerry, 1000},
            {Items.sootheBell, 1000},
            {Items.mentalHerb, 1000},
            {Items.choiceBand, 10000},
            {Items.silverPowder, 2000},
            {Items.amuletCoin, 15000},
            {Items.cleanseTag, 1000},
            {Items.soulDew, 200},
            {Items.metalCoat, 3000},
            {Items.leftovers, 10000},
            {Items.dragonScale, 3000},
            {Items.lightBall, 1000},
            {Items.softSand, 2000},
            {Items.hardStone, 2000},
            {Items.miracleSeed, 2000},
            {Items.blackGlasses, 2000},
            {Items.blackBelt, 2000},
            {Items.magnet, 2000},
            {Items.mysticWater, 2000},
            {Items.sharpBeak, 2000},
            {Items.poisonBarb, 2000},
            {Items.neverMeltIce, 2000},
            {Items.spellTag, 2000},
            {Items.twistedSpoon, 2000},
            {Items.charcoal, 2000},
            {Items.dragonFang, 2000},
            {Items.silkScarf, 2000},
            {Items.upgrade, 3000},
            {Items.shellBell, 6000},
            {Items.laxIncense, 3000},
            {Items.wideLens, 1500},
            {Items.muscleBand, 2000},
            {Items.wiseGlasses, 2000},
            {Items.expertBelt, 6000},
            {Items.lightClay, 1500},
            {Items.lifeOrb, 10000},
            {Items.powerHerb, 1000},
            {Items.toxicOrb, 1500},
            {Items.flameOrb, 1500},
            {Items.focusSash, 2000},
            {Items.zoomLens, 1500},
            {Items.metronome, 3000},
            {Items.ironBall, 1000},
            {Items.laggingTail, 1000},
            {Items.destinyKnot, 1500},
            {Items.blackSludge, 5000},
            {Items.icyRock, 200},
            {Items.smoothRock, 200},
            {Items.heatRock, 200},
            {Items.dampRock, 200},
            {Items.gripClaw, 1500},
            {Items.choiceScarf, 10000},
            {Items.stickyBarb, 1500},
            {Items.shedShell, 500},
            {Items.bigRoot, 1500},
            {Items.choiceSpecs, 10000},
            {Items.flamePlate, 2000},
            {Items.splashPlate, 2000},
            {Items.zapPlate, 2000},
            {Items.meadowPlate, 2000},
            {Items.iciclePlate, 2000},
            {Items.fistPlate, 2000},
            {Items.toxicPlate, 2000},
            {Items.earthPlate, 2000},
            {Items.skyPlate, 2000},
            {Items.mindPlate, 2000},
            {Items.insectPlate, 2000},
            {Items.stonePlate, 2000},
            {Items.spookyPlate, 2000},
            {Items.dracoPlate, 2000},
            {Items.dreadPlate, 2000},
            {Items.ironPlate, 2000},
            {Items.fullIncense, 1000},
            {Items.luckIncense, 15000},
            {Items.pureIncense, 1000},
            {Items.protector, 3000},
            {Items.electirizer, 3000},
            {Items.magmarizer, 3000},
            {Items.dubiousDisc, 3000},
            {Items.reaperCloth, 3000},
            {Items.razorClaw, 5000},
            {Items.tm01, 10000},
            {Items.tm02, 10000},
            {Items.tm03, 10000},
            {Items.tm04, 10000},
            {Items.tm05, 10000},
            {Items.tm06, 10000},
            {Items.tm07, 20000},
            {Items.tm08, 10000},
            {Items.tm09, 10000},
            {Items.tm10, 10000},
            {Items.tm11, 20000},
            {Items.tm12, 10000},
            {Items.tm13, 10000},
            {Items.tm14, 20000},
            {Items.tm15, 20000},
            {Items.tm16, 10000},
            {Items.tm17, 10000},
            {Items.tm18, 20000},
            {Items.tm19, 10000},
            {Items.tm20, 10000},
            {Items.tm21, 10000},
            {Items.tm22, 10000},
            {Items.tm23, 10000},
            {Items.tm24, 10000},
            {Items.tm25, 20000},
            {Items.tm26, 10000},
            {Items.tm27, 10000},
            {Items.tm28, 20000},
            {Items.tm29, 10000},
            {Items.tm30, 10000},
            {Items.tm31, 10000},
            {Items.tm32, 10000},
            {Items.tm33, 10000},
            {Items.tm34, 10000},
            {Items.tm35, 10000},
            {Items.tm36, 10000},
            {Items.tm37, 20000},
            {Items.tm38, 20000},
            {Items.tm39, 10000},
            {Items.tm40, 10000},
            {Items.tm41, 10000},
            {Items.tm42, 10000},
            {Items.tm43, 10000},
            {Items.tm44, 10000},
            {Items.tm45, 10000},
            {Items.tm46, 10000},
            {Items.tm47, 10000},
            {Items.tm48, 10000},
            {Items.tm49, 10000},
            {Items.tm50, 20000},
            {Items.tm51, 10000},
            {Items.tm52, 20000},
            {Items.tm53, 10000},
            {Items.tm54, 10000},
            {Items.tm55, 10000},
            {Items.tm56, 10000},
            {Items.tm57, 10000},
            {Items.tm58, 10000},
            {Items.tm59, 20000},
            {Items.tm60, 10000},
            {Items.tm61, 10000},
            {Items.tm62, 10000},
            {Items.tm63, 10000},
            {Items.tm64, 10000},
            {Items.tm65, 10000},
            {Items.tm66, 10000},
            {Items.tm67, 10000},
            {Items.tm68, 20000},
            {Items.tm69, 10000},
            {Items.tm70, 20000},
            {Items.tm71, 20000},
            {Items.tm72, 10000},
            {Items.tm73, 5000},
            {Items.tm74, 10000},
            {Items.tm75, 10000},
            {Items.tm76, 10000},
            {Items.tm77, 10000},
            {Items.tm78, 10000},
            {Items.tm79, 10000},
            {Items.tm80, 10000},
            {Items.tm81, 10000},
            {Items.tm82, 10000},
            {Items.tm83, 10000},
            {Items.tm84, 10000},
            {Items.tm85, 10000},
            {Items.tm86, 10000},
            {Items.tm87, 10000},
            {Items.tm88, 10000},
            {Items.tm89, 10000},
            {Items.tm90, 10000},
            {Items.tm91, 10000},
            {Items.tm92, 10000},
            {Items.fastBall, 300},
            {Items.levelBall, 300},
            {Items.lureBall, 300},
            {Items.heavyBall, 300},
            {Items.loveBall, 300},
            {Items.friendBall, 300},
            {Items.moonBall, 300},
            {Items.sportBall, 300},
            {Items.prismScale, 3000},
            {Items.eviolite, 10000},
            {Items.floatStone, 1000},
            {Items.rockyHelmet, 6000},
            {Items.airBalloon, 1000},
            {Items.redCard, 1000},
            {Items.ringTarget, 1000},
            {Items.bindingBand, 2000},
            {Items.absorbBulb, 1000},
            {Items.cellBattery, 1000},
            {Items.ejectButton, 1000},
            {Items.normalGem, 1000},
            {Items.dreamBall, 1000},
            {Items.casteliacone, 350},
            {Items.tm93, 1000},
            {Items.tm94, 10000},
            {Items.tm95, 1000},
            {Items.weaknessPolicy, 2000},
            {Items.assaultVest, 6000},
            {Items.pixiePlate, 2000},
            {Items.abilityCapsule, 5000},
            {Items.whippedDream, 3000},
            {Items.sachet, 3000},
            {Items.luminousMoss, 200},
            {Items.snowball, 200},
            {Items.safetyGoggles, 3000},
            {Items.roseliBerry, 1000},
            {Items.keeBerry, 1000},
            {Items.marangaBerry, 1000},
            {Items.tm96, 10000},
            {Items.tm97, 10000},
            {Items.tm98, 20000},
            {Items.tm99, 10000},
            {Items.adrenalineOrb, 300},
            {Items.iceStone, 3000},
            {Items.beastBall, 300},
            {Items.terrainExtender, 4000},
            {Items.protectivePads, 3000},
            {Items.electricSeed, 1000},
            {Items.psychicSeed, 1000},
            {Items.mistySeed, 1000},
            {Items.grassySeed, 1000},
            {Items.pewterCrunchies, 350},
            {Items.rustedSword, 3000},
            {Items.rustedShield, 3000},
            {Items.strawberrySweet, 3000},
            {Items.loveSweet, 3000},
            {Items.berrySweet, 3000},
            {Items.cloverSweet, 3000},
            {Items.flowerSweet, 3000},
            {Items.starSweet, 3000},
            {Items.ribbonSweet, 3000},
            {Items.sweetApple, 3000},
            {Items.tartApple, 3000},
            {Items.throatSpray, 1000},
            {Items.ejectPack, 1000},
            {Items.heavyDutyBoots, 2000},
            {Items.blunderPolicy, 1000},
            {Items.roomService, 1000},
            {Items.utilityUmbrella, 1000},
            {Items.expCandyXS, 200},
            {Items.expCandyS, 1600},
            {Items.dynamaxCandy, 500},
            {Items.tr00, 3000},
            {Items.tr01, 3000},
            {Items.tr02, 3000},
            {Items.tr03, 3000},
            {Items.tr04, 3000},
            {Items.tr05, 3000},
            {Items.tr06, 3000},
            {Items.tr07, 3000},
            {Items.tr08, 3000},
            {Items.tr09, 3000},
            {Items.tr10, 3000},
            {Items.tr11, 3000},
            {Items.tr12, 3000},
            {Items.tr13, 3000},
            {Items.tr14, 3000},
            {Items.tr15, 3000},
            {Items.tr16, 3000},
            {Items.tr17, 3000},
            {Items.tr18, 3000},
            {Items.tr19, 3000},
            {Items.tr20, 3000},
            {Items.tr21, 3000},
            {Items.tr22, 3000},
            {Items.tr23, 3000},
            {Items.tr24, 3000},
            {Items.tr25, 3000},
            {Items.tr26, 3000},
            {Items.tr27, 3000},
            {Items.tr28, 3000},
            {Items.tr29, 3000},
            {Items.tr30, 3000},
            {Items.tr31, 3000},
            {Items.tr32, 3000},
            {Items.tr33, 3000},
            {Items.tr34, 3000},
            {Items.tr35, 3000},
            {Items.tr36, 3000},
            {Items.tr37, 3000},
            {Items.tr38, 3000},
            {Items.tr39, 3000},
            {Items.tr40, 3000},
            {Items.tr41, 3000},
            {Items.tr42, 3000},
            {Items.tr43, 3000},
            {Items.tr44, 3000},
            {Items.tr45, 3000},
            {Items.tr46, 3000},
            {Items.tr47, 3000},
            {Items.tr48, 3000},
            {Items.tr49, 3000},
            {Items.tr50, 3000},
            {Items.tr51, 3000},
            {Items.tr52, 3000},
            {Items.tr53, 3000},
            {Items.tr54, 3000},
            {Items.tr55, 3000},
            {Items.tr56, 3000},
            {Items.tr57, 3000},
            {Items.tr58, 3000},
            {Items.tr59, 3000},
            {Items.tr60, 3000},
            {Items.tr61, 3000},
            {Items.tr62, 3000},
            {Items.tr63, 3000},
            {Items.tr64, 3000},
            {Items.tr65, 3000},
            {Items.tr66, 3000},
            {Items.tr67, 3000},
            {Items.tr68, 3000},
            {Items.tr69, 3000},
            {Items.tr70, 3000},
            {Items.tr71, 3000},
            {Items.tr72, 3000},
            {Items.tr73, 3000},
            {Items.tr74, 3000},
            {Items.tr75, 3000},
            {Items.tr76, 3000},
            {Items.tr77, 3000},
            {Items.tr78, 3000},
            {Items.tr79, 3000},
            {Items.tr80, 3000},
            {Items.tr81, 3000},
            {Items.tr82, 3000},
            {Items.tr83, 3000},
            {Items.tr84, 3000},
            {Items.tr85, 3000},
            {Items.tr86, 3000},
            {Items.tr87, 3000},
            {Items.tr88, 3000},
            {Items.tr89, 3000},
            {Items.tr90, 3000},
            {Items.tr91, 3000},
            {Items.tr92, 3000},
            {Items.tr93, 3000},
            {Items.tr94, 3000},
            {Items.tr95, 3000},
            {Items.tr96, 3000},
            {Items.tr97, 3000},
            {Items.tr98, 3000},
            {Items.tr99, 3000},
            {Items.lonelyMint, 1000},
            {Items.adamantMint, 1000},
            {Items.naughtyMint, 1000},
            {Items.braveMint, 1000},
            {Items.boldMint, 1000},
            {Items.impishMint, 1000},
            {Items.laxMint, 1000},
            {Items.relaxedMint, 1000},
            {Items.modestMint, 1000},
            {Items.mildMint, 1000},
            {Items.rashMint, 1000},
            {Items.quietMint, 1000},
            {Items.calmMint, 1000},
            {Items.gentleMint, 1000},
            {Items.carefulMint, 1000},
            {Items.sassyMint, 1000},
            {Items.timidMint, 1000},
            {Items.hastyMint, 1000},
            {Items.jollyMint, 1000},
            {Items.naiveMint, 1000},
            {Items.seriousMint, 1000},
            {Items.wishingPiece, 500},
            {Items.crackedPot, 3000},
            {Items.chippedPot, 3000},
            {Items.galaricaTwig, 100},
            {Items.armoriteOre, 1000},
            {Items.dyniteOre, 1000},
            {Items.abilityPatch, 5000},
    }).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

    public static final List<String> pokeIcons = Arrays.asList(
            "001_00s_n",
            "002_00s_n",
            "003_00f_n",
            "003_00f_n_g",
            "003_00m_n",
            "003_00m_n_g",
            "004_00s_n",
            "005_00s_n",
            "006_00s_n",
            "006_00s_n_g",
            "007_00s_n",
            "008_00s_n",
            "009_00s_n",
            "009_00s_n_g",
            "010_00s_n",
            "011_00s_n",
            "012_00f_n",
            "012_00f_n_g",
            "012_00m_n",
            "012_00m_n_g",
            "025_00f_n",
            "025_00f_n_g",
            "025_00m_n",
            "025_00m_n_g",
            "025_01m_n",
            "025_02m_n",
            "025_03m_n",
            "025_04m_n",
            "025_05m_n",
            "025_06m_n",
            "025_07m_n",
            "025_09m_n",
            "026_00f_n",
            "026_00m_n",
            "026_01s_n",
            "027_00s_n",
            "027_01s_n",
            "028_00s_n",
            "028_01s_n",
            "029_00f_n",
            "030_00f_n",
            "031_00f_n",
            "032_00m_n",
            "033_00m_n",
            "034_00m_n",
            "035_00s_n",
            "036_00s_n",
            "037_00s_n",
            "037_01s_n",
            "038_00s_n",
            "038_01s_n",
            "039_00s_n",
            "040_00s_n",
            "041_00f_n",
            "041_00m_n",
            "042_00f_n",
            "042_00m_n",
            "043_00s_n",
            "044_00f_n",
            "044_00m_n",
            "045_00f_n",
            "045_00m_n",
            "050_00s_n",
            "050_01s_n",
            "051_00s_n",
            "051_01s_n",
            "052_00s_n",
            "052_00s_n_g",
            "052_01s_n",
            "052_02s_n",
            "053_00s_n",
            "053_01s_n",
            "054_00s_n",
            "055_00s_n",
            "058_00s_n",
            "059_00s_n",
            "060_00s_n",
            "061_00s_n",
            "062_00s_n",
            "063_00s_n",
            "064_00f_n",
            "064_00m_n",
            "065_00f_n",
            "065_00m_n",
            "066_00s_n",
            "067_00s_n",
            "068_00s_n",
            "068_00s_n_g",
            "072_00s_n",
            "073_00s_n",
            "077_00s_n",
            "077_01s_n",
            "078_00s_n",
            "078_01s_n",
            "079_00s_n",
            "079_01s_n",
            "080_00s_n",
            "080_02s_n",
            "081_00n_n",
            "082_00n_n",
            "083_00s_n",
            "083_01s_n",
            "090_00s_n",
            "091_00s_n",
            "092_00s_n",
            "093_00s_n",
            "094_00s_n",
            "094_00s_n_g",
            "095_00s_n",
            "098_00s_n",
            "099_00s_n",
            "099_00s_n_g",
            "102_00s_n",
            "103_00s_n",
            "103_01s_n",
            "104_00s_n",
            "105_00s_n",
            "105_01s_n",
            "106_00m_n",
            "107_00m_n",
            "108_00s_n",
            "109_00s_n",
            "110_00s_n",
            "110_01s_n",
            "111_00f_n",
            "111_00m_n",
            "112_00f_n",
            "112_00m_n",
            "113_00f_n",
            "114_00s_n",
            "115_00f_n",
            "116_00s_n",
            "117_00s_n",
            "118_00f_n",
            "118_00m_n",
            "119_00f_n",
            "119_00m_n",
            "120_00n_n",
            "121_00n_n",
            "122_00s_n",
            "122_01s_n",
            "123_00f_n",
            "123_00m_n",
            "124_00f_n",
            "125_00s_n",
            "126_00s_n",
            "127_00s_n",
            "128_00m_n",
            "129_00f_n",
            "129_00m_n",
            "130_00f_n",
            "130_00m_n",
            "131_00s_n",
            "131_00s_n_g",
            "132_00n_n",
            "133_00f_n",
            "133_00f_n_g",
            "133_00m_n",
            "133_00m_n_g",
            "134_00s_n",
            "135_00s_n",
            "136_00s_n",
            "137_00n_n",
            "138_00s_n",
            "139_00s_n",
            "140_00s_n",
            "141_00s_n",
            "142_00s_n",
            "143_00s_n",
            "143_00s_n_g",
            "144_00n_n",
            "144_01n_n",
            "145_00n_n",
            "145_01n_n",
            "146_00n_n",
            "146_01n_n",
            "147_00s_n",
            "148_00s_n",
            "149_00s_n",
            "150_00n_n",
            "151_00n_n",
            "163_00s_n",
            "164_00s_n",
            "169_00s_n",
            "170_00s_n",
            "171_00s_n",
            "172_00s_n",
            "173_00s_n",
            "174_00s_n",
            "175_00s_n",
            "176_00s_n",
            "177_00s_n",
            "178_00f_n",
            "178_00m_n",
            "182_00s_n",
            "183_00s_n",
            "184_00s_n",
            "185_00f_n",
            "185_00m_n",
            "186_00f_n",
            "186_00m_n",
            "194_00f_n",
            "194_00m_n",
            "195_00f_n",
            "195_00m_n",
            "196_00s_n",
            "197_00s_n",
            "199_00s_n",
            "199_01s_n",
            "202_00f_n",
            "202_00m_n",
            "206_00s_n",
            "208_00f_n",
            "208_00m_n",
            "211_00s_n",
            "212_00f_n",
            "212_00m_n",
            "213_00s_n",
            "214_00f_n",
            "214_00m_n",
            "215_00f_n",
            "215_00m_n",
            "220_00s_n",
            "221_00f_n",
            "221_00m_n",
            "222_00s_n",
            "222_01s_n",
            "223_00s_n",
            "224_00f_n",
            "224_00m_n",
            "225_00s_n",
            "226_00s_n",
            "227_00s_n",
            "230_00s_n",
            "233_00n_n",
            "236_00m_n",
            "237_00m_n",
            "238_00f_n",
            "239_00s_n",
            "240_00s_n",
            "241_00f_n",
            "242_00f_n",
            "243_00n_n",
            "244_00n_n",
            "245_00n_n",
            "246_00s_n",
            "247_00s_n",
            "248_00s_n",
            "249_00n_n",
            "250_00n_n",
            "251_00n_n",
            "252_00s_n",
            "253_00s_n",
            "254_00s_n",
            "255_00f_n",
            "255_00m_n",
            "256_00f_n",
            "256_00m_n",
            "257_00f_n",
            "257_00m_n",
            "258_00s_n",
            "259_00s_n",
            "260_00s_n",
            "263_00s_n",
            "263_01s_n",
            "264_00s_n",
            "264_01s_n",
            "270_00s_n",
            "271_00s_n",
            "272_00f_n",
            "272_00m_n",
            "273_00s_n",
            "274_00f_n",
            "274_00m_n",
            "275_00f_n",
            "275_00m_n",
            "278_00s_n",
            "279_00s_n",
            "280_00s_n",
            "281_00s_n",
            "282_00s_n",
            "290_00s_n",
            "291_00s_n",
            "292_00n_n",
            "293_00s_n",
            "294_00s_n",
            "295_00s_n",
            "298_00s_n",
            "302_00s_n",
            "303_00s_n",
            "304_00s_n",
            "305_00s_n",
            "306_00s_n",
            "309_00s_n",
            "310_00s_n",
            "315_00f_n",
            "315_00m_n",
            "318_00s_n",
            "319_00s_n",
            "320_00s_n",
            "321_00s_n",
            "324_00s_n",
            "328_00s_n",
            "329_00s_n",
            "330_00s_n",
            "333_00s_n",
            "334_00s_n",
            "337_00n_n",
            "338_00n_n",
            "339_00s_n",
            "340_00s_n",
            "341_00s_n",
            "342_00s_n",
            "343_00n_n",
            "344_00n_n",
            "345_00s_n",
            "346_00s_n",
            "347_00s_n",
            "348_00s_n",
            "349_00s_n",
            "350_00f_n",
            "350_00m_n",
            "355_00s_n",
            "356_00s_n",
            "359_00s_n",
            "360_00s_n",
            "361_00s_n",
            "362_00s_n",
            "363_00s_n",
            "364_00s_n",
            "365_00s_n",
            "369_00f_n",
            "369_00m_n",
            "371_00s_n",
            "372_00s_n",
            "373_00s_n",
            "374_00n_n",
            "375_00n_n",
            "376_00n_n",
            "377_00n_n",
            "378_00n_n",
            "379_00n_n",
            "380_00f_n",
            "381_00m_n",
            "382_00n_n",
            "383_00n_n",
            "384_00n_n",
            "385_00n_n",
            "403_00f_n",
            "403_00m_n",
            "404_00f_n",
            "404_00m_n",
            "405_00f_n",
            "405_00m_n",
            "406_00s_n",
            "407_00f_n",
            "407_00m_n",
            "415_00f_n",
            "415_00m_n",
            "416_00f_n",
            "420_00s_n",
            "421_00s_n",
            "421_01s_n",
            "422_00s_n",
            "422_01s_n",
            "423_00s_n",
            "423_01s_n",
            "425_00s_n",
            "426_00s_n",
            "427_00s_n",
            "428_00s_n",
            "434_00s_n",
            "435_00s_n",
            "436_00n_n",
            "437_00n_n",
            "438_00s_n",
            "439_00s_n",
            "440_00f_n",
            "442_00s_n",
            "443_00f_n",
            "443_00m_n",
            "444_00f_n",
            "444_00m_n",
            "445_00f_n",
            "445_00m_n",
            "446_00s_n",
            "447_00s_n",
            "448_00s_n",
            "449_00f_n",
            "449_00m_n",
            "450_00f_n",
            "450_00m_n",
            "451_00s_n",
            "452_00s_n",
            "453_00f_n",
            "453_00m_n",
            "454_00f_n",
            "454_00m_n",
            "458_00s_n",
            "459_00f_n",
            "459_00m_n",
            "460_00f_n",
            "460_00m_n",
            "461_00f_n",
            "461_00m_n",
            "462_00n_n",
            "463_00s_n",
            "464_00f_n",
            "464_00m_n",
            "465_00f_n",
            "465_00m_n",
            "466_00s_n",
            "467_00s_n",
            "468_00s_n",
            "470_00s_n",
            "471_00s_n",
            "473_00f_n",
            "473_00m_n",
            "474_00n_n",
            "475_00m_n",
            "477_00s_n",
            "478_00f_n",
            "479_00n_n",
            "479_01n_n",
            "479_02n_n",
            "479_03n_n",
            "479_04n_n",
            "479_05n_n",
            "480_00n_n",
            "481_00n_n",
            "482_00n_n",
            "483_00n_n",
            "484_00n_n",
            "485_00s_n",
            "486_00n_n",
            "487_00n_n",
            "487_01n_n",
            "488_00f_n",
            "494_00n_n",
            "506_00s_n",
            "507_00s_n",
            "508_00s_n",
            "509_00s_n",
            "510_00s_n",
            "517_00s_n",
            "518_00s_n",
            "519_00s_n",
            "520_00s_n",
            "521_00f_n",
            "521_00m_n",
            "524_00s_n",
            "525_00s_n",
            "526_00s_n",
            "527_00s_n",
            "528_00s_n",
            "529_00s_n",
            "530_00s_n",
            "531_00s_n",
            "532_00s_n",
            "533_00s_n",
            "534_00s_n",
            "535_00s_n",
            "536_00s_n",
            "537_00s_n",
            "538_00m_n",
            "539_00m_n",
            "543_00s_n",
            "544_00s_n",
            "545_00s_n",
            "546_00s_n",
            "547_00s_n",
            "548_00f_n",
            "549_00f_n",
            "550_00s_n",
            "550_01s_n",
            "551_00s_n",
            "552_00s_n",
            "553_00s_n",
            "554_00s_n",
            "554_01s_n",
            "555_00s_n",
            "555_01s_n",
            "555_02s_n",
            "555_03s_n",
            "556_00s_n",
            "557_00s_n",
            "558_00s_n",
            "559_00s_n",
            "560_00s_n",
            "561_00s_n",
            "562_00s_n",
            "562_01s_n",
            "563_00s_n",
            "564_00s_n",
            "565_00s_n",
            "566_00s_n",
            "567_00s_n",
            "568_00s_n",
            "569_00s_n",
            "569_00s_n_g",
            "570_00s_n",
            "571_00s_n",
            "572_00s_n",
            "573_00s_n",
            "574_00s_n",
            "575_00s_n",
            "576_00s_n",
            "577_00s_n",
            "578_00s_n",
            "579_00s_n",
            "582_00s_n",
            "583_00s_n",
            "584_00s_n",
            "587_00s_n",
            "588_00s_n",
            "589_00s_n",
            "590_00s_n",
            "591_00s_n",
            "592_00f_n",
            "592_00m_n",
            "593_00f_n",
            "593_00m_n",
            "595_00s_n",
            "596_00s_n",
            "597_00s_n",
            "598_00s_n",
            "599_00n_n",
            "600_00n_n",
            "601_00n_n",
            "605_00s_n",
            "606_00s_n",
            "607_00s_n",
            "608_00s_n",
            "609_00s_n",
            "610_00s_n",
            "611_00s_n",
            "612_00s_n",
            "613_00s_n",
            "614_00s_n",
            "615_00n_n",
            "616_00s_n",
            "617_00s_n",
            "618_00s_n",
            "618_01s_n",
            "619_00s_n",
            "620_00s_n",
            "621_00s_n",
            "622_00n_n",
            "623_00n_n",
            "624_00s_n",
            "625_00s_n",
            "626_00s_n",
            "627_00m_n",
            "628_00m_n",
            "629_00f_n",
            "630_00f_n",
            "631_00s_n",
            "632_00s_n",
            "633_00s_n",
            "634_00s_n",
            "635_00s_n",
            "636_00s_n",
            "637_00s_n",
            "638_00n_n",
            "639_00n_n",
            "640_00n_n",
            "641_00m_n",
            "641_01m_n",
            "642_00m_n",
            "642_01m_n",
            "643_00n_n",
            "644_00n_n",
            "645_00m_n",
            "645_01m_n",
            "646_00n_n",
            "646_01n_n",
            "646_02n_n",
            "647_00n_n",
            "647_01n_n",
            "649_00n_n",
            "649_01n_n",
            "649_02n_n",
            "649_03n_n",
            "649_04n_n",
            "702_00s_n",
            "703_00s_n",
            "709_00s_n",
            "710_00s_n",
            "711_00s_n",
            "712_00s_n",
            "716_00s_n",
            "726_00s_n",
            "727_00s_n",
            "730_00s_n",
            "731_00s_n",
            "732_00s_n",
            "733_00s_n",
            "734_00m_n",
            "734_01f_n",
            "735_00s_n",
            "735_01s_n",
            "735_02s_n",
            "735_03s_n",
            "736_00s_n",
            "736_01s_n",
            "736_02s_n",
            "736_03s_n",
            "737_00s_n",
            "738_00s_n",
            "739_00s_n",
            "740_00s_n",
            "741_00s_n",
            "742_00s_n",
            "743_00s_n",
            "744_00s_n",
            "745_00s_n",
            "746_00s_n",
            "746_01s_n",
            "747_00s_n",
            "748_00s_n",
            "749_00s_n",
            "751_00s_n",
            "752_00s_n",
            "753_00s_n",
            "754_00s_n",
            "755_00s_n",
            "756_00s_n",
            "757_00s_n",
            "758_00s_n",
            "759_00s_n",
            "760_00s_n",
            "761_00s_n",
            "762_00s_n",
            "763_00s_n",
            "764_00s_n",
            "765_00s_n",
            "766_00n_n",
            "767_00s_n",
            "768_00n_n",
            "768_01n_n",
            "769_00n_n",
            "770_00n_n",
            "770_01n_n",
            "770_02n_n",
            "770_03n_n",
            "770_04n_n",
            "772_00n_n",
            "773_00n_n",
            "801_00s_n",
            "802_00s_n",
            "803_00s_n",
            "804_00s_n",
            "805_00s_n",
            "806_00f_n",
            "810_00s_n",
            "812_00f_n",
            "813_00f_n",
            "814_00f_n",
            "815_00s_n",
            "816_00s_n",
            "817_00s_n",
            "818_00s_n",
            "819_00s_n",
            "819_01s_n",
            "820_00s_n",
            "820_01s_n",
            "821_00s_n",
            "822_00s_n",
            "823_00s_n",
            "828_00s_n",
            "828_01s_n",
            "829_00s_n",
            "829_01s_n",
            "829_02s_n",
            "830_00n_n",
            "831_00n_n",
            "832_00n_n",
            "833_00n_n",
            "834_00s_n",
            "835_00s_n",
            "836_00s_n",
            "837_00s_n",
            "838_00s_n",
            "839_00s_n",
            "840_00s_n",
            "841_00s_n",
            "842_00s_n",
            "843_00s_n",
            "844_00s_n",
            "845_00s_n",
            "846_00s_n",
            "847_00s_n",
            "848_00s_n",
            "849_00s_n",
            "850_00s_n",
            "851_00s_n",
            "852_00s_n",
            "853_00s_n",
            "854_00s_n",
            "856_00s_n",
            "857_00n_n",
            "858_00s_n",
            "861_00n_n",
            "862_00n_n",
            "862_01n_n",
            "862_02n_n",
            "862_03n_n",
            "862_04n_n",
            "862_05n_n",
            "862_06n_n",
            "862_07n_n",
            "862_08n_n",
            "862_09n_n",
            "862_10n_n",
            "862_11n_n",
            "862_12n_n",
            "862_13n_n",
            "862_14n_n",
            "862_15n_n",
            "862_16n_n",
            "862_17n_n",
            "863_00s_n",
            "865_00n_n",
            "865_01n_n",
            "865_02n_n",
            "866_00s_n",
            "867_00s_n",
            "868_00s_n",
            "869_00s_n",
            "871_00n_n",
            "872_00n_n",
            "873_00n_n",
            "874_00n_n",
            "875_00n_n",
            "876_00n_n",
            "877_00n_n",
            "878_00n_n",
            "879_00n_n",
            "880_00n_n",
            "881_00n_n",
            "882_00n_n",
            "882_01n_n",
            "883_00n_n",
            "884_00n_n",
            "885_00n_n",
            "886_00n_n",
            "887_00n_n",
            "888_00n_n",
            "891_00n_n",
            "892_00n_n",
            "892_00n_n_g",
            "901_00s_n",
            "902_00s_n",
            "903_00s_n",
            "904_00s_n",
            "904_00s_n_g",
            "905_00s_n",
            "906_00s_n",
            "907_00s_n",
            "908_00s_n",
            "909_00s_n",
            "910_00s_n",
            "911_00s_n",
            "912_00s_n",
            "913_00s_n",
            "914_00s_n",
            "915_00s_n",
            "916_00s_n",
            "916_00s_n_g",
            "917_00s_n",
            "917_01s_n",
            "917_02s_n",
            "918_00s_n",
            "919_00s_n",
            "920_00s_n",
            "920_00s_n_g",
            "921_00s_n",
            "922_00s_n",
            "922_00s_n_g",
            "922_01s_n",
            "923_00n_n",
            "924_00f_n",
            "925_00f_n",
            "925_00f_n_g",
            "925_01f_n",
            "925_02f_n",
            "925_03f_n",
            "925_04f_n",
            "925_05f_n",
            "925_06f_n",
            "925_07f_n",
            "925_08f_n",
            "926_00s_n",
            "927_00s_n",
            "927_00s_n_g",
            "928_00s_n",
            "929_00s_n",
            "929_00s_n_g",
            "930_00s_n",
            "931_00s_n",
            "932_00s_n",
            "932_00s_n_g",
            "933_00s_n",
            "934_00s_n",
            "935_00s_n",
            "935_00s_n_g",
            "936_00s_n",
            "937_00s_n",
            "937_00s_n_g",
            "938_00n_n",
            "938_01n_n",
            "939_00n_n",
            "939_01n_n",
            "940_00n_n",
            "940_00n_n_g",
            "941_00s_n",
            "941_01s_n",
            "942_00m_n",
            "942_01f_n",
            "943_00s_n",
            "944_00s_n",
            "945_00s_n",
            "946_00s_n",
            "947_00s_n",
            "948_00s_n",
            "949_00s_n",
            "950_00s_n",
            "950_00s_n_g",
            "951_00s_n",
            "952_00s_n",
            "953_00s_n",
            "953_00s_n_g",
            "954_00s_n",
            "955_00s_n",
            "956_00s_n",
            "956_00s_n_g",
            "957_00m_n",
            "958_00m_n",
            "959_00m_n",
            "959_00m_n_g",
            "960_00n_n",
            "961_00n_n",
            "962_00n_n",
            "963_00n_n",
            "964_00s_n",
            "965_00s_n",
            "966_00s_n",
            "967_00s_n",
            "967_00s_n_g",
            "968_00s_n",
            "968_00s_n_g",
            "969_00f_n",
            "970_00f_n",
            "971_00f_n",
            "971_00f_n_g",
            "972_00n_n",
            "972_01n_n",
            "973_00n_n",
            "973_00s_n",
            "973_01n_n",
            "973_01s_n",
            "974_00s_n",
            "975_00s_n",
            "975_01s_n",
            "976_00s_n",
            "977_00s_n",
            "978_00s_n",
            "979_00s_n",
            "980_00s_n",
            "981_00s_n",
            "982_00s_n",
            "983_00s_n",
            "983_00s_n_g",
            "983_01s_n",
            "983_01s_n_g",
            "984_00n_n",
            "985_00n_n",
            "986_00n_n",
            "986_01n_n",
            "986_02n_n",
            "988_00n_n",
            "988_01n_n",
            "989_00n_n",
            "990_00n_n"
    );
}
