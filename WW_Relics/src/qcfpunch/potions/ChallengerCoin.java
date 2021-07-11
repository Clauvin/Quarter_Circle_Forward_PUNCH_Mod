package qcfpunch.potions;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.mod.replay.rooms.TeleportRoom;
import infinitespire.rooms.NightmareEliteRoom;
import qcfpunch.QCFP_Misc;
import qcfpunch.interfaces.IPostMapGenerationAddStuff;
import qcfpunch.map_generation.PostMapGenerationChange;
import qcfpunch.map_generation.PostMapGenerationManager;
import qcfpunch.rooms.MonsterRoomEmeraldElite;


public class ChallengerCoin extends OutOfCombatPotion implements IPostMapGenerationAddStuff {

	public static final String ID = QCFP_Misc.returnPrefix() + "Challenger_Coin";
	
	private static final PotionStrings potionStrings = CardCrawlGame.
			languagePack.getPotionString(ID);
	
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
	
	//Also, won't spawn in situations where FruitJuice wouldn't,
	//and only shows up once per shop at most
	//More details in code at qcfpunch.patches.
	public static final PotionRarity RARITY = PotionRarity.RARE;
	
	//Size apparently creates the recipient of the potion.
	//Since this potion is a coin, I will have to make my own files for that in the future.
	public static final PotionSize SIZE = PotionSize.SPHERE;
	public static final PotionColor COLOR = PotionColor.ATTACK;
	
	public static final String INFINITE_SPIRE_NIGHTMARE_ELITE_ROOM_SYMBOL = "NM";
	public static final String REPLAY_THE_SPIRE_TELEPORT_ROOM_SYMBOL = "PTL";
	
	public static final Logger logger = LogManager.getLogger(ChallengerCoin.class.getName());
	
	public static ArrayList<Integer> saved_save_slot;
	public static ArrayList<Integer> saved_act;
	public static ArrayList<Integer> saved_map_x_position, saved_map_y_position;
	public static ArrayList<String> saved_map_room;
	public static ArrayList<Integer> saved_post_map_gen_use_priority;
	
	//YES. I know this is a Mcguyverism and this part of the code (and stuff related) seriously needs
	// improvement. I will do that, one day. Maybe. Fingers crossed.
	//Also, if you are looking to this code thinking to use it as a base for your own changes in map,
	// this is NOT A GOOD CODE TO USE AS AN EXAMPLE. It works, but it could be better.
	// Thanks for your understanding.
	private int post_gen_priority;
	
	public ChallengerCoin() {
		super(NAME, ID, RARITY, SIZE, COLOR);		
		
		description = makeDescription();
		this.isThrown = false;
		this.tips.add(new PowerTip(this.name, this.description));
		
		avoidNullBugsCreatingArrayLists();
	}
	
	private static void avoidNullBugsCreatingArrayLists() {
		if (ChallengerCoin.saved_save_slot == null) ChallengerCoin.saved_save_slot = new ArrayList<Integer>();
		if (ChallengerCoin.saved_act == null) ChallengerCoin.saved_act = new ArrayList<Integer>();
		if (ChallengerCoin.saved_map_x_position == null) ChallengerCoin.saved_map_x_position = 
				new ArrayList<Integer>();
		if (ChallengerCoin.saved_map_y_position == null) ChallengerCoin.saved_map_y_position = 
				new ArrayList<Integer>();
		if (ChallengerCoin.saved_map_room == null) ChallengerCoin.saved_map_room =
				new ArrayList<String>();
		if (ChallengerCoin.saved_post_map_gen_use_priority == null) 
			ChallengerCoin.saved_post_map_gen_use_priority = new ArrayList<Integer>();
	}
	
	private String makeDescription() {

		if (AbstractDungeon.player != null){

			String description = DESCRIPTIONS[0];

			if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.replay_the_spire_class_code)) {
				description += DESCRIPTIONS[1];
			}

			description += DESCRIPTIONS[2];

			if (AbstractDungeon.player.hasRelic("SacredBark")){

				description += DESCRIPTIONS[3];
				description += DESCRIPTIONS[4];
				description += DESCRIPTIONS[5];

			} else {



				description += DESCRIPTIONS[4];
			}

