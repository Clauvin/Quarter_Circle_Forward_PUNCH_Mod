package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RemoveRelicFromPlayerAction extends AbstractGameAction {
	
	private String relic_id;
	
	public RemoveRelicFromPlayerAction(String relic_id) {
		
		this.relic_id = relic_id;
		
		actionType = ActionType.SPECIAL;
	}
	
	@Override
	public void update() {
		
		if (!this.isDone) {
			
			AbstractDungeon.player.loseRelic(relic_id);
			
			this.isDone = true;
			
		}
		
	}
	
	

}
