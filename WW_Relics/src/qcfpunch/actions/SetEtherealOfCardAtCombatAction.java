package qcfpunch.actions;

import java.util.UUID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class SetEtherealOfCardAtCombatAction extends AbstractGameAction {

	UUID uuid;
	boolean ethereal;
	
	public SetEtherealOfCardAtCombatAction(UUID uuid, boolean will_be_ethereal) {
		this.uuid = uuid;
		this.ethereal = will_be_ethereal;
		
		actionType = ActionType.SPECIAL;
	}
	
	@Override
	public void update() {
		
		if (!this.isDone) {
			
			CardGroup hand = AbstractDungeon.player.hand;
			CheckGroupForCardToSet(hand);
			if (this.isDone) return;
			
			CardGroup discard = AbstractDungeon.player.discardPile;
			CheckGroupForCardToSet(discard);
			if (this.isDone) return;
			
			CardGroup deck = AbstractDungeon.player.drawPile;
			CheckGroupForCardToSet(deck);
			if (this.isDone) return;
			
			CardGroup exhausted = AbstractDungeon.player.exhaustPile;
			CheckGroupForCardToSet(exhausted);
			this.isDone = true;
			
		}

	}
	
	private void CheckGroupForCardToSet(CardGroup card_group) {
		for (int i = 0; i < card_group.size(); i++) {
			if (card_group.getNCardFromTop(i).uuid == this.uuid) {
				AbstractCard the_card = card_group.getNCardFromTop(i);
				the_card.isEthereal = this.ethereal;
				
				String upper_cased_ethereal = GameDictionary.ETHEREAL.NAMES[0].
						substring(0, 1).toUpperCase() + 
						GameDictionary.ETHEREAL.NAMES[0].substring(1);
				
				the_card.rawDescription = upper_cased_ethereal + ". NL " +
								the_card.rawDescription;
				the_card.initializeDescription();
				
				this.isDone = true;
				break;
			}
		}
	}
	
}
