package qcfpunch.relics.chun_li;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.UnsteadyPower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class Handcuffs extends CustomRelic {
	public static final String ID = QCFP_Misc.returnPrefix() + "Handcuffs";
	
	private static final int NUMBER_OF_USES_PER_FIGHT = 1;
	private static final int NUMBER_OF_STR_DOWN_DEBUFFS = 0;
	private static final int NUMBER_OF_UNSTEADY_DEBUFFS = 2;
	private static final int NUMBER_OF_STUN_DEBUFFS = 1;
	
	public int number_of_uses_left_in_this_fight;
	public boolean handcuff_is_lost = false;
	
	public Handcuffs() {
		super(ID, GraphicResources.LoadRelicImage(
				"Handcuffs - handcuffs - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		String description;
		
		description = DESCRIPTIONS[0] + NUMBER_OF_UNSTEADY_DEBUFFS + DESCRIPTIONS[1];
		
		//Commented since there's no STR debuff and if I use a conditional here, a warning will stay
		//description += DESCRIPTIONS[2] + NUMBER_OF_STR_DOWN_DEBUFFS + DESCRIPTIONS[3];
		description += DESCRIPTIONS[6];
			
		description += DESCRIPTIONS[4] + NUMBER_OF_STUN_DEBUFFS + DESCRIPTIONS[5];
		
		return description;
	}
	
	public void atPreBattle() {
		number_of_uses_left_in_this_fight = NUMBER_OF_USES_PER_FIGHT;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		
		if (canBeUsed(info, damageAmount)) {
			
			if (NUMBER_OF_STR_DOWN_DEBUFFS > 0) {
				AbstractDungeon.actionManager.addToTop(
						 new ApplyPowerAction(target,
								 AbstractDungeon.player,
								 new StrengthPower(target, -1 * NUMBER_OF_STR_DOWN_DEBUFFS)));
			}

			if (NUMBER_OF_UNSTEADY_DEBUFFS > 0) {
				AbstractDungeon.actionManager.addToTop(
						 new ApplyPowerAction(target,
								 AbstractDungeon.player,
								 new UnsteadyPower(target, NUMBER_OF_UNSTEADY_DEBUFFS)));
			}

			AbstractDungeon.actionManager.addToTop(
					 new StunMonsterAction((AbstractMonster)target, AbstractDungeon.player));
			
			AbstractDungeon.actionManager.addToTop(
					new RelicAboveCreatureAction(target, this));
			
			number_of_uses_left_in_this_fight--;
		}
		
	}
	
	public boolean canBeUsed(DamageInfo info, int damageAmount) {
		
		boolean owner_not_null = info.owner != null;
		boolean normal_damage_type = info.type == DamageType.NORMAL;
		boolean suffered_damage = damageAmount > 0;
		boolean can_be_used_in_this_fight = number_of_uses_left_in_this_fight > 0;
		
		return ((owner_not_null) &&
				(normal_damage_type) &&
				(suffered_damage) &&
				(can_be_used_in_this_fight));
	}
	
	public boolean canSpawn()
	{
		return AbstractDungeon.player.masterDeck.getAttacks().size() > 0;
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new Handcuffs();
	}
}