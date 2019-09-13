package qcfpunch.powers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import qcfpunch.QCFPunch_MiscCode;

public class FuryPower extends AbstractPower {
	
	public static final String POWER_ID = QCFPunch_MiscCode.returnPrefix() +
			"Power_Fury";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	
	public static final Logger logger = LogManager.getLogger(FuryPower.class.getName());
	
	public FuryPower(AbstractCreature owner, int amount) {
		
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.type = AbstractPower.PowerType.BUFF;
		updateDescription();
		
		loadRegion("strength");
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		
		if (c.type == CardType.ATTACK) {
			
			flash();
			DamageInfo damage_info = new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS);
			
			switch (c.target) {
			
				case ENEMY:
					
					AbstractDungeon.actionManager.addToBottom(
							new DamageAction(m, damage_info));
					break;
					
				case ALL_ENEMY:
					
					int[] multi_damage = c.multiDamage;
					
					if (multi_damage == null) return;
					
					for (int i = 0; i < multi_damage.length; i++) {
						
						multi_damage[i] = amount;
						
					}
					
					AbstractDungeon.actionManager.addToBottom(
							new DamageAllEnemiesAction(
									AbstractDungeon.player,
									multi_damage, DamageType.THORNS,
									AbstractGameAction.AttackEffect.BLUNT_LIGHT));
					break;
					
				default:
					break;
			}
		}
	}


	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			AbstractDungeon.actionManager.addToBottom(
					new RemoveSpecificPowerAction(this.owner, this.owner,
							this.ID));
		}
	}

}