package test.java;

import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.action.FluentDefaultActions;
import org.fluentlenium.core.filter.matcher.ContainsMatcher;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;

import main.java.App;
import main.java.DB;
import spark.Spark;

import org.junit.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  //@Rule
  //public DatabaseRule database = new DatabaseRule();
  
  @Before
  public void beforeEveryTest() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pokedex_test", "postgres", "test"); // password might need to be changed in order to work
  }
  
  @After
  public void afterEveryTest() {
    try(Connection con = DB.sql2o.open()) {
      String deletePokemonsQuery = "DELETE FROM pokemons *;";
      String deleteMovesQuery = "DELETE FROM moves *;";
      String deleteMovesPokemonsQuery = "DELETE FROM moves_pokemons *;";
      con.createQuery(deletePokemonsQuery).executeUpdate();
      con.createQuery(deleteMovesQuery).executeUpdate();
      con.createQuery(deleteMovesPokemonsQuery).executeUpdate();
    }
  }

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  //@ClassRule
  //public static ServerRule server = new ServerRule();
  
  @BeforeClass
  public static void beforeAllTests() {
    String[] args = {};
    App.main(args);
  }
  
  @AfterClass
  public static void afterAllTests() {
	Spark.stop();
  }

  // Acceptance test:
  // Goes to the homepage and check if the page contains an element named Pokedex
  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }

  // Acceptance test:
  // Goes to the homepage, clickes the view Pokedex button, then checks if elements
  // with text "Ivysaur" and "Charizard" are present in the Pokedex.
  @Test
  public void allPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  // Acceptance test:
  // Go to the Pokedex entry page of Charizard (#6) and check if its name is present in the page.
  @Test
  public void individualPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  // Acceptance test:
  // Go to the Pokedex entry page of Charizard (#6),
  // click the Right triangle button,
  // then check if it went to the page of the next pokemon Squirtle (#7)
  // by verifying if the name Squirtle is now present.
  @Test
  public void arrowsCycleThroughPokedexCorrectly() {
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  // Acceptance test:
  // Go to the Pokedex, fill the "Search Pokémon by Name" text box with "char",
  // then check if Charizard is present in the results.
  @Test
  public void searchResultsReturnMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    click("#nameSearch");
    assertThat(pageSource().contains("Charizard"));
  }

  // Acceptance test:
  // Go to the Pokedex, fill the Name text box with "x" and check if no results was found
  // by looking for the presence of the text "No matches for your search results" in the
  // page.
  @Test
  public void searchResultsReturnNoMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("Missingno");
    click("#nameSearch");
    assertThat(pageSource().contains("No matches for your search results"));
  }
  
  // ------ New acceptance tests ------
  
  @Test
  public void getMusic() {
    goTo("http://localhost:4567/music");
    assertThat(pageSource().contains("MUSIC"));
  }
  
  @Test
  public void searchType() {
    goTo("http://localhost:4567/pokedex");
    fill("#strongAgainst").with("Mewtwo");
    click("#idealSearch");
    assertThat(pageSource().contains("Sandshrew"));
  }
  
}
