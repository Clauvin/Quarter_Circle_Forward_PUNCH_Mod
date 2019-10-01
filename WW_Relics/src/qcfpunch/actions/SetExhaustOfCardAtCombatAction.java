package qcfpunch.actions;

import java.util.UUID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class SetExhaustOfCardAtCombatAction extends AbstractGameAction  {

	UUID uuid;
	boolean exhausts;
	
	public SetExhaustOfCardAtCombatAction(UUID uuid, boolean it_exhausts) {
		this.uuid = uuid;
		this.exhausts = it_exhausts;
		
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
				card_group.getNCardFromTop(i).exhaust = this.exhausts;
				card_group.getNCardFromTop(i).rawDescription += 
						" NL " + GameDictionary.EXHAUST.NAMES[0] + ".";
				card_group.getNCardFromTop(i).initializeDescription();
				this.isDone = true;
				break;
			}
		}
	}

}
