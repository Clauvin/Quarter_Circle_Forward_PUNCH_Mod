package qcfpunch.relics.character_cameos_valerie;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.SetAlwaysRetainOfCardAtCombatAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

//Valerie's from Fantasy Strike, a fighting game from Sirlin.net
//Relic's weird since the character has manic depression, and
//I tried to emulate the condition a bit.
//Next random card relics won't have a pool of "every card",
//Or will be much more predictable about which cards they give.
public class RainbowBrush extends CustomRelic{
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Rainbow_Brush";
	
	public static CardGroup status_cards;
	public static CardGroup curse_cards;
	
	public static int COMMON_CHANCE = -1;
	public static int UNCOMMON_CHANCE = -1;
	public static int RARE_CHANCE = -1;
	public static int BLACK_CHANCE = -1;
	public static int CURSE_CHANCE = -1;
	public static int STATUS_CHANCE = -1;
	
	public static final int COMMON_INITIAL_CHANCE = 12;
	public static final int UNCOMMON_INITIAL_CHANCE = 50;
	public static final int RARE_INITIAL_CHANCE = 25;
	public static final int BLACK_INITIAL_CHANCE = 1;
	public static final int CURSE_INITIAL_CHANCE = 6;
	public static final int STATUS_INITIAL_CHANCE = 6;
	
	public static final int NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE = 5;
	public static final int PERCENTAGE_TO_REMOVE_OF_COMMON_UNCOMMON_RARE_CARDS = 6;
	
	public static boolean will_spawn_a_status_card = false;
	
	public static boolean extra_chance_for_a_bad_card = false;
	public static int countdown_of_black_card_extra_chances = 0;
	
	public static final boolean do_black_cards_exist = QCFP_Misc.
			silentlyCheckForMod(QCFP_Misc.infinite_spire_class_code);
	
	public static AbstractCard card_to_be_given;
	
	public static final Logger logger = LogManager.getLogger(
			RainbowBrush.class.getName());
	
	public RainbowBrush() {
		super(ID, GraphicResources.LoadRelicImage("Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
		
		this.counter = 0;
		
		initChance();
		
		status_cards = new CardGroup(CardGroupType.UNSPECIFIED);
		curse_cards = new CardGroup(CardGroupType.UNSPECIFIED);
		
		initStatusCards(); initCurseCards();
		
		card_to_be_given = null;
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
	
	public void initStatusCards() {
		if (status_cards.size() == 0) {
			
			ArrayList<AbstractCard> colorless_cards =
					CardLibrary.getCardList(LibraryType.COLORLESS);
			
			for (int i = 0; i < colorless_cards.size(); i++) {
				if (colorless_cards.get(i).type == CardType.STATUS) {
					if (colorless_cards.get(i).cardID != "Slimed")
						status_cards.addToTop(colorless_cards.get(i));
				}
			}
		
		}
	}
	
	public void initCurseCards() {
		if (curse_cards.size() == 0) {
			
			ArrayList<AbstractCard> list_of_curse_cards =
					CardLibrary.getCardList(LibraryType.CURSE);
			
			for (int i = 0; i < list_of_curse_cards.size(); i++) {
				if (list_of_curse_cards.get(i).cardID != "Parasite") {
					curse_cards.addToTop(list_of_curse_cards.get(i));
				}
			}
		
		}
	}

	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE +
				DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[3];
	}
	
	@Override
	public void atTurnStartPostDraw() {
		createCardToGiveLater();
		QCFP_Misc.fastLoggerLine(card_to_be_given.name);
		
	}
	
	public void createCardToGiveLater() {
		CardRarity rarity = generateRarity();
		
		if (extra_chance_for_a_bad_card) {
			if (card_to_be_given != null) {
				if (cardIsACurseOrStatus(card_to_be_given)) {
					if (QCFP_Misc.headsOrTails(AbstractDungeon.cardRng) > 0) {
						rarity = CardRarity.CURSE;
						will_spawn_a_status_card =
								card_to_be_given.type == CardType.STATUS;
					}
					extra_chance_for_a_bad_card = false;
				}
			}
		} else {
			extra_chance_for_a_bad_card = true;
		}
		
		card_to_be_given = generateCard(rarity);
		
		AbstractDungeon.effectList.add(
				new ThoughtBubble(
					AbstractDungeon.player.dialogX,
					AbstractDungeon.player.dialogY,
						4.0F, card_to_be_given.name, true));
		
	}
	
	private boolean cardIsACurseOrStatus(AbstractCard card) {
		return ((card.type == CardType.CURSE) || (card.type == CardType.STATUS));
	}
	
	public CardRarity generateRarity() {
		
		int which_rarity = QCFP_Misc.rollRandomValue(AbstractDungeon.cardRng, 100);
		
		int comparing_rarity;
		
		comparing_rarity = UNCOMMON_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.UNCOMMON;
		
		comparing_rarity += RARE_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.RARE;
		
		comparing_rarity += COMMON_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.COMMON;
		
		if (BLACK_CHANCE != 0) {
			comparing_rarity += BLACK_CHANCE;
			//if (which_rarity <= comparing_rarity) return CardRarity.BLACK;
		}
		
		comparing_rarity += CURSE_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.CURSE;
		
		will_spawn_a_status_card = true;
		
		return CardRarity.SPECIAL;
	}
	
	public AbstractCard generateCard(CardRarity rarity) {
		
		if (rarity == CardRarity.CURSE) {
			int random = 
				QCFP_Misc.rollRandomValue(
						AbstractDungeon.cardRng, curse_cards.size()-1);
			
			return curse_cards.getNCardFromTop(random);
		}
		
		if (!will_spawn_a_status_card) return CardLibrary.getAnyColorCard(rarity);
		else {
			int random = 
				QCFP_Misc.rollRandomValue(
						AbstractDungeon.cardRng, status_cards.size()-1);
			
			will_spawn_a_status_card = false;
			return status_cards.getNCardFromTop(random);
		}
		
	}
	
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		super.onPlayCard(c, m);
		
		this.counter++;
		
		if (counter >= NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE) {
			
			counter = 0;
			flash();
			
			AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(card_to_be_given, false, true));
			
			if (!cardIsACurseOrStatus(card_to_be_given))
				AbstractDungeon.actionManager.addToBottom(
					new SetAlwaysRetainOfCardAtCombatAction(card_to_be_given.uuid,
							true));
							
			changeProbabilities();
			
		}
		
	}
	
