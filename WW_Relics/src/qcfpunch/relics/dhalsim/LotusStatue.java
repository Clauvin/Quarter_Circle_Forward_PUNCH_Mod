package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class LotusStatue extends CustomRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Lotus_Statue";
	
	public LotusStatue() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.MAGICAL);
	}
	
}
