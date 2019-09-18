package qcfpunch.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import qcfpunch.QCFPunch_MiscCode;

public class WildHerbsOintmentPower extends AbstractPower {
	
	public static final String POWER_ID = QCFPunch_MiscCode.returnPrefix() +
			"Power_Wild_Herbs_Ointment";
	private static final PowerStrings powerStrings = 
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public int amount_of_Max_HP_to_add;
	public int maximum_HP_left_for_effect_to_trigger;
	
	public WildHerbsOintmentPower(AbstractCreature owner,
			int amount_of_Max_HP_to_add, int maximum_HP_left_for_effect_to_trigger)
	{
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = 0;
		this.type = AbstractPower.PowerType.BUFF;
		
		this.amount_of_Max_HP_to_add = amount_of_Max_HP_to_add;
		this.maximum_HP_left_for_effect_to_trigger =
				maximum_HP_left_for_effect_to_trigger;
		
		updateDescription();
		
		loadRegion("regen");
	}
	
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + maximum_HP_left_for_effect_to_trigger +
				DESCRIPTIONS[1] + amount_of_Max_HP_to_add +
				DESCRIPTIONS[2];
	}
	
	@Override
	public void onVictory() {
		if (owner.currentHealth <= maximum_HP_left_for_effect_to_trigger) {
			this.owner.increaseMaxHp(amount_of_Max_HP_to_add, true);
		}
	}
}
