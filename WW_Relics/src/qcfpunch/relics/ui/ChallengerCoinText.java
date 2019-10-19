package qcfpunch.relics.ui;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class ChallengerCoinText extends CustomRelic{

	public static final String ID = QCFP_Misc.returnPrefix() + "ChallengerCoinText";
	
	public ChallengerCoinText() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.SPECIAL, LandingSound.FLAT);

		description = makeDescription();
	}
	
	private String makeDescription() {
		
		String description = DESCRIPTIONS[0];
		
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.replay_the_spire_class_code)) {
			description += DESCRIPTIONS[1];
		}
		
		description += DESCRIPTIONS[2];
		
		return description;
		
	}

	@Override
	public AbstractRelic makeCopy() {
		// TODO Auto-generated method stub
		return null;
	}

}
