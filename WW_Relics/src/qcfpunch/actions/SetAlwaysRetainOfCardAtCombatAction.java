package qcfpunch.actions;

import java.util.UUID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class SetAlwaysRetainOfCardAtCombatAction extends AbstractGameAction  {

	UUID uuid;
	boolean retain;
	
	public SetAlwaysRetainOfCardAtCombatAction(UUID uuid, boolean it_retain) {
		this.uuid = uuid;
		this.retain = it_retain;
		
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
				the_card.retain = this.retain;
				
				String upper_cased_retain = GameDictionary.RETAIN.NAMES[0].
						substring(0, 1).toUpperCase() + 
						GameDictionary.RETAIN.NAMES[0].substring(1);

				if (Settings.language == GameLanguage.ZHS)
					upper_cased_retain = "" + upper_cased_retain;
				
				the_card.rawDescription = upper_cased_retain + ". NL " +
						the_card.rawDescription;
				the_card.initializeDescription();
				
				this.isDone = true;
				break;
			}
		}
	}
	
}
