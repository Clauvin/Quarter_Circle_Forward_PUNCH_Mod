package ww_relics.relics.guile;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.relics.OnLoseBlockRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import ww_relics.resources.relic_graphics.GraphicResources;

public class ArmyBoots extends CustomRelic implements OnLoseBlockRelic  {
	public static final String ID = "WW_Relics:Army_Boots";
	
	public static final Logger logger = LogManager.getLogger(ArmyBoots.class.getName());
	
	private static ArrayList<String> powers_affected_by_relic;

	public ArmyBoots() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.COMMON, LandingSound.SOLID);
		
		powers_affected_by_relic = new ArrayList<String>();
		powers_affected_by_relic.add("Frail");
		powers_affected_by_relic.add("Vulnerable");
	}
	
	public String getUpdatedDescription() {
		return "test";
	}
	
	@Override
	public int onLoseBlock(DamageInfo info, int damage_amount) {
		
		logger.info("1");
		if ((info.type == DamageType.NORMAL)) {
			flash();
			logger.info(info.name);
			
			AbstractPlayer player = AbstractDungeon.player;
			
			for (String power: powers_affected_by_relic){
				if (player.hasPower(power)) {
					logger.info(power);
				}
			}
		}
		return 0;
	}	
	
	public AbstractRelic makeCopy() {
		return new ArmyBoots();
	}

}