package util.data;

import game.data.Cast;

public class PlayerInfo {

	private int id = 0;
	private int ready = 0;
	private int active = 0;
	private String name = "";
	private Cast cast = null;

	public PlayerInfo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cast getCast() {
		return cast;
	}

	public void setCast(Cast cast) {
		this.cast = cast;
	}
}