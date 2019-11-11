package qcfpunch.relics.bosses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.relics.ken.UnceasingFlame;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class KillianEngineAlpha extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() +
			"Killian_Engine_Alpha";
	
	public static final int AMOUNT_OF_COMBAT_REWARDS = 6;
	public static final float CHANCE_OF_UPGRADED_CARDS = 0.1f;
	
	public static boolean is_it_time_to_add_a_card_reward = false;
	
	public static String current_description;
	
	public KillianEngineAlpha() {
		super(ID, GraphicResources.LoadRelicImage(
				"White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		if (current_description == null) return getCommonDescription();
		else return current_description;
	}
	
	public String getCommonDescription() {
		return DESCRIPTIONS[0];
	}

	public String getEmptyRelicDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onEquip() {
		
		//choose character
		CardColor chosen_color = CardColor.RED;
		
		//store info
		
		CardGroup rare_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		CardGroup uncommon_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		CardGroup common_extra_pool = new CardGroup(CardGroupType.CARD_POOL);
		
		makeCardGroupOfAColor(chosen_color, rare_extra_pool,
				uncommon_extra_pool, common_extra_pool);
		
		for (int i = 0; i < rare_extra_pool.size(); i++) {
			
			AbstractDungeon.rareCardPool.addToBottom(
					rare_extra_pool.getNCardFromTop(i));
			
		}
		
		for (int i = 0; i < uncommon_extra_pool.size(); i++) {
			
			AbstractDungeon.uncommonCardPool.addToBottom(
					uncommon_extra_pool.getNCardFromTop(i));
			
		}
		
		for (int i = 0; i < common_extra_pool.size(); i++) {
			
			AbstractDungeon.commonCardPool.addToBottom(
					common_extra_pool.getNCardFromTop(i));
			
		}
		
		
		
	}
	
	@SuppressWarnings("incomplete-switch")
	public void makeCardGroupOfAColor(
			CardColor card_color,
			CardGroup rare_extra_pool,
			CardGroup uncommon_extra_pool,
			CardGroup common_extra_pool) {

		for (Map.Entry<String, AbstractCard> a_card : CardLibrary.cards.entrySet()) {
			AbstractCard one_card = a_card.getValue();
			
			if (QCFP_Misc.cardIsOfChosenColor(one_card, card_color)) {		
				
				switch (one_card.rarity) {
					case RARE:
						rare_extra_pool.addToBottom((AbstractCard)one_card);
						break;
				    case UNCOMMON: 
				    	uncommon_extra_pool.addToBottom((AbstractCard)one_card);
				    	break;
				    case COMMON:
				    	common_extra_pool.addToBottom((AbstractCard)one_card);
				    	break;
				}
					
			}
		}		
		
	}
	
	public static void save(final SpireConfig config) {

    }
	
	public static void load(final SpireConfig config) {
		
    }
	
	public static void clear(final SpireConfig config) {

	}

}
