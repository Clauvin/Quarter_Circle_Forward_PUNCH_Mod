package qcfpunch.relics.zangief;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.actions.EndTurnNowAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class RedCycloneTeachings extends CustomRelic  {
	
	public static final String ID = QCFPunch_MiscCode.returnPrefix() + "Red_Cyclone_Teachings";
	
	public static final int MINIMUM_COST_TO_STUN = 3;
	public static final int MAXIMUM_AMOUNT_OF_USES_PER_COMBAT = 2;
	
	public RedCycloneTeachings() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.HEAVY);
		
		this.counter = MAXIMUM_AMOUNT_OF_USES_PER_COMBAT;
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + MAXIMUM_AMOUNT_OF_USES_PER_COMBAT +
				DESCRIPTIONS[1] + MINIMUM_COST_TO_STUN + 
			    DESCRIPTIONS[2];
	}
	
	@Override
	public void atPreBattle() {
		this.counter = MAXIMUM_AMOUNT_OF_USES_PER_COMBAT;
	}
	
	@Override
	public void onVictory() {
		this.counter = MAXIMUM_AMOUNT_OF_USES_PER_COMBAT;
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		if ((c.type == CardType.ATTACK) && (c.costForTurn >= MINIMUM_COST_TO_STUN)
				&& (this.counter > 0)) {
			
			this.counter--;
			
			c.exhaust = true;

			flash();
			
			switch (c.target) {
				case ENEMY:
					
					stunSingleTarget(m);
					break;
				
				case ALL_ENEMY:
					
					stunAllMonsters();
					
					break;
					
				case SELF:
					
					stunSelfSoEndTurn();
					break;
					
				case SELF_AND_ENEMY:
					
					stunSingleTarget(m);
					stunSelfSoEndTurn();
					
					break;
					
				case ALL:
					
					stunAllMonsters();
					
					stunSelfSoEndTurn();

				default:
					break;
				
			}

		}	
		
	}
	
	private void stunSingleTarget(AbstractMonster m) {
		AbstractDungeon.actionManager.addToTop(
				new StunMonsterAction(m, AbstractDungeon.player, 1));
	}
	
	private void stunAllMonsters() {
		MonsterGroup monster_group = AbstractDungeon.getCurrRoom().monsters;
		
		for (int i = 0; i < monster_group.monsters.size(); i++) {
			
			AbstractMonster monster = monster_group.monsters.get(i);
			if (!monster.isDeadOrEscaped()) {
				stunSingleTarget(monster);
			}					
			
		}
	}
	
	private void stunSelfSoEndTurn() {
		
		AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());
	    
	}
	
	public boolean canSpawn() {
		
		if (AbstractDungeon.player.hasRelic("Snecko Eye")) return true;
		
		CardGroup attacks_deck = AbstractDungeon.player.masterDeck.getAttacks();
		
		int amount_of_3_or_more_cost_cards = 0;
		
		for (int i = 0; i < attacks_deck.size(); i++) {
			if (attacks_deck.getNCardFromTop(i).cost >=
					MINIMUM_COST_TO_STUN) {
				amount_of_3_or_more_cost_cards++;
			}
		}
		
		if (amount_of_3_or_more_cost_cards > 0) return true;
		else return false;
		
	}
	
	public AbstractRelic makeCopy() {
		return new RedCycloneTeachings();
	}

}
