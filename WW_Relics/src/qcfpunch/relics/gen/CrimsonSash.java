package qcfpunch.relics.gen;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class CrimsonSash extends CustomRelic  {

	public static final String ID = QCFP_Misc.returnPrefix() + "Crimson_Sash";
	
	public static final float HEALING_LOWER_EFFICIENCY = 0.5f;
	public static final int MORE_ENERGY_YAY = 1;
	
	public CrimsonSash() {
		super(ID, GraphicResources.LoadRelicImage("Temp Army Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
	
	public void onEquip() {
		AbstractDungeon.player.energy.energyMaster += MORE_ENERGY_YAY;
	}

	public void onUnequip() {
		AbstractDungeon.player.energy.energyMaster -= MORE_ENERGY_YAY;
	}
	
	@Override
	public int onPlayerHeal(int healAmount) {
		int true_heal_amount = Math.round(healAmount * HEALING_LOWER_EFFICIENCY); 
		flash();
		return super.onPlayerHeal(true_heal_amount);
	}
	
	public AbstractRelic makeCopy() {
		return new CrimsonSash();
	}
	
}
