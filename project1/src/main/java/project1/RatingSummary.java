package project1;

import java.util.List;
import java.util.ArrayList;

/**
 * Ratings Summary supporting inner and outer statistics of the review 
 * @author tesic
*/
public class RatingSummary extends AbstractRatingSummary{

	/**
	 * 
	 * @param inNodeID unique identifier
	 * @param inDegree number of ratings for unique identifier
	 * @param inList list of review statistics
	 */
	public RatingSummary(final String inNodeID, final long inDegree, final List<Float> inList) {
		super(inNodeID, inDegree, inList);
	}

	/**
     * 
     * @param inNodeID unique identifier
     * @param inDegree number of ratings for unique identifier
     */
    public RatingSummary(final String inNodeID, final long inDegree) {

		super(inNodeID, inDegree);
		this.setList();
	}

	/**
     * Constructor
     * 
     * @param id        	product/review id
     * @param degree		number of times reviewed
     * @param productAvg    average rating of the product
     * @param reviewerAvg   average rating of the reviewer
     */
	public RatingSummary(final String id, final long degree, final float productAvg, final float reviewerAvg) {
		this(id, degree);
		this.setList(productAvg, reviewerAvg);
	}

	/**
	 * Constructor w 2 parameters
	 * @param id			product/review id
	 * @param rawRatings	review data
	 */
	public RatingSummary(final String id, final List<Rating> rawRatings) {
		super(id); 
		this.collectStats(rawRatings);
	}

	/**
     * List setter
     */
	public void setList() {
		super.setList(createList());
	}

	/**
     * List setter from stat data
     * 
     * @param productAvg    average rating of the product
     * @param reviewerAvg   average rating of the reviewer
     */
	public void setList(float productAvg, float reviewerAvg) {
		super.setList(this.createList(productAvg,reviewerAvg));
	}

	/**
     * Create List
	 * @return list reference
     */
	public List<Float> createList(){
		return new ArrayList<>();
	}

	/**
     * Create List
	 * @param productAvg    average rating of the product
     * @param reviewerAvg   average rating of the reviewer
	 * @return list reference
     */
	public List<Float> createList(float productAvg, float reviewerAvg) {
		List<Float> newList = createList();
		newList.add(Float.valueOf(productAvg));
		newList.add(Float.valueOf(reviewerAvg));
		return newList;
	}


	/**
	 * Prints RatingSummary object as form Id,degree,product avg,reviewer avg\n
	 * @return string
	 */
	@Override
	public String toString(){
		return (this.getNodeID()+","+this.getDegree()+","+this.printStats()+"\n");	
	}

	private String printStats() {

		StringBuilder stats = new StringBuilder();
		for(int i=0; i< this.getList().size(); i++) {
			stats.append(String.format("%.3f",this.getList().get(i)));
			if(i<this.getList().size()-1) {
				stats.append(",");
			}
		}
		return stats.toString();
	}

	/**
	 * Calculates product average, reviewer average, reviewer degrees, product degrees
	 * @param rawRatings raw data file, List<Rating>
	 */
	public void collectStats(final List<Rating> rawRatings){
		long[] reviewerDegree = {0}; 
		long[] productDegree = {0}; 
		ArrayList<String> productsReviewedArray = new ArrayList<String>(); 
		ArrayList<String> reviewersArray = new ArrayList<String>(); 
		long[] totalRating = {0}; // total number of ratings
		long[] ratingSum = {0}; // sum of the ratings of the products
		long[] reviewerSum = {0}; 
		long[] totalUserRatings = {0}; 
		long[] totalProductRatings = {0}; 
		long[] totalReviewersRating = {0}; 

		//STREAM FOR CALCULATING REVIEWER STATS
		rawRatings
      .stream()
      .filter(s -> {
        return (s.getReviewerID().equals(this.getNodeID()));
      })
      .forEach((s) ->{
        productsReviewedArray.add(s.getProductID());
        totalUserRatings[0]+=s.getRating();
        reviewerDegree[0]++; 
      });

		//STREAM FOR CALCULATING PRODUCT STATS
		rawRatings
      .stream()
      .filter(s -> {
        return (s.getProductID().equals(this.getNodeID()));
      })
      .forEach((s) ->{
        reviewersArray.add(s.getReviewerID()); 
        productDegree[0]++; 
        totalProductRatings[0]+=s.getRating(); 
      });

		rawRatings
      .stream()
      .filter(s -> {
        return (productsReviewedArray.contains(s.getProductID()));
      })
      .forEach((s) ->{
        ratingSum[0]+=s.getRating(); 
        totalRating[0]++;
      });

		rawRatings
      .stream()
      .filter(s -> {
        return (reviewersArray.contains(s.getReviewerID()));
      })
      .forEach((s) ->{
        reviewerSum[0]+=s.getRating(); 
        totalReviewersRating[0]++;
      });

		if (this.getNodeID().charAt(0) == 'A'){
      float prodAvg = (float) ratingSum[0] / totalRating[0]; 
      float reviewerAvg = (float) totalUserRatings[0] / reviewerDegree[0]; 
      this.setList(prodAvg, reviewerAvg);
      this.setDegree(reviewerDegree[0]);
		}

	else{
		float prodAvg = (float) totalProductRatings[0] / productDegree[0]; 
		float reviewerAvg = (float) reviewerSum[0] / totalReviewersRating[0]; 
		this.setList(prodAvg, reviewerAvg);
		this.setDegree(productDegree[0]);
		}
	}


  
	/**
	 * returns value for use when sorting collection
	 * @return value to use in sorting collection statistics
	 */
	public Float sortStats(){
    return this.getList().get(0).floatValue(); 
	}

	/** 
	 * avgScore method
	 * @return difference between product and reviewer average
	 */
	public Float avgScore(){
		return this.getList().get(0)-this.getList().get(1);
	}
}
