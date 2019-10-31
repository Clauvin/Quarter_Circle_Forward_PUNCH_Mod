package qcfpunch.restsite;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import qcfpunch.relics.ryu.FightingGloves;

public class FightingGlovesTrainOption extends AbstractCampfireOption {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    
    private boolean called_campfire_reset_effect = false; 
	
	public FightingGlovesTrainOption(boolean usable) {
		this.label = FightingGlovesTrainOption.TEXT[0];
		
		int cards_that_can_be_upgraded = FightingGloves.howManyCardsCanBeUpgraded();
		
		if (cards_that_can_be_upgraded > 1) {
			this.description = FightingGlovesTrainOption.TEXT[1] +
	        		FightingGloves.howManyCardsCanBeUpgraded() + 
	        		FightingGlovesTrainOption.TEXT[3];
		} else if (cards_that_can_be_upgraded == 1) {
			this.description = FightingGlovesTrainOption.TEXT[1] +
	        		FightingGloves.howManyCardsCanBeUpgraded() + 
	        		FightingGlovesTrainOption.TEXT[2];
		} else {
			this.description = "";
		}
		
        if (cards_that_can_be_upgraded > 0) {
        	this.img = ImageMaster.CAMPFIRE_TRAIN_BUTTON;
        } else this.img = ImageMaster.CAMPFIRE_TRAIN_DISABLE_BUTTON;
       
		this.usable = usable;
	}
	
	public void useOption() {
		AbstractDungeon.effectList.add(new FightingGlovesTrainEffect());
	}
    
	@Override
	public void update() {
		super.update();
		if ((FightingGloves.cards_have_been_upgraded_in_this_room) &&
				(!called_campfire_reset_effect)) {
			called_campfire_reset_effect = true;
			AbstractDungeon.effectList.add(new CampfireBurnResetEffect(this));
		}
		
	}
	
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Train Option");
        TEXT = FightingGlovesTrainOption.uiStrings.TEXT;
    }
}
