package qcfpunch.powers;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import qcfpunch.QCFP_Misc;
import qcfpunch.actions.FlamingAction;

public class FlamingPower extends AbstractPower {

	public static final String POWER_ID = QCFP_Misc.returnPrefix() + "Power_Flaming";
	private static final PowerStrings powerStrings = 
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	
	public int MINIMUM_DAMAGE = 2;
	
	public boolean fire_burned_enemy = false;
	public AbstractMonster enemy_targeted;
	public int hp_before = 0;
	
	public FlamingPower(AbstractCreature owner, int amount, int minimum_damage)
	{
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.type = AbstractPower.PowerType.BUFF;
		this.MINIMUM_DAMAGE = minimum_damage;
		
		updateDescription();
		
		loadRegion("firebreathing");
	}
	
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + MINIMUM_DAMAGE + DESCRIPTIONS[1];
	}
	
	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (cardCanTriggerEffect(card)) {
			fire_burned_enemy = true;
			enemy_targeted = m;
			if (enemy_targeted == null) {
				enemy_targeted = getTheOneEnemyOutThere();
			}
			if (enemy_targeted != null) {
				hp_before = enemy_targeted.currentHealth;
			}
			
		}
	}
	
	public AbstractMonster getTheOneEnemyOutThere() {
		ArrayList<AbstractMonster> monsters_in_the_fight = AbstractDungeon.getCurrRoom().monsters.monsters;
		
		for (int i = 0; i < monsters_in_the_fight.size(); i++) {
			
			if (!monsters_in_the_fight.get(i).isDead && !monsters_in_the_fight.get(i).isDying) {
				return monsters_in_the_fight.get(i);
			}
		}
		return null;
	}
	
	//This will be more complicated in a possible future
	public boolean cardCanTriggerEffect(AbstractCard card) {
		
		boolean card_is_an_attack = card.type == CardType.ATTACK;
		boolean card_has_a_single_target = (card.target == CardTarget.ENEMY)
				|| (card.target == CardTarget.SELF_AND_ENEMY);

		
		if (card_is_an_attack && card_has_a_single_target) return true;
		else if (card_is_an_attack){
			boolean card_hits_multiple_targets = (card.target == CardTarget.ALL_ENEMY)
					|| (card.target == CardTarget.ALL);
			boolean theres_only_one_enemy_alive = IsThereOnlyOneMonsterAlive();
			
			return card_hits_multiple_targets && theres_only_one_enemy_alive;
		}
		return false;
		
	}
	
	public boolean IsThereOnlyOneMonsterAlive() {
		
		int number_of_alive_monsters = 0;
		ArrayList<AbstractMonster> monsters_in_the_fight = AbstractDungeon.getCurrRoom().monsters.monsters;
		for (int i = 0; i < monsters_in_the_fight.size(); i++) {
			if (!monsters_in_the_fight.get(i).isDead && !monsters_in_the_fight.get(i).isDying) {
				number_of_alive_monsters++;
				if (number_of_alive_monsters > 1) return false;
			}
		}
		return true;
		
	}
	
	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		if (fire_burned_enemy) {
			if (enemy_targeted.isDead) {
				fire_burned_enemy = false;
				enemy_targeted = null;
			} else {
				
				AbstractDungeon.actionManager.addToBottom(
						new FlamingAction(enemy_targeted, hp_before, MINIMUM_DAMAGE));

			}
		}
	}
	
	public DamageInfo createDamageInfo(int damageAmount) {
		return new DamageInfo(AbstractDungeon.player, damageAmount,
				DamageType.HP_LOSS);
	}
	

	
}
