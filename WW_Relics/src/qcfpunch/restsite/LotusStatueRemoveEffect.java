package qcfpunch.restsite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import qcfpunch.relics.dhalsim.LotusStatue;
import qcfpunch.relics.ryu.FightingGloves;

public class LotusStatueRemoveEffect extends AbstractGameEffect {

    private LotusStatue lotus_statue;
    private boolean opened_screen;
    private Color screen_color;

    public LotusStatueRemoveEffect() {

        lotus_statue = null;

        if (AbstractDungeon.player.hasRelic(LotusStatue.ID)) {
            lotus_statue = (LotusStatue) AbstractDungeon.player.getRelic(LotusStatue.ID);
        }

        this.opened_screen = false;
        this.screen_color = AbstractDungeon.fadeColor.cpy();
    }

    @Override
    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }

        if (this.duration < 1.0F && !this.opened_screen) {
            this.opened_screen = true;
            LotusStatue.Set_right_click_in_relic_here_havent_happened(false);
            lotus_statue.removingCards();
        }
        if (LotusStatue.cards_have_been_removed_in_this_room) {
            this.isDone = true;
            this.opened_screen = false;
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screen_color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screen_color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }

    }


    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
