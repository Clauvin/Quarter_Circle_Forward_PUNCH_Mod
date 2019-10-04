package qcfpunch.relics.character_cameos_valerie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

//No, Valerie is not a character from Street Fighter
//She's from Fantasy Strike, a fighting game from Sirlin.net
public class RainbowBrush extends CustomRelic{
	
	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Rainbow_Brush";
	
	public static int COMMON_CHANCE;
	public static int UNCOMMON_CHANCE;
	public static int RARE_CHANCE;
	public static int BLACK_CHANCE;
	public static int CURSE_CHANCE;
	public static int STATUS_CHANCE;
	
	public static final int COMMON_INITIAL_CHANCE = 50;
	public static final int UNCOMMON_INITIAL_CHANCE = 25;
	public static final int RARE_INITIAL_CHANCE = 12;
	public static final int BLACK_INITIAL_CHANCE = 1;
	public static final int CURSE_INITIAL_CHANCE = 6;
	public static final int STATUS_INITIAL_CHANCE = 6;
	
	public static final int NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE = 7;
	
	public static final boolean do_black_cards_exist = QCFPunch_MiscCode.
			silentlyCheckForMod(QCFPunch_MiscCode.infinite_spire_class_code);
	
	public static final Logger logger = LogManager.getLogger(
			RainbowBrush.class.getName());
	
	public RainbowBrush() {
		super(ID, GraphicResources.LoadRelicImage("Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
		
		initChance();
		
		this.counter = 0;
	}
	
	public void initChance() {
		
		COMMON_CHANCE = COMMON_INITIAL_CHANCE;
		UNCOMMON_CHANCE = UNCOMMON_INITIAL_CHANCE;
		
		if (do_black_cards_exist) {
			RARE_CHANCE = RARE_INITIAL_CHANCE;
			BLACK_CHANCE = BLACK_INITIAL_CHANCE;
		} else {
			RARE_CHANCE = RARE_INITIAL_CHANCE + BLACK_INITIAL_CHANCE;
			BLACK_CHANCE = 0;
		}
		
		CURSE_CHANCE = CURSE_INITIAL_CHANCE;
		STATUS_CHANCE = STATUS_INITIAL_CHANCE;
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
	public void atPreBattle() {
		super.atPreBattle();
		
		//generate card accordingly to probabilities
		
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		super.onPlayCard(c, m);
		
		this.counter++;
		
		if (counter >= NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE) {
			
			counter = 0;
			flash();
			
			//add Retain if it's not a curse or Status (maybe add anyway?)
			//and the card to the player's hand
			//change probabilities
			//generate new card
			
		}
	}
	
	@Override
	public void onPlayerEndTurn() {
		super.onPlayerEndTurn();
		
		//change card to generate, accordingly to probabilities and rules
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
