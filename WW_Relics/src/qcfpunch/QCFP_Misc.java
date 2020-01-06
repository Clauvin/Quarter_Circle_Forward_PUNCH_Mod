package qcfpunch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.random.Random;

public class QCFP_Misc {

	public final static String infinite_spire_class_code =
			"infinitespire.InfiniteSpire";
	public final static String replay_the_spire_class_code = 
			"replayTheSpire.ReplayTheSpireMod";
	public final static String conspire_class_code = 
			"conspire.Conspire";
	public final static String the_artist_class_code =
			"theArtist.TheArtist";
	public final static PlayerClass[] base_game_player_classes =
			{PlayerClass.IRONCLAD, PlayerClass.THE_SILENT, PlayerClass.DEFECT};
	public final static CardColor[] base_game_player_classes_colors =
		{CardColor.RED, CardColor.GREEN, CardColor.BLUE, CardColor.COLORLESS};
	
	public final static byte THIS_BYTE_DOES_NOT_MATTER = -1;
	
	public static int number_of_challenger_coin_potions_at_shop = 0;
	
	public static final Logger logger = LogManager.getLogger(QCFP_Misc.class.getName()); // lets us log output
	
	public static String returnModName() {
		return "Quarter Circle Forward PUNCH!";
	}
	
	public static String returnPrefix() {
		return "qcfpunch:";
	}
	
	public static String returnDescription() {
		return "v0.17.2-UnstableGithub" +
				"\r\n"
				  + "\r\n Adds thirty-two relics (two need The Artist mod, one is Custom Mode only for now)"
				  + "\r\n"
				  + "based mostly in SF2's main characters (also other fighting games), twelve game modifiers, one event and one potion, all in English and simplified Chinese."
				  + "\r\n"
				  + "\r\n v1.0 will have more relics."
				  + "\r\n"
				  + "\r\n Most of the images in the mod are temporary and will be substituted/improved on v1.0.";
	}
	
	public static String returnLocalizationMainFolder() {
		return "qcfpunch/localization/";
	}
	
	public static String returnSpecificLocalizationFile(String specific_file_path) {
		return returnLocalizationMainFolder() + specific_file_path;
	}
	
	public static String returnCardsImageMainFolder() {
		return "qcfpunch/images/cards/";
	}
	
	//Yes, this is me not wanting to go raw to the ShopScreen.initPotions class.
	public static void resetNumberOfChallengerCoinPotionsVariable() {
		number_of_challenger_coin_potions_at_shop = 0;
	}
	
	public static void incrementNumberOfChallengerCoinPotionsAtShop() {
		number_of_challenger_coin_potions_at_shop++;
	}

	//just stole this code from ReplayTheSpire who stole this from blank lol
    public static boolean checkForMod(final String classPath) {
        try {
            Class.forName(classPath);
            QCFPunch_Mod.logger.info("Found mod: " + classPath);
            return true;
        }
        catch (ClassNotFoundException | NoClassDefFoundError ex) {
        	QCFPunch_Mod.logger.info("Could not find mod: " + classPath);
            return false;
        }
    }
        
    //just stole this code from ReplayTheSpire who stole this from blank lol
    public static boolean silentlyCheckForMod(final String classPath) {
        try {
            Class.forName(classPath);
            return true;
        }
        catch (ClassNotFoundException | NoClassDefFoundError ex) {
            return false;
        }
    }
    
    public static void addNonFastModeWaitAction(float amount) {
		if (!Settings.FAST_MODE) {
        	AbstractDungeon.actionManager.addToBottom(new WaitAction(amount));
        }
    }
    
	public static Boolean abscenceOfNoDrawPower() {
		return !AbstractDungeon.player.hasPower("No Draw");
	}
	
	public static Boolean hasNoDrawPower() {
		return AbstractDungeon.player.hasPower("No Draw");
	}
	
	public static boolean isItACurse(AbstractCard card) {
		return ((card.color == CardColor.CURSE) || (card.type == CardType.CURSE));
	}
	
	public static boolean isItAStatus(AbstractCard card) {
		return (card.type == CardType.STATUS);
	}
	
	public static void setCardToAlwaysRetain(
			AbstractCard the_card, boolean willRetain) {
		
		AlwaysRetainField.alwaysRetain.set(the_card, willRetain);
		the_card.retain = willRetain;
		
		String upper_cased_retain = GameDictionary.RETAIN.NAMES[0].
				substring(0, 1).toUpperCase() + 
				GameDictionary.RETAIN.NAMES[0].substring(1);

		if (Settings.language == GameLanguage.ZHS)
			upper_cased_retain = "" + upper_cased_retain;
		
		the_card.rawDescription = upper_cased_retain + ". NL " +
				the_card.rawDescription;
		the_card.initializeDescription();
		
	}
	
	public static void setCardToHaveExhaustOrEtherealIfItsNotAlready(
			AbstractCard the_card) {
		
		boolean has_ethereal = false;
		boolean has_exhaust = false;
		
		if (the_card.exhaust || the_card.exhaustOnFire ||
				the_card.exhaustOnUseOnce) {
			has_exhaust = true;
		}
		if (the_card.isEthereal) has_ethereal = true;
		
		if (has_ethereal || has_exhaust) return;
		
		int choose = headsOrTails(new Random());
		
		if (choose == 1) {
			setCardToHaveEthereal(the_card);
		} else {
			setCardToHaveExhaust(the_card);
		}

	}
	
