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
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

//old code, stored in this commit for use later with a better relic
public class NecklaceOfSkulls extends CustomRelic
							  implements OnRemoveCardFromMasterDeckRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Necklace_of_Skulls";
	
	private static int amount_of_upgrades = 0;
	private static int current_amount_of_upgrading = 0;
	
	public static boolean is_player_choosing_a_card = false;
	public static boolean try_to_upgrade_cards = false;
	
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
	
	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		
		if (amount_of_upgrades == 0) {
			
			CardGroup upgradeable_cards = AbstractDungeon.player.
					masterDeck.getUpgradableCards();
			
			if (upgradeable_cards.size() > 0) {
				
				++amount_of_upgrades;
				try_to_upgrade_cards = true;
				
			}
			
		} else {
			++amount_of_upgrades;
		}
		
	}
	
	public void update()
	{
		super.update();
		
		if (try_to_upgrade_cards) {
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
					
					showVFX(card_chosen, i);
	            }
	            
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
								
				AbstractDungeon.overlayMenu.hideBlackScreen();
				AbstractDungeon.dynamicBanner.appear();
				AbstractDungeon.isScreenUp = false;
				
				is_player_choosing_a_card = false;
				
				ifThereAreUpgradesToDoTryToDoThem();
		    }
		}		
		
	}
	
	public void upgradingCards(CardGroup upgradeable_cards) {

		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		current_amount_of_upgrading = amount_of_upgrades;
		
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
	
	private void showVFX(AbstractCard card_chosen, int positioning) {
		AbstractDungeon.effectsQueue.add(
				new UpgradeShineEffect(
						(positioning+1) * Settings.WIDTH / 4.0F,
						Settings.HEIGHT / 2.0F));
		AbstractDungeon.effectsQueue.add(
				new ShowCardBrieflyEffect(
						card_chosen.makeStatEquivalentCopy(),
						(positioning+1) * Settings.WIDTH / 4.0F,
						Settings.HEIGHT / 2.0F));
	}
	
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
		return DESCRIPTIONS[0];
	}

	public boolean canSpawn() {
		return true;
	}
	
	public AbstractRelic makeCopy() {
		return new NecklaceOfSkulls();
	}

}
