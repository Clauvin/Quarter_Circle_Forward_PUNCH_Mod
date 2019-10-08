package qcfpunch.relics.cammy;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Forethought;
import com.megacrit.cardcrawl.cards.green.Setup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.SetEtherealOfCardAtCombatAction;
import qcfpunch.actions.SetExhaustOfCardAtCombatAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class SpecialOpsInsignia extends CustomRelic  {

	public static final String ID = QCFP_Misc.returnPrefix() +
			"Special_Ops_Insignia";
	
	public static final int CARDS_TO_DRAW_TO_APPLY_EFFECT = 4;
	
	public int usual_hand_per_turn_start;
	public int actual_hand_per_turn_start;
	public int extra_cards_drawn_this_turn;
	
	public boolean effect_triggered_this_turn;
	
	public SpecialOpsInsignia() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + CARDS_TO_DRAW_TO_APPLY_EFFECT + DESCRIPTIONS[1];
	}

	@Override
	public void onEquip() {
		counter = 0;
	}
	
	@Override
	public void atBattleStartPreDraw() {
		
		CharSelectInfo char_info = AbstractDungeon.player.getLoadout();
		
		usual_hand_per_turn_start = char_info.cardDraw;
		
		counter = 0;
		
		effect_triggered_this_turn = false;
	}
	
	@Override
	public void atTurnStart() {
		
		if (AbstractDungeon.player.gameHandSize <= usual_hand_per_turn_start)
			actual_hand_per_turn_start = AbstractDungeon.player.gameHandSize;
		else actual_hand_per_turn_start = usual_hand_per_turn_start;
			
		setCardAndVariableCounters(0);

	}
	
	@Override
	public void onCardDraw(AbstractCard drawnCard) {
		
		if (!effect_triggered_this_turn) {
			
			if (actual_hand_per_turn_start > 0) actual_hand_per_turn_start--;
			else if (extra_cards_drawn_this_turn < CARDS_TO_DRAW_TO_APPLY_EFFECT){
				setCardAndVariableCounters(++extra_cards_drawn_this_turn);
			}
			
			if (extra_cards_drawn_this_turn >= CARDS_TO_DRAW_TO_APPLY_EFFECT) {
				AbstractCard new_setup = new Setup();
				AbstractCard new_breakthrough = new Forethought();
				
				flash();
				AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(new_setup, false, true));
				AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(new_breakthrough, false, true));
				
				AbstractDungeon.actionManager.addToBottom(
					new SetExhaustOfCardAtCombatAction(new_setup.uuid, true));
				AbstractDungeon.actionManager.addToBottom(
					new SetEtherealOfCardAtCombatAction(new_setup.uuid, true));
				AbstractDungeon.actionManager.addToBottom(
					new SetExhaustOfCardAtCombatAction(new_breakthrough.uuid, true));
				AbstractDungeon.actionManager.addToBottom(
					new SetEtherealOfCardAtCombatAction(new_breakthrough.uuid, true));
				
				setCardAndVariableCounters(-1);
				
				effect_triggered_this_turn = true;
			}
		}
		
	}
	
	@Override
	public void onPlayerEndTurn() {
		setCardAndVariableCounters(0);
		effect_triggered_this_turn = false;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new SpecialOpsInsignia();
	}
	
	public void setCardAndVariableCounters(int new_value) {
		extra_cards_drawn_this_turn = new_value;
		counter = new_value;
	}
	
}
