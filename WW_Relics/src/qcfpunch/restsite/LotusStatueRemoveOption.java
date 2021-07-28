package qcfpunch.restsite;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import qcfpunch.relics.dhalsim.LotusStatue;
import qcfpunch.relics.ryu.FightingGloves;

public class LotusStatueRemoveOption extends AbstractCampfireOption {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private boolean called_campfire_reset_effect = false;

    public LotusStatueRemoveOption(boolean usable) {
        this.label = LotusStatueRemoveOption.TEXT[0];

        int cards_that_can_be_removed = 0; 'OldLotusStatue.howManyCardsCanBeUpgraded();

        if (cards_that_can_be_removed > 1) {
            this.description = LotusStatueRemoveOption.TEXT[1] +
                    FightingGloves.howManyCardsCanBeUpgraded() +
                    LotusStatueRemoveOption.TEXT[3];
        } else if (cards_that_can_be_removed == 1) {
            this.description = LotusStatueRemoveOption.TEXT[1] +
                    FightingGloves.howManyCardsCanBeUpgraded() +
                    LotusStatueRemoveOption.TEXT[2];
        } else {
            this.description = "";
        }

        this.img = ImageMaster.CAMPFIRE_TRAIN_BUTTON;

        this.usable = usable;
    }

    public void useOption() {
        AbstractDungeon.effectList.add(new FightingGlovesTrainEffect());
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Remove Option");
        TEXT = LotusStatueRemoveOption.uiStrings.TEXT;
    }
}
