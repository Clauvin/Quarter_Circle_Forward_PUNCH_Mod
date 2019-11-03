package qcfpunch.relics.character_cameos.valerie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import qcfpunch.QCFP_Misc;

//In usual circunstances, this code would be inside
// the PrimaryBrush class.
// Giving that accessing the PrimaryBrush when there's no TheArtist Mod loaded 
// crashes the game, the code goes here.
public class PrimaryBrushNoArtistSaveFix {

	public static final Logger logger =
			LogManager.getLogger(PrimaryBrushNoArtistSaveFix.class.getName());
	public static final String primary_brush_id = 
			QCFP_Misc.returnPrefix() + "Primary_Brush";
	
	public static void clear(final SpireConfig config) {
		
		logger.info(QCFP_Misc.debugStringStartedClearDataManagement(
				primary_brush_id));
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
    	String field_text = "multicolor_pen_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot;
		
        config.remove(field_text + "_attacks_played");
        config.remove(field_text + "_skills_played");
        config.remove(field_text + "_powers_played");
        
        logger.info(QCFP_Misc.debugStringFinishedClearDataManagement(
        		primary_brush_id));
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}
	
}
