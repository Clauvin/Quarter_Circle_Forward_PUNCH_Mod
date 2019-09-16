package qcfpunch.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;

//base code from Saiyajin's KiBallParticleEffect.java
// address below:
// https://github.com/IGHARARI/Saiyajin/blob/8eca01666babb8417a3c1efe2b5fa9b49bd36b31/src/main/java/sts/saiyajin/ui/vfx/KiBallParticleEffect.java

public class WeakestEnergyBlastParticleEffect extends AbstractGameEffect {

    private float starting_point_X;
    private float starting_point_Y;
    private float target_X;
    private float target_Y;
    private float current_x;
    private float current_y;
    private float vY;
    private float vX;
    private TextureAtlas.AtlasRegion img;
    private boolean activated;
    private Color secondary_color;
    private float draw_scale;
    
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor) {
    	
    	this(sX, sY, tX, tY, ballColor, Color.BLACK.cpy());
    	
    }
    
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor, final Color secondaryColor) {
    	
    	this(sX, sY, tX, tY, ballColor, secondaryColor, 1.0f);
    	
    }
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor,
    		final Color secondaryColor, float drawScale) {
    	
        this.activated = false;
        this.img = ImageMaster.GLOW_SPARK_2;
        this.starting_point_X = sX + MathUtils.random(-90.0f, 90.0f) * Settings.scale;
        this.starting_point_Y = sY + MathUtils.random(-90.0f, 90.0f) * Settings.scale;
        this.target_X = tX + MathUtils.random(-50.0f, 50.0f) * Settings.scale;
        this.target_Y = tY + MathUtils.random(-50.0f, 50.0f) * Settings.scale;
        this.vX = this.starting_point_X + MathUtils.random(-200.0f, 200.0f) * Settings.scale;
        this.vY = this.starting_point_Y + MathUtils.random(-200.0f, 200.0f) * Settings.scale;
        this.current_x = this.starting_point_X;
        this.current_y = this.starting_point_Y;
        this.scale = 0.01f;
        this.startingDuration = 0.8f;
        this.duration = this.startingDuration;
        this.renderBehind = MathUtils.randomBoolean(0.2f);
        this.color = ballColor;
        this.secondary_color = secondaryColor;
        this.draw_scale = drawScale;
    }
    
    @Override
    public void update() {
        if (this.duration < this.startingDuration / 2.0f) {
            this.scale = Interpolation.pow3Out.apply(2.0f, 2.5f, this.duration / (this.startingDuration / 2.0f)) * Settings.scale;
            this.current_x = Interpolation.swingOut.apply(this.target_X, this.vX, this.duration / (this.startingDuration / 2.0f));
            this.current_y = Interpolation.swingOut.apply(this.target_Y, this.vY, this.duration / (this.startingDuration / 2.0f));
        }
        else
        {
        	this.scale = Interpolation.pow3In.apply(2.5f, this.startingDuration / 2.0f, (this.duration - this.startingDuration / 2.0f) / (this.startingDuration / 2.0f)) * Settings.scale;
            this.current_x = Interpolation.swingIn.apply(this.starting_point_X, this.vX, (this.duration - this.startingDuration / 2.0f) / (this.startingDuration / 2.0f));
            this.current_y = Interpolation.swingIn.apply(this.starting_point_Y, this.vY, (this.duration - this.startingDuration / 2.0f) / (this.startingDuration / 2.0f));
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < this.startingDuration / 2.0f && !this.activated) {
            this.activated = true;
            this.starting_point_X = this.current_x;
            this.starting_point_Y = this.current_y;
        }
        if (this.duration < 0.0f) {
            AbstractDungeon.effectsQueue.add(new AdditiveSlashImpactEffect(this.target_X, this.target_Y, this.color.cpy()));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, MathUtils.randomBoolean());
            this.isDone = true;
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
        sb.setColor(secondary_color);
        sb.draw(this.img, this.current_x, this.current_y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale * (this.draw_scale+1f), this.scale * (this.draw_scale+1f), this.rotation);
        sb.setColor(this.color);
        sb.draw(this.img, this.current_x, this.current_y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale*this.draw_scale, this.scale*this.draw_scale, this.rotation);
    }
    
    @Override
    public void dispose() {
    	
    }
}