package qcfpunch.relics.character_cameos.sakura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

/* Lose all hope of a clean and easy to do relic, those who enter here
 * thinking to copy this code and change it for their own creation.
 * ...seriously, I probably made another relic like this with a better code, search that out
 * (and no, it's not Duffel Bag).
 */
public class SchoolBackpack extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "School_Backpack";
	
	public static final int NUMBER_OF_EXTRA_CARDS = 5;
	public static final float CHANCE_OF_UPGRADED_CARDS = 0.1f;
	
	public static int number_of_cards_left = NUMBER_OF_EXTRA_CARDS;
	public static boolean empty_relic = false;
	public static int floor_of_last_stored_reward = 0;
	public static RewardItem card_reward;
	public static ArrayList<String> card_reward_rarity;
	public static ArrayList<String> card_reward_id;
	public static ArrayList<Boolean> card_reward_upgrade;
	
	public static String current_description;

	public static final Logger logger = LogManager.getLogger(SchoolBackpack.class.getName());
	
	public SchoolBackpack() {
		super(ID, GraphicResources.LoadRelicImage("Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"), 
				RelicTier.UNCOMMON, LandingSound.HEAVY);
		
		counter = NUMBER_OF_EXTRA_CARDS;
		
		card_reward_rarity = new ArrayList<String>();
		card_reward_id = new ArrayList<String>();
		card_reward_upgrade = new ArrayList<Boolean>();
		
	}
	
	public String getUpdatedDescription() {
		if (current_description == null) return getCommonDescription();
		else return current_description;
	}
	
	public String getCommonDescription() {
		return DESCRIPTIONS[0] + NUMBER_OF_EXTRA_CARDS + DESCRIPTIONS[1];
	}

	public String getEmptyRelicDescription() {
		return DESCRIPTIONS[3];
	}
	
	@Override
	public void atBattleStart() {
		
	}
	
	public void ChangeToEmptyRelicDescriptionAndToolTips() {
		current_description = getEmptyRelicDescription();
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, current_description));
		initializeTips();
	}
	
	@Override
	public void atPreBattle() {
		boolean no_saved_reward = true;
		
		card_reward = new RewardItem();
		
		if (counter <= 0) {
			ChangeToEmptyRelicDescriptionAndToolTips();
		}
		
		if (AbstractDungeon.floorNum == floor_of_last_stored_reward) {
			AddSavedReward();
			ifEmptyVanishWithCounterNumber();
			no_saved_reward = false;
			
		} else if (!empty_relic) {

			avoidCounterProblemsBetweenSaves();
			
			if (no_saved_reward && (number_of_cards_left > 0) && (AbstractDungeon.getCurrRoom().rewardAllowed)) {
				reduceNumberOfUsesByOne();
				ifEmptyVanishWithCounterNumber();
				if (counter <= 0) {
					ChangeToEmptyRelicDescriptionAndToolTips();
				}
				AddReward();
				floor_of_last_stored_reward = AbstractDungeon.floorNum;
			}
		}
			
		if (!empty_relic && counter <= 0) empty_relic = true;
	}
	
	public void avoidCounterProblemsBetweenSaves() {
		if (number_of_cards_left <= 0) {
			counter = number_of_cards_left;
		} else if ((counter != number_of_cards_left)){
			number_of_cards_left = NUMBER_OF_EXTRA_CARDS;
			counter = NUMBER_OF_EXTRA_CARDS;
		}
	}
	
	public void reduceNumberOfUsesByOne() {
		number_of_cards_left--;
		counter = number_of_cards_left;
	}
	
	public void ifEmptyVanishWithCounterNumber() {
		if (counter <= 0) {
			number_of_cards_left = -2;
			counter = -2;
		}
	}

	private void AddReward() {
		
		PlayerClass reward_class = getRandomBaseGameNotYoursPlayerClass();
		
		card_reward = createReward(reward_class);
		AbstractDungeon.getCurrRoom().addCardReward(card_reward);
		
		flash();
		
		AbstractDungeon.actionManager.addToTop(
				new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
	}
	
	private PlayerClass getRandomBaseGameNotYoursPlayerClass() {
		
		ArrayList<PlayerClass> base_game_classes = new ArrayList<PlayerClass>();
		
		for (PlayerClass base_game_player_class : QCFP_Misc.base_game_player_classes) {
			
			if (base_game_player_class != AbstractDungeon.player.chosenClass) {
				base_game_classes.add(base_game_player_class);
			}
			
		}
		
		return base_game_classes.get(AbstractDungeon.cardRng.random(base_game_classes.size() - 1));

	}
	
	public RewardItem createReward(PlayerClass reward_class) {
		RewardItem reward_created = new RewardItem();
		reward_created.cards.clear();
		reward_created.type = RewardType.CARD;
		reward_created.cards = createCardsFromOtherClassForReward(reward_class);
		reward_created.text = DESCRIPTIONS[2];
		return reward_created;
	}
	
	private ArrayList<AbstractCard> createCardsFromOtherClassForReward(PlayerClass a_class) {
		
		ArrayList<AbstractCard> basic_array_of_cards = new ArrayList<AbstractCard>();
	    
	    int num_cards = 3;
	    num_cards = QCFP_Misc.circunstancesThatChangeCardNumber(num_cards);
	    
	    AbstractCard.CardRarity rarity;
	    for (int i = 0; i < num_cards; i++)
	    {
	    	rarity = AbstractDungeon.rollRarity();
	    	AbstractCard card = null;
	    	
	    	applyRandomRarityToCard(rarity);
	    	
	    	card = getCardAvoidingDuplicates(basic_array_of_cards, rarity, a_class);
	    	
	        if (card != null) {
	        	basic_array_of_cards.add(card);
	        }
	    }
	    ArrayList<AbstractCard> array_of_cards_with_possible_upgrades = new ArrayList<AbstractCard>();
	    for (AbstractCard c : basic_array_of_cards) {
	    	array_of_cards_with_possible_upgrades.add(c.makeCopy());
	    }
	    for (AbstractCard c : array_of_cards_with_possible_upgrades) {
	    	if ((c.rarity != AbstractCard.CardRarity.RARE) && 
	    			(AbstractDungeon.cardRng.randomBoolean(CHANCE_OF_UPGRADED_CARDS)) && (c.canUpgrade())) {
	    		c.upgrade();
	    	} else if ((c.type == AbstractCard.CardType.ATTACK) && 
	    			(AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
	    		c.upgrade();
	    	} else if ((c.type == AbstractCard.CardType.SKILL) &&
	    			(AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
	    		c.upgrade();
	    	} else if ((c.type == AbstractCard.CardType.POWER) &&
	    			(AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
		        c.upgrade();
	    	}
	    }
	    return array_of_cards_with_possible_upgrades;
	}
	
	public void applyRandomRarityToCard(CardRarity rarity) {
		switch (rarity)
    	{
    		case RARE: 
    			AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzStartOffset;
    		break;
    		case UNCOMMON: 
    			break;
    		case COMMON: 
    			AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
		        if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset) {
		        	AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
		        }
		        break;
    		default: 
    			logger.info("Paraphrasing the base game code: WTF?");
    	}
	}

	private static AbstractCard getCardAvoidingDuplicates(ArrayList<AbstractCard> array_of_cards_to_check, 
			CardRarity rarity, PlayerClass a_class) {
		
		AbstractCard card = null;
		
		boolean contains_duplicate = true;
		while (contains_duplicate)
		{
    		contains_duplicate = false;
    		card = getCard(rarity, a_class);

    		for (AbstractCard c : array_of_cards_to_check) {
	        	if (c.cardID.equals(card.cardID))
	        	{
	        		contains_duplicate = true;
	        		break;
	        	}
	      	}
    	}
		
		return card;
	}	
	
	@SuppressWarnings("incomplete-switch")
	public static AbstractCard getCard(AbstractCard.CardRarity rarity, PlayerClass a_class)
	{
		CardGroup rare_class_CardPool = new CardGroup(CardGroupType.UNSPECIFIED);
		CardGroup uncommon_class_CardPool = new CardGroup(CardGroupType.UNSPECIFIED);
		CardGroup common_class_CardPool = new CardGroup(CardGroupType.UNSPECIFIED);
		
		CardColor class_color = getColorOfBaseGameClass(a_class);
		
		for (Map.Entry<String, AbstractCard> a_card : CardLibrary.cards.entrySet()) {
			AbstractCard one_more_card = a_card.getValue();
			
			if (cardIsOfChosenColor(one_more_card, class_color)) {
				
				CardRarity this_card_rarity = ((AbstractCard)one_more_card).rarity;			
				
				switch (this_card_rarity) {
					case RARE:
						rare_class_CardPool.addToBottom((AbstractCard)one_more_card);
						break;
				    case UNCOMMON: 
				    	uncommon_class_CardPool.addToBottom((AbstractCard)one_more_card);
				    	break;
				    case COMMON:
				    	common_class_CardPool.addToBottom((AbstractCard)one_more_card);
				    	break;
				}
					
			}
		}		

		switch (rarity)
		{
			case SPECIAL:  return rare_class_CardPool.getRandomCard(true);
		    case RARE:     return rare_class_CardPool.getRandomCard(true);
		    case UNCOMMON: return uncommon_class_CardPool.getRandomCard(true);
		    case COMMON:   return common_class_CardPool.getRandomCard(true);
		    case CURSE:    return common_class_CardPool.getRandomCard(true);
		    case BASIC:    return common_class_CardPool.getRandomCard(true);
	    }
	    logger.info("Paraphrasing the base code comment: No rarity on getCard in Abstract Dungeon");
	    return null;
	}
	
	public static CardColor getColorOfBaseGameClass(PlayerClass a_class) {
		
		CardColor class_color = CardColor.COLORLESS;
		
		if (a_class == PlayerClass.DEFECT) 				class_color = CardColor.BLUE;
		else if (a_class == PlayerClass.THE_SILENT) 	class_color = CardColor.GREEN;
		else if (a_class == PlayerClass.IRONCLAD) 		class_color = CardColor.RED;
		
		return class_color;
	}
	
	public static boolean cardIsOfChosenColor(AbstractCard one_card, CardColor class_color) {
		
		AbstractCard card = (AbstractCard)one_card;
		
		return (card.color == class_color) && (card.type != AbstractCard.CardType.STATUS) &&
				(card.type != AbstractCard.CardType.CURSE);
		
	}
	
	public void AddSavedReward() {
		
		card_reward = new RewardItem();
		card_reward.cards.clear();
		card_reward.type = RewardType.CARD;
		card_reward.cards = new ArrayList<AbstractCard>();
				
		int size = card_reward_id.size();

		for (int i = 0; i < size; i++) {
			
			AbstractCard reward_card = null;
						
			reward_card = CardLibrary.cards.get(card_reward_id.get(i)).makeCopy();
			
			logger.info(reward_card.name);
			
			if (card_reward_upgrade.get(i)) {
				reward_card.upgrade();
			}
			
			card_reward.cards.add(reward_card);	
		
		}
		
		card_reward.text = DESCRIPTIONS[2];
		
		AbstractDungeon.getCurrRoom().addCardReward(card_reward);
		
		flash();
		
		AbstractDungeon.actionManager.addToTop(
				new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
    		logger.info("Started saving School Backpack information from");
            logger.info(QCFP_Misc.classAndSaveSlotText());

    		if (AbstractDungeon.isDungeonBeaten || AbstractDungeon.player.isDead) {
    			
    		} 
    		else {
    			
    	    	String class_name = AbstractDungeon.player.getClass().getName();

    	    	//1
        		config.setInt("school_backpack_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_number_of_cards_left", number_of_cards_left);

        		//3
                config.setInt("school_backpack_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_floor_of_last_stored_reward", floor_of_last_stored_reward);
                
                //4
                config.setBool("school_backpack_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot +
        				"_empty_relic", empty_relic);
                
                storeCardRewardCreated(config, card_reward);
    			
                try {
    				config.save();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}

            logger.info("Finished saving School Backpack information from");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
        else {
        	clear(config);
        }

    }
	
	public static void storeCardRewardCreated(final SpireConfig config, RewardItem card_reward) {

    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (card_reward == null) {
			
        	config.setInt("school_backpack_" + class_name +
    						"_save_slot_" + CardCrawlGame.saveSlot +
    						"_reward_size", 0);
        	
        } else config.setInt("school_backpack_" + class_name +
								"_save_slot_" + CardCrawlGame.saveSlot +
								"_reward_size", card_reward.cards.size());
        
        if (card_reward != null) {
        	for (int i = 0; i < card_reward.cards.size(); i++) {
        		
            	config.setString("school_backpack_" + class_name +
			        				"_save_slot_" + CardCrawlGame.saveSlot +
			        				"_reward_" + String.valueOf(i),
            			card_reward.cards.get(i).cardID);
            	
            	config.setString("school_backpack_" + class_name + 
			                    	"_save_slot_" + CardCrawlGame.saveSlot + 
			                    	"_reward_rarity_" + String.valueOf(i),
            			card_reward.cards.get(i).rarity.toString());
            	
            	config.setBool("school_backpack" + class_name + 
			                    	"_save_slot_" + CardCrawlGame.saveSlot + 
			                    	"_reward_upgrade_" + String.valueOf(i),
            			card_reward.cards.get(i).upgraded);
            }
        }
		
	}
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading School Backpack info from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
        
        String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) && 
				config.has("school_backpack_class_" + class_name +
							"_save_slot_" + CardCrawlGame.saveSlot +
							"_number_of_cards_left")) {

			number_of_cards_left = config.getInt("school_backpack_class_" + class_name +
													"_save_slot_" + CardCrawlGame.saveSlot +
													"_number_of_cards_left");
			
			loadCardRewardStored(config);
			
			floor_of_last_stored_reward = config.getInt("school_backpack_class_" + class_name +
									    				"_save_slot_" + CardCrawlGame.saveSlot +
									    				"_floor_of_last_stored_reward");
			
			empty_relic = config.getBool("school_backpack_class_" + class_name +
						    				"_save_slot_" + CardCrawlGame.saveSlot +
						    				"_empty_relic");
				
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info("Finished loading School Backpack info from");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting School Backpack variables.");
		}
    }
	
	public static void loadCardRewardStored(final SpireConfig config) {
		
        String class_name = AbstractDungeon.player.getClass().getName();
		
		int size = config.getInt("school_backpack_" + class_name +
									"_save_slot_" + CardCrawlGame.saveSlot +
									"_reward_size");
		
		for (int i = 0; i < size; i++) {
			
			card_reward_rarity.add(config.getString("school_backpack_" + class_name + 
								                	"_save_slot_" + CardCrawlGame.saveSlot + 
								                	"_reward_rarity_" + String.valueOf(i)));		
			
			card_reward_id.add(config.getString("school_backpack_" + class_name +
								    				"_save_slot_" + CardCrawlGame.saveSlot +
								    				"_reward_" + String.valueOf(i)));
			
			card_reward_upgrade.add(config.getBool("school_backpack" + class_name + 
								                	"_save_slot_" + CardCrawlGame.saveSlot + 
								                	"_reward_upgrade_" + String.valueOf(i)));
			
		}
	}
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing School Backpack variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
        String class_name = AbstractDungeon.player.getClass().getName();
		
		//yeah, I know this part could be improved, but I don't have willpower to do it so now.
		//So...
		number_of_cards_left = NUMBER_OF_EXTRA_CARDS;
		empty_relic = false;
		floor_of_last_stored_reward = 0;
		card_reward = null;
		
        config.remove("school_backpack_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_number_of_cards_left");
        config.remove("school_backpack_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_floor_of_last_stored_reward");
        config.remove("school_backpack_class_" + class_name +
						"_save_slot_" + CardCrawlGame.saveSlot +
						"_empty_relic");
        
        if (config.has("school_backpack_" + class_name + 
				"_save_slot_" + CardCrawlGame.saveSlot + 
				"_reward_size")) {
        	clearCardRewardStored(config);
        }
        
        logger.info("Finished clearing School Backpack variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}
	
	public static void clearCardRewardStored(final SpireConfig config) {

        String class_name = AbstractDungeon.player.getClass().getName();
		
		int size = config.getInt("school_backpack_" + class_name + 
									"_save_slot_" + CardCrawlGame.saveSlot + 
									"_reward_size");
        
		for (int i = 0; i < size; i++) {
			
			config.remove("school_backpack_" + class_name + 
		                	"_save_slot_" + CardCrawlGame.saveSlot + 
		                	"_reward_rarity_" + String.valueOf(i));			
			config.remove("school_backpack_" + class_name +
		    				"_save_slot_" + CardCrawlGame.saveSlot +
		    				"_reward_" + String.valueOf(i));
			config.remove("school_backpack" + class_name + 
		                	"_save_slot_" + CardCrawlGame.saveSlot + 
		                	"_reward_upgrade_" + String.valueOf(i));
		
		}
		
		config.remove("school_backpack_" + class_name + 
						"_save_slot_" + CardCrawlGame.saveSlot + 
						"_reward_size");
		
	}
	
	@Override
	public CustomRelic makeCopy() {
		return new SchoolBackpack();
	}

}
