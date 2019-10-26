package qcfpunch.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import basemod.ReflectionHacks;
import qcfpunch.relics.ryu.FightingGloves;
import qcfpunch.restsite.FightingGlovesTrainOption;

//Yes, base from Vex's The Artificer's InitializeButtonsPatches.
@SpirePatch(cls = "com.megacrit.cardcrawl.rooms.CampfireUI", method = "initializeButtons")
public class InitializeButtonsPatches
{
	@SuppressWarnings("unchecked")
	public static void Postfix(Object __instance) {
		CampfireUI campfire = (CampfireUI)__instance;
	    
	    try {
	    	ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>)ReflectionHacks.getPrivate(campfire, CampfireUI.class, "buttons");
	    	if (AbstractDungeon.player.hasRelic("qcfpunch:Fighting_Gloves")) {
	    		FightingGloves gloves = (FightingGloves) AbstractDungeon.player.getRelic("qcfpunch:Fighting_Gloves");
	    		if (gloves.shouldTheRelicBeUsedNow()) {
	    			campfireButtons.add(new FightingGlovesTrainOption(true));
	    		}
	    		else {
	    			campfireButtons.add(new FightingGlovesTrainOption(false));
	    		}
	    		
	    	}
	    	
	    } catch (SecurityException|IllegalArgumentException e) {
	    	  e.printStackTrace();
	    }
	}
}
