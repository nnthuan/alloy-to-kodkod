package Model;

import java.lang.String;

class Gardener {
  private int resource;

  private GameState game_state;

  private GameEvent event;

  public Gardener(int resource, GameState game_state, GameEvent event) {
    this.resource = resource;
    this.game_state = game_state;
    this.event = event;
  }

  public String toString() {
    return "";
  }

  public void setResource(int resource) {
    this.resource = resource;
  }

  public int getResource() {
    return resource;
  }

  public void setGame_state(GameState game_state) {
    this.game_state = game_state;
  }

  public GameState getGame_state() {
    return game_state;
  }

  public void setEvent(GameEvent event) {
    this.event = event;
  }

  public GameEvent getEvent() {
    return event;
  }
}
