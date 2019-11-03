package qcfpunch.powers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import qcfpunch.QCFP_Misc;

public class PotentialPower extends TwoAmountPower {

	public static final String POWER_ID = QCFP_Misc.returnPrefix() + "Power_Potential";
	private static final PowerStrings powerStrings = 
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	
	public int numerator;
	public int denominator;
	
	public static final int ENERGY_GIVEN_WHEN_FULL = 1;
	
	public static final Logger logger = LogManager.getLogger(PotentialPower.class.getName());
	
	public PotentialPower(AbstractCreature owner, int numerator, int denominator) {
		
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		setFraction(numerator, denominator);
		this.type = AbstractPower.PowerType.BUFF;
		
		updateDescription();
		
		loadRegion("energized_green");
	}

	public void setFraction(int numerator, int denominator) {
		setNumerator(numerator);
		setDenominator(denominator);
	}

	public void setNumerator(int value) {
		this.numerator = value;
		this.amount2 = value;
	}
	
	public int getNumerator() {
		return numerator;
	}
	
	public void setDenominator(int value) {
		this.denominator = value;
		this.amount = value;
	}
	
	public int getDenominator() {
		return denominator;
	}
	
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + this.numerator + DESCRIPTIONS[1] + this.denominator +
				DESCRIPTIONS[2];
	}
	
	@Override
	public void stackPower(int stackAmount) {

	}
	
	@Override
	public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

		if (canWeMergePowers(power)) {
				
			PotentialPower the_new_potential = (PotentialPower) power;

			sumWithOtherPotential(the_new_potential);

			updateDescription();
			
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction
					(this.owner, this.owner, the_new_potential));
		}
		
		ifWholePartsExistThenTransformThemIntoEnergy();
		
		ifEmptyFractionThenRemoveThis();
	}
	
	public boolean fractionEqualOrBiggerThanOne() {
		return getNumerator() >= getDenominator();
	}
	
	public boolean canWeMergePowers(AbstractPower power) {
		
		boolean power_is_PotentialPower = power.ID == PotentialPower.POWER_ID;
		boolean power_is_not_THIS_power = power.hashCode() != this.hashCode();
		boolean both_powers_have_the_same_owner = this.owner == power.owner;
		
		return power_is_PotentialPower && power_is_not_THIS_power && both_powers_have_the_same_owner;
	}
	
	public void sumWithOtherPotential(PotentialPower other) {
		
		if (this.denominator == other.denominator) {
			setNumerator(this.numerator + other.numerator);
		} else {
			logger.info("Sum between Potentials with different denominators is not implemented.");
		}
	}
	
	public void ifWholePartsExistThenTransformThemIntoEnergy() {
		while (fractionEqualOrBiggerThanOne()){
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GIVEN_WHEN_FULL));
			
			setNumerator(getNumerator() - getDenominator()); 
		}
	}
	
	public void ifEmptyFractionThenRemoveThis() {
		if (getNumerator() <= 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction
					(this.owner, this.owner, this));
		}
	}
	
}
