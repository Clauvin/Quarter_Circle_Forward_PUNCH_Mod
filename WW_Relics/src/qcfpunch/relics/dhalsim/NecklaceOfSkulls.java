package qcfpunch.relics.dhalsim;

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
	
	public static boolean is_player_choosing_a_card = false;
	public static boolean try_to_upgrade_cards = false;
	
	public NecklaceOfSkulls() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);

	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		
		CardGroup upgradeable_cards = AbstractDungeon.player.
				masterDeck.getUpgradableCards();
		
		if (upgradeable_cards.size() > 0) {
			
			try_to_upgrade_cards = true;
			
		}
		
	}
	
	public void upgradingCards(CardGroup upgradeable_cards) {

		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		AbstractDungeon.gridSelectScreen.open(upgradeable_cards,
				1,
				getCardGridDescription(), false, false, true, false);
		
		is_player_choosing_a_card = true;
		try_to_upgrade_cards = false;
		
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
			if (isTimeToUpgradeTheChosenCard())
		    {
	            flash();
				
				AbstractCard card_chosen = getCardToUpgrade();
				
				AbstractDungeon.effectsQueue.add(
						new UpgradeShineEffect(
								Settings.WIDTH / 2.0F,
								Settings.HEIGHT / 2.0F));
				AbstractDungeon.player.bottledCardUpgradeCheck(card_chosen);
				card_chosen.upgrade();
				AbstractDungeon.effectsQueue.add(
						new ShowCardBrieflyEffect(
								card_chosen.makeStatEquivalentCopy()));
				
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				
				AbstractDungeon.overlayMenu.hideBlackScreen();
				AbstractDungeon.dynamicBanner.appear();
				AbstractDungeon.isScreenUp = false;
				is_player_choosing_a_card = false;
				
		    }
		}		
		
	}
	
	private boolean isTimeToUpgradeTheChosenCard() {
		
		return AbstractDungeon.gridSelectScreen.selectedCards.size() >= 1;
		
	}
	
	private static AbstractCard getCardToUpgrade() {	
		AbstractCard chosen_card = AbstractDungeon.gridSelectScreen.
				selectedCards.get(0);
		
		return chosen_card;
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
