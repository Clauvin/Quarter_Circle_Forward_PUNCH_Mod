package qcfpunch.relics.character_cameos.dan;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.WeakestFightingStylePower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class NotStrongestFightingStyleGuidebook extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Not_Strongest_Fighting_Style_Guidebook";
	
	public NotStrongestFightingStyleGuidebook() {
		super(ID, GraphicResources.LoadRelicImage("Temp Strongest Style Manual - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.COMMON, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	public void atBattleStartPreDraw() {
		
		AbstractPlayer player = AbstractDungeon.player;

		AbstractDungeon.actionManager.addToBottom(
				new RelicAboveCreatureAction(player, this));
		
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(player, player, new WeakestFightingStylePower(player, 0)));

	}
	
	@Override
	public void onEquip() {
		BaseMod.MAX_HAND_SIZE++;
	}
	
	@Override
	public void onUnequip() {
		BaseMod.MAX_HAND_SIZE--;
	}
	
	public AbstractRelic makeCopy() { 
		return new NotStrongestFightingStyleGuidebook();
	}
}
