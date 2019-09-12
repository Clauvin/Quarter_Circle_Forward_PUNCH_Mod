package qcfpunch.relics.dhalsim;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.resources.relic_graphics.GraphicResources;

public class Cattail extends CustomRelic {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() +
			"Cattail";
	
	public static int HOW_MANY_ROOMS_TO_GIVE_SMOKE_BOMB = 7;
	public static float WHICH_MAX_HP_PERCENTAGE_HEALED_WHEN_DEAD = 0.3f;
	
	private int rooms_left_to_give_second_smoke_bomb;
	
	public Cattail() {
		super(ID, GraphicResources.LoadRelicImage("Temp Army Boots - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.SHOP, LandingSound.MAGICAL);
		
		rooms_left_to_give_second_smoke_bomb = HOW_MANY_ROOMS_TO_GIVE_SMOKE_BOMB;
	}
	
	@Override
	public void onEquip() {
		AbstractDungeon.player.obtainPotion(new SmokeBomb());
	}
	
	@Override
	public void onEnterRoom(AbstractRoom room) {
		rooms_left_to_give_second_smoke_bomb--;
		
		if (rooms_left_to_give_second_smoke_bomb == 0) {
			AbstractDungeon.player.obtainPotion(new SmokeBomb());
		}
	}
	
	public boolean canSpawn() {
		return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
	}
	
	public AbstractRelic makeCopy() {
		return new Cattail();
	}

}
