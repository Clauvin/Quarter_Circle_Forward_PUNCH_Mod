package qcfpunch.relics.zangief;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.actions.RemoveRelicFromPlayerAction;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class WrestlersCloak extends CustomRelic implements ClickableRelic {
	
	public static final String ID = QCFPunch_MiscCode.returnPrefix() + "Wrestler's_Cloak";
	
	public final int TEMP_HP_TO_GIVE = 5;
	public final float HP_PERCENTAGE_TO_TRIGGER_WARNING = 0.20f;
	
	public boolean is_player_turn = false;
	
	public WrestlersCloak() {
		super(ID, GraphicResources.LoadRelicImage("White_Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.COMMON, LandingSound.FLAT);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + TEMP_HP_TO_GIVE + 
			    DESCRIPTIONS[1] + TEMP_HP_TO_GIVE +
			    DESCRIPTIONS[2];
	}
	
	@Override
	public void atTurnStart() {
		is_player_turn = true;
		if (shouldRemindThePlayerThatHeyThisRelicEXISTSTOSAVEYOU()) {
			flash();
		}
	}
	
	public boolean shouldRemindThePlayerThatHeyThisRelicEXISTSTOSAVEYOU() {
		
		float current_hp = (float)AbstractDungeon.player.currentHealth;
		float max_hp = (float)AbstractDungeon.player.maxHealth;
		
		if (current_hp / max_hp <= HP_PERCENTAGE_TO_TRIGGER_WARNING) return true;
		else return false;
	}
	
	@Override
	public void onPlayerEndTurn() {
		is_player_turn = false;
	}
	
	@Override
	public void atBattleStartPreDraw() {
		if (itIsAnEliteOrBossRoom()) {
			AbstractDungeon.actionManager.addToTop(
					new AddTemporaryHPAction(AbstractDungeon.player,
							AbstractDungeon.player, TEMP_HP_TO_GIVE));
		}
	}
	
	@Override
	public void onRightClick() {
		
		if (canThrowCloakAway()){
			
			AbstractDungeon.actionManager.addToBottom(
					new AddTemporaryHPAction(AbstractDungeon.player,
							AbstractDungeon.player, TEMP_HP_TO_GIVE));
			
			AbstractDungeon.actionManager.addToBottom(
					new RemoveRelicFromPlayerAction(ID));
					
		}			
	}
	
	private boolean itIsAnEliteOrBossRoom() {
		return ((AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) ||
				(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss));
	}
	
	private boolean canThrowCloakAway() {
		boolean can_throw_cloak = false;
		
		if (is_player_turn
				&& !AbstractDungeon.getCurrRoom().isBattleOver
				&& !AbstractDungeon.getCurrRoom().isBattleEnding()
				&& AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT){
			can_throw_cloak = true;
		}
		
		return can_throw_cloak;
	}
	
	public AbstractRelic makeCopy() {
		return new WrestlersCloak();
	}
}
