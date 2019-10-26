package qcfpunch.restsite;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class FightingGlovesTrainOption extends AbstractCampfireOption {

	public FightingGlovesTrainOption(boolean usable) {
		super();
		this.usable = usable;
	}
	
	public void useOption() {
		AbstractDungeon.effectList.add(new FightingGlovesTrainEffect());
	}
	
}
