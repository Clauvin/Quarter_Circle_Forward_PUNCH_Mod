package qcfpunch.relics.ken;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class RedGi extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Red_Gi";
	public static final int SEQUENTIAL_ATTACKS_TO_DO = 3;
	public static final int CARDS_TO_DRAW = 1;
	public static final int INCREASE_ATTACK_COST_BY = -1;
	public static int cards_to_be_affected = 0;

	
	public static boolean draw_effect = false;
	
	public RedGi() {
		super(ID, GraphicResources.LoadRelicImage("Red Gi - kimono - Delapouite - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.FLAT);
		
		setCounter(0);
	}	
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + SEQUENTIAL_ATTACKS_TO_DO +
				DESCRIPTIONS[1] + CARDS_TO_DRAW +
				DESCRIPTIONS[2] + INCREASE_ATTACK_COST_BY * -1 +
				DESCRIPTIONS[3];
	}
	
	@Override
	public void atBattleStart() {
		ResetCounter();
	}
	
	@Override
	public void onVictory() {
		ResetCounter();
	}
	
	@Override
	public void onPlayerEndTurn() {
		ResetCounter();
	}
	
	public void ResetCounter() {
		this.counter = 0;
	}
	
	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster monster) {
		if (card.type == CardType.ATTACK) {
			this.counter++;
		}
		else ResetCounter();
		
		if (counter == SEQUENTIAL_ATTACKS_TO_DO) {
			this.counter -= SEQUENTIAL_ATTACKS_TO_DO;
			
			draw_effect = true;
			
			StartCountingOfCardsToBeAffected();
			
			for (int i = 0; i < CARDS_TO_DRAW; i++) {
				DrawEffect();
			}
		}
	}
	
	public void StartCountingOfCardsToBeAffected() {
		cards_to_be_affected = CARDS_TO_DRAW;
	}
	
	public void DrawEffect() {
		flash();
		AbstractDungeon.actionManager.addToTop(
				new DrawCardAction(AbstractDungeon.player, 1));
		AbstractDungeon.actionManager.addToTop(
				new RelicAboveCreatureAction(AbstractDungeon.player, this));
	}
	
	@Override
	public void onCardDraw(AbstractCard drawnCard) {
		if (CardDrawnByRelicEffectIsAnAttack(drawnCard)) {
			
			AttackCostLessThisTurn(drawnCard);
			
		} else if (draw_effect) {
			
			OneLessCardToBeAffected();
			
		}
	}
	
	public boolean CardDrawnByRelicEffectIsAnAttack(AbstractCard drawnCard) {
		return ((draw_effect) && (drawnCard.type == CardType.ATTACK));
	}
	
	public void AttackCostLessThisTurn(AbstractCard drawnCard) {
		drawnCard.setCostForTurn(INCREASE_ATTACK_COST_BY);
		draw_effect = false;
	}
	
	public void OneLessCardToBeAffected() {
		if (cards_to_be_affected > 0) cards_to_be_affected--;
		if (cards_to_be_affected == 0) draw_effect = false;
	}
	
	public AbstractRelic makeCopy() {
		return new RedGi();
	}
}
