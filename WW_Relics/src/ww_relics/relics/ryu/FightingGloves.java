package ww_relics.relics.ryu;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.cards.CardGroup.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.CustomRelic;
import ww_relics.WW_Relics_MiscelaneaCode;
import ww_relics.resources.relic_graphics.GraphicResources;

import java.util.*;

public class FightingGloves extends CustomRelic implements ClickableRelic {
	
	public static final Logger logger = LogManager.getLogger(FightingGloves.class.getName());
	
	public static final String ID = "WW_Relics:Fighting_Gloves";
	private static final int INITIAL_CHARGES = 1;
	private static int positive_charges;
	private static final int EVERY_X_ROOMS_VISITED_ADDS_A_CHARGE = 4;
	private static int rooms_visited;
	
	private static int number_of_cards_that_can_be_upgraded;
	
	private static boolean cards_have_been_upgraded_in_this_room = false;
	private static int number_of_cards_upgraded_in_this_room = 0;
	
	private static boolean player_right_clicked_in_relic_in_this_room = false;
	private static boolean player_havent_right_clicked_in_relic_here_before = true;
	
	public FightingGloves() {
		super(ID, GraphicResources.LoadRelicImage("Fighting Gloves - mailed-gloves - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.SOLID);
		belowZeroCheck();
		counter = INITIAL_CHARGES;
		positive_charges = INITIAL_CHARGES;
		rooms_visited = 0;
	}
	
	public static int getCharges() {
		return positive_charges;
	}
	
	public static void setCharges(int value) {
		positive_charges = value;
		belowZeroCheck();
	}
	
	public static void belowZeroCheck() {
		if (positive_charges < 0) {
			logger.info("WARNING - For some reason, FightingGloves.positive_charges was with " +
					positive_charges + " value.");
			positive_charges = 0;
			logger.info("And it's now 0. Tell the developer about it? Thanks!");
		}
		
	}
	
	public static void addCharges(int value_to_add) {
		positive_charges += value_to_add;
		belowZeroCheck();
	}
	
	public static void removeCharges(int value_to_subtract) {
		positive_charges -= value_to_subtract;
		belowZeroCheck();
	}
	
	public String getUpdatedDescription() {
		String description = "";
	
		description = DESCRIPTIONS[0] + INITIAL_CHARGES + DESCRIPTIONS[1] + DESCRIPTIONS[2] +
					DESCRIPTIONS[3] + EVERY_X_ROOMS_VISITED_ADDS_A_CHARGE +
					DESCRIPTIONS[4];

		return description;
	}
	
	public String getCardGridDescription() {
		String description = "";
		
		description = DESCRIPTIONS[5] + number_of_cards_that_can_be_upgraded;
		
		if (number_of_cards_that_can_be_upgraded > 1) description += DESCRIPTIONS[7];
		else description += DESCRIPTIONS[6];
			
		description += DESCRIPTIONS[8];
		
		return description;

	}
	
	public void onEnterRoom(AbstractRoom room) {
		cards_have_been_upgraded_in_this_room = false;
		player_right_clicked_in_relic_in_this_room = false;
		player_havent_right_clicked_in_relic_here_before = true;
		number_of_cards_upgraded_in_this_room = 0;
		rooms_visited++;
		if (rooms_visited % EVERY_X_ROOMS_VISITED_ADDS_A_CHARGE == 0) {
			addCharges(1);
			flash();
		}
		counter = positive_charges;
		
		if ((room instanceof RestRoom) && (counter > 0)){
			flash();
		}
	}
	
	@Override
	public void onRightClick() {
		
		if (player_havent_right_clicked_in_relic_here_before) {
			if (haveCharges() && (haveCardsToUpgrade())) {
				if (AbstractDungeon.getCurrRoom() instanceof RestRoom && 
						AbstractDungeon.getCurrRoom().phase ==
						AbstractRoom.RoomPhase.INCOMPLETE && CampfireUI.hidden == false) {
					player_havent_right_clicked_in_relic_here_before = false;
					upgradingCards();
				}
			}
		}
		
	}
	
	private boolean haveCharges() {	return positive_charges > 0; }
	
	private boolean haveCardsToUpgrade() { return getValidCardGroup().size() > 0; }
	
	private CardGroup getValidCardGroup() {
		
		CardGroup valid_card_group = new CardGroup(CardGroupType.UNSPECIFIED);
		CardGroup master_deck = AbstractDungeon.player.masterDeck;
		
		for (AbstractCard c : master_deck.group){
			if (c.canUpgrade()) {
				valid_card_group.addToTop(c);
			}
		}
		
		return valid_card_group;
		
	}
	
	public void upgradingCards() {

		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		AbstractDungeon.getCurrRoom().phase = RoomPhase.INCOMPLETE;
		
		if (getValidCardGroup().size() >= positive_charges) {

			number_of_cards_that_can_be_upgraded = positive_charges;
			
		} else {
			
			number_of_cards_that_can_be_upgraded = getValidCardGroup().size();
			
		}
		
		AbstractDungeon.gridSelectScreen.open(getValidCardGroup(),
				number_of_cards_that_can_be_upgraded,
				getCardGridDescription(), false, false, true, false);
		
		player_right_clicked_in_relic_in_this_room = true;
		
	}
	
	public void update()
	{
		super.update();

		if (player_right_clicked_in_relic_in_this_room) {
			if (isTimeToUpgradeTheChosenCards())
		    {
				logger.info("Step 1");
				
	            flash();
				
				ArrayList<AbstractCard> cards_chosen = getCardsToUpgrade();
				
				upgradeAndShowChosenCards(cards_chosen);
				
				spendChargesForUpgradedCards();
				
				if (number_of_cards_upgraded_in_this_room == number_of_cards_that_can_be_upgraded)
					cards_have_been_upgraded_in_this_room = true;
				
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				
				AbstractDungeon.overlayMenu.hideBlackScreen();
				AbstractDungeon.dynamicBanner.appear();
				AbstractDungeon.isScreenUp = false;
				
		    }
		}
		
		
	}
	
	private static boolean isTimeToUpgradeTheChosenCards() {
		
		boolean relic_did_not_upgrade_cards_here = !cards_have_been_upgraded_in_this_room;
		boolean i_am_in_a_rest_room = AbstractDungeon.getCurrRoom() instanceof RestRoom;
		boolean all_cards_to_upgrade_have_been_chosen = 
				AbstractDungeon.gridSelectScreen.selectedCards.size() ==
					number_of_cards_that_can_be_upgraded;
		
		return relic_did_not_upgrade_cards_here && i_am_in_a_rest_room && 
				all_cards_to_upgrade_have_been_chosen;
	}
	
	private static ArrayList<AbstractCard> getCardsToUpgrade() {
		return AbstractDungeon.gridSelectScreen.selectedCards;
	}
	
	private static void upgradeAndShowChosenCards(ArrayList<AbstractCard> chosen_cards) {
		
		float x = Settings.WIDTH;
        float y = Settings.HEIGHT;
        float defined_x = 0.10f;
        float defined_y = 0.25f;
		
        logger.info("c " + chosen_cards.size());
        
		for (AbstractCard c: chosen_cards) {
    		c.upgrade();
			logger.info("Upgraded " + c.name);
    		
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(
    				c.makeStatEquivalentCopy(), defined_x * x, defined_y * y));
            
            defined_x += 0.15f;
            if (defined_x >= 0.9f) {
            	defined_x = 0.10f;
            	defined_y += 0.3f;
            }
            
            number_of_cards_upgraded_in_this_room++;
    	}
	}
	
	private void spendChargesForUpgradedCards() {
		addCharges(-number_of_cards_that_can_be_upgraded);
		
		counter = positive_charges;
	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
    		logger.info("Started saving Fighting Gloves information from");
    		logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());

        	String class_name = AbstractDungeon.player.getClass().getName();
    		
            config.setInt("fighting_gloves_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_rooms_visited", rooms_visited);
            config.setInt("fighting_gloves_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_positive_charges", positive_charges);

            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            logger.info("Finished saving Fighting Gloves info from");
            logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());	
        }
        else {

        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading Fighting Gloves info from");
        logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has("fighting_gloves_class_" + class_name +
	            		"_save_slot_" + CardCrawlGame.saveSlot +
	            		"_rooms_visited")) {

			rooms_visited = config.getInt("fighting_gloves_class_" + class_name +
            								"_save_slot_" + CardCrawlGame.saveSlot +
            								"_rooms_visited");
			setCharges(config.getInt("fighting_gloves_class_" + class_name +
            							"_save_slot_" + CardCrawlGame.saveSlot +
            							"_positive_charges"));
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info("Finished loading Fighting Gloves info from");
            logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting Fighting Gloves variables.");
		}
		
    }
	
	public boolean canSpawn() {
		
		return (AbstractDungeon.floorNum < 35) || (AbstractDungeon.floorNum > 54);
		
	}
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing Fighting Gloves variables from");
        logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
		
        config.remove("fighting_gloves_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_rooms_visited");
        config.remove("fighting_gloves_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_positive_charges");
        
        logger.info("Finished clearing Fighting Gloves variables from");
        logger.info(WW_Relics_MiscelaneaCode.classAndSaveSlotText());
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new FightingGloves();
	}

}