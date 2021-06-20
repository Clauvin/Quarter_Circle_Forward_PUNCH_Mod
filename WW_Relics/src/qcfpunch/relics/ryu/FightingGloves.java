package qcfpunch.relics.ryu;

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
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom.*;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;
import qcfpunch.restsite.FightingGlovesTrainOption;

import java.util.*;

public class FightingGloves extends CustomRelic implements ClickableRelic {
	
	public static final Logger logger = LogManager.getLogger(FightingGloves.class.getName());
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Fighting_Gloves";
	private static final int INITIAL_CHARGES = 1;
	private static int positive_charges;
	private static final int EVERY_X_ROOMS_VISITED_ADDS_A_CHARGE = 4;
	private static int rooms_visited;
	
	private static int number_of_cards_that_can_be_upgraded;
	
	public static boolean cards_have_been_upgraded_in_this_room = false;
	public static int number_of_cards_upgraded_in_this_room = 0;
	
	private static boolean player_right_clicked_in_relic_in_this_room = false;
	public static boolean player_havent_right_clicked_in_relic_here_before = true;

	private static CardGroup cards_to_be_upgraded;

	private static ShowCardBrieflyEffect show_card_briefly_effect;
	private static AbstractCard upgraded_card;
	
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
	
	public boolean shouldTheRelicBeUsedNow() {
		if (haveCharges() && (haveCardsToUpgrade() &&
				AbstractDungeon.getCurrRoom() instanceof RestRoom &&
				AbstractDungeon.getCurrRoom().phase ==
				AbstractRoom.RoomPhase.INCOMPLETE &&
				CampfireUI.hidden == false)) {
					return true;
		} else {
			return false;
		}
	}
	
	private boolean haveCharges() {	return positive_charges > 0; }
	
	private boolean haveCardsToUpgrade() { return getValidCardGroup().size() > 0; }
	
