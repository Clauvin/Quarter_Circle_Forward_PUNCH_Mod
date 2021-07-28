package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;
import qcfpunch.restsite.FightingGlovesTrainOption;

import java.util.ArrayList;

public class LotusStatue extends CustomRelic implements OnRemoveCardFromMasterDeckRelic {

    public static final String ID = QCFP_Misc.returnPrefix() +
            "Lotus_Statue";

    private static boolean using_this_relic_power_to_remove = false;
    private static final int INITIAL_AMOUNT_OF_CHARGES = 0;
    private static final int MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE = 1;
    private static final int CHARGES_GAINED_BY_REMOVAL = 1;

    private static boolean right_click_in_relic_here_havent_happened = true;
    private static boolean currently_choosing_removable_cards;
    private static int max_amount_of_cards_to_remove;
    private static boolean cards_to_remove_yet = false;

    private static final int CAN_SPAWN_BEFORE_FLOOR = 51;

    public LotusStatue() {
        super(ID, GraphicResources.LoadRelicImage(
                "Temp Lotus Statue - steeltoe-boots - Lorc - CC BY 3.0.png"),
                RelicTier.RARE, LandingSound.MAGICAL);

        this.counter = INITIAL_AMOUNT_OF_CHARGES;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE +
                DESCRIPTIONS[1] + CHARGES_GAINED_BY_REMOVAL +
                DESCRIPTIONS[2];
    }

    public String getCardGridDescription() {
        return DESCRIPTIONS[3] + MAX_AMOUNT_OF_CARDS_REMOVABLE_PER_CHARGE +
                DESCRIPTIONS[4];
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard card) {

        if (!using_this_relic_power_to_remove) {
            this.counter += 1;
            flash();
        }

    }

    public void onEnterRoom(AbstractRoom room) {
        using_this_relic_power_to_remove = false;
        right_click_in_relic_here_havent_happened = true;
        currently_choosing_removable_cards = false;
        cards_to_remove_yet = false;
        max_amount_of_cards_to_remove = 0;
    }

    public boolean shouldTheRelicBeUsedNow() {
        if (haveCharges() && (haveCardsToUpgrade() &&
                AbstractDungeon.getCurrRoom() instanceof RestRoom &&
                AbstractDungeon.getCurrRoom().phase ==
                        AbstractRoom.RoomPhase.INCOMPLETE &&
                CampfireUI.hidden == false)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        if (shouldTheRelicBeUsedNow()) {
            options.add(new LotusStatueRemoveOption(true));
        } else {
            options.add(new LotusStatueRemoveOption(false));
        }

    }




}
