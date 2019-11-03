package qcfpunch.restsite;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.dungeons.*;

import java.util.ArrayList;

import com.badlogic.gdx.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import basemod.ReflectionHacks;

import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.megacrit.cardcrawl.helpers.*;

// Copied from The-Evil-Pickle's Replay-the-Spire code
public class CampfireBurnResetEffect extends AbstractGameEffect
{
    private boolean openedScreen;
    private Color screenColor;
    private AbstractCampfireOption caller;

    public CampfireBurnResetEffect() {
    	this(null);
    }
    public CampfireBurnResetEffect(AbstractCampfireOption caller) {
    	this.caller = caller;
        this.openedScreen = false;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 1.5f;
        this.screenColor.a = 0.0f;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }
        if (this.duration < 1.0f && !this.openedScreen) {
            this.openedScreen = true;
            AbstractDungeon.closeCurrentScreen();
            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1");
            if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
                final RestRoom r = (RestRoom)AbstractDungeon.getCurrRoom();
                if (this.caller != null) {
                	@SuppressWarnings("rawtypes")
					final ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>)ReflectionHacks.getPrivate((Object)(r.campfireUI), (Class)CampfireUI.class, "buttons");
                    campfireButtons.remove(this.caller);
                }
                r.campfireUI.reopen();
            }
        }
        if (this.duration < 0.0f) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0f;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
            }
            CardCrawlGame.music.unsilenceBGM();
        }
    }
    
    private void updateBlackScreenColor() {
        if (this.duration > 1.0f) {
            this.screenColor.a = Interpolation.fade.apply(1.0f, 0.0f, (this.duration - 1.0f) * 2.0f);
        }
        else {
            this.screenColor.a = Interpolation.fade.apply(0.0f, 1.0f, this.duration / 1.5f);
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0f, 0.0f, Settings.WIDTH, Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}