package qcfpunch.relics.character_cameos.valerie;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class MixedPaintBucket extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Mixed_Paint_Bucket";
	
	public MixedPaintBucket() {
		super(ID, GraphicResources.LoadRelicImage(
				"Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new MixedPaintBucket();
	}
	
}
