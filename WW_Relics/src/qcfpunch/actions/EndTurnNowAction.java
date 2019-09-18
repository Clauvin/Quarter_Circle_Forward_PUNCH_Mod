package qcfpunch.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

// With the blessing of Alchyr, this action has been copied of his Chen char
// Link: https://github.com/Alchyr/Chen/blob/master/src/main/java/Chen/Actions/GenericActions/EndTurnNowAction.java

public class EndTurnNowAction extends AbstractGameAction {

	public EndTurnNowAction(){
		
		actionType = ActionType.SPECIAL;
		
	}
	
	@Override
    public void update() {
		
        AbstractDungeon.actionManager.cardQueue.clear();

        for (AbstractCard c : AbstractDungeon.player.limbo.group)
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));

        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);

        this.isDone = true;
        
    }
}
