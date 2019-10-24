package qcfpunch.restsite;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;

public class FightingGlovesTrainOption extends AbstractCampfireOption {

	public void useOption() {
		AbstractDungeon.effectList.add(new FightingGlovesTrainEffect());
	}
	
}
