package qcfpunch.restsite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import qcfpunch.relics.ryu.FightingGloves;

public class FightingGlovesTrainEffect extends AbstractGameEffect {

	private FightingGloves fighting_gloves;
	private boolean opened_screen;
	private Color screen_color;
	
	private AbstractCampfireOption caller_option;
	
	public FightingGlovesTrainEffect(AbstractCampfireOption caller_option) {
		  
		fighting_gloves = null;
	
		for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
			if (AbstractDungeon.player.hasRelic(FightingGloves.ID)) {
				fighting_gloves = (FightingGloves) 
						AbstractDungeon.player.getRelic(FightingGloves.ID);
					AbstractDungeon.player.relics.get(i);
					break;
			}
		}
		
		this.opened_screen = false;
		this.screen_color = AbstractDungeon.fadeColor.cpy();
		
		this.caller_option = caller_option;
	}
	
	@Override
	public void update() {
		if (!AbstractDungeon.isScreenUp) {
			this.duration -= Gdx.graphics.getDeltaTime();
			updateBlackScreenColor();
		} 
		
	    if (this.duration < 1.0F && !this.opened_screen) {
	        this.opened_screen = true;
	        FightingGloves.player_havent_right_clicked_in_relic_here_before = false;
			fighting_gloves.upgradingCards();
	    } 
	    if (FightingGloves.cards_have_been_upgraded_in_this_room) {
	    	this.isDone = true;
	    	this.opened_screen = false;
	    	AbstractDungeon.effectList.add(new CampfireBurnResetEffect(caller_option));
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
	public void dispose() {	}

	@Override
	public void render(SpriteBatch arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}