	private static CardGroup getValidCardGroup() {
		
		CardGroup valid_card_group = new CardGroup(CardGroupType.UNSPECIFIED);
		CardGroup master_deck = AbstractDungeon.player.masterDeck;
		
		for (AbstractCard c : master_deck.group){
			if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.conspire_class_code)) {
				supportInfiniteJournalLimitlessUpgrading(valid_card_group, c);
			} else if (c.canUpgrade()){
				valid_card_group.addToTop(c);
			}
		}
		
		return valid_card_group;
		
	}
	
	private static void supportInfiniteJournalLimitlessUpgrading(
			CardGroup valid_card_group, AbstractCard c) {
		if (conspire.relics.InfiniteJournal.canUpgradeCard(c)) {
			valid_card_group.addToTop(c);
		}
	}
	
	public void upgradingCards() {

		AbstractDungeon.dynamicBanner.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		AbstractDungeon.previousScreen = AbstractDungeon.screen;
		
		AbstractDungeon.getCurrRoom().phase = RoomPhase.INCOMPLETE;
		
		number_of_cards_that_can_be_upgraded = howManyCardsCanBeUpgraded();

		cards_to_be_upgraded = getValidCardGroup();

		AbstractDungeon.gridSelectScreen.open(cards_to_be_upgraded,
				number_of_cards_that_can_be_upgraded,
				getCardGridDescription(), false, false, true, false);

		player_right_clicked_in_relic_in_this_room = true;
		
	}
	
	public static int howManyCardsCanBeUpgraded() {
		
		if (getValidCardGroup().size() >= positive_charges) return positive_charges;
		else return getValidCardGroup().size();

	}
	
	public void update()
	{
		super.update();

		if (player_right_clicked_in_relic_in_this_room) {

			if (cards_to_be_upgraded != null){

				for (int i = 0; i < cards_to_be_upgraded.size(); i++)
				{
					if (cards_to_be_upgraded.getNCardFromTop(i).hb.hovered){
						showUpgradedVersionOfTheCard(
								cards_to_be_upgraded.getNCardFromTop(i));
					}
				}
			}

			if (isTimeToUpgradeTheChosenCards())
		    {
	            flash();
				
				ArrayList<AbstractCard> cards_chosen = getCardsToUpgrade();
				
				upgradeAndShowChosenCards(cards_chosen);
				
				spendChargesForUpgradedCards();
				
				if (number_of_cards_upgraded_in_this_room ==
						number_of_cards_that_can_be_upgraded)
					cards_have_been_upgraded_in_this_room = true;
				
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				
				AbstractDungeon.overlayMenu.hideBlackScreen();
				AbstractDungeon.isScreenUp = false;

				cards_to_be_upgraded = null;

		    }
		}
	}

	private static void showUpgradedVersionOfTheCard(AbstractCard card){

		float x = Settings.WIDTH;
		float y = Settings.HEIGHT;
		float defined_x = 0.10f;
		float defined_y = 0.50f;
		float duration = 4.0f;
		float starting_duration = 10.0f;

		QCFP_Misc.fastLoggerLine("2");
		QCFP_Misc.fastLoggerLine(upgraded_card == null);

		if (upgraded_card == null){
			upgraded_card = card.makeStatEquivalentCopy();
			upgraded_card.upgrade();

			if (show_card_briefly_effect == null){

				QCFP_Misc.fastLoggerLine("3");
				show_card_briefly_effect = new ShowCardBrieflyEffect(
						upgraded_card.makeStatEquivalentCopy(),
						defined_x * x, defined_y * y);
				show_card_briefly_effect.duration = duration;
				show_card_briefly_effect.startingDuration = starting_duration;

				AbstractDungeon.effectList.add(show_card_briefly_effect);

			} else {
				show_card_briefly_effect.duration = duration;
			}
		} else if (upgraded_card.originalName == card.name){
			show_card_briefly_effect.duration = duration;
		} else {

			upgraded_card = card.makeStatEquivalentCopy();
			upgraded_card.upgrade();

			show_card_briefly_effect = new ShowCardBrieflyEffect(
					upgraded_card.makeStatEquivalentCopy(),
					defined_x * x, defined_y * y);
			show_card_briefly_effect.duration = duration;
			show_card_briefly_effect.startingDuration = starting_duration;

			AbstractDungeon.effectList.add(show_card_briefly_effect);

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
	
	private static void upgradeAndShowChosenCards(
			ArrayList<AbstractCard> chosen_cards) {
		
		float x = Settings.WIDTH;
        float y = Settings.HEIGHT;
        float defined_x = 0.10f;
        float defined_y = 0.25f;
		float initial_time = 2.5f;
		float extra_time_per_card = 0.2f;
        
        logger.info("c " + chosen_cards.size());
        
		for (AbstractCard c: chosen_cards) {
			if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.conspire_class_code)) {
				conspire.relics.InfiniteJournal.upgradeCard(c);
			} else {
	    		c.upgrade();
			}
    		AbstractDungeon.player.bottledCardUpgradeCheck(c);
			logger.info("Upgraded " + c.name);
    		
			ShowCardBrieflyEffect card_brief_effect = new ShowCardBrieflyEffect(
    				c.makeStatEquivalentCopy(), defined_x * x, defined_y * y);
			card_brief_effect.duration = initial_time;
			card_brief_effect.startingDuration = initial_time;
						
            AbstractDungeon.effectList.add(card_brief_effect);
            
    		initial_time += extra_time_per_card;
            
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
	
	@Override
	public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
		if (shouldTheRelicBeUsedNow()) {
			options.add(new FightingGlovesTrainOption(true)); 
		} else {
			options.add(new FightingGlovesTrainOption(false)); 
		}
		
	}

	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
    		logger.info("Started saving Fighting Gloves information from");
    		logger.info(QCFP_Misc.classAndSaveSlotText());

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
            logger.info(QCFP_Misc.classAndSaveSlotText());	
        }
        else {

        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading Fighting Gloves info from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
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
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting Fighting Gloves variables.");
		}
		
    }
	
	public boolean canSpawn() {
		
		if (AbstractDungeon.id == TheEnding.ID) return false;
		else if ((AbstractDungeon.actNum % 3 == 0) &&
					(AbstractDungeon.currMapNode.y > 3 * AbstractDungeon.map.size() / 4))
			return false;
		else return true;
		
	}
	
	public static void clear(final SpireConfig config) {
		logger.info("Clearing Fighting Gloves variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
		
        config.remove("fighting_gloves_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_rooms_visited");
        config.remove("fighting_gloves_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_positive_charges");
        
        logger.info("Finished clearing Fighting Gloves variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new FightingGloves();
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		
	}

}