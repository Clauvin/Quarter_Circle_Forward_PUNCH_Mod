package qcfpunch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.logging.log4j.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.screens.custom.CustomMod;

import basemod.BaseMod;
//import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import qcfpunch.QCFP_Misc;
import qcfpunch.events.act2.FightingNoisesEvent;
import qcfpunch.modifiers.*;
import qcfpunch.monsters.elites.TiredGremlinNob;
import qcfpunch.potions.ChallengerCoin;

import qcfpunch.relics.chun_li.*;
import qcfpunch.relics.dhalsim.BrokenTusk;
import qcfpunch.relics.dhalsim.LotusStatue;
import qcfpunch.relics.dhalsim.NecklaceOfSkulls;
import qcfpunch.relics.guile.*;
import qcfpunch.relics.ken.*;
import qcfpunch.relics.ryu.*;
import qcfpunch.relics.seth.KillianEngineAlpha;
import qcfpunch.relics.zangief.*;
import qcfpunch.relics.cammy.*;
import qcfpunch.relics.character_cameos.dan.NotStrongestFightingStyleGuidebook;
import qcfpunch.relics.character_cameos.sakura.SchoolBackpack;
import qcfpunch.relics.character_cameos.valerie.MixedPaintBucket;
import qcfpunch.relics.character_cameos.valerie.PrimaryBrush;
import qcfpunch.relics.character_cameos.valerie.PrimaryBrushNoArtistSaveFix;
import qcfpunch.relics.character_cameos.valerie.RainbowBrush;
import qcfpunch.relics.mortal_kombat.*;
import qcfpunch.relics.no_relation.Cattail;