			return description;

		}

		return "This is a bug.";

	}
	
	@Override
	public int getPotency(int ascensionLevel) {
		return 0;
	}

	@Override
	public boolean canUse() {
		if ((AbstractDungeon.actionManager.turnHasEnded) && 
				(AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT)) {
				      return false;
		}
		if ((AbstractDungeon.getCurrRoom().event != null) && 
				((AbstractDungeon.getCurrRoom().event instanceof WeMeetAgain))) {
				      return false;
		}
		
		return theresARoomToUseThePotion();
	}
	
	private boolean theresARoomToUseThePotion() {
		
		ArrayList<ArrayList<MapRoomNode>> dungeon_map = AbstractDungeon.map;
		
		MapRoomNode current_room = AbstractDungeon.currMapNode;
		ArrayList<MapEdge> edges = current_room.getEdges();
		
		for (int i = 0; i < edges.size(); i++) {
			int x, y;
			x = edges.get(i).dstX; y = edges.get(i).dstY;

			MapRoomNode room_to_check = dungeon_map.get(y).get(x);
			AbstractRoom room = room_to_check.getRoom();
			
			if (CheckIfPotionCanBeUsed(room)) return true;
		}
		
		return false;
		
	}
	
	public static boolean CheckIfPotionCanBeUsed(AbstractRoom room) {
		
		boolean evaluation = !(room instanceof MonsterRoom) && 
				!(room instanceof MonsterRoomElite) &&
				!(room instanceof MonsterRoomBoss);
		
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.replay_the_spire_class_code)) {
			evaluation &= !(room instanceof TeleportRoom);
		}
		
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.infinite_spire_class_code)) {
			evaluation &= !(room instanceof NightmareEliteRoom);
		}
		
		return evaluation;
		
	}
	
	@Override
	public void use(AbstractCreature arg0) {
		
		ArrayList<ArrayList<MapRoomNode>> dungeon_map = AbstractDungeon.map;
		
		MapRoomNode current_room = AbstractDungeon.currMapNode;
		ArrayList<MapEdge> edges = current_room.getEdges();
		
		for (int i = 0; i < edges.size(); i++) {
			int x = edges.get(i).dstX;	int y = edges.get(i).dstY;
			
			if (changeRoom(dungeon_map, x, y, null, true)) {
				saved_post_map_gen_use_priority.add(PostMapGenerationManager.getPriorityCounter());
				logger.info("Made new room at " + x + " " + y);
			}
		}
		
	}
	
	public static boolean changeRoom(ArrayList<ArrayList<MapRoomNode>> dungeon_map, int x, int y) {
		
		return changeRoom(dungeon_map, x, y, null, true);
		
	}
	
	public static boolean changeRoom(ArrayList<ArrayList<MapRoomNode>> dungeon_map, int x, int y, String which_room,
										boolean map_changes_arent_being_loaded) {
		
		MapRoomNode room_to_change = dungeon_map.get(y).get(x);
		AbstractRoom room = room_to_change.getRoom();
		
		if (CheckIfPotionCanBeUsed(room)) {
		
			AbstractRoom new_room;
			
			if (map_changes_arent_being_loaded) {
				saved_save_slot.add(CardCrawlGame.saveSlot);
				saved_act.add(AbstractDungeon.actNum);
				saved_map_x_position.add(x);
				saved_map_y_position.add(y);
			}
			
			
			if (((which_room == null) && (checkIfNewRoomNeedsToBeEmeraldElite(room_to_change))) ||
					(which_room == "EmeraldElite") ||
					AbstractDungeon.player.hasRelic("SacredBark"))
			{	
				new_room = new MonsterRoomEmeraldElite();
				room_to_change.hasEmeraldKey = true;
				if (map_changes_arent_being_loaded) saved_map_room.add("EmeraldElite");
			}
			else {
				new_room = new MonsterRoomElite();
				if (map_changes_arent_being_loaded) saved_map_room.add("Elite");
			}
			
			room_to_change.room = new_room;
			return true;

		
		} else {
			
			return false;
		}
		
	}
	
	public static boolean checkIfNewRoomNeedsToBeEmeraldElite(MapRoomNode room_to_change) {
		
		if (room_to_change.getRoom() instanceof TreasureRoom) {
			if ((AbstractDungeon.player.hasRelic("SacredBark")) ||
					((!Settings.hasEmeraldKey))) {

				return true;
			} else {
				return false;
			}
		} else if (AbstractDungeon.player.hasRelic("SacredBark")){
			return true;
		} else return false;
	}

	@Override
	public boolean canDoEffectAfterMapGeneration() {
		int position = saved_post_map_gen_use_priority.indexOf(post_gen_priority);
		
		if (position == -1) return false;
		else {
			
			if ((saved_save_slot.get(position) != CardCrawlGame.saveSlot) || 
					(saved_act.get(position) != AbstractDungeon.actNum)) return false;
			else return true;

		}
			
	}
	
	@Override
	public void doEffectAfterMapGeneration() {
		
		ArrayList<ArrayList<MapRoomNode>> dungeon_map = AbstractDungeon.map;
		
		int position = saved_post_map_gen_use_priority.indexOf(post_gen_priority);
		
		if (position != -1) {
			
			changeRoom(dungeon_map, saved_map_x_position.get(position),
						saved_map_y_position.get(position), saved_map_room.get(position), false);
			
		}
		
	}

	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null) {
        	String class_name = AbstractDungeon.player.getClass().getName();
        	
        	logger.info("Started saving Challenger Coin information from");
    		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
        	
        	if (saved_map_x_position != null) {
        		
        		int quant = saved_map_x_position.size();
        		
            	config.setInt("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
            			+ "Save_Slot_" + CardCrawlGame.saveSlot, quant);
            	
            	for (int i = 0; i < quant; i++) {
            		
            		saveChallengeCoinRoomMadeVariables(config, class_name, i);
            		
            	}
            	
        	}
        	
            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            logger.info("Finished saving Challenger Coin info from");
    		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
        }
        else {
        	clear(config);
        }

    }
	
	private static void saveChallengeCoinRoomMadeVariables(
			final SpireConfig config, String class_name, int position) {
		
		config.setInt("Challenge_Coin_Save_Slot_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, saved_save_slot.get(position));
		config.setInt("Challenge_Coin_Saved_Act_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, saved_act.get(position));
		config.setInt("Challenger_Coin_X_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, saved_map_x_position.get(position));
    	config.setInt("Challenger_Coin_Y_" + class_name + "_"
    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, saved_map_y_position.get(position));
    	config.setString("Challenger_Coin_Room_" + class_name + "_"
    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, saved_map_room.get(position));
    	config.setInt("Challenger_Coin_priority_" + class_name + "_"
    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position,
    			saved_post_map_gen_use_priority.get(position));
    	
	}
	
	public static void load(final SpireConfig config) {
		
		String class_name = AbstractDungeon.player.getClass().getName();
		
		logger.info("Loading Challenger Coin info from");
		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
		
		if (config.has("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
				+ "Save_Slot_" + CardCrawlGame.saveSlot)){
                     
			int quant = config.getInt("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
					+ "Save_Slot_" + CardCrawlGame.saveSlot);
			
			cleanArrayLists();
			
			avoidNullBugsCreatingArrayLists();
			
			for (int i = 0; i < quant; i++) {
				
				if (sanitizationOfPossibleSaveFileAbandonedChallengeCoinRoomMadeVariables(config, class_name, i)) {
					logger.info("Found act " + AbstractDungeon.actNum++ + " abandoned");
					logger.info("Challenger Coin data. Sanitized it.");
				}
				
				loadChallengeCoinRoomMadeVariables(config, class_name, i);
					
				PostMapGenerationChange post_map_gen_changer = 
						createPostMapGenerationChangeForRoomChange(config, class_name, i);
					
				logger.info("Adding post map generation change");
				PostMapGenerationManager.addPostMapGenerationChange(post_map_gen_changer);
			}
			
            try {
				config.load();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            logger.info("Finished loading Challenger Coin info from");
    		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");

			logger.info("Finished setting Challenger Coin variables.");
		}
		
		
    }
	
	public static void sanitizingActOne(final SpireConfig config) {
		String class_name = AbstractDungeon.player.getClass().getName();
		
		logger.info("Sanitizing Challenger Coin act 1 info from");
		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
		
		if (config != null) {
			if (config.has("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
					+ "Save_Slot_" + CardCrawlGame.saveSlot)){
	                     
				int quant = config.getInt("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
						+ "Save_Slot_" + CardCrawlGame.saveSlot);
							
				for (int i = 0; i < quant; i++) {
					
					if (sanitizationOfPossibleSaveFileAbandonedChallengeCoinRoomMadeVariableActOne
							(config, class_name, i)) {
						logger.info("Found act " + AbstractDungeon.actNum + " abandoned");
						logger.info("Challenger Coin data. Sanitized it.");
					}
				}
				
	            try {
					config.save();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            logger.info("Finished sanitizing Challenger Coin info from");
	    		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
	        }
		}
	}
	
	private static void cleanArrayLists() {
		if (ChallengerCoin.saved_save_slot != null) saved_save_slot.clear();
		if (ChallengerCoin.saved_act != null) saved_act.clear();
		if (ChallengerCoin.saved_map_x_position != null) saved_map_x_position.clear();
		if (ChallengerCoin.saved_map_y_position != null) saved_map_y_position.clear();
		if (ChallengerCoin.saved_map_room != null) saved_map_room.clear();
		if (ChallengerCoin.saved_post_map_gen_use_priority != null) saved_post_map_gen_use_priority.clear();
	}
	
	private static boolean sanitizationOfPossibleSaveFileAbandonedChallengeCoinRoomMadeVariables(
			final SpireConfig config, String class_name, int position) {
		
		int current_act = AbstractDungeon.actNum;
		//current_act++ increments current_act AFTER the value is passed to next_act.
		//++current_act does the job here.
		int next_act = ++current_act;
		
		if (config.getInt("Challenge_Coin_Saved_Act_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position) == next_act) {
			config.setInt("Challenge_Coin_Saved_Act_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, Integer.MAX_VALUE);
	    	return true;
		}
		return false;
		
	}
	
	private static boolean sanitizationOfPossibleSaveFileAbandonedChallengeCoinRoomMadeVariableActOne(
			final SpireConfig config, String class_name, int position) {
		
		int current_act = AbstractDungeon.actNum;
		
		if (config.getInt("Challenge_Coin_Saved_Act_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position) == current_act) {
			config.setInt("Challenge_Coin_Saved_Act_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position, Integer.MAX_VALUE);
	    	return true;
		}
		return false;
		
	}
	
	
	private static void loadChallengeCoinRoomMadeVariables(
			final SpireConfig config, String class_name, int position) {
		
		ChallengerCoin.saved_save_slot.add(
				config.getInt("Challenge_Coin_Save_Slot_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		ChallengerCoin.saved_act.add(
				config.getInt("Challenge_Coin_Saved_Act_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		ChallengerCoin.saved_map_x_position.add(
			config.getInt("Challenger_Coin_X_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		ChallengerCoin.saved_map_y_position.add(
			config.getInt("Challenger_Coin_Y_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		ChallengerCoin.saved_map_room.add(
			config.getString("Challenger_Coin_Room_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		ChallengerCoin.saved_post_map_gen_use_priority.add(
			config.getInt("Challenger_Coin_priority_" + class_name + "_"
					+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position));
		
	}
	
	private static PostMapGenerationChange createPostMapGenerationChangeForRoomChange(
			final SpireConfig config, String class_name, int position) {
		
		PostMapGenerationChange post_map_gen_changer = new PostMapGenerationChange();
		
		ChallengerCoin object = new ChallengerCoin();
		object.post_gen_priority = config.getInt("Challenger_Coin_priority_" + class_name + "_"
				+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + position);
		
		post_map_gen_changer.post_map_gen_changer_object =
				(IPostMapGenerationAddStuff) object;	
		
		post_map_gen_changer.counter = ChallengerCoin.saved_post_map_gen_use_priority.get(position);
		
		return post_map_gen_changer;
		
	}
		
	public static void clear(final SpireConfig config) {
		String class_name = AbstractDungeon.player.getClass().getName();
		
		logger.info("Clearing Challenger Coin variables from ");
		logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
		
		if (config.has("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
				+ "Save_Slot_" + CardCrawlGame.saveSlot)) {
			
			int count = config.getInt("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
					+ "Save_Slot_" + CardCrawlGame.saveSlot);
			
			config.remove("Challenger_Coin_Number_Of_Rooms_Made_" + class_name
					+ "Save_Slot_" + CardCrawlGame.saveSlot);
			
			for (int i = 0; i < count; i++) {
				config.remove("Challenge_Coin_Save_Slot_" + class_name + "_"
						+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
				config.remove("Challenge_Coin_Saved_Act_" + class_name + "_"
						+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
				config.remove("Challenger_Coin_X_" + class_name + "_"
						+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
		    	config.remove("Challenger_Coin_Y_" + class_name + "_"
		    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
		    	config.remove("Challenger_Coin_Room_" + class_name + "_"
		    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
		    	config.remove("Challenger_Coin_priority_" + class_name + "_"
		    			+ "Save_Slot_" + CardCrawlGame.saveSlot + "_" + i);
			}
			
			cleanArrayLists();
			
			avoidNullBugsCreatingArrayLists();
			
		}		
		
        logger.info("Finished clearing Challenger Coin variables from ");
        logger.info("character " + class_name + ", save slot " + CardCrawlGame.saveSlot);
	}

	@Override
	public AbstractPotion makeCopy() {

		return new ChallengerCoin();
	}

}
