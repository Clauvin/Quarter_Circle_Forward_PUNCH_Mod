package qcfpunch.rooms;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

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
	    	if (AbstractDungeon.player.hasRelic("Black Star")) {
	        addNoncampRelicToRewards(returnRandomRelicTier());
	      }

	    	addEmeraldKey();
	    }
	    
	}

	protected void addEmeraldKey(){
		if (Settings.isFinalActAvailable &&
				!Settings.hasEmeraldKey &&
				!this.rewards.isEmpty() &&
				AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
			this.rewards.add(new RewardItem((RewardItem)this.rewards.get(this.rewards.size() - 1), RewardItem.RewardType.EMERALD_KEY));
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
	
}
