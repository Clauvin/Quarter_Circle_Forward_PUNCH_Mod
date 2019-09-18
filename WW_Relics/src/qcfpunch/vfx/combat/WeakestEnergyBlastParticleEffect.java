package qcfpunch.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

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
    private Color secondary_color;
    private float draw_scale;
    
    private Color true_color;
    private Color true_secondary_color;
    
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor) {
    	
    	this(sX, sY, tX, tY, ballColor, Color.BLACK.cpy());
    	
    }
    
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor, final Color secondaryColor) {
    	
    	this(sX, sY, tX, tY, ballColor, secondaryColor, 1.0f, 0.8f);
    	
    }
    public WeakestEnergyBlastParticleEffect(final float sX, final float sY,
    		final float tX, final float tY, final Color ballColor,
    		final Color secondaryColor, float drawScale, float starting_duration) {
    	
        this.img = ImageMaster.GLOW_SPARK_2;
        this.starting_point_X = sX + MathUtils.random(-15.0f, 15.0f) * Settings.scale;
        this.starting_point_Y = sY + MathUtils.random(-10.0f, 10.0f) * Settings.scale;;
        
        if (tX < this.starting_point_X) this.target_X = tX + 200.0f * Settings.scale;
        else this.target_X = tX - 200.0f * Settings.scale;
        
        this.target_Y = tY + MathUtils.random(-30.0f, 30.0f) * Settings.scale;
        this.vX = this.starting_point_X;
        this.vY = this.starting_point_Y;
        this.current_x = this.starting_point_X;
        this.current_y = this.starting_point_Y;
        this.scale = 1.0f;
        this.startingDuration = starting_duration;
        this.duration = this.startingDuration;
        this.renderBehind = MathUtils.randomBoolean(0.2f);
        this.color = ballColor;
        this.secondary_color = secondaryColor;
        this.draw_scale = drawScale;
        
        this.true_color = this.color;
        this.true_secondary_color = this.secondary_color;
    }
    
    @Override
    public void update() {
    	if ((duration >= 0.0f) && (!this.isDone)) {
    		this.current_x = Interpolation.linear.apply(this.target_X, this.vX, this.duration);
            this.current_y = Interpolation.linear.apply(this.target_Y, this.vY, this.duration);	
    	}
        
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
    	true_secondary_color.a = secondary_color.a * (this.duration / this.startingDuration);
        sb.setColor(true_secondary_color);
        sb.draw(this.img, this.current_x, this.current_y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale * (this.draw_scale+1f), this.scale * (this.draw_scale+1f), this.rotation);
        true_color.a = color.a * (this.duration / this.startingDuration);
        sb.setColor(this.color);
        sb.draw(this.img, this.current_x, this.current_y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale*this.draw_scale, this.scale*this.draw_scale, this.rotation);
    }
    
    @Override
    public void dispose() {
    	
    }
}