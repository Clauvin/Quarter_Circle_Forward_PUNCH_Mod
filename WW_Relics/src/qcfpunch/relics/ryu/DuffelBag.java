package qcfpunch.relics.ryu;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;

import basemod.abstracts.CustomRelic;
import qcfpunch.DuffelBagBandageCardReward;
import qcfpunch.DuffelBagPanaceaCardReward;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

//also known as: The champion relic when they are measured regarding bug fixing. -_- 
public class DuffelBag extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Duffel_Bag";
	private static final int NUMBER_OF_STATIC_CARDS = 2;

	private static final int NUMBER_OF_RANDOM_COMMON_RELICS = 2;

	private static final int PRETENDED_NUMBER_OF_EXTRA_REWARDS = NUMBER_OF_STATIC_CARDS +
			NUMBER_OF_RANDOM_COMMON_RELICS;

	private static int last_floor_where_relic_was_used = 0;
	
	public static final Logger logger = LogManager.getLogger(DuffelBag.class.getName());
	
	public DuffelBag() {
		super(ID, GraphicResources.LoadRelicImage("Duffel_Bag - swap-bag - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.FLAT);

		SetNumberofRewards(PRETENDED_NUMBER_OF_EXTRA_REWARDS);
		
		last_floor_where_relic_was_used = 0;
	}

	private void SetNumberofRewards(int new_value) {
		this.counter = new_value;
		if (this.counter == 0) this.counter = -2; 
	}

	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + NUMBER_OF_RANDOM_COMMON_RELICS +
				DESCRIPTIONS[1];
	}
		
	@Override
	public void atPreBattle() {
		
		if ((currentRoomIsAMonsterOrMonsterEliteRoom()) &&
				this.counter > 0 && 
				AbstractDungeon.getCurrRoom().rewardAllowed){
			
			if (last_floor_where_relic_was_used == AbstractDungeon.floorNum){
				
				//line below necessary to avoid wrong reward being added when game is loaded at reward screen
				AddNumberOfRewards(1);
				AddReward();
				AddNumberOfRewards(-1);
				
			} else if (last_floor_where_relic_was_used < AbstractDungeon.floorNum){
				
				last_floor_where_relic_was_used = AbstractDungeon.floorNum;
				AddReward();
				AddNumberOfRewards(-1);
				
			}
			
		}
		
		if (counter <= 0) {
			ChangeToSecondDescription();
		}
		
	}
	
	public boolean currentRoomIsAMonsterOrMonsterEliteRoom() {
		return AbstractDungeon.getCurrRoom() instanceof MonsterRoom ||
				AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite;
	}
	
	private void AddNumberOfRewards(int added) {
		this.counter += added;
		if (this.counter == 0) this.counter = -2; 
	}
	
	@Override
	public void atBattleStart() {
		if (this.counter == -2)
		{
			ChangeToSecondDescription();
		}
	}
	
	private void ChangeToSecondDescription(){
		this.counter = -3;
		this.description = this.DESCRIPTIONS[2];
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		initializeTips();
	}
	
	public void onUsePotion() {
		
		if ((AbstractDungeon.player.isEscaping) &&
				(last_floor_where_relic_was_used == AbstractDungeon.floorNum)) {
			AddNumberOfRewards(1);
		}
		
		if (this.counter > 0)
		{
			ChangeToFirstDescription();
		}
		
	}
	
	private void ChangeToFirstDescription() {
		this.description = DESCRIPTIONS[0] + NUMBER_OF_RANDOM_COMMON_RELICS +
				DESCRIPTIONS[1];
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		initializeTips();
	}
	
	private void AddReward() {
		
		if (this.counter - NUMBER_OF_STATIC_CARDS > 0) {
			
			AddCard();
			
		} else if (this.counter > 0) {
			
			AddRelic();
			
		}
		
	}
	
	private void AddCard() {
		
		int card_position = this.counter - NUMBER_OF_STATIC_CARDS;

		if (card_position == 2){
			AbstractDungeon.getCurrRoom().addCardReward(new DuffelBagBandageCardReward());
		} else if (card_position == 1){
			AbstractDungeon.getCurrRoom().addCardReward(new DuffelBagPanaceaCardReward());
		}

		flash();
	}
	
	private String createMessageFoundStuffInside(String name_of_stuff) {
		logger.info(name_of_stuff);
		if (name_of_stuff.compareTo("Bandage Up") == 0)
			return DESCRIPTIONS[3] + DESCRIPTIONS[5] + DESCRIPTIONS[4];
		
		else return DESCRIPTIONS[3] + name_of_stuff + DESCRIPTIONS[4];
	}	
	
	private void AddRelic() {
		
		AbstractRelic relic = AbstractDungeon.returnRandomRelic(RelicTier.COMMON);
		RewardItem relic_reward = new RewardItem(relic);
		relic_reward.text = createMessageFoundStuffInside(relic.name);
		AbstractDungeon.getCurrRoom().rewards.add(relic_reward);
		flash();

	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
        	String class_name = AbstractDungeon.player.getClass().getName();
        	
    		logger.info("Started saving Duffel Bag information from");
    		logger.info(QCFP_Misc.classAndSaveSlotText());

            config.setInt("duffel_bag_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_last_floor_where_relic_was_used",
            			last_floor_where_relic_was_used);
            
            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            logger.info("Finished saving Duffel Bag info from");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info("Loading Duffel Bag info");
        logger.info(QCFP_Misc.classAndSaveSlotText());
        
    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has("duffel_bag_class_" + class_name +
	            		"_save_slot_" + CardCrawlGame.saveSlot +
	            		"_last_floor_where_relic_was_used")) {

			last_floor_where_relic_was_used = config.getInt(
					"duffel_bag_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot +
            		"_last_floor_where_relic_was_used");
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info("Finished loading Duffel Bag info");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting Duffel Bag variables.");
		}
		
    }
	
	public static void clear(final SpireConfig config) {

    	String class_name = AbstractDungeon.player.getClass().getName();
		
		if (config.has("duffel_bag_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot +
        		"_last_floor_where_relic_was_used")) {
			
			logger.info("Clearing Duffel Bag variables from");
	        logger.info(QCFP_Misc.classAndSaveSlotText());			
			
			config.remove("duffel_bag_class_" + class_name +
	        		"_save_slot_" + CardCrawlGame.saveSlot +
	        		"_last_floor_where_relic_was_used");
			
			logger.info("Finished clearing Duffel Bag variables from");
	        logger.info(QCFP_Misc.classAndSaveSlotText());	
			
		} else {
			logger.info("No Duffel Bag variables to clean from this case.");
		}
	
        
	}
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new DuffelBag();
	}	
}
