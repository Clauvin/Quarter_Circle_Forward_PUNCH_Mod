package qcfpunch.relics.akuma;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class DarkGi extends CustomRelic {
	
	public static final String ID = QCFP_Misc.returnPrefix() + "Dark_Gi";

	public static final String on_equip_sound_file_name = "Dark Gi - se_mid_00011.wav";
	public static Sfx dark_gi_sound;
	public static final float PERCENTAGE_OF_MAX_HP_TO_LOSE = 0.35f;
	public static final int AMOUNT_TO_DRAW_WHEN_A_CARD_IS_PLAYED = 1;
	public DarkGi() {
		super(ID, GraphicResources.LoadRelicImage("Dark Gi - kimono - Delapouite - CC BY 3.0.png"),
				RelicTier.BOSS, LandingSound.FLAT);
		dark_gi_sound = new Sfx("qcfpunch/resources/sounds/" + on_equip_sound_file_name, false);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onEquip() {
		int max_hp_to_lose = (int) Math.floor(AbstractDungeon.player.maxHealth * 
				PERCENTAGE_OF_MAX_HP_TO_LOSE);
		
		AbstractDungeon.player.decreaseMaxHealth(max_hp_to_lose);
		if (Math.random() < 0.25) {
			dark_gi_sound.play(Settings.MASTER_VOLUME * Settings.SOUND_VOLUME, 1, 0);
		}
		else {
			CardCrawlGame.sound.play("CARD_EXHAUST", -0.3f);
		}
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		boolean x_cost_card_spent_energy = false;
		if ((c.cost == -1) && (EnergyPanel.totalCount > 0)) {
			x_cost_card_spent_energy = true;
		}
		super.onPlayCard(c, m);
		if (c.costForTurn > 0 || x_cost_card_spent_energy) {
			AbstractDungeon.actionManager.addToBottom(
					new DrawCardAction(AbstractDungeon.player,
							AMOUNT_TO_DRAW_WHEN_A_CARD_IS_PLAYED));
		}
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new DarkGi();
	}

}
