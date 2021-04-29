package test.java;
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.*;
import org.sql2o.*;

import main.java.Move;
import main.java.Pokemon;

public class MoveTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Move_instantiatesCorrectly_true() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    assertEquals(true, myMove instanceof Move);
  }

  @Test
  public void getName_moveInstantiatesWithName_String() {
    Move myMove = new Move("Solar Beam", "Normal", 50.0, 100);
    assertEquals("Solar Beam", myMove.getName());
  }
  
  @Test
  public void getType_moveInstantiatesWithType_String() {
    Move myMove = new Move("Solar Beam", "Normal", 50.0, 100);
    assertEquals("Normal", myMove.getType());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Move.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfMovesAreTheSame_true() {
    Move firstMove = new Move("Punch", "Normal", 50.0, 100);
    Move secondMove = new Move("Punch", "Normal", 50.0, 100);
    assertTrue(firstMove.equals(secondMove));
  }
  
  @Test
  public void equals_returnsFalseIfMovesAreDifferent_false() {
    Move firstMove = new Move("Punch", "Normal", 50.0, 100);
    Move secondMove = new Move("Bubble", "Water", 40.0, 50);
    assertFalse(firstMove.equals(secondMove));
  }
  
  @Test
  public void equals_returnsFalseIfMoveComparedWithPokemon_false() {
    Move firstMove = new Move("Punch", "Normal", 50.0, 100);
    Pokemon poke = new Pokemon("Meowth", "Normal", "Normal", "Some cat", 50.0, 12, 16, false);
    assertFalse(firstMove.equals(poke));
  }

  @Test
  public void save_savesMoveCorrectly_1() {
    Move newMove = new Move("Punch", "Normal", 50.0, 100);
    newMove.save();
    assertEquals(1, Move.all().size());
  }

  @Test
  public void find_findsMoveInDatabase_true() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Move savedMove = Move.find(myMove.getId());
    assertTrue(myMove.equals(savedMove));
  }

  @Test
  public void accuracy_checksAccuracy_false() {
    Move myMove = new Move("Flail", "Normal", 100.0, 0);
    myMove.save();
    assertFalse(myMove.hitCalculator());
  }

  @Test
  public void accuracy_checksAccuracy_true() {
    Move myMove = new Move("Flail", "Normal", 100.0, 100);
    myMove.save();
    assertTrue(myMove.hitCalculator());
  }
  @Test
  public void effectiveness_test_works() {
    Move myMove = new Move("Flail", "Water", 100.0, 100);
    myMove.save();
    Pokemon otherPokemon = new Pokemon("Flaming Rock Pikachu", "Rock", "Fire", "A flaming rat", 50.0, 12, 16, false);
    assertEquals(4, myMove.effectiveness(otherPokemon), 0);
  }

  @Test
  public void effectiveness_test_works_strongAgainstBoth_point25() {
    Move myMove = new Move("Flail", "Water", 100.0, 100);
    Pokemon otherPokemon = new Pokemon("Chia-Squirtle", "Water", "Grass", "A squirtle with chia-pet seeds on its shell", 50.0, 12, 16, false);
    assertEquals(.25, myMove.effectiveness(otherPokemon), 0);
  }
  @Test
  public void attack_method_does_damage() {
    Move myMove = new Move("Punch", "Normal", 60.0, 100);
    Pokemon otherPokemon = new Pokemon("Vanilla pokemon", "Normal", "None", "a normal pokemon", 50.0, 12, 16, false);
    assertEquals(String.format("The attack does %.2f damage!", 60.00), myMove.attack(otherPokemon)); // changed to consider different comma symbols depending on the system's locale
  }

  @Test
  public void getPokemons_getPokemonFromMoveSearch() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Pokemon savedPokemon = myMove.getPokemons().get(0);
    assertTrue(myPokemon.equals(savedPokemon));
  }
  
  @Test
  public void searchByName_findAllMovesWithSearchInputString_List() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    assertEquals(myMove, Move.searchByName("pun").get(0));
  }
  
  @Test
  public void searchByExactName_findExactMovesWithSearchInputString_List() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    assertEquals(myMove, Move.searchByExactName("punch").get(0));
  }
  
  @Test
  public void delete_deleteMove() {
  	Move myMove = new Move("Bubble", "Water", 50.0, 100);
  	myMove.save();
  	assertEquals(1, Move.all().size());
  	myMove.delete();
  	assertEquals(0, Move.all().size());
  }
}
