package qcfpunch.relics.ryu;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.DrawRandomCardToHandAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class RedHeadband extends CustomRelic {
	public static final String ID = QCFP_Misc.returnPrefix() + "Red_Headband";
	private static final int DRAW_PER_STATUS_OR_CURSE = 1;
	private static final int NUMBER_OF_DRAWS = 1;
	
	private static int drawn_status_and_curses_in_the_turn = 0;
		
	//Solution: load Texture instead of String
	public RedHeadband() {
		super(ID, GraphicResources.LoadRelicImage("Red_Headband - headband-knot - Delapouite - CC BY 3.0.png"), 
				RelicTier.COMMON, LandingSound.FLAT);	
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
	}
	
	public void onCardDraw(AbstractCard drawnCard) {
		if ((drawnCard.type == AbstractCard.CardType.CURSE) ||
			(drawnCard.type == AbstractCard.CardType.STATUS)) 
		{
			if (drawn_status_and_curses_in_the_turn  < NUMBER_OF_DRAWS) {
				AbstractDungeon.actionManager.addToBottom(
						new RelicAboveCreatureAction(AbstractDungeon.player, this));
				
		        AbstractDungeon.actionManager.addToBottom(
		        		new DiscardSpecificCardAction(drawnCard));
				
		        AbstractPlayer p = AbstractDungeon.player;

		        if (QCFP_Misc.abscenceOfNoDrawPower()) {
			        for (int i = 0; i < DRAW_PER_STATUS_OR_CURSE; i++) {
				        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
				        AbstractDungeon.actionManager.
				        	addToBottom(new DrawCardAction(p, 1));
				    }
				    drawn_status_and_curses_in_the_turn++;
			    } else {
		        	p.getPower("No Draw").flash();
		        }
			}
		}
		
	}
	
	public void atTurnStart() {
		drawn_status_and_curses_in_the_turn = 0;
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new RedHeadband();
	}
}
