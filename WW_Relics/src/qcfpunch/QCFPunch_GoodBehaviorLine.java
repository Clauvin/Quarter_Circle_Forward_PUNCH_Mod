package qcfpunch;

//this class needs some heavy refactoring to avoid hardcoding?
// yes
//am I going to do that now?
// nope.
//In the future?
// hopefully yes.

//and yes, I'm having some fun with this class name so what it does?
// it avoids a resource war between Necklace of Skulls and Lotus Statue.
// Basically, each relic asks if it can do something with the
// grid screen, and sets their variables here accordingly.
public class QCFPunch_GoodBehaviorLine {
	
	public static boolean time_of_necklace_of_skulls = false;
	public static int waiting_time_for_necklace_of_skulls = 0;
	public static boolean time_of_lotus_statue = false;
	public static int waiting_time_for_lotus_statue = 0;

	public static boolean canLotusStatueWork() {
		return !time_of_necklace_of_skulls &&
				waiting_time_for_lotus_statue == 0;
	}
	
	public static void lotusStatueWorkingHere() {
		time_of_lotus_statue = true;
	}
	
	public static void lotusStatueFinished() {
		time_of_lotus_statue = false;
		waiting_time_for_lotus_statue += 10;
	}
	
	public static void lotusStatueTick(){
		if (waiting_time_for_lotus_statue > 0)
			waiting_time_for_lotus_statue -= 1;
	}
	
	public static boolean canNecklaceOfSkullsWork() {
		
		return !time_of_lotus_statue && 
				waiting_time_for_necklace_of_skulls == 0;
	}
	
	public static void necklaceOfSkullsWorkingHere() {
		time_of_necklace_of_skulls = true;
	}
	
	public static void necklaceOfSkullsFinished() {
		time_of_necklace_of_skulls = false;
		waiting_time_for_necklace_of_skulls += 10;
	}
	
	public static void necklaceOfSkullsTick(){
		if (waiting_time_for_necklace_of_skulls > 0)
			waiting_time_for_necklace_of_skulls -= 1;
	}
	
}
