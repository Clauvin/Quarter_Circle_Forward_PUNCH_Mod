package qcfpunch.relics.akuma;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class DarkGi extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Dark_Gi";
	
	public static final float PERCENTAGE_OF_MAX_HP_TO_LOSE = 0.3f;
	public static final float HEAL_EFFECTIVENESS = 0.5f;
	public static final int AMOUNT_TO_DRAW_WHEN_A_CARD_IS_PLAYED = 1;
	public static final int EXTRA_DRAW_EACH_TURN = 1;
	public static final int EXTRA_HAND_SIZE = 1;
	
	public DarkGi() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onEquip() {
		int max_hp_to_lose = (int) Math.floor(AbstractDungeon.player.maxHealth * 
				PERCENTAGE_OF_MAX_HP_TO_LOSE);
		
		AbstractDungeon.player.decreaseMaxHealth(max_hp_to_lose);
		
		BaseMod.MAX_HAND_SIZE += EXTRA_HAND_SIZE;
		AbstractDungeon.player.masterHandSize += EXTRA_DRAW_EACH_TURN;
	}
	
	@Override
	public int onPlayerHeal(int healAmount) {
		int amount_to_heal = (int)Math.floor(healAmount * HEAL_EFFECTIVENESS);
		
		return amount_to_heal;
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		super.onPlayCard(c, m);
		AbstractDungeon.actionManager.addToBottom(
				new DrawCardAction(AbstractDungeon.player,
						AMOUNT_TO_DRAW_WHEN_A_CARD_IS_PLAYED));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new DarkGi();
	}

}
