package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
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

    private static CardGroup cards_to_be_upgraded;
    private static AbstractCard upgraded_card;
    private static ShowCardBrieflyEffect show_card_briefly_effect;

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

        if ((this.counter > 0) && (!AbstractDungeon.isScreenUp)){

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

        cards_to_be_upgraded = upgradeable_cards;

        AbstractDungeon.gridSelectScreen.open(upgradeable_cards,
                current_amount_of_upgrading,
                getCardGridDescription(), false, false, false, false);

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

            if (cards_to_be_upgraded != null){

                for (int i = 0; i < cards_to_be_upgraded.size(); i++)
                {
                    if (cards_to_be_upgraded.getNCardFromTop(i).hb.hovered){
                        showUpgradedVersionOfTheCard(
                                cards_to_be_upgraded.getNCardFromTop(i));
                    }
                }
            }
        }
    }

    private static void showUpgradedVersionOfTheCard(AbstractCard card){

        float x = Settings.WIDTH;
        float y = Settings.HEIGHT;
        float defined_x = 0.10f;
        float defined_y = 0.50f;
        float duration = 1.0f;
        float starting_duration = 10.0f;

        if (upgraded_card == null){
            upgraded_card = card.makeStatEquivalentCopy();
            upgraded_card.upgrade();

            if (show_card_briefly_effect == null){

                upgraded_card = card.makeStatEquivalentCopy();
                upgraded_card.upgrade();

                show_card_briefly_effect = new ShowCardBrieflyEffect(
                        upgraded_card.makeStatEquivalentCopy(),
                        defined_x * x, defined_y * y);
                show_card_briefly_effect.duration = duration;
                show_card_briefly_effect.startingDuration = starting_duration;

                AbstractDungeon.effectList.add(show_card_briefly_effect);

            } else {

                show_card_briefly_effect.duration = duration;
            }
        } else if (upgraded_card.originalName == card.name){
            if (show_card_briefly_effect == null){

            } else if (show_card_briefly_effect.isDone){


                upgraded_card = card.makeStatEquivalentCopy();
                upgraded_card.upgrade();

                show_card_briefly_effect = new ShowCardBrieflyEffect(
                        upgraded_card.makeStatEquivalentCopy(),
                        defined_x * x, defined_y * y);
                show_card_briefly_effect.duration = duration;
                show_card_briefly_effect.startingDuration = starting_duration;

                AbstractDungeon.effectList.add(show_card_briefly_effect);
            } else {

                show_card_briefly_effect.duration = duration;
            }
        } else {


            upgraded_card = card.makeStatEquivalentCopy();
            upgraded_card.upgrade();

            show_card_briefly_effect = new ShowCardBrieflyEffect(
                    upgraded_card.makeStatEquivalentCopy(),
                    defined_x * x, defined_y * y);
            show_card_briefly_effect.duration = duration;
            show_card_briefly_effect.startingDuration = starting_duration;

            AbstractDungeon.effectList.add(show_card_briefly_effect);

        }

    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    private String getCardGridDescription() {
        return DESCRIPTIONS[2];
    }

    public boolean canSpawn() {
        if (AbstractDungeon.id == TheEnding.ID) return false;
        else if ((AbstractDungeon.actNum % 3 == 0) &&
                (AbstractDungeon.currMapNode.y > 3 * AbstractDungeon.map.size() / 4))
            return false;
        else return true;
    }

    public AbstractRelic makeCopy() {
        return new NecklaceOfSkulls();
    }


}
