package qcfpunch.modifiers;

import java.util.List;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.custom.CustomMod;

import qcfpunch.QCFP_Misc;

public class EasierRunModifiers {

	public static final String LOWERING_THE_HANDICAP_ID = QCFP_Misc.returnPrefix() + "LoweringTheHandicap";
	public static final int LOWERING_THE_HANDICAP_STARTING_HEALTH_PERCENTAGE = 125;
	
	public static final void AddEasierSetModifiers(List<CustomMod> list) {
		
		CustomMod lowering_the_handicap = 
				new CustomMod(EasierRunModifiers.LOWERING_THE_HANDICAP_ID, "y", true);
		
		list.add(lowering_the_handicap);
	}
	
	public static void AddLoweringTheHandicaptHealthBarEffectsToRun() {
		multiplyMaxHPByNewPercentage(EasierRunModifiers.LOWERING_THE_HANDICAP_STARTING_HEALTH_PERCENTAGE);
	}
	
	public static void multiplyMaxHPByNewPercentage(float new_percentage) {
    	float new_startingMaxHP = 
    			AbstractDungeon.player.startingMaxHP *
    			new_percentage / 100;
        AbstractDungeon.player.startingMaxHP = (int)new_startingMaxHP;
        AbstractDungeon.player.maxHealth = (int)new_startingMaxHP;
        AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
    }
	
}
