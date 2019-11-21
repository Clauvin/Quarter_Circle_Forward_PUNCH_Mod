package qcfpunch.relics.seth;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class KillianEngineAlpha extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Killian_Engine_Alpha";
	
	public static final int CARD_AMOUNT_TO_CHOOSE_FROM = 20;
	public static final int CARD_AMOUNT_TO_PICK_AT_MOST = 5;

	public boolean upgrade_card_grid_have_appeared = false;
	public boolean remove_card_grid_have_appeared = false;
	public boolean is_done = false;
	
	private int amount_of_cards_added;
	
	public static String current_description;
	
	public KillianEngineAlpha() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	public void update() {
	    super.update();
	    if (this.upgrade_card_grid_have_appeared && 
	    	!this.remove_card_grid_have_appeared &&
	    		!AbstractDungeon.isScreenUp &&
	    		!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
	    	
	    	amount_of_cards_added = AbstractDungeon.
	    			gridSelectScreen.selectedCards.size();
	      
	    	for (int i = 0;
	    			i < amount_of_cards_added;
	    			i++) {
	    		
	    		AbstractCard c = ((AbstractCard)AbstractDungeon.
	    				gridSelectScreen.selectedCards.get(i)).makeCopy();
	    		
	    		AbstractDungeon.player.masterDeck.addToTop(c);
	    		
	    	}
	      
	    	AbstractDungeon.gridSelectScreen.selectedCards.clear();
	    	
	    	if (CardGroup.getGroupWithoutBottledCards(
	    			AbstractDungeon.player.masterDeck.getPurgeableCards())
	                .size() > 0) {
	    		
	    		AbstractDungeon.gridSelectScreen.open(
	    			CardGroup.getGroupWithoutBottledCards(
	    					AbstractDungeon.player.masterDeck
	    				.getPurgeableCards()), amount_of_cards_added, "Testing 2",
	    			false, false, false, false);
	    		
	    	}

	    	remove_card_grid_have_appeared = true;
	    	
	    	
	    }
	    
	    if (this.upgrade_card_grid_have_appeared && 
		    	this.remove_card_grid_have_appeared &&
		    	!is_done &&
		    		!AbstractDungeon.isScreenUp &&
		    		!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
	    	
	    	QCFP_Misc.fastLoggerLine("Huh");
	    	
	    	for (int i = 0;
	    			i < AbstractDungeon.gridSelectScreen.selectedCards.size();
	    			i++) {
	    		
	    		AbstractCard card = AbstractDungeon.
	    				gridSelectScreen.selectedCards.get(i);
	    		
	    		AbstractDungeon.player.masterDeck.removeCard(card);
	    		
	    	}
	    	
	    	AbstractDungeon.gridSelectScreen.selectedCards.clear();
	    	
	    	is_done = true;
	    }
	    
	}
	
	@Override
	public void onEquip() {
		
        CardGroup cards_to_choose = new CardGroup(
        		CardGroup.CardGroupType.UNSPECIFIED);

        for (int i = 0; i < CARD_AMOUNT_TO_CHOOSE_FROM; i++) {
        	AbstractCard card = getCardOfAnyOtherClass(
        			AbstractDungeon.rollRarity());
          
        	boolean containsDupe = true;
        	while (containsDupe) {
        		containsDupe = false;
            
        		for (AbstractCard c : cards_to_choose.group) {
        			if (c.cardID.equals(card.cardID)) {
        				containsDupe = true;
        				card = getCardOfAnyOtherClass(
        	        			AbstractDungeon.rollRarity());
        			} 
        		} 
        	} 
          
        	if (!cards_to_choose.contains(card)) {
        		
        		if (card.type == AbstractCard.CardType.ATTACK &&
        				AbstractDungeon.player.hasRelic("Molten Egg 2")) {
        			card.upgrade();
        		} else if (card.type == AbstractCard.CardType.SKILL &&
        				AbstractDungeon.player.hasRelic("Toxic Egg 2")) {
        			card.upgrade();
        		} else if (card.type == AbstractCard.CardType.POWER &&
        				AbstractDungeon.player.hasRelic("Frozen Egg 2")) {
        			card.upgrade();
        		}
        		
        		cards_to_choose.addToBottom(card);
        	
        	} else {
	            i--;
        	} 
        } 
        
        for (AbstractCard c : cards_to_choose.group) {
        	UnlockTracker.markCardAsSeen(c.cardID);
        }

        upgrade_card_grid_have_appeared = true;
        
        AbstractDungeon.gridSelectScreen.open(cards_to_choose,
        		CARD_AMOUNT_TO_PICK_AT_MOST, "Testing", false);

        return;
	}
	
	public AbstractCard getCardOfAnyOtherClass(CardRarity rarity) {
		
		//make list of colors to not get, and add the player class to it
		ArrayList<CardColor> card_colors_to_avoid = new ArrayList<CardColor>();
		
		card_colors_to_avoid.add(CardColor.COLORLESS);
		card_colors_to_avoid.add(CardColor.CURSE);
		card_colors_to_avoid.add(AbstractDungeon.player.getCardColor());
		
		
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.infinite_spire_class_code)) {
			card_colors_to_avoid.add(infinitespire.patches.
						CardColorEnumPatch.CardColorPatch.INFINITE_BLACK);
		}
		
		AbstractCard card; 
		boolean card_is_of_a_color_to_avoid;

		do{
			card_is_of_a_color_to_avoid = false;
			
			card = CardLibrary.getAnyColorCard(rarity);
			
			for (CardColor bad_color: card_colors_to_avoid) {
				if (card.color == bad_color) {
					card_is_of_a_color_to_avoid = true;
					break;
				}
			}
			
		} while (card_is_of_a_color_to_avoid);

		return card.makeCopy();
	}
	
	/*public static void save(final SpireConfig config) {

    }
	
	public static void load(final SpireConfig config) {
		
    }
	
	public static void clear(final SpireConfig config) {

	}*/
	
	@Override
	public CustomRelic makeCopy() {
		return new KillianEngineAlpha();
	}

}
