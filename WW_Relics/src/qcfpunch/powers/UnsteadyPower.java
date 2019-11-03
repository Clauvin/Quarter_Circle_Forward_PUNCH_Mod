package qcfpunch.powers;

import org.apache.logging.log4j.*;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import qcfpunch.QCFP_Misc;
import qcfpunch.actions.UnsteadyAction;

public class UnsteadyPower extends AbstractPower {

	public static final String POWER_ID = QCFP_Misc.returnPrefix() + "Power_Unsteady";
	private static final PowerStrings powerStrings = 
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	
	public static final Logger logger = LogManager.getLogger(UnsteadyPower.class.getName());
	
	public DamageInfo unsteady_block_reducer;
	
	public UnsteadyPower(AbstractCreature owner, int amount)
	{
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.type = AbstractPower.PowerType.DEBUFF;
		
		updateDescription();
		
		loadRegion("frail");

		unsteady_block_reducer = new DamageInfo(owner, amount, DamageType.NORMAL);
		unsteady_block_reducer.name = "Unsteady";
	}
	
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		
		if (!isPlayer) {
			AbstractDungeon.actionManager.addToBottom(new UnsteadyAction(owner, unsteady_block_reducer));
		}

	}
	  
}
