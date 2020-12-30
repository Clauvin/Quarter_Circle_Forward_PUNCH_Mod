package qcfpunch.relics.seth;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.cards.ui.NoCardThanks5;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class KillianEngineAlpha extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Killian_Engine_Alpha";
	
	public static final int CARD_AMOUNT_TO_CHOOSE_FROM = 25;
	public static final int CARD_AMOUNT_TO_PICK_AT_MOST = 5;

	public boolean upgrade_card_grid_have_appeared = false;
	public boolean remove_card_grid_have_appeared = false;
	public boolean is_done = true;
	
	private int amount_of_cards_added;
	
	public static String current_description;
	
	public KillianEngineAlpha() {
		super(ID, GraphicResources.LoadRelicImage(
				"Killian Engine Alpha - triple-yin - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + CARD_AMOUNT_TO_PICK_AT_MOST + 
				DESCRIPTIONS[1] + CARD_AMOUNT_TO_CHOOSE_FROM + 
				DESCRIPTIONS[2];
	}
	
	public String textForAddCardsGrid() {
		return DESCRIPTIONS[3] + CARD_AMOUNT_TO_PICK_AT_MOST + DESCRIPTIONS[4];
	}
	
	public String textForRemoveCardsGrid() {
		if (amount_of_cards_added > 1)
			return DESCRIPTIONS[5] + amount_of_cards_added + DESCRIPTIONS[6];
		else {
			return DESCRIPTIONS[5] + amount_of_cards_added + DESCRIPTIONS[7];
		}
	}

	public void update() {
	    super.update();
	    
	    if (!is_done) {
	    	
	    	if (canIAddTheChosenCardsToTheDeck()) {
	    	    	
	    		amount_of_cards_added = AbstractDungeon.
	    	    			gridSelectScreen.selectedCards.size();

	    		int amount_of_no_card_thanks = 0;
	    		
	    		for (int i = 0; i < amount_of_cards_added; i++) {
	    	    	
	    			if (AbstractDungeon.
    	    				gridSelectScreen.selectedCards.get(i).cardID == 
    	    				NoCardThanks5.ID) {
	    				amount_of_no_card_thanks++;
	    				continue;
	    			}
	    			
    	    		AbstractCard c = ((AbstractCard)AbstractDungeon.
    	    				gridSelectScreen.selectedCards.get(i)).makeCopy();
    	    		
    	    		AbstractDungeon.player.masterDeck.addToTop(c);
    	    		
    	    		for (AbstractRelic r : AbstractDungeon.player.relics) {
    	    	        r.onObtainCard(c);
    	    	    }
    	    		
    	    	    for (AbstractRelic r : AbstractDungeon.player.relics) {
    	    	    	r.onMasterDeckChange();
    	    	    }
    	    		
    	    	}
	    	      
    	    	AbstractDungeon.gridSelectScreen.selectedCards.clear();
    	    	
    	    	amount_of_cards_added -= amount_of_no_card_thanks;
    	    	
    	    	if (amount_of_cards_added == 0) {
    	    		is_done = true;
    	    		return;
    	    	}
    	    	
    	    	if (CardGroup.getGroupWithoutBottledCards(
    	    			AbstractDungeon.player.masterDeck.getPurgeableCards())
    	                .size() > 0) {
    	    		
    	    		AbstractDungeon.gridSelectScreen.open(
    	    			CardGroup.getGroupWithoutBottledCards(
    	    					AbstractDungeon.player.masterDeck
    	    				.getPurgeableCards()), amount_of_cards_added,
    	    			textForRemoveCardsGrid(),
    	    			false, false, false, false);
    	    		
    	    	}

    	    	remove_card_grid_have_appeared = true;
    	    	
    	    }
	    	    
    	    if (canIRemoveTheChosenCardsOfTheDeck()) {
    	    	    	    	
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
	    
	}
	
	public boolean canIAddTheChosenCardsToTheDeck() {
		
		return this.upgrade_card_grid_have_appeared && 
				!this.remove_card_grid_have_appeared &&
				!AbstractDungeon.isScreenUp &&
				!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty();
		
	}
	
	public boolean canIRemoveTheChosenCardsOfTheDeck() {
		
		return this.upgrade_card_grid_have_appeared && 
				this.remove_card_grid_have_appeared &&
				!AbstractDungeon.isScreenUp &&
				!AbstractDungeon.gridSelectScreen.
				selectedCards.isEmpty();
		
	}
	
	@Override
	public void onEquip() {
		
		is_done = false;
		
        CardGroup cards_to_choose = new CardGroup(
        		CardGroup.CardGroupType.UNSPECIFIED);
        
        for (int i = 0; i < CARD_AMOUNT_TO_CHOOSE_FROM; i++) {
        	
        	AbstractCard card = getCardOfAnyOtherClass(
        			AbstractDungeon.rollRarity());
          
        	theDuplicateCardTest(cards_to_choose, card);
          
        	if (!cards_to_choose.contains(card)) cards_to_choose.addToBottom(card);
        	else i--;
        	
        } 
        
        for (AbstractCard c : cards_to_choose.group) {
        	UnlockTracker.markCardAsSeen(c.cardID);
        }
        
        for (int i = 0; i < CARD_AMOUNT_TO_PICK_AT_MOST; i++) {
        	
        	cards_to_choose.addToBottom(new NoCardThanks5());
        
        }

        upgrade_card_grid_have_appeared = true;
        
        AbstractDungeon.gridSelectScreen.open(cards_to_choose,
        		CARD_AMOUNT_TO_PICK_AT_MOST, textForAddCardsGrid(), false);
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
				if ((card.color == bad_color)) {
					card_is_of_a_color_to_avoid = true;
					break;
				}
			}
			
			if (card.type == CardType.CURSE) {
				card_is_of_a_color_to_avoid = true;
				break;
			}
			
		} while (card_is_of_a_color_to_avoid);

		return card.makeCopy();
	}
	
	public void theDuplicateCardTest(CardGroup card_group, AbstractCard card) {
		
		boolean containsDupe = true;
    	while (containsDupe) {
    		containsDupe = false;
        
    		for (AbstractCard c : card_group.group) {
    			if (c.cardID.equals(card.cardID)) {
    				containsDupe = true;
    				card = getCardOfAnyOtherClass(
    	        			AbstractDungeon.rollRarity());
    			} 
    		} 
    	} 
		
	}
	
	@Override
	public CustomRelic makeCopy() {
		return new KillianEngineAlpha();
	}

}
