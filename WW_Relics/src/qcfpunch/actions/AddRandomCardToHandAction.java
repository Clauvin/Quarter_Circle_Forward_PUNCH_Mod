package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddRandomCardToHandAction extends AbstractGameAction {

	private AbstractPlayer player;
		
	public AddRandomCardToHandAction() {
		
		player = AbstractDungeon.player;
		this.actionType = ActionType.DRAW;
		
	}

	@Override
	public void update() {
		
		if (!this.isDone) {
	    	int draw_pile_size = player.drawPile.group.size();
	    	
	    	int which_card = (int)AbstractDungeon.cardRandomRng.randomLong();
	    	
	    	if (which_card <0) which_card *= -1;

	    	if (draw_pile_size > 0) {
		    	which_card %= draw_pile_size;
		    	AbstractCard the_card = player.drawPile.getNCardFromTop(which_card);
		    	player.drawPile.moveToHand(the_card, player.drawPile);
	    	}

	    	
	    	this.isDone = true;
		}
		
	}
	
}
