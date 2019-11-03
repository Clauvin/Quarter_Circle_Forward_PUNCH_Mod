package qcfpunch.relics.chun_li;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class SpikyBracers extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Spiky_Bracers";
	
	private static final int MINIMUM_WORKING_COST = 2;
	private static final int UPDATE_COST_BY = -1;
	private static final int UPDATE_COST_TEXT = -UPDATE_COST_BY;
	private static final int NUMBER_OF_CARDS_TO_APPLY_EFFECT = 2;
	
	public static AbstractCard[] cards_chosen;
	public static int number_of_cards_chosen = 0;
	
	public static boolean cards_are_selected = false;
	public boolean power_tip_updated = false;
	
	static Logger logger = LogManager.getLogger(SpikyBracers.class.getName());
	
	public SpikyBracers() {
		super(ID, GraphicResources.LoadRelicImage("spiky-bracers - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.HEAVY);
		number_of_cards_chosen = 0;
		cards_chosen = new AbstractCard[NUMBER_OF_CARDS_TO_APPLY_EFFECT];
	}
	
	public String getUpdatedDescription() {
		
		String base_description = DESCRIPTIONS[0] + NUMBER_OF_CARDS_TO_APPLY_EFFECT +
				DESCRIPTIONS[1] + UPDATE_COST_TEXT +
				DESCRIPTIONS[2];

		if (number_of_cards_chosen > 0 && cards_chosen != null) {
			base_description += DESCRIPTIONS[3];
			
			for (int i = 0; i < cards_chosen.length; i++) {
				if (cards_chosen[i] == null) {
					base_description += DESCRIPTIONS[4];
				} else {
					base_description += FontHelper.colorString(cards_chosen[i].name, "y");
				}
				if (i == 0) {
					base_description += DESCRIPTIONS[5];
				} else if (i == 1){
					base_description += DESCRIPTIONS[6];
				}

			}
		}
		
		return base_description;
	}
	
	public void updateTipPostCardsChosen() {
		String text_for_tip = getUpdatedDescription();
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, text_for_tip));
		initializeTips();
	}
	
	public void onUnequip() {
		resetRelic();
	}
	
	public void onEnterRoom(AbstractRoom room) {
		resetRelic();

	}
	
	public void onVictory() {
		resetRelic();
	}
	
	public void resetRelic() {
		if (cards_are_selected) {		
			cards_chosen = new AbstractCard[NUMBER_OF_CARDS_TO_APPLY_EFFECT];
			cards_are_selected = false;
			power_tip_updated = false;
			number_of_cards_chosen = 0;
			updateTipPostCardsChosen();
		}
	}
	
	public void atTurnStartPostDraw() {
		if (number_of_cards_chosen < NUMBER_OF_CARDS_TO_APPLY_EFFECT) {
			int amount_of_cards_in_hand = AbstractDungeon.player.hand.size();
			for (int i = 0; i < amount_of_cards_in_hand; i++) {
				if (cardCanReceiveEffect(AbstractDungeon.player.hand.getNCardFromTop(i))) {
					flash();
				}
			}
		}
	}
	
	public void onCardDraw(AbstractCard drawnCard) {
		if (number_of_cards_chosen < NUMBER_OF_CARDS_TO_APPLY_EFFECT) {
			if (cardCanReceiveEffect(drawnCard)) {
				flash();
			}
		}
	}
	
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (weStillNeedToMakeCardsCheaper()) {
			
			if (cardCanReceiveEffect(card)){
				if (card.cost >= MINIMUM_WORKING_COST) {
					AddCardToEffectedList(card);	
					
					card.modifyCostForCombat(UPDATE_COST_BY);
				}
				else if (card.energyOnUse >= MINIMUM_WORKING_COST) {
					AddCardToEffectedList(card);
				}
			}
		}
	}
	
	public boolean weStillNeedToMakeCardsCheaper() {
		return number_of_cards_chosen < cards_chosen.length &&
				number_of_cards_chosen < NUMBER_OF_CARDS_TO_APPLY_EFFECT;
	}
	
	public boolean cardCanReceiveEffect(AbstractCard card) {
		boolean is_skill = card.type == CardType.SKILL;
		boolean cost_equal_or_higher_than_2 = cardHasValidCostForRelic(card);
		
		boolean has_been_chosen_already = cardHasBeenChosenAlready(card);
		
		return is_skill && cost_equal_or_higher_than_2 && !has_been_chosen_already;
	}
	
	public void AddCardToEffectedList(AbstractCard card) {
		cards_chosen[number_of_cards_chosen] = card.makeSameInstanceOf();
		number_of_cards_chosen++;
		cards_are_selected = true;
		updateTipPostCardsChosen();		
	}
	
	public boolean cardHasValidCostForRelic(AbstractCard card) {
		
		if (card.cost >= MINIMUM_WORKING_COST) return true;
		else return false;
		
	}
	
	public static boolean cardHasBeenChosenAlready(AbstractCard card) {
		boolean has_been_chosen_already = false;
		
		for (AbstractCard card_chosen: cards_chosen) {
			if (card_chosen != null) {
				logger.info(card_chosen.name);
				if (card_chosen.uuid == card.uuid) {
					has_been_chosen_already = true;
					break;
				}
			}
		}
		
		logger.info("card has been chosen = " + has_been_chosen_already);
		return has_been_chosen_already;
	}
	
	public boolean canSpawn()
	{
		CardGroup powers;
		int number_of_powers_costing_2_or_more = 0;

		CardGroup skills;
		int number_of_skills_costing_2_or_more = 0;
		
		powers = AbstractDungeon.player.masterDeck.getPowers();
		number_of_powers_costing_2_or_more = countNumberOfValidCards(powers);
		
		skills = AbstractDungeon.player.masterDeck.getSkills();
		number_of_skills_costing_2_or_more = countNumberOfValidCards(skills);
		
		return (number_of_powers_costing_2_or_more + number_of_skills_costing_2_or_more)
					>= NUMBER_OF_CARDS_TO_APPLY_EFFECT; 
	}
	
	public int countNumberOfValidCards(CardGroup card_group) {
		int number_of_cards_costing_2_or_more = 0;
		
		card_group.sortByCost(false);
		
		for (int i = 0; i < card_group.size(); i++) {
			if (card_group.getNCardFromTop(i).cost >= MINIMUM_WORKING_COST)
				number_of_cards_costing_2_or_more += 1;
			 
		}
		
		return number_of_cards_costing_2_or_more;
	}
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new SpikyBracers();
	}

}
