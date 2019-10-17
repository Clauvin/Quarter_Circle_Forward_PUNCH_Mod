package qcfpunch.modifiers;

import java.util.*;

import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import qcfpunch.QCFP_Misc;
import qcfpunch.relics.cammy.GreenLeotard;
import qcfpunch.relics.cammy.RedBeret;
import qcfpunch.relics.cammy.SpecialOpsInsignia;
import qcfpunch.relics.chun_li.*;
import qcfpunch.relics.dhalsim.BrokenTusk;
import qcfpunch.relics.dhalsim.LotusStatue;
import qcfpunch.relics.dhalsim.NecklaceOfSkulls;
import qcfpunch.relics.guile.*;
import qcfpunch.relics.ken.BlackTrainingShirt;
import qcfpunch.relics.ken.RedGi;
import qcfpunch.relics.ken.UnceasingFlame;
import qcfpunch.relics.ryu.*;
import qcfpunch.relics.zangief.RedCycloneTeachings;
import qcfpunch.relics.zangief.WildHerbsOintment;
import qcfpunch.relics.zangief.WrestlersCloak;

public class RelicSetModifiers {

	//Yes, I know this can be refactored to be a better class, I will do it, bear with me a while.
	
	public static final String WANDERING_WARRIOR_ID =
			QCFP_Misc.returnPrefix() + "WanderingWarrior";
	public static final String BLUE_JADE_ID =
			QCFP_Misc.returnPrefix() + "BlueJade";
	public static final String INDESTRUCTIBLE_FORTRESS_ID = 
			QCFP_Misc.returnPrefix() + "IndestructibleFortress";
	public static final String BLAZING_FIST_ID =
			QCFP_Misc.returnPrefix() + "BlazingFist";
	public static final String RED_CYCLONE_ID =
			QCFP_Misc.returnPrefix() + "RedCyclone";
	public static final String ASCETIC_MONK_ID =
			QCFP_Misc.returnPrefix() + "AsceticMonk";
	public static final String DELTA_SPIKE_ID =
			QCFP_Misc.returnPrefix() + "DeltaSpike";
	
	public static void addRelicSetModifiers(List<CustomMod> list) {
		CustomMod wandering_warrior =
				new CustomMod(RelicSetModifiers.WANDERING_WARRIOR_ID, "y", true);
		CustomMod blue_jade =
				new CustomMod(RelicSetModifiers.BLUE_JADE_ID, "y", true);
		CustomMod indestructible_fortress =
				new CustomMod(RelicSetModifiers.INDESTRUCTIBLE_FORTRESS_ID, "y", true);
		CustomMod blazing_fist =
				new CustomMod(RelicSetModifiers.BLAZING_FIST_ID, "y", true);
		CustomMod red_cyclone =
				new CustomMod(RelicSetModifiers.RED_CYCLONE_ID, "y", true);
		CustomMod ascetic_monk =
				new CustomMod(RelicSetModifiers.ASCETIC_MONK_ID, "y", true);
		CustomMod delta_spike =
				new CustomMod(RelicSetModifiers.DELTA_SPIKE_ID, "y", true);
		
		list.add(wandering_warrior);
		list.add(blue_jade);
		list.add(indestructible_fortress);
		list.add(blazing_fist);
		list.add(red_cyclone);
		list.add(ascetic_monk);
		list.add(delta_spike);
	}
	
	public static void addWanderingWarriorRelicsToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(DuffelBag.ID, relics);
		addRelicToCustomRunRelicList(FightingGloves.ID, relics);
		addRelicToCustomRunRelicList(RedHeadband.ID, relics);
	}
	
	public static void addBlueJadeRelicsToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(SpikyBracers.ID, relics);
		addRelicToCustomRunRelicList(WhiteBoots.ID, relics);
		addRelicToCustomRunRelicList(Handcuffs.ID, relics);
	}
	
	public static void addIndestructibleFortressToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(ArmyBoots.ID, relics);
		addRelicToCustomRunRelicList(ChainWithNametags.ID, relics);
		addRelicToCustomRunRelicList(CombatFatigues.ID, relics);
	}
	
	public static void addBlazingFistToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(BlackTrainingShirt.ID, relics);
		addRelicToCustomRunRelicList(RedGi.ID, relics);
		addRelicToCustomRunRelicList(UnceasingFlame.ID, relics);
	}
	
	public static void addRedCycloneToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(WrestlersCloak.ID, relics);
		addRelicToCustomRunRelicList(WildHerbsOintment.ID, relics);
		addRelicToCustomRunRelicList(RedCycloneTeachings.ID, relics);
	}
	
	public static void addAsceticMonkToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(BrokenTusk.ID, relics);
		addRelicToCustomRunRelicList(NecklaceOfSkulls.ID, relics);
		addRelicToCustomRunRelicList(LotusStatue.ID, relics);
	}
	
	public static void addDeltaSpikeToCustomRun(ArrayList<String> relics) {
		addRelicToCustomRunRelicList(RedBeret.ID, relics);
		addRelicToCustomRunRelicList(GreenLeotard.ID, relics);
		addRelicToCustomRunRelicList(SpecialOpsInsignia.ID, relics);
	}
	
	public static void addRelicToCustomRunRelicList(String ID, ArrayList<String> relics) {
		relics.add(ID);
		UnlockTracker.markRelicAsSeen(ID);
	}
	
	
}
