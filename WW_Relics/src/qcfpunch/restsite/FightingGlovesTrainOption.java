package qcfpunch.restsite;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class FightingGlovesTrainOption extends AbstractCampfireOption {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
	
	public FightingGlovesTrainOption(boolean usable) {
		this.label = FightingGlovesTrainOption.TEXT[0];
        this.description = FightingGlovesTrainOption.TEXT[1];
        this.img = ImageMaster.CAMPFIRE_TRAIN_BUTTON;
		this.usable = usable;
	}
	
	public void useOption() {
		AbstractDungeon.effectList.add(new FightingGlovesTrainEffect(this));
	}
    
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Train Option");
        TEXT = FightingGlovesTrainOption.uiStrings.TEXT;
    }
}
