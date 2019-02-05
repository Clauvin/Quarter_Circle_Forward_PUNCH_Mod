package ww_relics.relics.chun_li;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomRelic;

public class WhiteBoots extends CustomRelic implements ClickableRelic {
	public static final String ID = "WW_Relics_White_Boots";
	private static final int NUMBER_OF_USES_PER_FIGHT = 1;
	private static final int NUMBER_OF_CHOSEN_ATTACKS = 1;
	private static final int NUMBER_OF_MAXIMUM_COST = 1;
	private static final int NUMBER_OF_COPIES = 3;
	private static final int EFFECT_ON_COST_OF_THE_GENERATED_CARDS = -1;
	
	public int number_of_uses_left_in_this_fight;
	public int number_of_copies_left_to_use;
	public boolean player_activated;
	public boolean card_is_selected;
	public AbstractCard card_selected;
	public AbstractCard card_copied;
	
	public int original_cost;
	
	public int[] copied_cards_x_position = {100, 120, 120, 100};
	public int[] copied_cards_y_position = {100, 80, -80, -100};
	
	public boolean effect_happened = false;
	
	public static final Logger logger = LogManager.getLogger(WhiteBoots.class.getName());
	
	private boolean have_uses_left = false;
	private boolean is_on_combat = false;
	private boolean is_alive = false;
	private boolean turn_wont_end_soon = false;
	private boolean player_isnt_ending_turn = false;
	private boolean turn_havent_ended = false;
	private boolean has_an_attack_in_hand = false;
	private boolean attacks_are_0_or_1_cost = false;
	
	public WhiteBoots() {
		super(ID, "abacus.png", //add method for textures here.
				RelicTier.RARE, LandingSound.SOLID);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + NUMBER_OF_CHOSEN_ATTACKS +
				DESCRIPTIONS[3] + DESCRIPTIONS[4] + NUMBER_OF_COPIES + DESCRIPTIONS[5] +
				EFFECT_ON_COST_OF_THE_GENERATED_CARDS + DESCRIPTIONS[6];
	}
	
	public void atPreBattle() {
		number_of_uses_left_in_this_fight = NUMBER_OF_USES_PER_FIGHT;
		card_is_selected = false;
		card_selected = null;
		card_copied = null;
		player_activated = false;
		
		AbstractDungeon.gridSelectScreen.selectedCards.clear();
		
		have_uses_left = false;
		is_on_combat = false;
		is_alive = false;
		turn_wont_end_soon = false;
		player_isnt_ending_turn = false;
		turn_havent_ended = false;
		has_an_attack_in_hand = false;
		attacks_are_0_or_1_cost = false;
	}
	
	@Override
	public void onRightClick() {
		have_uses_left = number_of_uses_left_in_this_fight > 0;
		is_on_combat = AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
		is_alive = !AbstractDungeon.player.isDead;
		turn_wont_end_soon = !AbstractDungeon.player.endTurnQueued;
		player_isnt_ending_turn = !AbstractDungeon.player.isEndingTurn;
		turn_havent_ended = !AbstractDungeon.actionManager.turnHasEnded;
		has_an_attack_in_hand = AbstractDungeon.player.hand.getAttacks().
			size() > 0;
		attacks_are_0_or_1_cost = false; 
		
		logger.info("have_uses_left " + have_uses_left);
		logger.info("is_on_combat " + is_on_combat);
		logger.info("is_alive " + is_alive);
		logger.info("turn_wont_end_soon " + turn_wont_end_soon);
		logger.info("player_isnt_ending_turn " + player_isnt_ending_turn);
		logger.info("turn_havent_ended " + turn_havent_ended);
		logger.info("has_an_attack_in_hand " + has_an_attack_in_hand);
		
		
		if (have_uses_left && is_on_combat && is_alive && turn_wont_end_soon &&
			player_isnt_ending_turn && turn_havent_ended &&
			has_an_attack_in_hand) {
			
			CardGroup attacks = AbstractDungeon.player.hand.getAttacks();

			for (int i = 0; i < attacks.size(); i++) {
				int cost = attacks.getNCardFromTop(i).cost;
				if (cost <= NUMBER_OF_MAXIMUM_COST) {
					attacks_are_0_or_1_cost = true;
					break;
				}
			}
			
			logger.info("attacks_are_0_or_1_cost " + attacks_are_0_or_1_cost);
			
			if (attacks_are_0_or_1_cost) {
				
				number_of_copies_left_to_use = NUMBER_OF_COPIES;
				
				CardGroup list_of_all_hand_attacks = AbstractDungeon.player.hand.getPurgeableCards().
						getAttacks();
				CardGroup list_of_attacks = new CardGroup(CardGroupType.UNSPECIFIED);
				
				
				for (int i = 0; i < list_of_all_hand_attacks.size(); i++) {
					
					AbstractCard card = list_of_all_hand_attacks.getNCardFromTop(i);
					if (card.cost <= NUMBER_OF_MAXIMUM_COST) {
						list_of_attacks.addToTop(card);
						logger.info(list_of_attacks.size());
					}
					
				}
				
				if (list_of_attacks.size() >= 1) {
					player_activated = true;
				}
				
				if (list_of_attacks.size() == 1) {
					this.card_is_selected = true;
					this.card_selected = list_of_attacks.getTopCard(); 
				} else if (list_of_attacks.size() > 1){
					AbstractDungeon.gridSelectScreen.open(
						list_of_attacks, 1,
						this.DESCRIPTIONS[7] +
							NUMBER_OF_CHOSEN_ATTACKS + this.DESCRIPTIONS[3],
						false, false, false, false);
			    }
			}
		}
		
		logger.info("player_activated " + player_activated);
		logger.info("!this.card_is_selected " + !this.card_is_selected);
		logger.info("!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() " + 
				!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty());
	}
	
	public void update()
	{
		super.update();

		if ((have_uses_left) && (player_activated)){
			
			if ((!card_is_selected) && (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())) {
				number_of_uses_left_in_this_fight--;
				
				card_is_selected = true;
				card_selected = ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0));	
			}
			
			if (card_is_selected) {
				
				number_of_uses_left_in_this_fight--;
			
				card_copied = card_selected.makeCopy();
				if (card_selected.upgraded == true) card_copied.upgrade();
				
				original_cost = card_copied.cost;
				if (card_copied.cost > 0) card_copied.updateCost(- 1); 
				
				for (int i = 0; i < NUMBER_OF_COPIES; i++) {
					AbstractDungeon.actionManager.addToBottom(
							new MakeTempCardInDrawPileAction(
									card_copied, 1, true, true, false,
									copied_cards_x_position[i],
									copied_cards_y_position[i]));
				}
				
				AbstractDungeon.player.hand.moveToExhaustPile(card_selected);
				
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
		}
	}
	
	public void onPlayCard(AbstractCard c, AbstractMonster m)  {
		
		if ((card_is_selected) && (c.compareTo(card_copied) == 0)
				&& (c.isCostModified)){
			
			if (original_cost > c.cost) c.cost += 1;
			c.isCostModified = false;
			number_of_copies_left_to_use--;
			
			if (number_of_copies_left_to_use == 0) {
				card_copied = null;
				card_selected = null;
				card_is_selected = false;
			}
			
		}
		
	}
	
	public boolean canSpawn()
	{
		return AbstractDungeon.player.masterDeck.getAttacks().size() > 0;
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new WhiteBoots();
	}
}