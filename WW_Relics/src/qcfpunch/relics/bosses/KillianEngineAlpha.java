package qcfpunch.relics.bosses;

import java.io.IOException;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.relics.ken.UnceasingFlame;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class KillianEngineAlpha extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Killian_Engine_Alpha";
	
	public KillianEngineAlpha() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	public static void save(final SpireConfig config) {

    }
	
	public static void load(final SpireConfig config) {
		
    }
	
	public static void clear(final SpireConfig config) {

	}

}