	public void changeProbabilities() {
		
		CardRarity card_rarity = card_to_be_given.rarity;
		CardType card_type = card_to_be_given.type;
		int bad_card_extra_chance = 0;
		int bad_card_initial_chance = 0;
		
		QCFP_Misc.fastLoggerLine(card_to_be_given.name);
		QCFP_Misc.fastLoggerLine(card_rarity.toString());
		
		if (cardTypeIsCurseOrStatus(card_type)) {
			
			if (card_type == CardType.CURSE) {
				bad_card_extra_chance = CURSE_CHANCE;
				bad_card_initial_chance = CURSE_INITIAL_CHANCE;
			} else {
				bad_card_extra_chance = STATUS_CHANCE;
				bad_card_initial_chance = STATUS_INITIAL_CHANCE;
			}

			int extra_chance = bad_card_extra_chance - bad_card_initial_chance;
			int extra_common, extra_uncommon, extra_rare;
			
			if (card_type == CardType.CURSE) CURSE_CHANCE = bad_card_initial_chance;
			else STATUS_CHANCE = bad_card_initial_chance;
			
			extra_uncommon = extra_chance / 2;
			extra_rare = extra_chance / 3;
			extra_common = extra_chance / 6;
			
			if (extra_uncommon + extra_rare + extra_common < extra_chance) {
				int distributed_chance = extra_chance - 
						(extra_uncommon + extra_rare + extra_common);
				
				for (int i = 1; i <= distributed_chance; i++) {
					if (i < 4) extra_uncommon += 1;
					else extra_rare += 1;
				}
			}
			
			UNCOMMON_CHANCE += extra_uncommon;
			RARE_CHANCE += extra_rare;
			COMMON_CHANCE += extra_common;
			
		} else if ((card_rarity == CardRarity.UNCOMMON) ||
					(card_rarity == CardRarity.RARE) ||
					(card_rarity == CardRarity.COMMON)) {
			
			int pass_by = 0;
			final int PERCENTAGE_TO_REMOVE =
					PERCENTAGE_TO_REMOVE_OF_COMMON_UNCOMMON_RARE_CARDS;
			int amount;
			
			if (card_rarity == CardRarity.UNCOMMON) {
				
				amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, UNCOMMON_CHANCE);
				UNCOMMON_CHANCE -= amount; pass_by += amount;
				
			} else 	if (card_rarity == CardRarity.RARE) {
				
				amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, RARE_CHANCE);
				RARE_CHANCE -= amount; pass_by += amount;
				
			} else if (card_rarity == CardRarity.COMMON) {
				
				amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, COMMON_CHANCE);
				COMMON_CHANCE -= amount; pass_by += amount;
				
			}
			
			for (int i = 0; i < pass_by; i++) {
				if (i%2 == 0) CURSE_CHANCE += 1;
				else STATUS_CHANCE += 1;
			}
			
		}
		
		QCFP_Misc.fastLoggerLine(UNCOMMON_CHANCE + "");
		QCFP_Misc.fastLoggerLine(RARE_CHANCE + "");
		QCFP_Misc.fastLoggerLine(COMMON_CHANCE + "");
		QCFP_Misc.fastLoggerLine(BLACK_CHANCE + "");
		QCFP_Misc.fastLoggerLine(CURSE_CHANCE + "");
		QCFP_Misc.fastLoggerLine(STATUS_CHANCE + "");
	}
	
	private boolean cardTypeIsCurseOrStatus(CardType type) {
		return ((type == CardType.CURSE) || (type == CardType.STATUS));
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
            logger.info(QCFP_Misc.classAndSaveSlotText());

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
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
        else {
        	clear(config);
        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading Rainbow Brush info from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
        
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
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");
			logger.info("Finished setting Rainbow Brush variables.");
		}
    }
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing Rainbow Brush variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
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
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}
	
	public boolean canSpawn()
	{
		return true;
	}
	
	public AbstractRelic makeCopy() { 
		return new RainbowBrush();
	}
}
