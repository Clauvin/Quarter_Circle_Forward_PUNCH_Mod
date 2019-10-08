package qcfpunch.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import qcfpunch.QCFP_Misc;

public class ChallengerCoinOnlySpawnsOncePerMerchantShopPatches {
	
	@SpirePatch(
	        clz=ShopScreen.class,
	        method="initPotions")
	
	public static class ShopScreenPatchAddsVariable{
		
		@SpireInsertPatch(rloc=2)
		public static void Insert(ShopScreen __instance) {
			QCFP_Misc.resetNumberOfChallengerCoinPotionsVariable();
		}
		
	}
	
	@SpirePatch(
	        cls="com.megacrit.cardcrawl.shop.ShopScreen",
	        method="initPotions")
	
	public static class ShopScreenPatchUsesVariable{
		
		public static ExprEditor Instrument()
		{
			return new ExprEditor() {
				@Override
				public void edit(NewExpr n) throws CannotCompileException
				{				
					if (n.getClassName().toString().equals(StorePotion.class.getName().toString())) {
						try {
							n.replace("$_ = $proceed($$);"
									+ "if ((qcfpunch.QCFPunch_MiscCode.number_of_challenger_coin_potions_at_shop == 0) && "
									+ "($_.potion.ID.equals(qcfpunch.potions.ChallengerCoin.ID))){"
									+ "qcfpunch.QCFPunch_MiscCode.incrementNumberOfChallengerCoinPotionsAtShop();"
									+ "} else if ($_.potion.ID.equals(qcfpunch.potions.ChallengerCoin.ID)){"
										+ "while ($_.potion.ID.equals(qcfpunch.potions.ChallengerCoin.ID)){"
											+ "$_ == new com.megacrit.cardcrawl.shop.StorePotion("
												+ "com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnRandomPotion(), i, this);"
										+ "}"
									+ "}");
						} catch (CannotCompileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			
		}
	}
	
}
