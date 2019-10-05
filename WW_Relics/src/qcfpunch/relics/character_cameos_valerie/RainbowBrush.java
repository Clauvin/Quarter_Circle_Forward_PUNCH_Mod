package qcfpunch.relics.character_cameos_valerie;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.actions.SetRetainOfCardAtCombatAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

//No, Valerie is not a character from Street Fighter
//She's from Fantasy Strike, a fighting game from Sirlin.net
public class RainbowBrush extends CustomRelic{
	
	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Rainbow_Brush";
	
	public static int COMMON_CHANCE = -1;
	public static int UNCOMMON_CHANCE = -1;
	public static int RARE_CHANCE = -1;
	public static int BLACK_CHANCE = -1;
	public static int CURSE_CHANCE = -1;
	public static int STATUS_CHANCE = -1;
	
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
		
		this.counter = 0;
		
		initChance();
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
			CardRarity rarity = CardRarity.SPECIAL;
			int which_rarity = (int)AbstractDungeon.cardRandomRng.randomLong() + 1;
			which_rarity %= 100;
			int comparing_rarity;
			
			comparing_rarity = COMMON_CHANCE;
			if (which_rarity <= comparing_rarity) rarity = CardRarity.COMMON;
			
			if (rarity == CardRarity.SPECIAL) {
				comparing_rarity += UNCOMMON_CHANCE;
				if (which_rarity <= comparing_rarity) rarity = CardRarity.UNCOMMON;
			}
			
			if (rarity == CardRarity.SPECIAL) {
				comparing_rarity += RARE_CHANCE;
				if (which_rarity <= comparing_rarity) rarity = CardRarity.RARE;
			}
			
			if ((rarity == CardRarity.SPECIAL) && (BLACK_CHANCE != 0)) {
				comparing_rarity += BLACK_CHANCE;
				//if (which_rarity <= comparing_rarity) rarity = CardRarity.BLACK;
			}
			
			if (rarity == CardRarity.SPECIAL) {
				comparing_rarity += CURSE_CHANCE;
				//Spawned card is a CURSE
				//if (which_rarity <= comparing_rarity)
			}
			
			else {
				//Spawned card is a Status
			}

			AbstractCard card = AbstractDungeon.getCard(CardRarity.COMMON);
			
			AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(card, false, true));
			
			AbstractDungeon.actionManager.addToBottom(
					new SetRetainOfCardAtCombatAction(card.uuid, true));
			
		}
		
		logger.info(COMMON_CHANCE + " " + UNCOMMON_CHANCE);
	}
	
	@Override
	public void onPlayerEndTurn() {
		super.onPlayerEndTurn();
		
		//change card to generate, accordingly to probabilities and rules
	}
	
	@Override
	public void onEquip() {
		super.onEquip();
		
		if (COMMON_CHANCE == -1) initChance();
		
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
    			
    			String class_name = AbstractDungeon.player.getClass().getName();
    			String start_of_save_variable = "rainbow_brush_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot;
    			
        		config.setInt(start_of_save_variable +
        				"_COMMON_CHANCE", COMMON_CHANCE);

                config.setInt(start_of_save_variable +
        				"_UNCOMMON_CHANCE", UNCOMMON_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_RARE_CHANCE", RARE_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_BLACK_CHANCE", BLACK_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_CURSE_CHANCE", CURSE_CHANCE);         
                
                config.setInt(start_of_save_variable +
        				"_STATUS_CHANCE", STATUS_CHANCE);  
                
                //storeCardRewardCreated(config, card_reward);
    			
                try {
    				config.save();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			
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
        String start_of_save_variable = "rainbow_brush_class_" + class_name +
				"_save_slot_" + CardCrawlGame.saveSlot;
		
		if (AbstractDungeon.player.hasRelic(ID) && 
				config.has(start_of_save_variable +
						"_COMMON_CHANCE")) {
			
    		COMMON_CHANCE = config.getInt(start_of_save_variable +
    				"_COMMON_CHANCE");

    		UNCOMMON_CHANCE = config.getInt(start_of_save_variable +
    				"_UNCOMMON_CHANCE");
            
    		RARE_CHANCE = config.getInt(start_of_save_variable +
    				"_RARE_CHANCE");
            
    		BLACK_CHANCE = config.getInt(start_of_save_variable +
    				"_BLACK_CHANCE");
            
    		CURSE_CHANCE = config.getInt(start_of_save_variable +
    				"_CURSE_CHANCE");         
            
    		STATUS_CHANCE = config.getInt(start_of_save_variable +
    				"_STATUS_CHANCE"); 
			
			//loadCardRewardStored(config);
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            logger.info("Finished loading Rainbow Brush info from");
            logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");
			logger.info("Finished setting Rainbow Brush variables.");
		}
    }
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing Rainbow Brush variables from");
        logger.info(QCFPunch_MiscCode.classAndSaveSlotText());
		
        String class_name = AbstractDungeon.player.getClass().getName();
        String start_of_save_variable = "rainbow_brush_class_" + class_name +
				"_save_slot_" + CardCrawlGame.saveSlot;
        
        config.remove(start_of_save_variable + "_COMMON_CHANCE");
        
        config.remove(start_of_save_variable + "_UNCOMMON_CHANCE");
        
        config.remove(start_of_save_variable + "_RARE_CHANCE");
        
        config.remove(start_of_save_variable + "_BLACK_CHANCE");
        
        config.remove(start_of_save_variable + "_CURSE_CHANCE");
        
        config.remove(start_of_save_variable + "_STATUS_CHANCE");
        
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
