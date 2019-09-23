package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

//Base code from Thorton's OctopusAction
public class PickACardToAddToShuffleInDrawPileAction extends AbstractGameAction {
	
	private boolean action_started = false;
	private CardGroup cards_to_choose_from;
	private String grid_description;
	
	public PickACardToAddToShuffleInDrawPileAction(CardGroup cards,
			String grid_description) {
		
		actionType = ActionType.WAIT;
		duration = Settings.ACTION_DUR_XLONG;
		this.grid_description = grid_description;
		
		cards_to_choose_from = cards;
		
		if (cards_to_choose_from.size() <= 0) {
			this.isDone = true;
		}
		
	}
	
	public void update() {
		
		if (!this.isDone) {
			if (!action_started) {
	            action_started = true;
	 
	            AbstractDungeon.gridSelectScreen.open(cards_to_choose_from,
	    				1,
	    				grid_description, false, false, false, false);
	            
			}
			else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
				
				AbstractCard card_chosen =
						AbstractDungeon.gridSelectScreen.selectedCards.get(0);
	            AbstractDungeon.gridSelectScreen.selectedCards.clear();
	            
	            AbstractDungeon.actionManager.addToBottom(
	            		new MakeTempCardInDrawPileAction(card_chosen, 1,
	            				true, true));
	            
	            this.isDone = true;
			}
		}
	}

}
