package qcfpunch.relics.dhalsim;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class Cattail extends CustomRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Cattail";
	
	public static int HOW_MANY_ROOMS_TO_GIVE_SMOKE_BOMB = 7;
	
	private static int last_floor_where_relic_counter_was_changed = 0;
	
	public static final Logger logger = LogManager.getLogger(Cattail.class.getName());
	
	public Cattail() {
		super(ID, GraphicResources.LoadRelicImage("Temp Army Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.SHOP, LandingSound.MAGICAL);
		
		counter = HOW_MANY_ROOMS_TO_GIVE_SMOKE_BOMB;
		
		last_floor_where_relic_counter_was_changed = 0;
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + HOW_MANY_ROOMS_TO_GIVE_SMOKE_BOMB +
				DESCRIPTIONS[1];
	}
	
	@Override
	public void onEquip() {
		flash();
		AbstractDungeon.player.obtainPotion(new SmokeBomb());
	}
	
	@Override
	public void onEnterRoom(AbstractRoom room) {
		
		if (last_floor_where_relic_counter_was_changed == AbstractDungeon.floorNum){
			
		}
		else if (last_floor_where_relic_counter_was_changed < AbstractDungeon.floorNum) {
			
			last_floor_where_relic_counter_was_changed = AbstractDungeon.floorNum;
			counter--;
			
		}
		
		if (counter == 0) {
			flash();
			AbstractDungeon.player.obtainPotion(new SmokeBomb());
			counter = -2;
		}
	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
        	String class_name = AbstractDungeon.player.getClass().getName();
        	
    		logger.info("Started saving " + ID + " information from");
    		logger.info(QCFPunch_MiscCode.classAndSaveSlotText());

            config.setInt("cattail_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_last_floor_where_relic_was_used",
            		last_floor_where_relic_counter_was_changed);
            
            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            logger.info("Finished saving " + ID + " info from");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading " + ID + " info");
        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        
    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has("cattail_class_" + class_name +
	            		"_save_slot_" + CardCrawlGame.saveSlot +
	            		"_last_floor_where_relic_was_used")) {

			last_floor_where_relic_counter_was_changed = config.getInt(
					"cattail_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_last_floor_where_relic_was_used");
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info("Finished loading " + ID + " info");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting " + ID + " variables.");
		}
		
    }
	
	public static void clear(final SpireConfig config) {

    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (config.has("cattail_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_last_floor_where_relic_was_used")) {
			
			logger.info("Clearing " + ID + " variables from");
	        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());			
			
			config.remove("cattail_class_" + class_name +
	        		"_save_slot_" + CardCrawlGame.saveSlot +
	        		"_last_floor_where_relic_was_used");
			
			logger.info("Finished clearing " + ID + " variables from");
	        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());	
			
		} else {
			logger.info("No " + ID + " variables to clean from this case.");
		}
	
        
	}
	
	public boolean canSpawn() {
		return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
	}
	
	public AbstractRelic makeCopy() {
		return new Cattail();
	}

}
