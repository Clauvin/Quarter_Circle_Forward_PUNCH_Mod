package qcfpunch.relics.guile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class CombatFatigues extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Combat_Fatigues";
	
	public static boolean gained_block_last_turn = false;
	public static boolean havent_attacked_last_turn = true; 
	public static boolean is_first_turn = true;
	
	public static final int EXTRA_STRENGTH = 2;
	public static final int CAN_SPAWN_AFTER_FLOOR = 7;
	
	public static final Logger logger = LogManager.getLogger(CombatFatigues.class.getName()); 
	
	public CombatFatigues() {
		super(ID, GraphicResources.LoadRelicImage("Temp Combat Fatigues - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + DESCRIPTIONS[1] + EXTRA_STRENGTH + DESCRIPTIONS[2];
	}
	
	public int onPlayerGainedBlock(float blockAmount) {
		
		gained_block_last_turn = true;
		
		return MathUtils.floor(blockAmount);
	}
	@Override
	public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
		
		if (targetCard.type == AbstractCard.CardType.ATTACK) {
			logger.info(targetCard.name);
			havent_attacked_last_turn = false;
		}
	}
	
	@Override	
	public void atTurnStart() {
		if (firstTurnPassed()) {
			
			logger.info("gained_block_last_turn " + gained_block_last_turn);
			logger.info("havent_attacked_last_turn " + havent_attacked_last_turn);
			
			if (relicCanDoItsEffect()) {
				showRelicVisualEffect();
				giveDoubleDamage();
				giveExtraDamage();
			}
			
		} else is_first_turn = false;
		
		resetConditionChecks();
	}
	
	private boolean firstTurnPassed() {
		return !is_first_turn;
	}
	
	private boolean relicCanDoItsEffect() {
		return gained_block_last_turn && havent_attacked_last_turn;
	}
	
	private void showRelicVisualEffect() {
		 AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
	}
	
	private void giveDoubleDamage() {
	     AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PenNibPower(AbstractDungeon.player, 1), 1, true));
	}
	
	private void giveExtraDamage() {
		AbstractPlayer player = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(player, player, new StrengthPower(player, EXTRA_STRENGTH), EXTRA_STRENGTH));
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(player, player, new LoseStrengthPower(player, EXTRA_STRENGTH), EXTRA_STRENGTH));
	}
	
	private void resetConditionChecks() {
		gained_block_last_turn = false;
		havent_attacked_last_turn = true;
	}
	
	@Override
	public boolean canSpawn() {
		return AbstractDungeon.floorNum > CAN_SPAWN_AFTER_FLOOR;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new CombatFatigues();
	}
	
}
