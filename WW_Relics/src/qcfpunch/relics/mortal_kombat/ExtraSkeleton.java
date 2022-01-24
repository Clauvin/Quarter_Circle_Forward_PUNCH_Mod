package qcfpunch.relics.mortal_kombat;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class ExtraSkeleton extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Extra_Skeleton";
	
	public static final int COUNTER_INITIAL_VALUE = 0;
	public static final int COUNTER_INCREASE_VALUE = 1;
	public static final int COUNTER_MAX_VALUE = 6;
	
	public static final int BUFFER_POWER_AMOUNT_ADDED = 1;
	
	public ExtraSkeleton() {
		super(ID, GraphicResources.LoadRelicImage("Temp Extra Skeleton - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.SPECIAL, LandingSound.MAGICAL);
		
		this.counter = COUNTER_INITIAL_VALUE;
	}

	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + BUFFER_POWER_AMOUNT_ADDED +
				DESCRIPTIONS[1] + COUNTER_MAX_VALUE +
				DESCRIPTIONS[2];
	}
	
	@Override
	public void atTurnStart() {
		
		this.counter++;
		
		if (counter == COUNTER_MAX_VALUE - 1) {
			this.pulse = true;
			beginPulse();
		}
		else {
			this.pulse = false;
			stopPulse();
		}
		
		if (counter >= COUNTER_MAX_VALUE) applyEffect();
		
		super.atTurnStart();
	}
	
	private void applyEffect() {
		
		flash();
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
						new BufferPower(AbstractDungeon.player, BUFFER_POWER_AMOUNT_ADDED)));
		this.counter = 0;
		
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new ExtraSkeleton();
	}
}
