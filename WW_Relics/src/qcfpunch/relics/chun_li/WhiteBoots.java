package qcfpunch.relics.chun_li;

import java.io.IOException;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import org.apache.logging.log4j.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.*;

public class WhiteBoots extends CustomRelic {
	public static final String ID = QCFP_Misc.returnPrefix() + "White_Boots";
	private static final int CONSTANT_DAMAGE = 4;
	private static final int CARDS_DREW_FOR_NORMAL_ATTACKS = 3;
	
	private static int number_of_attacks_drew;
	
	public static final Logger logger = LogManager.getLogger(WhiteBoots.class.getName());
	
	public WhiteBoots() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
			RelicTier.UNCOMMON, LandingSound.SOLID);
		
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + CARDS_DREW_FOR_NORMAL_ATTACKS +
				DESCRIPTIONS[1] + CONSTANT_DAMAGE +
				DESCRIPTIONS[2];
	}
	
	@Override
	public void onEquip() {
		counter = number_of_attacks_drew;
	}
	
	@Override
	public void onCardDraw(AbstractCard c) {

		if (isAnAttackCard(c)) {
			addOneToNumberOfAttacksDrew();
			if (isTimeToDoAttackCardDamage()) {
				doDamageToTargets(c);
			}
		}

		setNumberOfAttacksDrew();
		setCounter();
		QCFP_Misc.fastLoggerLine(counter);
	}

	private boolean isAnAttackCard(AbstractCard c) {
		return c.type == CardType.ATTACK;
	}

	private void addOneToNumberOfAttacksDrew() {
		number_of_attacks_drew++;
	}	
	
	private boolean isTimeToDoAttackCardDamage() {
		return (AbstractDungeon.getCurrRoom().monsters.areMonstersDead() == false) &&
				(number_of_attacks_drew >= CARDS_DREW_FOR_NORMAL_ATTACKS); 
	}

	private void doDamageToTargets(AbstractCard card) {
		int total_damage = 0;
		
		total_damage += CONSTANT_DAMAGE;
		
		for (int i = 0; i < total_damage; i++) {
			flash();
			AbstractDungeon.actionManager.addToBottom(
					new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(1, true),
							DamageType.HP_LOSS, AttackEffect.BLUNT_LIGHT));
		}
	}
	
	private void setNumberOfAttacksDrew() {
		number_of_attacks_drew %= CARDS_DREW_FOR_NORMAL_ATTACKS;
	}
	
	private void setCounter() {
		counter = number_of_attacks_drew;
	}
	
	@Override
	public void atBattleStartPreDraw() {

	}
	
	private boolean enemyAttackedCounts(DamageInfo info) {
		return info.type == DamageType.NORMAL;
	}

	public boolean canSpawn()
	{
		return AbstractDungeon.player.masterDeck.getAttacks().size() > 0;
	}
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
        	String class_name = AbstractDungeon.player.getClass().getName();
        	
        	
    		logger.info("Started saving White Boots information from");
    		logger.info(QCFP_Misc.classAndSaveSlotText());
    		
            config.setInt("White_Boots_number_of_draws_class_" + class_name +
            				"_save_slot_" + CardCrawlGame.saveSlot, 
            				WhiteBoots.number_of_attacks_drew);
            
            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            logger.info("Finished saving White Boots info from");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
        else {

        }

    }
	
	public static void load(final SpireConfig config) {
		
		String class_name = AbstractDungeon.player.getClass().getName();
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has("White_Boots_number_of_draws_class_" + class_name +
        				"_save_slot_" + CardCrawlGame.saveSlot)){
			
			logger.info("Loading White Boots info from");
			logger.info(QCFP_Misc.classAndSaveSlotText());
			
            WhiteBoots.number_of_attacks_drew = 
            		config.getInt("White_Boots_number_of_draws_class_" + class_name +
            				"_save_slot_" + CardCrawlGame.saveSlot);
            
            try {
				config.load();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info("Finished loading White Boots from");
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");
			number_of_attacks_drew = 0;
			logger.info("Finished setting White Boots variables.");
		}
		
		
    }
		
	public static void clear(final SpireConfig config) {
		String class_name = AbstractDungeon.player.getClass().getName();
		
		logger.info("Clearing White Boots variables from");
		logger.info(QCFP_Misc.classAndSaveSlotText()); 
		
		config.remove("White_Boots_number_of_draws_class_" + class_name +
				"_save_slot_" + CardCrawlGame.saveSlot);
		
		
        logger.info("Finished clearing White Boots variables from");
        logger.info(QCFP_Misc.classAndSaveSlotText());  
	}

	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new WhiteBoots();
	}
}