package qcfpunch.relics.character_cameos.valerie;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class MulticolorPen extends CustomRelic  {

	public static final String ID = QCFP_Misc.returnPrefix() + "Multicolor_Pen";
	
	private static int attacks_played = 0;
	private static int skills_played = 0;
	private static int powers_played = 0;
	
	public static final Logger logger =
			LogManager.getLogger(MulticolorPen.class.getName());
	
	public MulticolorPen() {
		super(ID, GraphicResources.LoadRelicImage(
				"Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
        	logger.info(QCFP_Misc.debugStringStartedSaveDataManagement(MulticolorPen.ID));
    		logger.info(QCFP_Misc.classAndSaveSlotText());

        	String class_name = AbstractDungeon.player.getClass().getName();
        	String field_text = "multicolor_pen_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot;
    		
            config.setInt(field_text + "_attacks_played", attacks_played);
            config.setInt(field_text + "_skills_played", skills_played);
            config.setInt(field_text + "_powers_played", powers_played);

            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            logger.info(QCFP_Misc.debugStringFinishedSaveDataManagement(MulticolorPen.ID));
            logger.info(QCFP_Misc.classAndSaveSlotText());	
        }
        else {

        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info(QCFP_Misc.debugStringStartedLoadDataManagement(MulticolorPen.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
    	String field_text = "multicolor_pen_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot;
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has(field_text + "_attacks_played")) {

			attacks_played = config.getInt(field_text + "_attacks_played");
			skills_played = config.getInt(field_text + "_skills_played");
			powers_played = config.getInt(field_text + "_powers_played");
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info(QCFP_Misc.debugStringFinishedLoadDataManagement(
            		MulticolorPen.ID));
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");
			logger.info("Finished setting MulticolorPen variables.");
		}
		
    }
	
	public static void clear(final SpireConfig config) {
		logger.info(QCFP_Misc.debugStringStartedClearDataManagement(
        		MulticolorPen.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
    	String field_text = "multicolor_pen_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot;
		
        config.remove(field_text + "_attacks_played");
        config.remove(field_text + "_skills_played");
        config.remove(field_text + "_powers_played");
        
        logger.info(QCFP_Misc.debugStringFinishedClearDataManagement(
        		MulticolorPen.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}
	
	public boolean canSpawn() {
		return QCFP_Misc.checkForMod(QCFP_Misc.the_artist_class_code);
	}
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new MulticolorPen();
	}
	
}
