package qcfpunch.relics.zangief;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.WildHerbsOintmentPower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class WildHerbsOintment extends CustomRelic  {

	public static final String ID = QCFP_Misc.returnPrefix() + "Wild_Herbs_Ointment";
	
	public final int AMOUNT_OF_MAX_HP_GAINED = 4;
	public final float PERCENTAGE_OF_MAX_HP_TO_LOSE = 0.3f;
	
	public static boolean had_enough_hp_at_combat_start;
	
	public static final Logger logger = LogManager.getLogger(WildHerbsOintment.class.getName());
	
	public WildHerbsOintment() {
		super(ID, GraphicResources.LoadRelicImage("Temp Wild Herbs Ointment - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + (int)(PERCENTAGE_OF_MAX_HP_TO_LOSE * 100) + 
			    DESCRIPTIONS[1] + (int)(PERCENTAGE_OF_MAX_HP_TO_LOSE * 100) +
			    DESCRIPTIONS[2] + AMOUNT_OF_MAX_HP_GAINED +
			    DESCRIPTIONS[3];
	}
	
	
	@Override
	public void atBattleStartPreDraw() {
		
		if (hasEnoughHP()) {
			
			AbstractPlayer player = AbstractDungeon.player;
			int HP_to_have_at_most_in_victory =	player.currentHealth - 
						(int)(player.maxHealth * (PERCENTAGE_OF_MAX_HP_TO_LOSE));
			
			WildHerbsOintmentPower wild_herbs_power =
					new WildHerbsOintmentPower(player, AMOUNT_OF_MAX_HP_GAINED,
							HP_to_have_at_most_in_victory);
			
			AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(player, player, wild_herbs_power));
			
		}
		
	}
	
	private boolean hasEnoughHP() {
		
		float current_hp = (float)AbstractDungeon.player.currentHealth;
		float current_max_hp = (float)AbstractDungeon.player.maxHealth;
		
		if (current_hp / current_max_hp > PERCENTAGE_OF_MAX_HP_TO_LOSE) {
			int value_to_lose = (int)(current_max_hp * PERCENTAGE_OF_MAX_HP_TO_LOSE);
			if (current_hp - value_to_lose > 0)
				return true;
			else return false;
			
		} else return false;
		
	}
	
	public AbstractRelic makeCopy() {
		return new WildHerbsOintment();
	}
	
}
