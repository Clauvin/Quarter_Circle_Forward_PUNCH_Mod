package qcfpunch.relics.character_cameos.dan;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.WeakestFightingStylePower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class NotStrongestFightingStyleGuidebook extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Not_Strongest_Fighting_Style_Guidebook";
	public static final String on_equip_sound_file_name = "Strongest - se_mid_00033.wav";
	public static Sfx strongest_sound;
	
	public NotStrongestFightingStyleGuidebook() {
		super(ID, GraphicResources.LoadRelicImage("Strongest Style Manual - Based on Oni - Rights Reserved.png"),
				GraphicResources.LoadOutlineImage("Strongest Style Manual Outline.png"),
				RelicTier.COMMON, LandingSound.FLAT);
		strongest_sound = new Sfx("qcfpunch/resources/sounds/" + on_equip_sound_file_name, false);
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
		strongest_sound.play(Settings.MASTER_VOLUME * Settings.SOUND_VOLUME, 1, 0);
	}
	
	@Override
	public void onUnequip() {
		BaseMod.MAX_HAND_SIZE--;
	}
	
	public AbstractRelic makeCopy() { 
		return new NotStrongestFightingStyleGuidebook();
	}
}
