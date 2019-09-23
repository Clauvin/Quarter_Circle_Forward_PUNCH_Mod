package qcfpunch.relics.dhalsim;

import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.actions.PickACardToAddToShuffleInDrawPileAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class NecklaceOfSkulls extends CustomRelic
							  implements OnRemoveCardFromMasterDeckRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Necklace_of_Skulls";
	
	private static int STARTING_AMOUNT_OF_CHARGES = 1;
	private static int MAX_AMOUNT_OF_CHARGES = 3;
	
	public NecklaceOfSkulls() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
		
		this.counter = STARTING_AMOUNT_OF_CHARGES;
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + STARTING_AMOUNT_OF_CHARGES +
				DESCRIPTIONS[1] + MAX_AMOUNT_OF_CHARGES +
				DESCRIPTIONS[2];
	}
	
	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		if (card.type == CardType.CURSE) {
			if (counter < MAX_AMOUNT_OF_CHARGES) {
				++counter;
				flash();
			}
		}
	}
	
	@Override
	public void atBattleStart() {
		if (this.counter == 1) shuffleOneRandomPowerToDrawPile();
		else if (this.counter > 1) shuffleOneChosenRandomPowerToDrawPile();
	}
	
	private void shuffleOneRandomPowerToDrawPile() {
		
		AbstractCard new_power =
				AbstractDungeon.returnTrulyRandomCardInCombat
					(CardType.POWER);
		
		flash();
		
		AbstractDungeon.actionManager.addToBottom(
        		new MakeTempCardInDrawPileAction(new_power, 1,
        				true, true));

	}
	
	private void shuffleOneChosenRandomPowerToDrawPile() {
		
		CardGroup powers = new CardGroup(CardGroupType.UNSPECIFIED);
		
		for (int i = 0; i < counter; i++) {
			powers.addToBottom(
					AbstractDungeon.returnTrulyRandomCardInCombat(
							AbstractCard.CardType.POWER).makeCopy());
		}
		
		AbstractDungeon.actionManager.addToBottom(
				new PickACardToAddToShuffleInDrawPileAction(powers, "Testing"));
		
	}
	
	
	
	public boolean canSpawn() {
		return true;
	}
	
	public AbstractRelic makeCopy() {
		return new NecklaceOfSkulls();
	}

}
