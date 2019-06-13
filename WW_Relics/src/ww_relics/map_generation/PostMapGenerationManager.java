package ww_relics.map_generation;

import java.util.ArrayList;
import java.util.Collections;

// I SERIOUSLY DON'T RECOMMEND THE USE OF THIS CODE AS AN EXAMPLE.
// SERIOUSLY, DON'T GO COPYING THIS POST MAG GEN SYSTEM TO YOUR MOD.
// IF YOU WILL, TALK WITH ME FIRST.
// Thanks.
public class PostMapGenerationManager {

	public static boolean initialized = false;
	static int counter;
	static ArrayList<PostMapGenerationChange> post_map_gen_changers; 
	
	public PostMapGenerationManager() {
	
		if (initialized == false) {
			preparePostMapGenerationManager();
		}

	}

	static void preparePostMapGenerationManager() {
		initialized = true;
		counter = 0;
		post_map_gen_changers = new ArrayList<PostMapGenerationChange>();
	}
	
	public int getCounter() {
		return ++counter;
	}
	
	public static void addPostMapGenerationChange(PostMapGenerationChange map_changer) {
		if (initialized == false) {
			preparePostMapGenerationManager();
		}
		post_map_gen_changers.add(map_changer);
	}
	
	public static void doIfPossiblePostMapGenerationChangers() {
		sortPostMapGenerationChangers();
		callAllPostMapGenerationChanges();
		loadCounter();
		cleanPostMapGenerationChanges();
	}
	
	static void sortPostMapGenerationChangers() {
		Collections.sort(post_map_gen_changers);
	}
	
	static void callAllPostMapGenerationChanges() {
		for (int i = 0; i < post_map_gen_changers.size(); i++) {
			
			if (post_map_gen_changers.get(i).post_map_gen_changer_object.canDoAfterMapGeneration()) {
				post_map_gen_changers.get(i).post_map_gen_changer_object.doAfterMapGeneration();
			}
			
		}
	}
	
	static void loadCounter() {
		if (post_map_gen_changers.size() > 0) {
			int new_counter = post_map_gen_changers.get(post_map_gen_changers.size()-1).counter;
			if (counter < new_counter) counter = new_counter;
		}
	}
	
	static void cleanPostMapGenerationChanges() {
		post_map_gen_changers.clear();
	}
	
		
}