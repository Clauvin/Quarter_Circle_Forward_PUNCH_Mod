package qcfpunch;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;

public class DuffelBagCardReward extends CustomReward {

    public DuffelBagCardReward(Texture icon, String text) {
        super(icon, text, RewardType.CARD);
    }

    @Override
    public boolean claimReward() {
        return false;
    }
}
