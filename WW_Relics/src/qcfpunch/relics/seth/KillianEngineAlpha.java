package qcfpunch.relics.seth;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class KillianEngineAlpha extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Killian_Engine_Alpha";
	
	public static final int CARD_AMOUNT_TO_CHOOSE_FROM = 15;
	public static final int CARD_AMOUNT_TO_PICK_AT_MOST = 5;

	public static String current_description;
	
	public KillianEngineAlpha() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public void onEquip() {
		
        CardGroup cards_to_choose = new CardGroup(
        		CardGroup.CardGroupType.UNSPECIFIED);

        for (int i = 0; i < 20; i++) {
        	AbstractCard card = AbstractDungeon.getCard(
        		  AbstractDungeon.rollRarity()).makeCopy();
          
        	boolean containsDupe = true;
        	while (containsDupe) {
        		containsDupe = false;
            
        		for (AbstractCard c : cards_to_choose.group) {
        			if (c.cardID.equals(card.cardID)) {
        				containsDupe = true;
        				card = AbstractDungeon.getCard(
        						AbstractDungeon.rollRarity()).makeCopy();
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
        AbstractDungeon.gridSelectScreen.open(cards_to_choose,
        		CARD_AMOUNT_TO_PICK_AT_MOST, "Testing", false);
        return;
		
		//choose character
		//CardColor chosen_color = AbstractDungeon.player.getCardColor();
		
		//store info
		
		/*CardGroup rare_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		CardGroup uncommon_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		CardGroup common_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		
		makeCardGroupOfAColor(chosen_color, rare_extra_pool,
				uncommon_extra_pool, common_extra_pool);
		
		for (int i = 0; i < rare_extra_pool.size(); i++) {
			
			AbstractDungeon.rareCardPool.addToBottom(
					rare_extra_pool.getNCardFromTop(i));
			
		}
		
		for (int i = 0; i < uncommon_extra_pool.size(); i++) {
			
			AbstractDungeon.uncommonCardPool.addToBottom(
					uncommon_extra_pool.getNCardFromTop(i));
			
		}
		
		for (int i = 0; i < common_extra_pool.size(); i++) {
			
			AbstractDungeon.commonCardPool.addToBottom(
					common_extra_pool.getNCardFromTop(i));
			
		}*/
		
	}
	
	/*@SuppressWarnings("incomplete-switch")
	public void makeCardGroupOfAColor(
			CardColor card_color,
			CardGroup rare_extra_pool,
			CardGroup uncommon_extra_pool,
			CardGroup common_extra_pool) {

		for (Map.Entry<String, AbstractCard> a_card : CardLibrary.cards.entrySet()) {
			AbstractCard one_card = a_card.getValue();
			
			if (QCFP_Misc.cardIsOfChosenColor(one_card, card_color)) {		
				
				switch (one_card.rarity) {
					case RARE:
						rare_extra_pool.addToBottom((AbstractCard)one_card);
						break;
				    case UNCOMMON: 
				    	uncommon_extra_pool.addToBottom((AbstractCard)one_card);
				    	break;
				    case COMMON:
				    	common_extra_pool.addToBottom((AbstractCard)one_card);
				    	break;
				}
					
			}
		}		
		
	}*/
	
	public static void save(final SpireConfig config) {

    }
	
	public static void load(final SpireConfig config) {
		
    }
	
	public static void clear(final SpireConfig config) {

	}
	
	@Override
	public CustomRelic makeCopy() {
		return new KillianEngineAlpha();
	}

}
