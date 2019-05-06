package ww_relics.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class RiskyOffensivePower extends AbstractPower {

	public static final String POWER_ID = "WW_Relics:Power_Risky_Offensive";
	private static final PowerStrings powerStrings = 
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	
	public RiskyOffensivePower(AbstractCreature owner, int amount)
	{
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.type = AbstractPower.PowerType.DEBUFF;
		
		updateDescription();
		
		loadRegion("frail");
	}
	
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.type == CardType.SKILL) {
			int less_strength = -amount;
			
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
					 new StrengthPower(owner, less_strength)));
			AbstractDungeon.actionManager.addToBottom(
					new RemoveSpecificPowerAction(owner, owner, this));
		}
	}
	
}
