package qcfpunch.relics.dhalsim;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class NecklaceOfSkulls extends CustomRelic
        implements OnRemoveCardFromMasterDeckRelic {

    public static final String ID = QCFP_Misc.returnPrefix() +
            "Necklace_of_Skulls";

    public NecklaceOfSkulls() {
        super(ID, GraphicResources.
                        LoadRelicImage("Necklace of Skulls - death-skull - sbed - CC BY 3.0.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard abstractCard) {
        this.counter += 1;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }


}
