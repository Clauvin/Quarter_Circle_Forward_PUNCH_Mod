package qcfpunch.relics.cammy;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class RedBeret extends CustomRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() + "Red_Beret";
	
	public static final int EXTRA_CARD_DRAW_TO_GIVE = 2; 
	
	public boolean gave_extra_draw = false;
	
	public RedBeret() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.COMMON, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + EXTRA_CARD_DRAW_TO_GIVE + DESCRIPTIONS[1];
	}
	
	@Override
	public void atBattleStart() {
		gave_extra_draw = false;
		super.atBattleStart();
	}
	
	public int onPlayerGainedBlock(float blockAmount) {
		
		if (canGiveExtraDraw()) {
			flash();
			AbstractDungeon.actionManager.addToBottom(
					new DrawCardAction(AbstractDungeon.player, EXTRA_CARD_DRAW_TO_GIVE));
			gave_extra_draw = true;
		} else if (QCFPunch_MiscCode.hasNoDrawPower())
			AbstractDungeon.player.getPower("No Draw").flash();
		
		return super.onPlayerGainedBlock(blockAmount);
	}

	public boolean canGiveExtraDraw() {
		return 	QCFPunch_MiscCode.abscenceOfNoDrawPower() && !gave_extra_draw;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new RedBeret();
	}
	
}