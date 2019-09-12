package qcfpunch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class QCFPunch_MiscCode {

	public final static String infinite_spire_class_code = "infinitespire.InfiniteSpire";
	public final static String replay_the_spire_class_code = "replayTheSpire.ReplayTheSpireMod";
	public final static PlayerClass[] base_game_player_classes =
			{PlayerClass.IRONCLAD, PlayerClass.THE_SILENT, PlayerClass.DEFECT};
	
	public final static byte THIS_BYTE_DOES_NOT_MATTER = -1;
	
	public static int number_of_challenger_coin_potions_at_shop = 0;
	
	public static final Logger logger = LogManager.getLogger(QCFPunch_MiscCode.class.getName()); // lets us log output
	
	public static String returnModName() {
		return "Quarter Circle Forward PUNCH!";
	}
	
	public static String returnPrefix() {
		return "qcfpunch:";
	}
	
	public static String returnDescription() {
		return "v0.14.0-UnstableGithub" +
				"\r\n"
				  + "\r\n Adds twenty-two relics based mostly in SF2's main characters (also other fighting games), eight game modifiers, one event and one potion."
				  + "\r\n"
				  + "\r\n v1.0 will have 32+ relics."
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
    
    public static String classAndSaveSlotText() {
    	return "character " + AbstractDungeon.player.getClass().getName() +
    			", save slot " + CardCrawlGame.saveSlot + ".";
    }
    
    public static void debugOnlyLoggerLine(Logger logger, String message) {
    	if (Loader.DEBUG) {
            logger.info(message);
        }
    }
	
}
