package qcfpunch.relics.character_cameos_valerie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

//No, Valerie is not a character from Street Fighter
//She's from Fantasy Strike, a fighting game from Sirlin.net
public class RainbowBrush extends CustomRelic{
	
	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Rainbow_Brush";
	
	public static final Logger logger = LogManager.getLogger(
			RainbowBrush.class.getName());
	
	public RainbowBrush() {
		super(ID, GraphicResources.LoadRelicImage("Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}

	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	public String getPreCardDecidedDescription() {
		return DESCRIPTIONS[0];
	}
	
	public String getCardDecidedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		
	}
	
	//Don't forget to add something to avoid cases where a card mod
	// is removed mid-game 
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
    		logger.info("Started saving Rainbow Brush information from");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());

    		if (AbstractDungeon.isDungeonBeaten || AbstractDungeon.player.isDead) {
    			
    		} 
    		else {
    			
    			/*String class_name = AbstractDungeon.player.getClass().getName();

        		config.setInt("rainbow_brush_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_number_of_cards_left", number_of_cards_left);

                config.setInt("rainbow_brush_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_floor_of_last_stored_reward", floor_of_last_stored_reward);
                
                config.setBool("rainbow_brush_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_empty_relic", empty_relic);
                
                storeCardRewardCreated(config, card_reward);
    			
                try {
    				config.save();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}*/
    			
    		}

            logger.info("Finished saving Rainbow Brush information from");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        }
        else {
        	clear(config);
        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading Rainbow Brush info from");
        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        
        String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) && 
				config.has("rainbow_brush_class_" + class_name +
							"_save_slot_" + CardCrawlGame.saveSlot +
							"_number_of_cards_left")) {

			/*number_of_cards_left = config.getInt("rainbow_brush_class_" + class_name +
													"_save_slot_" + CardCrawlGame.saveSlot +
													"_number_of_cards_left");
			
			loadCardRewardStored(config);
			
			floor_of_last_stored_reward = config.getInt("rainbow_brush_class_" + class_name +
									    				"_save_slot_" + CardCrawlGame.saveSlot +
									    				"_floor_of_last_stored_reward");
			
			empty_relic = config.getBool("rainbow_brush_class_" + class_name +
						    				"_save_slot_" + CardCrawlGame.saveSlot +
						    				"_empty_relic");
				
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            logger.info("Finished loading Rainbow Brush info from");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting School Backpack variables.");
		}
    }
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing Rainbow Brush variables from");
        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
		
        //String class_name = AbstractDungeon.player.getClass().getName();
		
		//yeah, I know this part could be improved, but I don't have willpower to do it so now.
		//So...

		
        /*config.remove("rainbow_brush_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_number_of_cards_left");
        config.remove("rainbow_brush_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_floor_of_last_stored_reward");
        config.remove("rainbow_brush_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_empty_relic");
        
        if (config.has("rainbow_brush_" + class_name + 
				"_save_slot_" + CardCrawlGame.saveSlot + 
				"_reward_size")) {
        	clearCardRewardStored(config);
        }*/
        
        logger.info("Finished clearing Rainbow Brush variables from");
        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
	}
	
	public boolean canSpawn()
	{
		return true;
	}
	
	public AbstractRelic makeCopy() { 
		return new RainbowBrush();
	}
}
