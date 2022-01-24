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
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

//Currently won't work with Vampires event
//since Vampires event doesn't use the function we have a hook
//to activate the effect 
public class OldNecklaceOfSkulls extends CustomRelic
							  implements OnRemoveCardFromMasterDeckRelic {

	public static final String ID = QCFP_Misc.returnPrefix() +
			"Old_Necklace_of_Skulls";

	private static int amount_of_upgrades = 0;
	private static int current_amount_of_upgrading = 0;
	
	public static boolean is_player_choosing_a_card = false;
	public static boolean try_to_upgrade_cards = false;
	public static boolean waiting_to_upgrade = false;
	
	public static QCFPunch_GoodBehaviorLine behavior_line; 
	
	public static final Logger logger = LogManager.getLogger(
			NecklaceOfSkulls.class.getName());
	
	public OldNecklaceOfSkulls() {
		super(ID, GraphicResources.
				LoadRelicImage("Necklace of Skulls - death-skull - sbed - CC BY 3.0.png"),
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
		if (!behavior_line.time_of_necklace_of_skulls)
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
						
						if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.conspire_class_code)) {
							conspire.relics.InfiniteJournal.
								upgradeCard(card_chosen);
						} else {
				    		card_chosen.upgrade();
						}
						
						if (current_amount_of_upgrading == 1) {
							showVFX(card_chosen, i, true);
						} else {
							showVFX(card_chosen, i, false);
						}
		            }
		            
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
									
					AbstractDungeon.overlayMenu.hideBlackScreen();
					AbstractDungeon.dynamicBanner.hide();
					AbstractDungeon.isScreenUp = false;
					
					is_player_choosing_a_card = false;
					behavior_line.necklaceOfSkullsFinished();
					
					ifThereAreUpgradesToDoSetToDoThem();
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
	private void ifThereAreUpgradesToDoSetToDoThem() {
		
		amount_of_upgrades -= current_amount_of_upgrading;
		current_amount_of_upgrading = 0;
		
		if ((!behavior_line.canNecklaceOfSkullsWork()) &&
				(amount_of_upgrades > 0)){
			waiting_to_upgrade = true;
		}
		
	}
	
	@SuppressWarnings("static-access")
	private void ifThereAreUpgradesToDoTryToDoThem() {
		
		amount_of_upgrades -= current_amount_of_upgrading;
		current_amount_of_upgrading = 0;
		
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
		return new OldNecklaceOfSkulls();
	}

}
