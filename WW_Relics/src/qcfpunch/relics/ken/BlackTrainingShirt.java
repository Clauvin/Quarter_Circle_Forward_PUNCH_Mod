package qcfpunch.relics.ken;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.RiskyOffensivePower;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class BlackTrainingShirt extends CustomRelic {
	public static final String ID = QCFP_Misc.returnPrefix() + "Black_Training_Shirt";
	public static final int EXTRA_STRENGTH = 2;
	
	public BlackTrainingShirt() {
		super(ID, GraphicResources.LoadRelicImage("Black Training Shirt - Oni - Rights Reserved.png"),
				RelicTier.COMMON, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + EXTRA_STRENGTH + DESCRIPTIONS[1] +
				EXTRA_STRENGTH + DESCRIPTIONS[2];
	}
	
	
	
	@Override
	public void atBattleStart() {
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
						new StrengthPower(AbstractDungeon.player,
								EXTRA_STRENGTH), EXTRA_STRENGTH));

		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
						new RiskyOffensivePower(AbstractDungeon.player, EXTRA_STRENGTH)));
	}

	
	public AbstractRelic makeCopy() {
		return new BlackTrainingShirt();
	}

}
