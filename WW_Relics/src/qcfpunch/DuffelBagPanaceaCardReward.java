package qcfpunch;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.BandageUp;
import com.megacrit.cardcrawl.cards.colorless.Panacea;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import qcfpunch.resources.relic_graphics.GraphicResources;

import java.util.ArrayList;

public class DuffelBagPanaceaCardReward extends CustomReward {

    public static final String ID = QCFP_Misc.returnPrefix() + "Duffel_Bag_Panacea_Card_Reward";

    public static final String[] TEXT = CardCrawlGame.languagePack.
            getRelicStrings(QCFP_Misc.returnPrefix() + "Duffel_Bag").DESCRIPTIONS;

    public DuffelBagPanaceaCardReward() {
        super(GraphicResources.LoadRewardImage("Duffel Bag Reward.png"),
                "", RewardType.CARD);
        this.cards = new ArrayList<AbstractCard>();
        this.cards.add(new Panacea());
        this.text = TEXT[3] + cards.get(0).name + TEXT[4];
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        return false;
    }
}