package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import qcfpunch.QCFPunch_MiscCode;

public class FlamingAction extends AbstractGameAction {

	public AbstractCreature monster_to_affect;
	public int monster_hp_before;
	public int monster_hp_now;
	public int minimum_damage;
	
	public FlamingAction(AbstractCreature monster_to_affect, int hp_before, int minimum_damage) {

		this.monster_to_affect = monster_to_affect;
		this.monster_hp_before = hp_before;
		this.minimum_damage = minimum_damage;
		
		actionType = ActionType.DAMAGE;
	}
	
	@Override
	public void update() {
		
		if (!this.isDone) {
			
			if (!monster_to_affect.isDead) {
				
				monster_hp_now = monster_to_affect.currentHealth;
				
				int damage_to_apply = monster_hp_before - monster_hp_now;
				
				if (damage_to_apply >= minimum_damage) {
					applyFlamesDamage(damage_to_apply);
				} else {
					applyFlamesDamage(minimum_damage);
				}
				
				
				removeFlamingPower();
				
			}

			this.isDone = true;
		}

	}
	
	public void applyFlamesDamage(int damage_to_apply) {
		AbstractDungeon.actionManager.addToBottom(
				new DamageAction(monster_to_affect,
								createDamageInfo(damage_to_apply/2),
								AttackEffect.FIRE));
		AbstractDungeon.actionManager.addToBottom(
				new DamageAction(monster_to_affect,
								createDamageInfo(damage_to_apply/2 + damage_to_apply%2),
								AttackEffect.FIRE));
	}
	
	public DamageInfo createDamageInfo(int damageAmount) {
		return new DamageInfo(AbstractDungeon.player, damageAmount,
				DamageType.HP_LOSS);
	}
	
	public void removeFlamingPower() {
		AbstractDungeon.actionManager.addToBottom(
				new RemoveSpecificPowerAction(AbstractDungeon.player,
						AbstractDungeon.player,
						AbstractDungeon.player.getPower(QCFPunch_MiscCode.returnPrefix() + "Power_Flaming")));
	}

}
