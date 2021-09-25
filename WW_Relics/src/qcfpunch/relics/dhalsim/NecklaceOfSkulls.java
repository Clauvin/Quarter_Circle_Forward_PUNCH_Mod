package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

import java.util.ArrayList;

public class NecklaceOfSkulls extends CustomRelic
        implements ClickableRelic,
                    OnRemoveCardFromMasterDeckRelic {

    public static final String ID = QCFP_Misc.returnPrefix() +
            "Necklace_of_Skulls";

    private static int current_amount_of_upgrading = 0;
    private static final int INITIAL_CHARGES = 0;

    public static boolean is_player_choosing_a_card = false;
    public static boolean try_to_upgrade_cards = false;
    public static boolean waiting_to_upgrade = false;

    public NecklaceOfSkulls() {
        super(ID, GraphicResources.
                        LoadRelicImage("Necklace of Skulls - death-skull - sbed - CC BY 3.0.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);

        counter = INITIAL_CHARGES;
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard abstractCard) {
        this.counter += 1;
    }

    private boolean isTimeToUpgradeTheChosenCards() {
        return AbstractDungeon.gridSelectScreen.selectedCards.size()
                >= current_amount_of_upgrading;
    }

    @SuppressWarnings("static-access")
    private void ifThereAreUpgradesToDoTryToDoThem() {

        if (this.counter > 0) {

            CardGroup upgradeable_cards =
                    AbstractDungeon.player.masterDeck.getUpgradableCards();

            if (upgradeable_cards.size() > 0)
                upgradingCards(upgradeable_cards);

        }

    }

    public void upgradingCards(CardGroup upgradeable_cards) {

        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.overlayMenu.cancelButton.hide();
        AbstractDungeon.previousScreen = AbstractDungeon.screen;

        current_amount_of_upgrading = 1;

        AbstractDungeon.gridSelectScreen.open(upgradeable_cards,
                current_amount_of_upgrading,
                getCardGridDescription(), false, false, true, false);

        is_player_choosing_a_card = true;
        try_to_upgrade_cards = false;

    }

    private void showVFX(AbstractCard card_chosen, int positioning,
                         boolean only_card) {

        int true_positioning;

        if (only_card) {
            true_positioning = 2;
        } else {
            true_positioning = positioning+1;
        }

        AbstractDungeon.effectsQueue.add(
                new UpgradeShineEffect(
                        true_positioning * Settings.WIDTH / 4.0F,
                        Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectsQueue.add(
                new ShowCardBrieflyEffect(
                        card_chosen.makeStatEquivalentCopy(),
                        true_positioning * Settings.WIDTH / 4.0F,
                        Settings.HEIGHT / 2.0F));
    }

    private static ArrayList<AbstractCard> getCardsToUpgrade() {
        ArrayList<AbstractCard> chosen_cards = AbstractDungeon.gridSelectScreen.
                selectedCards;

        return chosen_cards;
    }

    @Override
    public void onRightClick() {
        AbstractRoom curr_room = AbstractDungeon.getCurrRoom();

        if (curr_room.getMapSymbol() == "R")
            ifThereAreUpgradesToDoTryToDoThem();

    }

    public void update(){

        super.update();

        if (is_player_choosing_a_card) {
            if (isTimeToUpgradeTheChosenCards()) {
                flash();

                ArrayList<AbstractCard> cards_chosen = getCardsToUpgrade();

                for (int i = 0; i < current_amount_of_upgrading; i++) {
                    AbstractCard card_chosen = cards_chosen.get(i);

                    AbstractDungeon.player.bottledCardUpgradeCheck(card_chosen);

                    if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.conspire_class_code)) {
                        conspire.relics.InfiniteJournal.
                                upgradeCard(card_chosen);
                    } else {
                        card_chosen.upgrade();
                    }

                    if (current_amount_of_upgrading == 1) {
                        showVFX(card_chosen, i, true);
                    } else {
                        showVFX(card_chosen, i, false);
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                AbstractDungeon.overlayMenu.hideBlackScreen();
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.isScreenUp = false;

                is_player_choosing_a_card = false;

                this.counter -= 1;
            }
        }
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    private String getCardGridDescription() {
        return DESCRIPTIONS[2];
    }

    public boolean canSpawn() {
        return true;
    }

    public AbstractRelic makeCopy() {
        return new NecklaceOfSkulls();
    }


}