	public static void setCardToHaveEthereal(AbstractCard the_card) {
		
		the_card.isEthereal = true;
		
		String upper_cased_ethereal = GameDictionary.ETHEREAL.NAMES[0].
				substring(0, 1).toUpperCase() + 
				GameDictionary.ETHEREAL.NAMES[0].substring(1);
		
		if (Settings.language == GameLanguage.ZHS)
			upper_cased_ethereal = "" + upper_cased_ethereal;
		
		the_card.rawDescription = upper_cased_ethereal + ". NL " +
				the_card.rawDescription;
		the_card.initializeDescription();
		
	}
	
	public static void setCardToHaveExhaust(AbstractCard the_card) {
		
		the_card.exhaust = true;
		
		String upper_cased_exhaust = GameDictionary.EXHAUST.NAMES[0].
				substring(0, 1).toUpperCase() + 
				GameDictionary.EXHAUST.NAMES[0].substring(1);
		
		QCFP_Misc.fastLoggerLine(upper_cased_exhaust);
		
		if (Settings.language == GameLanguage.ZHS)
			upper_cased_exhaust = "" + upper_cased_exhaust;
		
		the_card.rawDescription += " NL " +
				upper_cased_exhaust + ".";
		
		QCFP_Misc.fastLoggerLine(the_card.rawDescription);
		
		the_card.initializeDescription();
		
		
	}
	
	public static boolean cardIsACurseOrStatus(AbstractCard card) {
		return ((card.type == CardType.CURSE) || (card.type == CardType.STATUS)
				|| (card.color == CardColor.CURSE));
	}
	
	public static boolean cardIsACurse(AbstractCard card) {
		return (card.type == CardType.CURSE) || (card.color == CardColor.CURSE);
	}
	
	public static void reduceCardCostIfNotStatusOrCurseByOne(AbstractCard card) {
		
		if (!cardIsACurseOrStatus(card)) {
			if (card.cost > 0) card.modifyCostForCombat(-1);
		}
		
	}
	
	public static AbstractCard doCopyWithEtherealExhaustAndDescription(AbstractCard
			original_card) {
		
		AbstractCard new_card = original_card.makeStatEquivalentCopy();
		new_card.isEthereal = original_card.isEthereal;
		new_card.exhaust = original_card.exhaust;
		new_card.exhaustOnFire = original_card.exhaustOnFire;
		new_card.exhaustOnUseOnce = original_card.exhaustOnUseOnce;
		new_card.description = original_card.description;
		new_card.rawDescription = original_card.rawDescription;
		
		if (new_card.isEthereal && original_card.cost == 0) {
			new_card.modifyCostForCombat(1);
		}
		
		return new_card;
	}
	
	public static int circunstancesThatChangeCardNumber(int num_cards) {
		if (AbstractDungeon.player.hasRelic("Question Card")) 	num_cards++;
		if (AbstractDungeon.player.hasRelic("Busted Crown")) 	num_cards -= 2;
		if (ModHelper.isModEnabled("Binary")) 					num_cards--;
	
		return num_cards;
	}
	
	public static boolean cardIsOfChosenColor(AbstractCard one_card, CardColor class_color) {
		
		AbstractCard card = (AbstractCard)one_card;
		
		return (card.color == class_color) &&
				(card.type != AbstractCard.CardType.STATUS) &&
				(card.type != AbstractCard.CardType.CURSE);
		
	}
	
	public static boolean haveSpaceForANewPotion() {
		int index = 0;
	    for (AbstractPotion p : AbstractDungeon.player.potions) {
	    	if (p instanceof PotionSlot) break;
	    	index++;
	    } 
	    
	    if (index < AbstractDungeon.player.potionSlots) return true;
	    else return false;
	}
	
	public static int headsOrTails(Random random) {
		return random.random(1);
	}
	
	public static int rollRandomValue(Random random, int range) {
		logger.info("range " + range);
		int value = random.random(range);
		logger.info("value " + value);
		if (value < 0) value *= -1;
		return value;
	}
	
	//Yeah, I know, this can be better
	public static int rollPositiveRandomValue(Random random, int range) {
		int value = rollRandomValue(random, range-1);
		return value+1;
	}
	
	public static int min(int a, int b) {
		if (a <= b) return a;
		else return b;
	}
    
	public static String debugStringStartedSaveDataManagement(String name) {
		return "Saving " + name + " information from";
	}
	
	public static String debugStringFinishedSaveDataManagement(String name) {
		return "Finished saving " + name + " information from";
	}
	
	public static String debugStringStartedLoadDataManagement(String name) {
		return "Loading " + name + " information from";
	}
	
	public static String debugStringFinishedLoadDataManagement(String name) {
		return "Finished loading " + name + " information from";
	}
	
	public static String debugStringStartedClearDataManagement(String name) {
		return "Clearing " + name + " information from";
	}
	
	public static String debugStringFinishedClearDataManagement(String name) {
		return "Finished clearing " + name + " information from";
	}
	
    public static String classAndSaveSlotText() {
    	return "character " + AbstractDungeon.player.getClass().getName() +
    			", save slot " + CardCrawlGame.saveSlot + ".";
    }

    public static void debugOnlyLoggerLine(Logger logger, String message) {
    	if (Loader.DEBUG) {
            logger.info(message);
        }
    }
    
    public static void fastLoggerLine(Boolean message) {
    	String converted_message = message.toString();
    	fastLoggerLine(converted_message);
    }
    
    public static void fastLoggerLine(int message) {
    	fastLoggerLine("" + message);
    }
    
    public static void fastLoggerLine(String message) {
    	logger.info(message);
    }
    
}
