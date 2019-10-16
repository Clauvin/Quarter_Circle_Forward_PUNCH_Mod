package qcfpunch.relics.character_cameos.valerie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.SetAlwaysRetainOfCardAtCombatAction;
import qcfpunch.cards.ui.ErrorCard;
import qcfpunch.resources.relic_graphics.GraphicResources;

//Valerie's from Fantasy Strike, a fighting game from Sirlin.net
//Relic's weird since the character has manic depression, and
//I tried to emulate the condition a bit.
//Next random card relics won't have a pool of "every card",
//Or will be much more predictable about which cards they give.
public class RainbowBrush extends CustomRelic{
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Rainbow_Brush";
	
	public static ArrayList<String> common_cards_ids;
	public static ArrayList<String> uncommon_cards_ids;
	public static ArrayList<String> rare_cards_ids;
	public static ArrayList<String> black_cards_ids;
	public static CardGroup status_cards;
	public static CardGroup curse_cards;
	
	public static int common_CHANCE = -1;
	public static int uncommon_CHANCE = -1;
	public static int rare_CHANCE = -1;
	public static int black_CHANCE = -1;
	public static int curse_CHANCE = -1;
	public static int status_CHANCE = -1;
	
	public static final int COMMON_INITIAL_CHANCE = 12;
	public static final int UNCOMMON_INITIAL_CHANCE = 50;
	public static final int RARE_INITIAL_CHANCE = 25;
	public static final int BLACK_INITIAL_CHANCE = 1;
	public static final int CURSE_INITIAL_CHANCE = 6;
	public static final int STATUS_INITIAL_CHANCE = 6;
	
	public static final int NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE = 5;
	public static final int PERCENTAGE_TO_REMOVE_OF_COMMON_UNCOMMON_RARE_CARDS = 6;
	
	public static boolean will_spawn_a_status_card = false;
	public static boolean will_spawn_a_black_card = false;
	
	public static boolean extra_chance_for_a_bad_card = false;
	public static int countdown_of_black_card_extra_chances = 0;
	
	public static boolean do_black_cards_exist;
	
	public static AbstractCard card_to_be_given;
	public static AbstractCard card_to_be_shown_with_thought_balloon;
	public static AbstractCard card_to_be_shown_while_hovering_relic;
	
	public static boolean save_slot_0_helper_tip_given = false;
	public static boolean save_slot_1_helper_tip_given = false;
	public static boolean save_slot_2_helper_tip_given = false;
	
	public static final Logger logger = LogManager.getLogger(
			RainbowBrush.class.getName());
	
