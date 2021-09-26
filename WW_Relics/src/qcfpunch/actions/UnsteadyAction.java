package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UnsteadyAction extends AbstractGameAction {

	public static AbstractCreature affected;
	public static DamageInfo damage_to_apply;
	
	public UnsteadyAction(AbstractCreature target, DamageInfo info) {

		affected = target;
		damage_to_apply = info;
		actionType = ActionType.SPECIAL;
		attackEffect = AttackEffect.SHIELD;
		
		setValues(target, target);
	
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			
			float blockAmount = target.currentBlock;
			
			AdjustToBlockSmallerThanDamage(blockAmount);

			if (blockAmount > 0) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(affected, damage_to_apply));
			}

			this.isDone = true;
		}

	}
	
	public void AdjustToBlockSmallerThanDamage(float blockAmount) {
		if (blockAmount < damage_to_apply.base) {
			
			damage_to_apply.base = (int)blockAmount;
			damage_to_apply.output = (int)blockAmount;
		}
	}

}
