package qcfpunch.relics.character_cameos.valerie;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.MixedBucketAction;
import qcfpunch.resources.relic_graphics.GraphicResources;
import theArtist.cards.PalettePick;

public class MixedPaintBucket extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Mixed_Paint_Bucket";
	
	public AbstractCard card_to_give;
	
	public MixedPaintBucket() {
		super(ID, GraphicResources.LoadRelicImage(
				"Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
		
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.the_artist_class_code)) {
			card_to_give = new PalettePick();
		}
		else card_to_give = new Discovery();
		
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		AbstractDungeon.actionManager.addToBottom(
				new MixedBucketAction(card_to_give));
		
		flash();
	}
	
	public boolean canSpawn() {
		return QCFP_Misc.checkForMod(QCFP_Misc.the_artist_class_code);
	}
	
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new MixedPaintBrush();
	}
	
}
