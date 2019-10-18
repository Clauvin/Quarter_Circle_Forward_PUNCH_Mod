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
import com.megacrit.cardcrawl.random.Random;

public class QCFP_Misc {

	public final static String infinite_spire_class_code =
			"infinitespire.InfiniteSpire";
	public final static String replay_the_spire_class_code = 
			"replayTheSpire.ReplayTheSpireMod";
	public final static String the_artist_class_code =
			"theArtist.TheArtist";
	public final static PlayerClass[] base_game_player_classes =
			{PlayerClass.IRONCLAD, PlayerClass.THE_SILENT, PlayerClass.DEFECT};
	
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
		return "v0.15.5-UnstableGithub" +
				"\r\n"
				  + "\r\n Adds twenty-seven relics based mostly in SF2's main characters (also other fighting games), twelve game modifiers, one event and one potion, most of them in English and simplified Chinese."
				  + "\r\n"
				  + "\r\n v1.0 will have 34+ relics."
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
	
	public static boolean cardIsACurseOrStatus(AbstractCard card) {
		return ((card.type == CardType.CURSE) || (card.type == CardType.STATUS)
				|| (card.color == CardColor.CURSE));
	}
	
	public static void reduceCardCostIfNotStatusOrCurse(AbstractCard card) {
		
		if (!cardIsACurseOrStatus(card)) {
			if (card.cost > 0) card.modifyCostForCombat(-1);
		}
		
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
    
    public static String classAndSaveSlotText() {
    	return "character " + AbstractDungeon.player.getClass().getName() +
    			", save slot " + CardCrawlGame.saveSlot + ".";
    }
    
    public static void debugOnlyLoggerLine(Logger logger, String message) {
    	if (Loader.DEBUG) {
            logger.info(message);
        }
    }
	
    public static void fastLoggerLine(String message) {
    	logger.info(message);
    }
    
}
