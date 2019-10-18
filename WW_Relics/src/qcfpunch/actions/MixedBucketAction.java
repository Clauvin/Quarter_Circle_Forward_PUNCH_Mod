package qcfpunch.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import qcfpunch.QCFP_Misc;

public class MixedBucketAction extends AbstractGameAction {

	AbstractCard card_to_give;
	
	public MixedBucketAction(AbstractCard card_to_give) {
		
		this.card_to_give = card_to_give;
		
	}

	@Override
	public void update() {
		if (!isDone) {
			
			CardGroup hand = AbstractDungeon.player.hand;
			QCFP_Misc.fastLoggerLine(hand.size() + "");
			
			ArrayList<CardColor> colors = new ArrayList<CardColor>();
			int amount_of_cards_to_give = 0;
			
			for (int i = 0; i < hand.size(); i++) {
				QCFP_Misc.fastLoggerLine("Loop 1");
				CardColor color = hand.getNCardFromTop(i).color;
				boolean new_color = true;
				for (int j = 0; j < colors.size(); j++) {
					if (colors.get(j) == color) {
						new_color = false;
						break;
					}
				}
				if (new_color) {
					colors.add(color);
					amount_of_cards_to_give += 1;
				}
			}
					
			for (int i = 0; i < amount_of_cards_to_give; i++) {
				QCFP_Misc.fastLoggerLine("Loop 2");
				AbstractCard given_card = card_to_give.makeStatEquivalentCopy();
				
				QCFP_Misc.reduceCardCostIfNotStatusOrCurseByOne(given_card);
				
				AbstractDungeon.actionManager.addToBottom(
						new MakeTempCardInHandAction(given_card, false, true));
				
				AbstractDungeon.actionManager.addToBottom(
						new SetExhaustOfCardAtCombatAction(given_card.uuid, true));
				
				AbstractDungeon.actionManager.addToBottom(
						new SetAlwaysRetainOfCardAtCombatAction(given_card.uuid, true));
			}
			
			
			isDone = true;
		}
		
	}
	
}
