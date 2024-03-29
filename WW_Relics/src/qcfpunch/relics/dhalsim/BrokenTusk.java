package qcfpunch.relics.dhalsim;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.FuryPower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class BrokenTusk extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() +
			"Broken_Tusk";
	
	public static final int EXTRA_DAMAGE_PER_CURSE_OR_STATUS = 2;
	public boolean extra_damage_got_activated = false;
	
	public BrokenTusk() {
		super(ID, GraphicResources.LoadRelicImage(
				"Broken Tusk - Oni - Rights Reserved.png"),
				GraphicResources.LoadOutlineImage("Broken Tusk Outline.png"),
				RelicTier.COMMON, LandingSound.HEAVY);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + EXTRA_DAMAGE_PER_CURSE_OR_STATUS +
				DESCRIPTIONS[1];
	}
	
	@Override
	public void atBattleStart() {
		extra_damage_got_activated = false;
	}
	
	public void onCardDraw(AbstractCard drawnCard) {
		if (QCFP_Misc.cardIsACurseOrStatus(drawnCard)) {
			if (!extra_damage_got_activated) {
				extra_damage_got_activated = true;
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(AbstractDungeon.player,
							AbstractDungeon.player,
							new FuryPower(AbstractDungeon.player, 2)));
			}
		}
		
		
	}
	
	public boolean canSpawn() {
		return true;
	}
	
	public AbstractRelic makeCopy() {
		return new BrokenTusk();
	}
	
}
