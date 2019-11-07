package qcfpunch.relics.chun_li;

import java.io.IOException;

import org.apache.logging.log4j.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class WhiteBoots extends CustomRelic {
	public static final String ID = QCFP_Misc.returnPrefix() + "White_Boots";
	private static final int CONSTANT_DAMAGE = 1;
	private static final int DAMAGE_FOR_EACH_UPGRADE = 1;
	private static final int CARDS_DREW_FOR_MULTIPLIER = 3;
	private static final int SIZE_OF_MULTIPLIER = 2;
	
	private static int number_of_attacks_drew;
	
	private static AbstractCreature single_enemy_attacked;
	
	public static final Logger logger = LogManager.getLogger(WhiteBoots.class.getName());
	
	public WhiteBoots() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
			RelicTier.UNCOMMON, LandingSound.SOLID);
		
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + CONSTANT_DAMAGE + DESCRIPTIONS[1] + DESCRIPTIONS[2] +
				CONSTANT_DAMAGE * 2 + DESCRIPTIONS[3];
	}
	
	@Override
	public void onEquip() {
		counter = number_of_attacks_drew;
	}
	
	@Override
	public void onCardDraw(AbstractCard c) {
		
		if (isAnAttackCard(c)) {
			addOneToNumberOfAttacksDrew();
			if (isTimeToDoDamage()) {
				doDamageToTarget(c, single_enemy_attacked);
			}
		}
		
	}
	
	public boolean isAnAttackCard(AbstractCard c) {
		return c.type == CardType.ATTACK;
	}
	
	public void addOneToNumberOfAttacksDrew() {
		number_of_attacks_drew++;
		counter = number_of_attacks_drew % CARDS_DREW_FOR_MULTIPLIER;
	}
	
	public boolean isTimeToDoDamage() {
		return single_enemy_attacked != null;
	}
	
	public void doDamageToTarget(AbstractCard card, AbstractCreature creature) {
		int total_damage = 0;
		int number_of_upgrades = card.timesUpgraded;
		boolean is_third_card = isThirdCard();
		
		total_damage += CONSTANT_DAMAGE;
		total_damage += DAMAGE_FOR_EACH_UPGRADE * number_of_upgrades;
		if (is_third_card) total_damage *= SIZE_OF_MULTIPLIER;
		
		DamageInfo damage_info = new DamageInfo(AbstractDungeon.player, total_damage, DamageInfo.DamageType.HP_LOSS);
		flash();
		AbstractDungeon.actionManager.addToBottom(
				new PummelDamageAction(creature, damage_info));
	}
	
	public boolean isThirdCard() {
		return number_of_attacks_drew % CARDS_DREW_FOR_MULTIPLIER == 0;
	}
	
	@Override
	public void atBattleStartPreDraw() {
		
		single_enemy_attacked = null;
		
	}
	
	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		
		if (enemyAttackedCounts(info)) {
			single_enemy_attacked = target;
		}
		
	}
	
	public boolean enemyAttackedCounts(DamageInfo info) {
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