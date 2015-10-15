package model;

/**
 * This class will organize the coordinates.
 * @author Lili de Bree
 *
 */
public class Coordinates {

	private double x;
	private double y;
	private double r;
	private double dx;
	private double dy;
	private double dr;

	public Coordinates(double x, double y, double r,
			double dx, double dy, double dr) {
		this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getR() {
		return r;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	
	public double getDX() {
		return dx;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	public double getDY() {
		return dy;
	}
	
	public void setDY(double dy) {
		this.dy = dy;
	}
	
	public double getDR() {
		return dr;
	}
	
	public void setDR(double dr) {
		this.dr = dr;
	}
	
	
}
