package Networks;

import general.CommonObject;
import general.Constant;

public class Wavelength extends CommonObject {
	private double Capacity;

	public Wavelength(String name, int index) {
		super(name, index);
		// TODO Auto-generated constructor stub
		Capacity = Constant.WaveCap;
	}
	
	public double getCapacity() {
		return this.Capacity;
	}

	public void setCapacity(double newCapacity) {
		this.Capacity = newCapacity;
	}

	public boolean canUseCapacity(double Capacity) {
		return (this.Capacity >= Capacity) ? true : false;
	}

	public void useCapacity(double Capacity) {
		this.Capacity -= Capacity;
	}

	public void releaseCapacity(double Capacity) {
		this.Capacity += Capacity;
	}
	
	public boolean isExistCapacity(double requiredCapacity) {
		double Cap = this.Capacity;
		if(Cap >= requiredCapacity) {
			return true;
		}
		else
			return false;
	}
	
}