package clustering.partitioning;

import datasets.DatasetPoint;

public class Centroid {
	private int ID;
	private double x;
	private double y;
	private double [] updateRate;
	private double [] SD;
	private double [] pHat;
	private double [] p;
	private double alpha;
	private double beta;
	private double c;
	private double k;
	private double taw;
	
	public Centroid(int id, double x, double y) {
		this.ID = id;
		this.x = x;
		this.y = y;
		this.updateRate = new double[2];
		this.SD = new double[2];
		this.pHat = new double[2];
		this.p = new double[2];
		this.alpha = 0.5;
		this.beta = 0.5;
		this.c = 0.5;
		this.k = 0.5;
		this.taw = 0;
		this.updateRate[0]= this.updateRate[1]=  this.SD[0] = this.SD[1] = this.pHat[0] = this.pHat[1] = this.p[0] = this.p[1] = 0;
		
	}
	
	public void updateCentroid(DatasetPoint p){
		double lambda = 0.5*(1- (calculateDotPoroduct(this.x, this.y, p.getX(), p.getY())/calculateMag(this.x, this.y, p.getX(), p.getY())));
		this.taw = Math.exp(-this.alpha*(1-lambda));
		this.taw = this.k*this.taw + (1-this.k) * this.taw;
		this.x= this.x - (this.x-p.getX())*this.taw*this.p[0]*calculatePE();
		this.y= this.y - (this.y-p.getY())*this.taw*this.p[1]*calculatePE();
		
		double [] diffs = new double[2];
		diffs[0] = Math.abs(p.getX()-this.x);
		diffs[1] = Math.abs(p.getY()-this.y);
		updateTheUpdatingRate(diffs);
		updateSD(diffs);
		updatePHat();
		updateP();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getID() {
		return ID;
	}
	
	private double calculateDotPoroduct(double v10, double v11, double v20, double v21){
		return v10*v20 + v11*v21;
	}
	
	private double calculateMag(double v10, double v11, double v20, double v21){
		double diffX = Math.pow(v10-v20, 2);
		double diffY = Math.pow(v11-v21, 2);
		return Math.sqrt(diffX+diffY);
	}
	
	
	
	public double calculateDistance(DatasetPoint point){
		double xDiff = this.x - point.getX();
		double yDiff = this.y - point.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}
	
	public double calculatePE(){
		double normX = this.normalizedX();
		double normY = this.normalizedY();
		double pe = normX*Math.log10(normY) + normY*Math.log10(normY);
		return (1- (pe/Math.log10(0.5)));
	}
	
	private void updateTheUpdatingRate(double [] distance){
		this.updateRate[0] = (this.alpha*this.updateRate[0]) + ((1-this.alpha)*distance[0]);
		this.updateRate[1] = (this.alpha*this.updateRate[1]) + ((1-this.alpha)*distance[1]);
	}
	
	private void updateSD(double [] distance){
		this.SD[0] = (this.beta*this.SD[0]) + ((1-this.beta) * Math.abs(this.updateRate[0]- distance[0]) );
		this.SD[1] = (this.beta*this.SD[1]) + ((1-this.beta) * Math.abs(this.updateRate[1]- distance[1]) );
	}
	
	private void updatePHat(){
		double e0 = Math.exp(-0.5*(this.SD[0]/(1+this.updateRate[0])));
		double e1 = Math.exp(-0.5*(this.SD[1]/(1+this.updateRate[1])));
		this.pHat[0] = (2/(1+ e0 ))-1;
		this.pHat[1] = (2/(1+ e1 ))-1;
	}
	
	private void updateP(){
		this.p[0] = c*this.p[0] + (1-c)*this.pHat[0];
		this.p[1] = c*this.p[1] + (1-c)*this.pHat[1];
	}
	
	private double normalizedX(){
		return this.x/(this.x+this.y);
	}
	
	
	private double normalizedY(){
		return this.y/(this.x+this.y);
	}
	
	public static void main(String[] args) {
		System.out.println(Math.log10(0.5));
		int x,y;
		x=y=5;
		System.out.println(x);
		System.out.println(y);
	}
	
}
