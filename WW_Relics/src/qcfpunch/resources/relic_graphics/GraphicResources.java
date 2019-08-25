package qcfpunch.resources.relic_graphics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class GraphicResources {

	public static final String GRAPHIC_RESOURCES_ADDRESS = "qcfpunch/resources/";
	public static final String RELIC_GRAPHICS_SUBADDRESS = "relic_graphics/";
	public static final String RELIC_TEXTURE_ADDRESS = GRAPHIC_RESOURCES_ADDRESS + RELIC_GRAPHICS_SUBADDRESS;
	
	public static Texture img_red_headband;
	public static Texture bg_img_red_headband;
	
	public static Texture LoadRelicImage(String relic_filename) {
		return ImageMaster.loadImage(GraphicResources.RELIC_TEXTURE_ADDRESS +
				relic_filename);
	}
	
}
