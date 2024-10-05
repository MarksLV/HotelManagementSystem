package application;

public class Rooms {

	// Rooms variables
	private int id;
	private int roomNumber;
	private double roomCost;
	private boolean roomAvailability;
	
	public Rooms(int id, int roomNumber, double roomCost, boolean roomAvailability) {
		super();
		this.id = id;
		this.roomNumber = roomNumber;
		this.roomCost = roomCost;
		this.roomAvailability = roomAvailability;
	}
	
	// Getter and setter method for ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	// Getter and setter method for Room Number
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	// Getter and setter method for Room Cost
	public double getRoomCost() {
		return roomCost;
	}
	public void setRoomCost(double roomCost) {
		this.roomCost = roomCost;
	}
	
	// Getter and setter method for Room Availability
	public boolean getRoomAvailability() {
		return roomAvailability;
	}
	public void setRoomAvailability(boolean roomAvailability) {
		this.roomAvailability = roomAvailability;
	}
}
