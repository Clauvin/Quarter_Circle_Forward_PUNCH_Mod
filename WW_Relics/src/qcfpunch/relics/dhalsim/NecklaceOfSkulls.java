package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

import java.util.ArrayList;

public class NecklaceOfSkulls extends CustomRelic
        implements OnRemoveCardFromMasterDeckRelic {

    public static final String ID = QCFP_Misc.returnPrefix() +
            "Necklace_of_Skulls";

    private static int current_amount_of_upgrading = 0;

    public static boolean is_player_choosing_a_card = false;
    public static boolean try_to_upgrade_cards = false;
    public static boolean waiting_to_upgrade = false;

    public NecklaceOfSkulls() {
        super(ID, GraphicResources.
                        LoadRelicImage("Necklace of Skulls - death-skull - sbed - CC BY 3.0.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard abstractCard) {
        this.counter += 1;
    }

    @SuppressWarnings("static-access")
    private void ifThereAreUpgradesToDoTryToDoThem() {

        this.counter -= current_amount_of_upgrading;
        current_amount_of_upgrading = 0;

        if (amount_of_upgrades > 0) {

            CardGroup upgradeable_cards =
                    AbstractDungeon.player.masterDeck.getUpgradableCards();

            if (upgradeable_cards.size() > 0)
                upgradingCards(upgradeable_cards);

        }

    }

    private static ArrayList<AbstractCard> getCardsToUpgrade() {
        ArrayList<AbstractCard> chosen_cards = AbstractDungeon.gridSelectScreen.
                selectedCards;

        return chosen_cards;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    private String getCardGridDescription() {
        return DESCRIPTIONS[1];
    }

    public boolean canSpawn() {
        return true;
    }

    public AbstractRelic makeCopy() {
        return new OldNecklaceOfSkulls();
    }
}
