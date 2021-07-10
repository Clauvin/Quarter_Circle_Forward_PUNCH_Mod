package qcfpunch.rooms;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import qcfpunch.QCFP_Misc;

import java.util.Iterator;

public class MonsterRoomEmeraldElite extends MonsterRoomElite {

	@Override
	public void dropReward() {

	    AbstractRelic.RelicTier tier = returnRandomRelicTier();
	    if ((Settings.isEndless) && (AbstractDungeon.player.hasBlight("MimicInfestation")))
	    {
	      AbstractDungeon.player.getBlight("MimicInfestation").flash();
	    }
	    else
	    {
	    	addRelicToRewards(tier);
			tier = returnRandomRelicTier();
			addRelicToRewards(tier);
	    	if (AbstractDungeon.player.hasRelic("Black Star")) {
				addNoncampRelicToRewards(returnRandomRelicTier());
				addNoncampRelicToRewards(returnRandomRelicTier());
	      	}

	    	addEmeraldKey();
	    }
	    
	}

	protected void addEmeraldKey(){
		if (Settings.isFinalActAvailable &&
				!Settings.hasEmeraldKey &&
				!this.rewards.isEmpty()) {
			this.rewards.add(new RewardItem(
					(RewardItem)this.rewards.get(this.rewards.size() - 1),
					RewardItem.RewardType.EMERALD_KEY));
		}
	}
	
	private RelicTier returnRandomRelicTier() {
		
		int roll = AbstractDungeon.relicRng.random(0, 99);
		if (ModHelper.isModEnabled("Elite Swarm")) {
			roll += 10;
	    }
	    if (roll < 50) {
	    	return AbstractRelic.RelicTier.COMMON;
	    }
	    if (roll > 82) {
	    	return AbstractRelic.RelicTier.RARE;
	    }
	    return AbstractRelic.RelicTier.UNCOMMON;
	}

	public void applyEmeraldEliteBuff(){
		//Why the weird var1 iterator?
		// Because I copied this from MonsterRoomElite when decompiled on Intelij,
		// while trying to make this overriding function work :/
		Iterator var1;
		AbstractMonster m;
		switch(AbstractDungeon.mapRng.random(0, 3)) {
			case 0:
				var1 = this.monsters.monsters.iterator();

				while(var1.hasNext()) {
					m = (AbstractMonster)var1.next();
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
				}

				return;
			case 1:
				var1 = this.monsters.monsters.iterator();

				while(var1.hasNext()) {
					m = (AbstractMonster)var1.next();
					AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, 0.25F, true));
				}

				return;
			case 2:
				var1 = this.monsters.monsters.iterator();

				while(var1.hasNext()) {
					m = (AbstractMonster)var1.next();
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
				}

				return;
			case 3:
				var1 = this.monsters.monsters.iterator();

				while(var1.hasNext()) {
					m = (AbstractMonster)var1.next();
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
				}
		}
		QCFP_Misc.fastLoggerLine(AbstractDungeon.actionManager.actions.size());
	}
	
}