@SpireInitializer
public class QCFPunch_Mod implements AddCustomModeModsSubscriber, EditStringsSubscriber, EditRelicsSubscriber,
			EditCardsSubscriber, 
			EditKeywordsSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, 
			PostCreateStartingRelicsSubscriber, StartGameSubscriber
	{

	public static final Logger logger = LogManager.getLogger(QCFPunch_Mod.class.getName()); // lets us log output

	public static final String MODNAME = QCFP_Misc.returnModName(); // mod name
	public static final String MODID = QCFP_Misc.returnPrefix();
	public static final String AUTHOR = "Levender"; // your name
	public static final String DESCRIPTION = QCFP_Misc.returnDescription();
	
	public static final String INITIAL_LANGUAGE = "eng";
	
	public QCFPunch_Mod() {
		BaseMod.subscribe(this);
	}

	public static void initialize()	{
	    new QCFPunch_Mod();
	}
	
	public String getJsonText(String filepath) {
		return Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8));
	}
	
	@Override
	public void receiveEditKeywords() {
		
		logger.info("begin editing keywords");
		
        String language = Settings.language.toString().toLowerCase();
        if (!language.equals(INITIAL_LANGUAGE)) {
            try {
                logger.info("inserting " + language + " keywords.");
                insertKeywords(language);
                logger.info("finished inserting " + language + " keywords.");
            } catch (GdxRuntimeException e) {
                logger.info(language + " keywords not found.");
                
                logger.info("inserting eng keywords.");
                insertKeywords(INITIAL_LANGUAGE);
        		logger.info("finished inserting " + language + " keywords.");
            }
        }
        else {
        	logger.info("inserting eng keywords.");
            insertKeywords(INITIAL_LANGUAGE);
    		logger.info("finished inserting " + language + " keywords.");
        }
		
		logger.info("done editing keywords");
		
	}
	
	private void insertKeywords(String language) {
						
		Gson gson = new Gson();
		String keywordStringsAddress = 
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Keywords.json");
		String json = getJsonText(keywordStringsAddress); 
		KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);
	    
		if (keywords != null) {
            for (KeywordWithProper keyword : keywords) {
            	
            	BaseMod.addKeyword(MODID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            
            }
        }
		
	}
	
	@Override
	public void receiveEditStrings()
	{
	    logger.info("begin editing strings");

	    String language = Settings.language.toString().toLowerCase();

	    if (!language.equals(INITIAL_LANGUAGE)) {
            try {
            	LoadMostJSONs(language);
            	
            } catch (GdxRuntimeException e) {
                logger.info(language + " strings not found.");
                language = INITIAL_LANGUAGE;
                
                LoadMostJSONs(language);
            }
	    } else {
	    	language = INITIAL_LANGUAGE;
            
            LoadMostJSONs(language);
	    }
	    
	    logger.info("done editing strings");
	}
	
	private void LoadMostJSONs(String language) {
		
		logger.info("begin editing " + language + " strings");
    	
    	LoadRelicsJSON(language);
	    LoadPowersJSON(language);
	    LoadModifiersJSON(language);
	    LoadCardsJSON(language);
	    LoadPotionsJSON(language);
	    LoadEventsJSON(language);
	    LoadMonstersJSON(language);
	    LoadUIJSON(language);
	    
	    logger.info("finished editing " + language + " strings");
		
	}
	
	private void LoadRelicsJSON(String language) {
		String relicStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Relics.json");
	    String relicStrings = getJsonText(relicStringsAddress);
	    
	    BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
	}
	
	private void LoadPowersJSON(String language) {
		String powerStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Powers.json");
	    String powerStrings = getJsonText(powerStringsAddress);
	    
	    BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
	}
	
	private void LoadModifiersJSON(String language) {
		String modifiersStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Modifiers.json");
	    String modifiersStrings = getJsonText(modifiersStringsAddress);
	    
	    BaseMod.loadCustomStrings(RunModStrings.class, modifiersStrings);
	}
	
	private void LoadCardsJSON(String language) {
		String cardsStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Cards.json");
	    String cardsStrings = getJsonText(cardsStringsAddress);
	    
	    BaseMod.loadCustomStrings(CardStrings.class, cardsStrings);
	}
	
	private void LoadPotionsJSON(String language) {
		String potionsStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Potions.json");
		String potionsStrings = getJsonText(potionsStringsAddress);
		
		BaseMod.loadCustomStrings(PotionStrings.class, potionsStrings);
	}
	
	private void LoadEventsJSON(String language) {
		String eventsStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Events.json");
		String eventsStrings = getJsonText(eventsStringsAddress);
		
		BaseMod.loadCustomStrings(EventStrings.class, eventsStrings);
	}
	
	private void LoadMonstersJSON(String language) {
		String monstersStringsAddress =
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_Monsters.json");
		String monstersStrings = getJsonText(monstersStringsAddress);
		
		BaseMod.loadCustomStrings(MonsterStrings.class, monstersStrings);
	}
	
	private void LoadUIJSON(String language) {
		String uiStringsAddress = 
				QCFP_Misc.returnSpecificLocalizationFile(
						language + "/WW_Relics_UI.json");
		String uiStrings = getJsonText(uiStringsAddress);
		
		BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
	}
	
	@Override
	public void receiveEditRelics() {
		logger.info("Begin adding relics");
		addRyuRelics();
		addKenRelics();
		addChunLiRelics();
		addGuileRelics();
		addCammyRelics();
		addZangiefRelics();
		addDhalsimRelics();
		addCharacterCameoRelics();
		addGameCameoRelics();
		addNoRelationRelics();
		addBossRelics();
		logger.info("Done adding relics");
	}

	
	private void addRyuRelics() {
		BaseMod.addRelic(new DuffelBag(), RelicType.SHARED);
		BaseMod.addRelic(new FightingGloves(), RelicType.SHARED);
		BaseMod.addRelic(new RedHeadband(), RelicType.SHARED);
	}
	
	private void addKenRelics() {
		BaseMod.addRelic(new BlackTrainingShirt(), RelicType.SHARED);
		BaseMod.addRelic(new RedGi(), RelicType.SHARED);
		BaseMod.addRelic(new UnceasingFlame(), RelicType.SHARED);
	}
	
	private void addChunLiRelics() {
		BaseMod.addRelic(new Handcuffs(), RelicType.SHARED);
		BaseMod.addRelic(new SpikyBracers(), RelicType.SHARED);
		BaseMod.addRelic(new WhiteBoots(), RelicType.SHARED);
	}
	
	private void addGuileRelics() {
		BaseMod.addRelic(new ArmyBoots(), RelicType.SHARED);
		BaseMod.addRelic(new ChainWithNametags(), RelicType.SHARED);
		BaseMod.addRelic(new CombatFatigues(), RelicType.SHARED);
	}
	
	private void addCammyRelics() {
		BaseMod.addRelic(new RedBeret(), RelicType.SHARED);
		BaseMod.addRelic(new GreenLeotard(), RelicType.SHARED);
		BaseMod.addRelic(new SpecialOpsInsignia(), RelicType.GREEN);
	}
	
	private void addZangiefRelics() {
		BaseMod.addRelic(new WrestlersCloak(), RelicType.SHARED);
		BaseMod.addRelic(new WildHerbsOintment(), RelicType.SHARED);
		BaseMod.addRelic(new RedCycloneTeachings(), RelicType.SHARED);
	}
	
	private void addDhalsimRelics() {
		BaseMod.addRelic(new BrokenTusk(), RelicType.SHARED);
		BaseMod.addRelic(new NecklaceOfSkulls(),  RelicType.SHARED);
		BaseMod.addRelic(new LotusStatue(), RelicType.SHARED);
	}
	
	private void addCharacterCameoRelics() {
		addDanRelics();
		addSakuraRelics();
		addValerieRelics();
	}
	
	private void addDanRelics() {
		BaseMod.addRelic(new NotStrongestFightingStyleGuidebook(), RelicType.SHARED);
	}
	
	private void addSakuraRelics() {
		BaseMod.addRelic(new SchoolBackpack(), RelicType.SHARED);
	}
	
	private void addValerieRelics() {
		if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.the_artist_class_code)) {
			BaseMod.addRelic(new MixedPaintBucket(), RelicType.SHARED);
			BaseMod.addRelic(new PrimaryBrush(), RelicType.SHARED);
		}
		BaseMod.addRelic(new RainbowBrush(), RelicType.SHARED);
	}
	
	private void addGameCameoRelics() {
		addMortalKombatRelics();
	}
	
	private void addMortalKombatRelics() {
		BaseMod.addRelic(new NeverendingBlood(), RelicType.SHARED);
		BaseMod.addRelic(new ExtraSkeleton(), RelicType.SHARED);
	}
	
	private void addNoRelationRelics() {
		BaseMod.addRelic(new Cattail(), RelicType.SHARED);
	}
	
	private void addBossRelics() {
		BaseMod.addRelic(new DarkGi(), RelicType.SHARED);
		BaseMod.addRelic(new KillianEngineAlpha(), RelicType.SHARED);
	}
	
	@Override
	public void receiveEditCards() {
		//logger.info("Begin adding cards");
		
		//logger.info("Done adding cards");
	}
	
	public void addMonsters() {
		BaseMod.addMonster(TiredGremlinNob.ID, () -> new TiredGremlinNob(0.0f, 0.0f));
	}
	
	@Override
	public void receiveCustomModeMods(List<CustomMod> list) {
		RelicSetModifiers.addRelicSetModifiers(list);
		EasierRunModifiers.AddEasierSetModifiers(list);
		HarderRunModifiers.AddHarderSetModifiers(list);
	 }
	 
	@Override
	public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
		if (isCustomModActive(RelicSetModifiers.WANDERING_WARRIOR_ID)) {
			RelicSetModifiers.addWanderingWarriorRelicsToCustomRun(relics);
        }	
		
		if (isCustomModActive(RelicSetModifiers.BLUE_JADE_ID)) {
			RelicSetModifiers.addBlueJadeRelicsToCustomRun(relics);
        }
		
		if (isCustomModActive(RelicSetModifiers.INDESTRUCTIBLE_FORTRESS_ID)) {
			RelicSetModifiers.addIndestructibleFortressToCustomRun(relics);
		}
		
		if (isCustomModActive(RelicSetModifiers.BLAZING_FIST_ID)) {
			RelicSetModifiers.addBlazingFistToCustomRun(relics);
		}
		
		if (isCustomModActive(RelicSetModifiers.RED_CYCLONE_ID)) {
			RelicSetModifiers.addRedCycloneToCustomRun(relics);
		}
		
		if (isCustomModActive(RelicSetModifiers.ASCETIC_MONK_ID)) {
			RelicSetModifiers.addAsceticMonkToCustomRun(relics);
		}
		
		if (isCustomModActive(RelicSetModifiers.DELTA_SPIKE_ID)) {
			RelicSetModifiers.addDeltaSpikeToCustomRun(relics);
		}
		
		if (isCustomModActive(RelicSetModifiers.UNBOUNDED_CREATIVITY_ID)) {
			RelicSetModifiers.addUnboundedCreativityToCustomRun(relics);
		}
	}
	
	
    @Override
    public void receivePostDungeonInitialize() {     
        if (isCustomModActive(EasierRunModifiers.LOWERING_THE_HANDICAP_ID)) {
        	EasierRunModifiers.AddLoweringTheHandicaptHealthBarEffectsToRun();
        }

		if (isCustomModActive(HarderRunModifiers.FRESH_START_ID)) {
			HarderRunModifiers.AddFreshStartEffectsToRun();
		}
		
		if (isCustomModActive(HarderRunModifiers.HALF_HEALTH_BAR_ID)) {
			HarderRunModifiers.AddHaltHealthBarEffectsToRun();
		}
		
		if (isCustomModActive(HarderRunModifiers.QUARTER_HEALTH_BAR_ID)) {
			HarderRunModifiers.AddQuarterHealthBarEffectsToRun();
		}
    }
    
	public static void loadRunData() {
        logger.info("Loading " + QCFP_Misc.returnModName()+ " data from");
    	logger.info(QCFP_Misc.classAndSaveSlotText());
        try {
            final SpireConfig config = new SpireConfig(QCFP_Misc.returnModName(), "SaveData");
            WhiteBoots.load(config);
            DuffelBag.load(config);
            FightingGloves.load(config);
            UnceasingFlame.load(config);
            SchoolBackpack.load(config);
            RainbowBrush.load(config);
            if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.the_artist_class_code)) {
            	PrimaryBrush.load(config);
            }
            Cattail.load(config);
            if (shouldSanitizeActOne()) {
            	ChallengerCoin.sanitizingActOne(config);
            }
            ChallengerCoin.load(config);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Done loading " + QCFP_Misc.returnModName() + " data");
	}
	
	public static boolean shouldSanitizeActOne() {
		return ((AbstractDungeon.actNum == 1) && (AbstractDungeon.floorNum < 2));
	}
	
    public static void saveRunData() {
        logger.info("Saving " + QCFP_Misc.returnModName() + " data from");
    	logger.info(QCFP_Misc.classAndSaveSlotText());
        try {
        	final SpireConfig config = new SpireConfig(QCFP_Misc.returnModName(), "SaveData");
            WhiteBoots.save(config);
            DuffelBag.save(config);
            FightingGloves.save(config);
            UnceasingFlame.save(config);
            SchoolBackpack.save(config);
            Cattail.save(config);
            RainbowBrush.save(config);
            if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.the_artist_class_code)) {
            	PrimaryBrush.save(config);
            }
            ChallengerCoin.save(config);
            
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        logger.info("Done saving " + QCFP_Misc.returnModName() + " data");
    }
    
    public static void clearRunData() {
    	logger.info("Clearing Saved " + QCFP_Misc.returnModName() + " data from");
    	logger.info(QCFP_Misc.classAndSaveSlotText());
        try {
        	final SpireConfig config = new SpireConfig(QCFP_Misc.returnModName(), "SaveData");
            WhiteBoots.clear(config);
            DuffelBag.clear(config);
            FightingGloves.clear(config);
            UnceasingFlame.clear(config);
            SchoolBackpack.clear(config);
            Cattail.clear(config);
            RainbowBrush.clear(config);
            if (QCFP_Misc.silentlyCheckForMod(QCFP_Misc.the_artist_class_code)) {
            	PrimaryBrush.clear(config);
            } else {
            	PrimaryBrushNoArtistSaveFix.clear(config);
            }
            
            ChallengerCoin.clear(config);
        	config.save();

        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        logger.info("Done clearing saved " + QCFP_Misc.returnModName() + " data");
    }
    
    public void receiveStartGame() {
    	loadRunData();
    }

    public static boolean isCustomModActive(String ID) {
        return CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ID);
    }
    
	@Override
	public void receivePostInitialize() {

		addPotions();
		addMonsters();
		addEvents();
		
		String modBadgeAddress = "qcfpunch/resources/ModBadge.png";
		Texture badgeTexture = new Texture(Gdx.files.internal(modBadgeAddress));
		//uncomment line below and replaces settingsPanel to the null when
		//the mod panel has something to show
        //ModPanel settingsPanel = new ModPanel();
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, null);
	}
	
	public void addPotions() {
		
		logger.info("Begin adding potions");
		BaseMod.addPotion(ChallengerCoin.class, Color.LIME.cpy(), Color.PINK.cpy(), Color.BLUE.cpy(),
				ChallengerCoin.ID);
		logger.info("Done adding potions");
	}
	
	public void addEvents() {
		
		BaseMod.addEvent(FightingNoisesEvent.ID, FightingNoisesEvent.class,
				FightingNoisesEvent.TO_WHICH_ACT_ADD);
		
	}
}