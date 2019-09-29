package qcfpunch.relics.dhalsim;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_GoodBehaviorLine;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.cards.ui.Finished;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class LotusStatue extends CustomRelic
							implements OnRemoveCardFromMasterDeckRelic,
									   ClickableRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Lotus_Statue";
	
	private static boolean using_this_relic_power_to_remove = false;
	private static final int INITIAL_AMOUNT_OF_CHARGES = 0;
	private static final int MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE = 1;
	private static final int CHARGES_GAINED_BY_REMOVAL = 1;

	private static boolean right_click_in_relic_here_havent_happened = true;
	private static boolean currently_choosing_removable_cards;
	private static int max_amount_of_cards_to_remove;
	private static boolean cards_to_remove_yet = false;
	
	private static final int CAN_SPAWN_BEFORE_FLOOR = 51;
	
	public static QCFPunch_GoodBehaviorLine behavior_line; 
		
	public LotusStatue() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.MAGICAL);
		
		this.counter = INITIAL_AMOUNT_OF_CHARGES;
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE +
				DESCRIPTIONS[1] + CHARGES_GAINED_BY_REMOVAL +
				DESCRIPTIONS[2];
	}
	
	public String getCardGridDescription() {
		return DESCRIPTIONS[3] + MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE +
				DESCRIPTIONS[4];
	}

	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		
		if (!using_this_relic_power_to_remove) {
			this.counter += 1;
			flash();
		}
		
	}
	
	public void onEnterRoom(AbstractRoom room) {
		if ((room instanceof RestRoom) && (counter > 0)) flash();
		
		using_this_relic_power_to_remove = false;
		right_click_in_relic_here_havent_happened = true;
		currently_choosing_removable_cards = false;
		cards_to_remove_yet = false;
		max_amount_of_cards_to_remove = 0;
	}

	@Override
	public void onRightClick() {
		if (right_click_in_relic_here_havent_happened) {
			if ((this.counter > 0) && (haveCardsToRemove())) {
				if (restOptionsHaventBeenPickedUpYet()) {
					right_click_in_relic_here_havent_happened = false;
					removingCards();
				}
			}
		}
	}

	private boolean haveCardsToRemove() {
		CardGroup removable_cards = AbstractDungeon.player.masterDeck.
					getPurgeableCards();
		
		removable_cards = CardGroup.getGroupWithoutBottledCards(removable_cards);
		
		return removable_cards.size() > 0;
	}
	
	private boolean restOptionsHaventBeenPickedUpYet() {
		
		return (AbstractDungeon.getCurrRoom() instanceof RestRoom && 
				AbstractDungeon.getCurrRoom().phase == 
					AbstractRoom.RoomPhase.INCOMPLETE &&
				CampfireUI.hidden == false);
		
	}
	
	@SuppressWarnings("static-access")
	private void removingCards() {
		
		behavior_line.lotusStatueWorkingHere();
		
		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		AbstractDungeon.getCurrRoom().phase = RoomPhase.INCOMPLETE;
		
		CardGroup removable_cards = AbstractDungeon.player.masterDeck.
				getPurgeableCards();
		
		removable_cards = CardGroup.getGroupWithoutBottledCards(removable_cards);
		
		removable_cards.addToBottom(new Finished());
		
		max_amount_of_cards_to_remove = 1;
		
		AbstractDungeon.gridSelectScreen.open(removable_cards,
				max_amount_of_cards_to_remove,
				getCardGridDescription(), false, false, false, true);
		
		currently_choosing_removable_cards = true;
		
	}
	
	@SuppressWarnings("static-access")
	public void update()
	{
		super.update();
		if (!behavior_line.time_of_lotus_statue) behavior_line.lotusStatueTick();
		
		if (behavior_line.canLotusStatueWork()) {
			if ((cards_to_remove_yet) && (!currently_choosing_removable_cards))  {
				if ((this.counter > 0) && (haveCardsToRemove())) {
					if (restOptionsHaventBeenPickedUpYet()) {
						right_click_in_relic_here_havent_happened = false;
						removingCards();
					}
				}
			} else {
				if (currently_choosing_removable_cards) {
					if (isTimeToRemoveTheChosenCard()) {
				    
			            flash();
						
						AbstractCard card_chosen = getCardToRemove();
						
						if (!(card_chosen instanceof Finished)) {
							using_this_relic_power_to_remove = true;
							removeAndShowChosenCard(card_chosen);
							using_this_relic_power_to_remove = false;
							
							spendChargesForRemovedCard();
						}
						
						AbstractDungeon.gridSelectScreen.selectedCards.clear();
						
						AbstractDungeon.overlayMenu.hideBlackScreen();
						AbstractDungeon.dynamicBanner.appear();
						AbstractDungeon.isScreenUp = false;

						currently_choosing_removable_cards = false;
						behavior_line.lotusStatueFinished();
						
						if (!(card_chosen instanceof Finished) &&
							((this.counter > 0) && (haveCardsToRemove()))) {
								cards_to_remove_yet = true;
						} else {
							cards_to_remove_yet = false;
						}
				    }
				}
			}
		}
	}
		
	private static boolean isTimeToRemoveTheChosenCard() {
		
		boolean i_am_in_a_rest_room = AbstractDungeon.getCurrRoom()
				instanceof RestRoom;
		boolean card_to_remove_have_been_chosen = 
				AbstractDungeon.gridSelectScreen.selectedCards.size() == 1;
		
		return currently_choosing_removable_cards && 
				i_am_in_a_rest_room && card_to_remove_have_been_chosen;
	}
	
	private static AbstractCard getCardToRemove() {
		return AbstractDungeon.gridSelectScreen.selectedCards.get(0);
	}
	
	private static void removeAndShowChosenCard(AbstractCard chosen_card) {
    
		AbstractDungeon.player.masterDeck.removeCard(chosen_card);
		
		AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(chosen_card,
						(Settings.WIDTH / 2),
						(Settings.HEIGHT / 2)));
		
	}
	
	private void spendChargesForRemovedCard() {	
		this.counter -= 1;
		if (this.counter < 0) this.counter = 0;
	}
	
	public boolean canSpawn() {
		return (AbstractDungeon.floorNum < CAN_SPAWN_BEFORE_FLOOR) ||
					(Settings.isEndless);
	}
	
	public AbstractRelic makeCopy() {
		return new LotusStatue();
	}
	
}
