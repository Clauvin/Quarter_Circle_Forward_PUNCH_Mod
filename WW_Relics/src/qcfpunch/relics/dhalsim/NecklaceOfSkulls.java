package qcfpunch.relics.dhalsim;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_GoodBehaviorLine;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

//Currently won't work with Vampires event
//since Vampires event doesn't use the function we have a hook
//to activate the effect 
public class NecklaceOfSkulls extends CustomRelic
							  implements OnRemoveCardFromMasterDeckRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Necklace_of_Skulls";
	
	private static int amount_of_upgrades = 0;
	private static int current_amount_of_upgrading = 0;
	
	public static boolean is_player_choosing_a_card = false;
	public static boolean try_to_upgrade_cards = false;
	public static boolean waiting_to_upgrade = false;
	
	public static QCFPunch_GoodBehaviorLine behavior_line; 
	
	public static final Logger logger = LogManager.getLogger(
			NecklaceOfSkulls.class.getName());
	
	public NecklaceOfSkulls() {
		super(ID, GraphicResources.
				LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		
		if (amount_of_upgrades == 0) {
			
			CardGroup upgradeable_cards = AbstractDungeon.player.
					masterDeck.getUpgradableCards();
			
			if (upgradeable_cards.size() > 0) {
				
				++amount_of_upgrades;
				try_to_upgrade_cards = true;
				behavior_line.necklaceOfSkullsWorkingHere();
				
			}
			
		} else {
			++amount_of_upgrades;
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void update()
	{
		super.update();
		behavior_line.necklaceOfSkullsTick();
		
		if (behavior_line.canNecklaceOfSkullsWork()) {
			if (waiting_to_upgrade) {
				waiting_to_upgrade = false;
				ifThereAreUpgradesToDoTryToDoThem();
			}
			else if (try_to_upgrade_cards) {
				if (!AbstractDungeon.gridSelectScreen.confirmScreenUp) {
					CardGroup upgradeable_cards = AbstractDungeon.player.
							masterDeck.getUpgradableCards();
					
					upgradingCards(upgradeable_cards);
				}
			}
			else if (is_player_choosing_a_card) {
				if (isTimeToUpgradeTheChosenCards())
			    {
		            flash();
					
		            ArrayList<AbstractCard> cards_chosen = getCardsToUpgrade();
		            
		            for (int i = 0; i < current_amount_of_upgrading; i++) {
		            	AbstractCard card_chosen = cards_chosen.get(i);
						
						AbstractDungeon.player.bottledCardUpgradeCheck(card_chosen);
						card_chosen.upgrade();
						
						if (current_amount_of_upgrading == 1) {
							showVFX(card_chosen, i, true);
						} else {
							showVFX(card_chosen, i, false);
						}
		            }
		            
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
									
					AbstractDungeon.overlayMenu.hideBlackScreen();
					AbstractDungeon.dynamicBanner.appear();
					AbstractDungeon.isScreenUp = false;
					
					is_player_choosing_a_card = false;
					behavior_line.necklaceOfSkullsFinished();
					
					ifThereAreUpgradesToDoTryToDoThem();
			    }
			}		
		}
	}
	
	public void upgradingCards(CardGroup upgradeable_cards) {

		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		current_amount_of_upgrading = 1;
		
		AbstractDungeon.gridSelectScreen.open(upgradeable_cards,
				current_amount_of_upgrading,
				getCardGridDescription(), false, false, false, false);
		
		is_player_choosing_a_card = true;
		try_to_upgrade_cards = false;
		
	}

	private boolean isTimeToUpgradeTheChosenCards() {
		return AbstractDungeon.gridSelectScreen.selectedCards.size()
				>= current_amount_of_upgrading;
	}
	
	private void showVFX(AbstractCard card_chosen, int positioning,
			boolean only_card) {
		
		int true_positioning;
		
		if (only_card) {
			true_positioning = 2;
		} else {
			true_positioning = positioning+1;
		}
		
		AbstractDungeon.effectsQueue.add(
				new UpgradeShineEffect(
						true_positioning * Settings.WIDTH / 4.0F,
						Settings.HEIGHT / 2.0F));
		AbstractDungeon.effectsQueue.add(
				new ShowCardBrieflyEffect(
						card_chosen.makeStatEquivalentCopy(),
						true_positioning * Settings.WIDTH / 4.0F,
						Settings.HEIGHT / 2.0F));
	}
	
	@SuppressWarnings("static-access")
	private void ifThereAreUpgradesToDoTryToDoThem() {
		
		amount_of_upgrades -= current_amount_of_upgrading;
		current_amount_of_upgrading = 0;
		
		if (!behavior_line.canNecklaceOfSkullsWork()) {
			waiting_to_upgrade = true;
		}
		
		if (amount_of_upgrades > 0) {
			
			CardGroup upgradeable_cards = 
					AbstractDungeon.player.masterDeck.getUpgradableCards();
			
			if (upgradeable_cards.size() > 0)
				upgradingCards(upgradeable_cards);
			
		}
		
	}
	

	
	private static ArrayList<AbstractCard> getCardsToUpgrade() {	
		ArrayList<AbstractCard> chosen_cards = AbstractDungeon.gridSelectScreen.
				selectedCards;
		
		return chosen_cards;
	}
	
	private String getCardGridDescription() {
		return DESCRIPTIONS[1];
	}

	public boolean canSpawn() {
		return true;
	}
	
	public AbstractRelic makeCopy() {
		return new NecklaceOfSkulls();
	}

}
