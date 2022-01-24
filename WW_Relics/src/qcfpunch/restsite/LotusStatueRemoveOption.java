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
    private static LotusStatue lotus_statue;

    private boolean called_campfire_reset_effect = false;

    public LotusStatueRemoveOption(boolean usable) {
        if (AbstractDungeon.player.hasRelic(LotusStatue.ID)) {
            lotus_statue = (LotusStatue) AbstractDungeon.player.getRelic(LotusStatue.ID);
        }

        this.label = LotusStatueRemoveOption.TEXT[0];

        int cards_that_can_be_removed = 0;

        if (cards_that_can_be_removed > 1) {
            this.description = LotusStatueRemoveOption.TEXT[1];// +
            //        LotusStatue.howManyCardsCanBeRemoved() +
            //        LotusStatueRemoveOption.TEXT[3];
        } else if (cards_that_can_be_removed == 1) {
            this.description = LotusStatueRemoveOption.TEXT[1];// +
            //        FightingGloves.howManyCardsCanBeUpgraded() +
            //        LotusStatueRemoveOption.TEXT[2];
        } else {
            this.description = "";
        }

        this.img = ImageMaster.CAMPFIRE_TRAIN_BUTTON;

        this.usable = usable;
    }

    public void useOption() {
        AbstractDungeon.effectList.add(new LotusStatueRemoveEffect());
    }

    @Override
    public void update() {
        super.update();
        if ((LotusStatue.cards_have_been_removed_in_this_room) &&
                (!called_campfire_reset_effect)) {
            called_campfire_reset_effect = true;
            AbstractDungeon.effectList.add(new CampfireBurnResetEffect(this));
        }
        if (lotus_statue != null){
            if (lotus_statue.counter == 0){
                this.usable = false;
            } else {
                this.usable = true;
            }
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Remove Option");
        TEXT = LotusStatueRemoveOption.uiStrings.TEXT;
    }
}
