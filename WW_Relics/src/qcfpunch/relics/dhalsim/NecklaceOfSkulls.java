package qcfpunch.relics.dhalsim;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class NecklaceOfSkulls extends CustomRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Necklace_Of_Skulls";
	
	private static int STARTING_AMOUNT_OF_CHARGES = 1;
	private static int MAX_AMOUNT_OF_CHARGES = 3;
	
	public NecklaceOfSkulls() {
		super(ID, GraphicResources.LoadRelicImage("Temp Army Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
		
		this.counter = STARTING_AMOUNT_OF_CHARGES;
	}
	
	@Override
	public void atBattleStart() {
		if (this.counter == 1) {
			
			AbstractCard new_power =
					AbstractDungeon.returnTrulyRandomCardInCombat
						(CardType.POWER);
			
			flash();
			
			AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(new_power));
			
		}
	}
	
	
	
	public boolean canSpawn() {
		return true;
	}
	
	public AbstractRelic makeCopy() {
		return new NecklaceOfSkulls();
	}
	
}
