package qcfpunch;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.BandageUp;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import qcfpunch.patches.RewardItemTypeEnumPatch;
import qcfpunch.resources.relic_graphics.GraphicResources;

import java.util.ArrayList;

public class DuffelBagBandageCardReward extends CustomReward {

    public static final String ID = QCFP_Misc.returnPrefix() + "Duffel_Bag_Bandage_Card_Reward";

    public static final String[] TEXT = CardCrawlGame.languagePack.
            getRelicStrings(QCFP_Misc.returnPrefix() + "Duffel_Bag").DESCRIPTIONS;

    public DuffelBagBandageCardReward() {
        super(GraphicResources.LoadRewardImage("Duffel Bag Reward.png"),
                "", RewardItemTypeEnumPatch.DUFFELBAGBANDAGECARDREWARD);
        this.cards = new ArrayList<AbstractCard>();
        this.cards.add(new BandageUp());
        this.text = TEXT[3] + TEXT[5] + TEXT[4];
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, text);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        return true;
    }
}
