package temp;

public class TestCar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarutiCar m = new MarutiCar(); //Child class
		m.start();
		m.refuel();
		m.musicSystem();
		
		Car c = new Car(); //Parent class
		c.start();
		c.refuel();
	}

}
