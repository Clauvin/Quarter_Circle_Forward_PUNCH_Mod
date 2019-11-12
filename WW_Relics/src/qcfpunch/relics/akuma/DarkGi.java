package qcfpunch.relics.akuma;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class DarkGi extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Dark_Gi";
	
	public DarkGi() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onEquip() {
		// TODO Auto-generated method stub
		super.onEquip();
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new DarkGi();
	}

}
