package com.dabomstew.pkrandom.constants;

import com.dabomstew.pkrandom.pokemon.MoveCategory;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            2, 3, 4, 13, 26, 27, 41, 49, 82, 96, 99, 112, 117, 119, 121, 125, 128, 131, 132, 140, 145, 146, 148, 149,
            158, 159, 166, 169, 171, 185, 193, 216, 218, 222, 228, 237, 265, 274, 287, 289, 290, 293, 294, 300, 301,
            302, 316, 318, 320, 324, 327, 346, 354, 357, 358, 363, 373, 376, 377, 378, 381, 382, 386, 391, 426, 429,
            431, 443, 445, 448, 449, 456, 464, 465, 466, 477, 481, 485, 498, 507, 516, 531, 537, 547, 563, 569, 593,
            600, 617, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639,
            640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 658, 665, 671,
            672, 686, 690, 695, 696, 697, 698, 699, 700, 701, 702, 703, 719, 723, 724, 725, 726, 727, 728, 729, 730,
            731, 732, 733, 734, 735, 736, 737, 738, 739, 740, 741
    );

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
}
