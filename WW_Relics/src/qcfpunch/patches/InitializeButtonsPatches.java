package qcfpunch.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;

import basemod.ReflectionHacks;

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
	    		campfireButtons.add(new SmithOption(true));
	    	}
	    	
	    } catch (SecurityException|IllegalArgumentException e) {
	    	  e.printStackTrace();
	    } 
	}
}
