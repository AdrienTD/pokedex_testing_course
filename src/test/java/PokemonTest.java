package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sql2o.*;

import main.java.Move;
import main.java.Pokemon;

@RunWith(Enclosed.class)
public class PokemonTest {
  public static class UnparamTest {
    @Rule
    public DatabaseRule database = new DatabaseRule();

    // Unit test
    // Create a new instance of Pokemon and check if it is an instance of the Pokemon class
    @Test
    public void Pokemon_instantiatesCorrectly_true() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals(true, myPokemon instanceof Pokemon);
    }

    // Unit test
    // Create a new instance of Pokemon with name string "Squirtle"
    // and check if the string returned by getName() is the same.
    @Test
    public void getName_pokemonInstantiatesWithName_String() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals("Squirtle", myPokemon.getName());
    }

    // Unit test
    // Check if there are no instances of Pokemon when the test is launched
    @Test
    public void all_emptyAtFirst() {
    	assertEquals(Pokemon.all().size(), 0);
    }

    // Unit test
    // Check if the Pokemon.equals method returns true with
    // two Pokemon instances created with the same values.
    @Test
    public void equals_returnsTrueIfPokemonAreTheSame_true() {
    	Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertTrue(firstPokemon.equals(secondPokemon));
    }

    // Unit test
    // Check if saving Pokemon instances with Pokemon.save() works and are now counted
    // in Pokemon.all().
    @Test
    public void save_savesPokemonCorrectly_1() {
    	Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	newPokemon.save();
    	assertEquals(1, Pokemon.all().size());
    }

    // Unit test
    // Check if a saved Pokemon instance can be found with Pokemon.find by its ID.
    @Test
    public void find_findsPokemonInDatabase_true() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
    	assertTrue(myPokemon.equals(savedPokemon));
    }

    // Unit test
    // Check if adding moves to a Pokemon with Pokemon.addMove works,
    // and that Pokemon.getMoves() returns the newly added move.
    @Test
    public void addMove_addMoveToPokemon() {
    	Move myMove = new Move("Punch", "Normal", 50.0, 100);
    	myMove.save();
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	myPokemon.addMove(myMove);
    	Move savedMove = myPokemon.getMoves().get(0);
    	assertTrue(myMove.equals(savedMove));
    }

    // Unit test
    // Check if deleting a Pokemon works and deletes the associated Moves too.
    @Test
    public void delete_deleteAllPokemonAndMovesAssociations() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	Move myMove = new Move("Bubble", "Water", 50.0, 100);
    	myMove.save();
    	myPokemon.addMove(myMove);
    	myPokemon.delete();
    	assertEquals(0, Pokemon.all().size());
    	assertEquals(0, myPokemon.getMoves().size());
    }

    // Unit test
    // Check if Pokemon.searchByName can find an added "Squirtle" pokemon by searching for "squir".
    @Test
    public void searchByName_findAllPokemonWithSearchInputString_List() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
    }
    
    // ------ New tests ------
    
    // Unit test
    // Check if the Pokemon.equals method returns false with
    // two Pokemon instances created with different values.
    @Test
    public void equals_returnsFalseIfPokemonAreDifferent_false() {
    	Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	Pokemon secondPokemon = new Pokemon("Meowth", "Normal", "None", "A cute cat", 40.0, 13, 17, true);
    	assertFalse(firstPokemon.equals(secondPokemon));
    }
    
    // Unit test
    // Check if the Pokemon.equals method returns false
    // when compared with a Move
    @Test
    public void equals_returnsFalseIfPokemonComparedWithMove_false() {
      Move firstMove = new Move("Punch", "Normal", 50.0, 100);
      Pokemon poke = new Pokemon("Meowth", "Normal", "Normal", "Some cat", 50.0, 12, 16, false);
      assertFalse(poke.equals(firstMove));
    }
    
    // Unit test
    // Check if Pokemon.searchByType gives an empty list if no Pokemons of such type exist.
    @Test
    public void searchByType_findAllPokemonOfType_List() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "Fire", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	var list = Pokemon.searchByType("Water");
    	assertEquals(myPokemon, list.get(0));
    }
    
    // Unit test: check if Pokemon.getHp returns 0 just after construction
    @Test
    public void getHp_pokemonInstantiatesWithZeroHp_Integer() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals(0, myPokemon.getHp());
    }
    
    // Unit test: check if Pokemon.getDescription returns the value given to the constructor
    @Test
    public void getDescription_pokemonInstantiatesWithDescription_String() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals("A cute turtle", myPokemon.getDescription());
    }
    
    // Unit test: check if Pokemon.getWeight returns the value given to the constructor
    @Test
    public void getWeight_pokemonInstantiatesWithWeight_Double() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals((int)50, (int)myPokemon.getWeight());
    }
    
    // Unit test: check if Pokemon.getWeight returns the value given to the constructor
    @Test
    public void getHeight_pokemonInstantiatesWithHeight_Integer() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals(12, myPokemon.getHeight());
    }
    
    // Unit test: check if Pokemon.getWeight returns the value given to the constructor
    @Test
    public void getEvolves_pokemonInstantiatesWithEvolves_Integer() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals(16, myPokemon.getEvolves());
    }
    
    // Unit test: check if Pokemon.getWeight returns the value given to the constructor
    @Test
    public void getMega_evolves_pokemonInstantiatesWithMegaEvolves_Boolean() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals(false, myPokemon.getMega_evolves());
    }
    
    // Unit test: check if Pokemon.getImageName returns the Pokemon's name with the .gif postfix
    @Test
    public void getImageName_pokemonCorrectImageName_String() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	assertEquals("Squirtle.gif", myPokemon.getImageName());
    }
    
    // Unit test: check if Pokemon.getPreviousId returns the ID of the previously saved Pokemon
    @Test
    public void getPreviousId_pokemonCorrectPreviousId_Integer() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	Pokemon secondPokemon = new Pokemon("Meowth", "Normal", "None", "A cute cat", 40.0, 13, 17, true);
    	secondPokemon.save();
    	assertEquals(myPokemon.getId(), secondPokemon.getPreviousId());
    }
    
    // Unit test: check if Pokemon.getNextId returns the ID of the next saved Pokemon
    @Test
    public void getNextId_pokemonCorrectNextId_Integer() {
    	Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    	myPokemon.save();
    	Pokemon secondPokemon = new Pokemon("Meowth", "Normal", "None", "A cute cat", 40.0, 13, 17, true);
    	secondPokemon.save();
    	assertEquals(secondPokemon.getId(), myPokemon.getNextId());
    }
  }

  // Unit test
  // Check if attacking a Pokemon with 500 HP with a move that causes damage of 100 HP 4 times
  // will make the Pokemon have 400 HP.
  @RunWith(Parameterized.class)
  public static class ParamTest {    
    private Pokemon myPokemon;
    private Move myMove;
    private Integer startHp;
    private Integer endHp;
    
    public ParamTest(Pokemon pok, Move mov, Integer startHp, Integer endHp) {
      this.myPokemon = pok;
      this.myMove = mov;
      this.startHp = startHp;
      this.endHp = endHp;
    }
    
    @Parameterized.Parameters
    public static Collection data() {
      return Arrays.asList(new Object[][] {
        {new Pokemon("Squirtle", "Water", "Normal", "A cute turtle", 50.0, 12, 16, false),
         new Move("Bubble", "Water", 50.0, 100),
         500, 400},
        {new Pokemon("Raboot", "Fire", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ground", 50.0, 1000),
         800, 400},
        {new Pokemon("Raboot", "Fire", "Normal", "Some bunny", 50.0, 12, 16, false),
             new Move("Very inaccurate move", "Ground", 50.0, 0),
             800, 800},
        {new Pokemon("Somebody", "Rock", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Normal", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Ghost", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Normal", 50.0, 1000),
         500, 500},
        {new Pokemon("Somebody", "Fire", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Fire", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Grass", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Fire", 50.0, 1000),
         500, 100},
        {new Pokemon("Somebody", "Grass", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ground", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Flying", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ground", 50.0, 1000),
         500, 500},
        {new Pokemon("Somebody", "Water", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Electric", 50.0, 1000),
         500, 100},
        {new Pokemon("Somebody", "Grass", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Electric", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Ground", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Electric", 50.0, 1000),
         500, 500},
        {new Pokemon("Somebody", "Fire", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Grass", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Water", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Grass", 50.0, 1000),
         500, 100},
        {new Pokemon("Somebody", "Fire", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ice", 50.0, 1000),
         500, 400},
        {new Pokemon("Somebody", "Grass", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ice", 50.0, 1000),
         500, 100},
        {new Pokemon("Somebody", "Poison", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Fighting", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Ghost", "Normal", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Fighting", 50.0, 1000),
         500, 500},
        {new Pokemon("Somebody", "Grass", "Poison", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Poison", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Grass", "Electric", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Flying", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Fighting", "Psychic", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Psychic", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Grass", "Fighting", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Bug", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Fire", "Ground", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Rock", 50.0, 1000),
         500, 300},
        {new Pokemon("Somebody", "Psychic", "Electric", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ghost", 50.0, 1000),
         500, 100},
        {new Pokemon("Somebody", "Normal", "Electric", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Ghost", 50.0, 1000),
         500, 500},
        {new Pokemon("Somebody", "Dragon", "Electric", "Some bunny", 50.0, 12, 16, false),
         new Move("Something", "Dragon", 50.0, 1000),
         500, 100},
      });
    }
    
    @Test
    public void fighting_damagesDefender() {
      myPokemon.hp = startHp;
      myMove.attack(myPokemon);
      System.out.println(myPokemon.hp);
      myMove.attack(myPokemon);
      System.out.println(myPokemon.hp);
      myMove.attack(myPokemon);
      System.out.println(myPokemon.hp);
      myMove.attack(myPokemon);
      assertEquals(endHp.intValue(), myPokemon.hp);
    }
  }

}