	public RainbowBrush() {
		super(ID, GraphicResources.
				LoadRelicImage("Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
		
		this.counter = 0;
		
		initChance();
		
		status_cards = new CardGroup(CardGroupType.UNSPECIFIED);
		curse_cards = new CardGroup(CardGroupType.UNSPECIFIED);
		common_cards_ids = new ArrayList<String>();
		uncommon_cards_ids = new ArrayList<String>();
		rare_cards_ids = new ArrayList<String>();
		if (do_black_cards_exist) {
			black_cards_ids = new ArrayList<String>();
		}
		
		initStatusCards(); initCurseCards(); initUsualCardsNames();
		
		do_black_cards_exist = QCFP_Misc.
				checkForMod(QCFP_Misc.infinite_spire_class_code);
		
		card_to_be_given = null;
		card_to_be_shown_with_thought_balloon = null;
		card_to_be_shown_while_hovering_relic = null;
	}
	
	public void initChance() {
		
		common_CHANCE = COMMON_INITIAL_CHANCE;
		uncommon_CHANCE = UNCOMMON_INITIAL_CHANCE;
		
		if (do_black_cards_exist) {
			rare_CHANCE = RARE_INITIAL_CHANCE;
			black_CHANCE = BLACK_INITIAL_CHANCE;
		} else {
			rare_CHANCE = RARE_INITIAL_CHANCE + BLACK_INITIAL_CHANCE;
			black_CHANCE = 0;
		}
		
		curse_CHANCE = CURSE_INITIAL_CHANCE;
		status_CHANCE = STATUS_INITIAL_CHANCE;
	}
	
	public static void initStatusCards() {
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
	
	public static void initCurseCards() {
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
	
	public static void initUsualCardsNames() {
		
		for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
			AbstractCard card = c.getValue();
			
			//Ok, Java is ambiguous here so
			// "continue" means:
			// end this loop's iteration and start the next iteration,
			// not "go ahead in this iteration"
			if ((QCFP_Misc.isItACurse(card)) || (QCFP_Misc.isItAStatus(card)) ||
				 (card.rarity == CardRarity.BASIC))
				continue;
			
			if (do_black_cards_exist) {
				if (card.color ==
						infinitespire.patches.
						CardColorEnumPatch.CardColorPatch.INFINITE_BLACK) {
					black_cards_ids.add(card.cardID);
					continue;
				}
			}
			
			switch (card.rarity) {
				case COMMON:
					common_cards_ids.add(card.cardID);
					break;
				case UNCOMMON:
					uncommon_cards_ids.add(card.cardID);
					break;
				case RARE:
					rare_cards_ids.add(card.cardID);
					break;
				default:
					logger.info(card.cardID.toString() + " not added.");
					break;
			}
		}
		
	}

	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + NUMBER_OF_CARDS_PLAYED_TO_ACTIVATE +
				DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[3];
	}
	
	@Override
	public void atBattleStartPreDraw() {
				
		int which_save_slot = CardCrawlGame.saveSlot;
		
		if (((which_save_slot == 0) && (!save_slot_0_helper_tip_given)) ||
			((which_save_slot == 1) && (!save_slot_1_helper_tip_given)) ||
			((which_save_slot == 2) && (!save_slot_2_helper_tip_given)))
		{
			
			 AbstractDungeon.ftue = new FtueTip(DESCRIPTIONS[4],
					 							DESCRIPTIONS[5],
					 							Settings.WIDTH / 2.0F,
					 							Settings.HEIGHT / 2.0F,
					 							FtueTip.TipType.COMBAT);
			 
			 switch(which_save_slot) {
			 	case 0:
			 		save_slot_0_helper_tip_given = true;
			 		break;
			 	case 1:
			 		save_slot_1_helper_tip_given = true;
			 		break;
			 	case 2:
			 		save_slot_2_helper_tip_given = true;
			 		break;
			 	default:
			 		logger.info("Something is clearly wrong here...");
			 		break;
			 }
		}
	}
	
	@Override
	public void atTurnStartPostDraw() {
		createCardToGiveLater();
	}
	
	public void createCardToGiveLater() {
		CardRarity rarity = generateRarity();
		
		if (extra_chance_for_a_bad_card) {
			if (card_to_be_given != null) {
				if (cardIsACurseOrStatus(card_to_be_given)) {
					if (QCFP_Misc.headsOrTails(AbstractDungeon.cardRandomRng) > 0) {
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
		card_to_be_shown_with_thought_balloon = 
				card_to_be_given.makeStatEquivalentCopy();
		card_to_be_shown_while_hovering_relic =
				card_to_be_given.makeStatEquivalentCopy();
		
		AbstractDungeon.effectList.add(
				new ThoughtBubble(
					AbstractDungeon.player.dialogX,
					AbstractDungeon.player.dialogY,
						2.5F, card_to_be_shown_with_thought_balloon.name, true));
		
		AbstractDungeon.effectList.add(
				new ShowCardBrieflyEffect(card_to_be_shown_with_thought_balloon,
						AbstractDungeon.player.dialogX + 
						3 * card_to_be_shown_with_thought_balloon.hb.width,
						AbstractDungeon.player.dialogY));
			
	}
	
	private boolean cardIsACurseOrStatus(AbstractCard card) {
		return ((card.type == CardType.CURSE) || (card.type == CardType.STATUS));
	}
	
	public CardRarity generateRarity() {
		
		int which_rarity = QCFP_Misc.rollRandomValue(
				AbstractDungeon.cardRandomRng, 100);
		
		int comparing_rarity;
		
		comparing_rarity = uncommon_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.UNCOMMON;
		
		comparing_rarity += rare_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.RARE;
		
		comparing_rarity += common_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.COMMON;
		
		if (black_CHANCE != 0) {
			comparing_rarity += black_CHANCE;
			if (which_rarity <= comparing_rarity) {
				will_spawn_a_black_card = true;
				return CardRarity.SPECIAL;
			}
		}
		
		comparing_rarity += curse_CHANCE;
		if (which_rarity <= comparing_rarity) return CardRarity.CURSE;
		
		will_spawn_a_status_card = true;
		
		return CardRarity.SPECIAL;
	}
	
	public AbstractCard generateCard(CardRarity rarity) {
		
		if (rarity == CardRarity.CURSE) {
			if (curse_cards.size() == 0) {
				logger.info("Curse_cards.size() == 0");
				return new ErrorCard();
			}
			
			int random = 
				QCFP_Misc.rollRandomValue(
						AbstractDungeon.cardRandomRng, curse_cards.size()-1);
			
			return curse_cards.getNCardFromTop(random);
		}
		
		if ((!will_spawn_a_status_card) && (!will_spawn_a_black_card)) {
			
			return getAnyColorCard(rarity);
			
		}
		else if (!will_spawn_a_black_card) {
			if (status_cards.size() == 0) {
				logger.info("Status_cards.size() == 0");
				return new ErrorCard();
			}
			
			int random = 
				QCFP_Misc.rollRandomValue(
						AbstractDungeon.cardRandomRng, status_cards.size()-1);
			
			will_spawn_a_status_card = false;
			return status_cards.getNCardFromTop(random);
		} else {
			will_spawn_a_black_card = false;
			return getBlackCard();
		}
		
	}
	
	public AbstractCard getAnyColorCard(CardRarity rarity) {
		
		ArrayList<String> list_of_cards_ids = new ArrayList<String>();
		
		int random_number;
		String card_id;
		switch (rarity) {
			case COMMON:
				list_of_cards_ids = common_cards_ids;
				break;
			case UNCOMMON:
				list_of_cards_ids = uncommon_cards_ids;
				break;
			case RARE:
				list_of_cards_ids = rare_cards_ids;
				break;
			default:
				logger.info("Something wrong happened with getAnyColorCard.");
				break;
		}
		
		if (list_of_cards_ids.size() == 0) {
			logger.info("Rarity " + rarity.toString() + "_cards_id.size() == 0");
			return new ErrorCard();
		}
		random_number = QCFP_Misc.rollPositiveRandomValue(
				AbstractDungeon.cardRandomRng, list_of_cards_ids.size()-1);
		card_id = list_of_cards_ids.get(random_number);
		
		return CardLibrary.cards.get(card_id).makeCopy();
		
	}
	
	public AbstractCard getBlackCard() { 
		
		int random_number;
		String card_id;
		
		if (black_cards_ids.size() == 0) {
			logger.info("black_cards_ids.size() == 0");
			return new ErrorCard();
		}
		
		random_number = QCFP_Misc.rollPositiveRandomValue(
				AbstractDungeon.cardRandomRng, black_cards_ids.size()-1);
		card_id = black_cards_ids.get(random_number);
		
		return CardLibrary.cards.get(card_id).makeCopy();
		
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
		
		logger.info(card_to_be_given.name);
		logger.info(card_rarity.toString());
		
		if (cardTypeIsCurseOrStatus(card_type)) {
			
			if (card_type == CardType.CURSE) {
				bad_card_extra_chance = curse_CHANCE;
				bad_card_initial_chance = CURSE_INITIAL_CHANCE;
			} else {
				bad_card_extra_chance = status_CHANCE;
				bad_card_initial_chance = STATUS_INITIAL_CHANCE;
			}

			int extra_chance = bad_card_extra_chance - bad_card_initial_chance;
			
			if (card_type == CardType.CURSE) curse_CHANCE = bad_card_initial_chance;
			else status_CHANCE = bad_card_initial_chance;
			
			distributeCurseAndStatusExtraProbToCommonUncommonAndRare(extra_chance);
			
			
		} else if (cardRarityIsCommonUncommonOrRare(card_rarity)) {
			
			changeProbIfCardGivenWasCommonUncommonOrRare(card_rarity);
			
		}
		
		QCFP_Misc.debugOnlyLoggerLine(logger, uncommon_CHANCE + "");
		QCFP_Misc.debugOnlyLoggerLine(logger, rare_CHANCE + "");
		QCFP_Misc.debugOnlyLoggerLine(logger, common_CHANCE + "");
		QCFP_Misc.debugOnlyLoggerLine(logger, black_CHANCE + "");
		QCFP_Misc.debugOnlyLoggerLine(logger, curse_CHANCE + "");
		QCFP_Misc.debugOnlyLoggerLine(logger, status_CHANCE + "");
	}
	
	private boolean cardTypeIsCurseOrStatus(CardType type) {
		return ((type == CardType.CURSE) || (type == CardType.STATUS));
	}
	
	private boolean cardRarityIsCommonUncommonOrRare(CardRarity card_rarity) {
		return ((card_rarity == CardRarity.UNCOMMON) ||
				(card_rarity == CardRarity.RARE) ||
				(card_rarity == CardRarity.COMMON));
	}
	
	private void distributeCurseAndStatusExtraProbToCommonUncommonAndRare(
			int extra_chance) {
		
		int extra_uncommon = extra_chance / 2;
		int extra_rare = extra_chance / 3;
		int extra_common = extra_chance / 6;
		
		if (extra_uncommon + extra_rare + extra_common < extra_chance) {
			int distributed_chance = extra_chance - 
					(extra_uncommon + extra_rare + extra_common);
			
			for (int i = 1; i <= distributed_chance; i++) {
				if (i < 4) extra_uncommon += 1;
				else extra_rare += 1;
			}
		}
		
		uncommon_CHANCE += extra_uncommon;
		rare_CHANCE += extra_rare;
		common_CHANCE += extra_common;
		
	}
	
	private void changeProbIfCardGivenWasCommonUncommonOrRare(CardRarity card_rarity) {
		
		int pass_by = 0;
		final int PERCENTAGE_TO_REMOVE =
				PERCENTAGE_TO_REMOVE_OF_COMMON_UNCOMMON_RARE_CARDS;
		int amount;
		
		if (card_rarity == CardRarity.UNCOMMON) {
			
			amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, uncommon_CHANCE);
			uncommon_CHANCE -= amount; pass_by += amount;
			
		} else 	if (card_rarity == CardRarity.RARE) {
			
			amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, rare_CHANCE);
			rare_CHANCE -= amount; pass_by += amount;
			
		} else if (card_rarity == CardRarity.COMMON) {
			
			amount = QCFP_Misc.min(PERCENTAGE_TO_REMOVE, common_CHANCE);
			common_CHANCE -= amount; pass_by += amount;
			
		}
		
		raiseEquallyCurseAndStatusProbabilities(pass_by);
		
	}
	
	private void raiseEquallyCurseAndStatusProbabilities(int amount_to_pass_by) {
		for (int i = 0; i < amount_to_pass_by; i++) {
			if (i%2 == 0) curse_CHANCE += 1;
			else status_CHANCE += 1;
		}
	}
	
	@Override
	public void onEquip() {
		super.onEquip();
		
		if (common_CHANCE == -1) initChance();
		
	}
	
	@Override
	public void renderInTopPanel(SpriteBatch sb) {
		super.renderInTopPanel(sb);
		
		if ((this.hb.hovered) &&
			(AbstractDungeon.getCurrRoom() != null)) {
			if ((AbstractDungeon.getCurrRoom() instanceof MonsterRoom) &&
				(card_to_be_given != null)) {
						
				QCFP_Misc.fastLoggerLine("True");
					
				float drawScale = 1.0f;
				card_to_be_shown_while_hovering_relic.drawScale = drawScale;
				card_to_be_shown_while_hovering_relic.current_x = this.currentX +
						3 * card_to_be_given.hb.width;
				card_to_be_shown_while_hovering_relic.current_y = this.currentY -
						card_to_be_given.hb.height;
				
				card_to_be_shown_while_hovering_relic.render(sb);
							
			}
		}
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
    			String start_of_helper_tip_variables = "rainbow_brush_save_slot_";
    			
        		config.setInt(start_of_save_variable +
        				"_COMMON_CHANCE", common_CHANCE);

                config.setInt(start_of_save_variable +
        				"_UNCOMMON_CHANCE", uncommon_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_RARE_CHANCE", rare_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_BLACK_CHANCE", black_CHANCE);
                
                config.setInt(start_of_save_variable +
        				"_CURSE_CHANCE", curse_CHANCE);         
                
                config.setInt(start_of_save_variable +
        				"_STATUS_CHANCE", status_CHANCE);  
                
                config.setBool(start_of_helper_tip_variables + 0,
                		save_slot_0_helper_tip_given);
                
                config.setBool(start_of_helper_tip_variables + 1,
                		save_slot_0_helper_tip_given);
                
                config.setBool(start_of_helper_tip_variables + 2,
                		save_slot_0_helper_tip_given);

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
        String start_of_helper_tip_variables = "rainbow_brush_save_slot_";
		
		if (AbstractDungeon.player.hasRelic(ID) && 
				config.has(start_of_save_variable +
						"_COMMON_CHANCE")) {
			
    		common_CHANCE = config.getInt(start_of_save_variable +
    				"_COMMON_CHANCE");

    		uncommon_CHANCE = config.getInt(start_of_save_variable +
    				"_UNCOMMON_CHANCE");
            
    		rare_CHANCE = config.getInt(start_of_save_variable +
    				"_RARE_CHANCE");
            
    		black_CHANCE = config.getInt(start_of_save_variable +
    				"_BLACK_CHANCE");
            
    		curse_CHANCE = config.getInt(start_of_save_variable +
    				"_CURSE_CHANCE");         
            
    		status_CHANCE = config.getInt(start_of_save_variable +
    				"_STATUS_CHANCE"); 
			
		}
		
		save_slot_0_helper_tip_given =
				config.getBool(start_of_helper_tip_variables + 0);
         
		save_slot_1_helper_tip_given = 
				config.getBool(start_of_helper_tip_variables + 1);
         
		save_slot_2_helper_tip_given =
				config.getBool(start_of_helper_tip_variables + 2);

			
        try {
			config.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
        logger.info("Finished loading Rainbow Brush info from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
		logger.info("Finished setting Rainbow Brush variables.");

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
