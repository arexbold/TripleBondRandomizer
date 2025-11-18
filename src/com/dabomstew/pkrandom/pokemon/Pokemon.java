package com.dabomstew.pkrandom.pokemon;

/*----------------------------------------------------------------------------*/
/*--  Pokemon.java - represents an individual Pokemon, and contains         --*/
/*--                 common Pokemon-related functions.                      --*/
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

import com.dabomstew.pkrandom.constants.Species;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Pokemon implements Comparable<Pokemon> {

    public String name;
    public int number;

    public String formeSuffix = "";
    public Pokemon baseForme = null;
    public int formeNumber = 0;
    public int cosmeticForms = 0;
    public int formeSpriteIndex = 0;
    public boolean actuallyCosmetic = false;
    public List<Integer> realCosmeticFormNumbers = new ArrayList<>();

    public Type primaryType, secondaryType;

    public int hp, attack, defense, spatk, spdef, speed, special;

    public int ability1, ability2, ability3;

    public int catchRate, expYield;

    public int guaranteedHeldItem, commonHeldItem, rareHeldItem, darkGrassHeldItem;

    public int genderRatio;

    public int frontSpritePointer, picDimensions;

    public int callRate;

    public ExpCurve growthCurve;

    public List<Evolution> evolutionsFrom = new ArrayList<>();
    public List<Evolution> evolutionsTo = new ArrayList<>();

    public List<MegaEvolution> megaEvolutionsFrom = new ArrayList<>();
    public List<MegaEvolution> megaEvolutionsTo = new ArrayList<>();

    protected List<Integer> shuffledStatsOrder;

    // A flag to use for things like recursive stats copying.
    // Must not rely on the state of this flag being preserved between calls.
    public boolean temporaryFlag;

    public Pokemon() {
        shuffledStatsOrder = Arrays.asList(0, 1, 2, 3, 4, 5);
    }

    public void shuffleStats(Random random) {
        Collections.shuffle(shuffledStatsOrder, random);
        applyShuffledOrderToStats();
    }
    
    public void copyShuffledStatsUpEvolution(Pokemon evolvesFrom) {
        // If stats were already shuffled once, un-shuffle them
        shuffledStatsOrder = Arrays.asList(
                shuffledStatsOrder.indexOf(0),
                shuffledStatsOrder.indexOf(1),
                shuffledStatsOrder.indexOf(2),
                shuffledStatsOrder.indexOf(3),
                shuffledStatsOrder.indexOf(4),
                shuffledStatsOrder.indexOf(5));
        applyShuffledOrderToStats();
        shuffledStatsOrder = evolvesFrom.shuffledStatsOrder;
        applyShuffledOrderToStats();
    }

    protected void applyShuffledOrderToStats() {
        List<Integer> stats = Arrays.asList(hp, attack, defense, spatk, spdef, speed);

        // Copy in new stats
        hp = stats.get(shuffledStatsOrder.get(0));
        attack = stats.get(shuffledStatsOrder.get(1));
        defense = stats.get(shuffledStatsOrder.get(2));
        spatk = stats.get(shuffledStatsOrder.get(3));
        spdef = stats.get(shuffledStatsOrder.get(4));
        speed = stats.get(shuffledStatsOrder.get(5));
    }

    public void randomizeStatsWithinBST(Random random) {
        int retryCount = 0;
        final int MAX_RETRIES = 50;

        // Keep track of original BST so that retries don't change it
        final int originalBST = bst();

        if (number == Species.shedinja) {
            // Shedinja is horribly broken unless we restrict him to 1HP.
            int bst = originalBST - 51;

            // Make weightings
            double atkW = random.nextDouble(), defW = random.nextDouble();
            double spaW = random.nextDouble(), spdW = random.nextDouble(), speW = random.nextDouble();

            double totW = atkW + defW + spaW + spdW + speW;

            hp = 1;
            attack = (int) Math.max(1, Math.round(atkW / totW * bst)) + 10;
            defense = (int) Math.max(1, Math.round(defW / totW * bst)) + 10;
            spatk = (int) Math.max(1, Math.round(spaW / totW * bst)) + 10;
            spdef = (int) Math.max(1, Math.round(spdW / totW * bst)) + 10;
            speed = (int) Math.max(1, Math.round(speW / totW * bst)) + 10;
            return;
        }

        // Bases determined once from the original BST
        int hp_base = 0;
        int stat_base = 0;

        if (originalBST < 300) {
            hp_base = 20;
            stat_base = 15;
        } else if (originalBST < 400) {
            hp_base = 25;
            stat_base = 20;
        } else if (originalBST < 500) {
            hp_base = 30;
            stat_base = 25;
        } else if (originalBST < 600) {
            hp_base = 35;
            stat_base = 30;
        } else {
            hp_base = 40;
            stat_base = 35;
        }

        // Set the base total minimum then subtract from bst so that it keeps the same total bst
        final int base_total = hp_base + stat_base * 5;
        final int bstPool = originalBST - base_total;

        do {
            //Weightings with some extra randominess
            double hpW = 0.9 + 0.2 * random.nextDouble();
            double atkW = 0.9 + 0.2 * random.nextDouble();
            double defW = 0.9 + 0.2 * random.nextDouble();
            double spaW = 0.9 + 0.2 * random.nextDouble();
            double spdW = 0.9 + 0.2 * random.nextDouble();
            double speW = 0.9 + 0.2 * random.nextDouble();
            
            // Class system for dominant stats
            int classPick;
            boolean[] isDominant = new boolean[6];
            boolean[] isPrimary = new boolean[6]; // For primary dominant stats that need more weight
            boolean fastGlassCannon = false;
            if (random.nextDouble() < 0.08) { // 8% chance for Even Stevens
                classPick = 7;
            }
            else {
                classPick = random.nextInt(7); // Other classes
            }

            switch (classPick) {
                case 0: // Pure Tank
                    isDominant[2] = true; isPrimary[2] = true; // DEF
                    isDominant[4] = true; isPrimary[4] = true; // SPD
                    break;
                case 1: // ATK Tank
                    isDominant[1] = true; // ATK
                    isDominant[2] = true; isPrimary[2] = true; // DEF
                    isDominant[4] = true; isPrimary[4] = true; // SPD
                    break;
                case 2: // SPA Tank
                    isDominant[3] = true; // SPA
                    isDominant[2] = true; isPrimary[2] = true;// DEF
                    isDominant[4] = true; isPrimary[4] = true; // SPD
                    break;
                case 3: // Mixed Attacker
                    isDominant[1] = true; isPrimary[1] = true; // ATK
                    isDominant[3] = true; isPrimary[3] = true; // SPA
                    int randomMix = random.nextInt(3); // Either lean towards 1 side or don't
                    if (randomMix == 0) { // Lean towards ATK
                        atkW *= 1.3;
                    }
                    else if (randomMix == 1) { // Lean towards SPA
                        spaW *= 1.3;
                    }
                    break;
                case 4: // Pure ATK
                    isDominant[1] = true; isPrimary[1] = true; // ATK
                    if (random.nextDouble() < 0.15) { // 15% chance to be dominant in SPE too
                        isDominant[5] = true; // SPE
                        fastGlassCannon = true;
                    }
                    break;
                case 5: // Pure SPA
                    isDominant[3] = true; isPrimary[3] = true; // SPA
                    if (random.nextDouble() < 0.15) { // 15% chance to be dominant in SPE too
                        isDominant[5] = true; // SPE
                        fastGlassCannon = true;
                    }
                    break;
                case 6: // Speed Demon
                    isDominant[5] = true; isPrimary[5] = true; // SPE
                    int randomMix2 = random.nextInt(3); // Either lean towards 1 side or don't 
                    if (randomMix2 == 0) { // Sub dominant ATK
                        isDominant[1] = true;
                    }
                    else if (randomMix2 == 1) { // Sub dominant SPA
                        isDominant[3] = true;
                    }
                    break;
                case 7: // Even Steven
                    if (random.nextBoolean()) {
                        isDominant[1] = true; // ATK
                    }
                    if (random.nextBoolean()) {
                        isDominant[2] = true; // DEF
                    }
                    if (random.nextBoolean()) {
                        isDominant[3] = true; // SPA
                    }
                    if (random.nextBoolean()) {
                        isDominant[4] = true; // SPD
                    }
                    if (random.nextBoolean()) {
                        isDominant[5] = true; // SPE
                    }
                    break;
            }

            // Randomly make HP or SPE dominant
            if (random.nextDouble() < 0.30) { // 30% chance to be dominant in HP
                isDominant[0] = true; // HP
                if (random.nextDouble() < 0.50) { // 50% chance to make it primary too
                    isPrimary[0] = true;
                }
            }

            if (random.nextDouble() < 0.25) { // 25% chance to be dominant in SPE
                isDominant[5] = true; // SPE
                if (random.nextDouble() < 0.50) { // 50% chance to make it primary too
                    isPrimary[5] = true;
                }
            }
            else if (fastGlassCannon) { // Special case if a glass cannon already rolled a dominant SPE but failed the 25% chance above
                if (random.nextDouble() < 0.50) { // 50% chance to make it primary too
                    isPrimary[5] = true;
                }
            }

            // Base multiplier for 1 dominant stat
            double dominantMulti = 1.8 + (random.nextDouble() * 0.9); // 1.8–2.7
            
            //Find the dominant bools and then multi them. If they're not dominate, -40% ~ +20% variance.
            if (isDominant[0]) hpW *= dominantMulti; else hpW *= 0.60 + 0.60 * random.nextDouble();
            if (isDominant[1]) atkW *= dominantMulti; else atkW *= 0.60 + 0.60 * random.nextDouble();
            if (isDominant[2]) defW *= dominantMulti; else defW *= 0.60 + 0.60 * random.nextDouble();
            if (isDominant[3]) spaW *= dominantMulti; else spaW *= 0.60 + 0.60 * random.nextDouble();
            if (isDominant[4]) spdW *= dominantMulti; else spdW *= 0.60 + 0.60 * random.nextDouble();
            if (isDominant[5]) speW *= dominantMulti; else speW *= 0.60 + 0.60 * random.nextDouble();

            // Extra boost for primary dominant stats
            double primaryMulti  = 1.05 + (random.nextDouble() * 0.15); // 1.05–1.20
            if (isPrimary[0]) hpW *= primaryMulti;
            if (isPrimary[1]) atkW *= primaryMulti;
            if (isPrimary[2]) defW *= primaryMulti;
            if (isPrimary[3]) spaW *= primaryMulti;
            if (isPrimary[4]) spdW *= primaryMulti;
            if (isPrimary[5]) speW *= primaryMulti;

            // If both DEF and SPD are dominant, randomly skew them so tanks aren't always even
            if (isDominant[2] && isDominant[4]) {
                double r = random.nextDouble();
                if (r < 0.60) { // 60% chance mild skew
                    if (random.nextBoolean()) { defW *= 1.15; spdW *= 0.85; }
                    else { defW *= 0.85; spdW *= 1.15; }
                } else if (r < 0.80) { // 20% chance strong skew
                    if (random.nextBoolean()) { defW *= 1.30; spdW *= 0.70; }
                    else { defW *= 0.70; spdW *= 1.30; }
                }
                // Then 20% to leave them even
            }
            // If both ATK and SPA are dominant, randomly skew them so mixed attackers aren't always even
            if (isDominant[1] && isDominant[3]) {
                double r = random.nextDouble();
                if (r < 0.60) { // 60% chance mild skew
                    if (random.nextBoolean()) { atkW *= 1.15; spaW *= 0.85; }
                    else { atkW *= 0.85; spaW *= 1.15; }
                } else if (r < 0.80) { // 20% chance strong skew
                    if (random.nextBoolean()) { atkW *= 1.30; spaW *= 0.70; }
                    else { atkW *= 0.70; spaW *= 1.30; }
                }
                // Then 20% to leave them even
            }

            double totW = hpW + atkW + defW + spaW + spdW + speW;

            hp = (int) Math.max(1, Math.round(hpW / totW * bstPool)) + hp_base;
            attack = (int) Math.max(1, Math.round(atkW / totW * bstPool)) + stat_base;
            defense = (int) Math.max(1, Math.round(defW / totW * bstPool)) + stat_base;
            spatk = (int) Math.max(1, Math.round(spaW / totW * bstPool)) + stat_base;
            spdef = (int) Math.max(1, Math.round(spdW / totW * bstPool)) + stat_base;
            speed = (int) Math.max(1, Math.round(speW / totW * bstPool)) + stat_base;

            retryCount++;

            // Fallback to capping at 255 if need be
            if (retryCount > MAX_RETRIES) {
                hp = Math.min(255, hp);
                attack = Math.min(255, attack);
                defense = Math.min(255, defense);
                spatk = Math.min(255, spatk);
                spdef = Math.min(255, spdef);
                speed = Math.min(255, speed);
                break;
            }

        } while (hp > 255 || attack > 255 || defense > 255 || spatk > 255 || spdef > 255 || speed > 255);

    }


    public void copyRandomizedStatsUpEvolution(Pokemon evolvesFrom) {
        double ourBST = bst();
        double theirBST = evolvesFrom.bst();

        double bstRatio = ourBST / theirBST;

        hp = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.hp * bstRatio)));
        attack = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.attack * bstRatio)));
        defense = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.defense * bstRatio)));
        speed = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.speed * bstRatio)));
        spatk = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.spatk * bstRatio)));
        spdef = (int) Math.min(255, Math.max(1, Math.round(evolvesFrom.spdef * bstRatio)));
    }

    public void assignNewStatsForEvolution(Pokemon evolvesFrom, Random random) {

        double ourBST = bst();
        double theirBST = evolvesFrom.bst();

        double bstDiff = ourBST - theirBST;

        // Make weightings
        double hpW = random.nextDouble(), atkW = random.nextDouble(), defW = random.nextDouble();
        double spaW = random.nextDouble(), spdW = random.nextDouble(), speW = random.nextDouble();

        double totW = hpW + atkW + defW + spaW + spdW + speW;

        double hpDiff = Math.round((hpW / totW) * bstDiff);
        double atkDiff = Math.round((atkW / totW) * bstDiff);
        double defDiff = Math.round((defW / totW) * bstDiff);
        double spaDiff = Math.round((spaW / totW) * bstDiff);
        double spdDiff = Math.round((spdW / totW) * bstDiff);
        double speDiff = Math.round((speW / totW) * bstDiff);

        hp = (int) Math.min(255, Math.max(1, evolvesFrom.hp + hpDiff));
        attack = (int) Math.min(255, Math.max(1, evolvesFrom.attack + atkDiff));
        defense = (int) Math.min(255, Math.max(1, evolvesFrom.defense + defDiff));
        speed = (int) Math.min(255, Math.max(1, evolvesFrom.speed + speDiff));
        spatk = (int) Math.min(255, Math.max(1, evolvesFrom.spatk + spaDiff));
        spdef = (int) Math.min(255, Math.max(1, evolvesFrom.spdef + spdDiff));
    }

    protected int bst() {
        return hp + attack + defense + spatk + spdef + speed;
    }

    public int bstForPowerLevels() {
        // Take into account Shedinja's purposefully nerfed HP
        if (number == Species.shedinja) {
            return (attack + defense + spatk + spdef + speed) * 6 / 5;
        } else {
            return hp + attack + defense + spatk + spdef + speed;
        }
    }

    public double getAttackSpecialAttackRatio() {
        return (double)attack / ((double)attack + (double)spatk);
    }

    public int getBaseNumber() {
        Pokemon base = this;
        while (base.baseForme != null) {
            base = base.baseForme;
        }
        return base.number;
    }

    public void copyBaseFormeBaseStats(Pokemon baseForme) {
        hp = baseForme.hp;
        attack = baseForme.attack;
        defense = baseForme.defense;
        speed = baseForme.speed;
        spatk = baseForme.spatk;
        spdef = baseForme.spdef;
    }

    public void copyBaseFormeAbilities(Pokemon baseForme) {
        ability1 = baseForme.ability1;
        ability2 = baseForme.ability2;
        ability3 = baseForme.ability3;
    }

    public void copyBaseFormeEvolutions(Pokemon baseForme) {
        evolutionsFrom = baseForme.evolutionsFrom;
    }

    public int getSpriteIndex() {
        return formeNumber == 0 ? number : formeSpriteIndex + formeNumber - 1;
    }

    public String fullName() {
        return name + formeSuffix;
    }

    @Override
    public String toString() {
        return "Pokemon [name=" + name + formeSuffix + ", number=" + number + ", primaryType=" + primaryType
                + ", secondaryType=" + secondaryType + ", hp=" + hp + ", attack=" + attack + ", defense=" + defense
                + ", spatk=" + spatk + ", spdef=" + spdef + ", speed=" + speed + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pokemon other = (Pokemon) obj;
        return number == other.number;
    }

    @Override
    public int compareTo(Pokemon o) {
        return number - o.number;
    }

    private static final List<Integer> legendaries = Arrays.asList(Species.articuno, Species.zapdos, Species.moltres,
            Species.mewtwo, Species.mew, Species.raikou, Species.entei, Species.suicune, Species.lugia, Species.hoOh,
            Species.celebi, Species.regirock, Species.regice, Species.registeel, Species.latias, Species.latios,
            Species.kyogre, Species.groudon, Species.rayquaza, Species.jirachi, Species.deoxys, Species.uxie,
            Species.mesprit, Species.azelf, Species.dialga, Species.palkia, Species.heatran, Species.regigigas,
            Species.giratina, Species.cresselia, Species.phione, Species.manaphy, Species.darkrai, Species.shaymin,
            Species.arceus, Species.victini, Species.cobalion, Species.terrakion, Species.virizion, Species.tornadus,
            Species.thundurus, Species.reshiram, Species.zekrom, Species.landorus, Species.kyurem, Species.keldeo,
            Species.meloetta, Species.genesect, Species.xerneas, Species.yveltal, Species.zygarde, Species.diancie,
            Species.hoopa, Species.volcanion, Species.typeNull, Species.silvally, Species.tapuKoko, Species.tapuLele,
            Species.tapuBulu, Species.tapuFini, Species.cosmog, Species.cosmoem, Species.solgaleo, Species.lunala,
            Species.necrozma, Species.magearna, Species.marshadow, Species.zeraora);

    private static final List<Integer> strongLegendaries = Arrays.asList(Species.mewtwo, Species.lugia, Species.hoOh,
            Species.kyogre, Species.groudon, Species.rayquaza, Species.dialga, Species.palkia, Species.regigigas,
            Species.giratina, Species.arceus, Species.reshiram, Species.zekrom, Species.kyurem, Species.xerneas,
            Species.yveltal, Species.cosmog, Species.cosmoem, Species.solgaleo, Species.lunala);

    private static final List<Integer> ultraBeasts = Arrays.asList(Species.nihilego, Species.buzzwole, Species.pheromosa,
            Species.xurkitree, Species.celesteela, Species.kartana, Species.guzzlord, Species.poipole, Species.naganadel,
            Species.stakataka, Species.blacephalon);

    //Triple Bond Challenge specifics
    private static final List<Integer> pseudoLegendaries = Arrays.asList(Species.dragonite, Species.tyranitar, Species.metagross, Species.garchomp, Species.slaking, Species.salamence);

    public boolean isLegendary() {
        return formeNumber == 0 ? legendaries.contains(this.number) : legendaries.contains(this.baseForme.number);
    }

    public boolean isStrongLegendary() {
        return formeNumber == 0 ? strongLegendaries.contains(this.number) : strongLegendaries.contains(this.baseForme.number);
    }

     //Triple Bond Challenge specifics
    public boolean isPseudoLegendary() {
        return formeNumber == 0 ? pseudoLegendaries.contains(this.number) : pseudoLegendaries.contains(this.baseForme.number);
    }

    // This method can only be used in contexts where alt formes are NOT involved; otherwise, some alt formes
    // will be considered as Ultra Beasts in SM.
    // In contexts where formes are involved, use "if (ultraBeastList.contains(...))" instead,
    // assuming "checkPokemonRestrictions" has been used at some point beforehand.
    public boolean isUltraBeast() {
        return ultraBeasts.contains(this.number);
    }

    public int getCosmeticFormNumber(int num) {
        return realCosmeticFormNumbers.isEmpty() ? num : realCosmeticFormNumbers.get(num);
    }

}
