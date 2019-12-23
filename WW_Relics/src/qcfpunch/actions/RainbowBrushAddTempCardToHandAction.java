package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import qcfpunch.QCFP_Misc;

public class RainbowBrushAddTempCardToHandAction extends
	MakeTempCardInHandAction {

	private AbstractCard card_to_temp;
	private boolean this_action_is_other_card_in_center;
	private static final float PADDING = 25.0F * Settings.scale;
	
	public RainbowBrushAddTempCardToHandAction(AbstractCard card, boolean isOtherCardInCenter, boolean sameUUID) {
		super(card, isOtherCardInCenter, sameUUID);
		card_to_temp = card;
		this_action_is_other_card_in_center = isOtherCardInCenter;

	}
	
	public void update() {
		if (this.amount == 0) {
			this.isDone = true;
			return;
	    } 
	    int discardAmount = 0;
	    int handAmount = this.amount;

	    
	    if (this.amount + AbstractDungeon.player.hand.size() > 10) {
	    	AbstractDungeon.player.createHandIsFullDialog();
	    	discardAmount = this.amount +
	    			AbstractDungeon.player.hand.size() - 10;
	    	handAmount -= discardAmount;
	    } 
	    
	    addToHand(handAmount);
	    addToDiscard(discardAmount);
	    
	    if (this.amount > 0) {
	      AbstractDungeon.actionManager.addToTop(
	    		  new WaitAction(0.8F));
	    }
	    
	    this.isDone = true;
	  }
	
	private void addToHand(int handAmt) {
		switch (this.amount) {
		case 0:
			return;
		case 1:
			if (handAmt == 1) {
				if (this.this_action_is_other_card_in_center) {
					AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(
	                  makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH,
	                  Settings.HEIGHT / 2.0F));
				}
	          else {
	        	  AbstractDungeon.effectList.add(
	        			  new ShowCardAndAddToHandEffect(makeNewCard()));
	          } 
	        }
		}
    }
	
	private void addToDiscard(int discardAmt) {
		switch (this.amount) {
			case 0:
				return;
			case 1:
				if (discardAmt == 1) {
					AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(
		                makeNewCard(), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
		        }
		}
	}
	
	public AbstractCard makeNewCard() {
		QCFP_Misc.fastLoggerLine("Here");
		return QCFP_Misc.doCopyWithEtherealExhaustAndDescription(card_to_temp);
	}

		
}
