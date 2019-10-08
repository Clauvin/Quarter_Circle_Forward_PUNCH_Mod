package qcfpunch.relics.guile;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.relics.OnLoseBlockRelic;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class ArmyBoots extends CustomRelic implements OnLoseBlockRelic  {
	public static final String ID = QCFP_Misc.returnPrefix() + "Army_Boots";
	
	private static ArrayList<String> powers_affected_by_relic;
	private static boolean relic_effect_activated_this_combat = false;
	
	private final static int AMOUNT_REMOVED_OF_EACH_DEBUFF = 1;
	
	public ArmyBoots() {
		super(ID, GraphicResources.LoadRelicImage("Temp Army Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.COMMON, LandingSound.SOLID);
		
		InitializeListOfRemovableDebuffs();
	}
	
	private void InitializeListOfRemovableDebuffs() {
		powers_affected_by_relic = new ArrayList<String>();
		powers_affected_by_relic.add("Frail");
		powers_affected_by_relic.add("Vulnerable");
		powers_affected_by_relic.add("Weakened");
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT_REMOVED_OF_EACH_DEBUFF + DESCRIPTIONS[1];
	}
	
	@Override
	public void atPreBattle() {
		relic_effect_activated_this_combat = false;
	}
	
	@Override
	public int onLoseBlock(DamageInfo info, int damage_amount) {
		
		if (RelicShouldWorkNow(info, damage_amount)) {
			
			RemoveDebuffsAndCallVisualEffects();
			
		}
		
		return damage_amount;
	}
	
	private boolean RelicShouldWorkNow(DamageInfo info, int damage_amount) {
		boolean relic_havent_been_activated_in_this_combat = !relic_effect_activated_this_combat;
		boolean damage_received_is_normal = info.type == DamageType.NORMAL;
		boolean block_is_broken = damage_amount >= AbstractDungeon.player.currentBlock;
		
		return relic_havent_been_activated_in_this_combat &&
				damage_received_is_normal &&
				block_is_broken;
	}
	
	private void RemoveDebuffsAndCallVisualEffects() {
		
		AbstractPlayer player = AbstractDungeon.player;
		boolean showed_relic_image = false;
		
		for (String power_to_remove: powers_affected_by_relic){
			
			if (player.hasPower(power_to_remove)) {
				
				relic_effect_activated_this_combat = true;
				
				if (IsTimeToCallRelicVisualEffects(showed_relic_image)) {
					RelicVisualEffects();
					showed_relic_image = true;
				}

				AbstractDungeon.actionManager.addToBottom(
						new ReducePowerAction(player, player, power_to_remove, AMOUNT_REMOVED_OF_EACH_DEBUFF));
				
			}
			
		}
	}
	
	private boolean IsTimeToCallRelicVisualEffects(boolean showed_relic_image) {
		boolean relic_image_havent_show_up_yet = !showed_relic_image;
		
		return relic_image_havent_show_up_yet;
	}
	
	private void RelicVisualEffects() {
		flash();
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
	}
	
	public AbstractRelic makeCopy() {
		return new ArmyBoots();
	}

}
