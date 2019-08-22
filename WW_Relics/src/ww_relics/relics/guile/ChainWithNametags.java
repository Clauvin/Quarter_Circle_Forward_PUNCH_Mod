package ww_relics.relics.guile;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;

import basemod.abstracts.CustomRelic;
import ww_relics.resources.relic_graphics.GraphicResources;

public class ChainWithNametags extends CustomRelic {
	public static final String ID = "WW_Relics:Chain_With_Nametags";
	
	public static final int PERCENTAGE_OF_TEMP_HP_GAINED = 20;
	
	public ChainWithNametags() {
		super(ID, GraphicResources.LoadRelicImage("Temp Chain With Nametags - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + PERCENTAGE_OF_TEMP_HP_GAINED + DESCRIPTIONS[1];
	}
	
	public int onPlayerGainedBlock(float blockAmount) {
		
		float amount_of_temp_hp = blockAmount * PERCENTAGE_OF_TEMP_HP_GAINED / 100;
		
		if (amount_of_temp_hp >= 1) {
			
			int converted_temp_hp = (int)Math.floor(amount_of_temp_hp);
			
			AbstractDungeon.actionManager.addToBottom(
					new AddTemporaryHPAction(AbstractDungeon.player,
							AbstractDungeon.player, converted_temp_hp));
			
			flash();
			
			blockAmount -= converted_temp_hp;
			
		}

		return MathUtils.floor(blockAmount);
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new ChainWithNametags();
	}

}
