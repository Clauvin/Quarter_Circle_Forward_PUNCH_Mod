package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import qcfpunch.relics.no_relation.Cattail;

public class CattailTacticalEspionageAction extends AbstractGameAction {

	public Cattail cattail;
	
	public CattailTacticalEspionageAction(Cattail players_cattail) {
		cattail = players_cattail;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			
			AbstractDungeon.effectList.add(
					new SmokeBombEffect(cattail.currentX, cattail.currentY));
			AbstractDungeon.actionManager.addToBottom(
					new RemoveRelicFromPlayerAction(Cattail.ID));
			this.isDone = true;
		}
		
	}

	
	
}
