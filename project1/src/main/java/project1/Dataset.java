package project1;

/* Team needs to import relevant packages here */

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.nio.file.Paths;

/**
 * Dataset
 * 
 * @author tesic
 */
public class Dataset {

	// Constructors

	/**
	 * Default constructor
	 */
	public Dataset() {
		this("", Paths.get(""));
	}

	/**
	 * Dataset constructor w 2 parameters
	 * @param dataId unique dataset identifier
	 * @param inRawFile  Amazon rating input file
	 */
	public Dataset(String dataId, Path inRawFile) {
		this.dataId = dataId;
		this.rawFile = inRawFile;
		this.ratingList = new ArrayList<>();
		this.ratingStat = new ArrayList<>();
		loadRatings();
	}

	/**
	 * Implement loadRatings method
	 * Add javadoc
   * @return number of items in ratingList
	 */
	public int loadRatings() {
    //test

    try{
      File file = this.getRawFile().toFile();
      Scanner scnr = new Scanner(file); 
      String line;
      String pID;
      String rID;
      float rating;


      
      while (scnr.hasNextLine()){
        line = scnr.nextLine(); 
        String[] lineArray = line.split(",");

        pID = lineArray[0];
        rID = lineArray[1];
        rating = Float.parseFloat(lineArray[2]);

        Rating tempRating = new Rating(pID, rID, rating);

        this.ratingList.add(tempRating);
      }
      scnr.close();
    }

    catch (FileNotFoundException e){ 
      System.out.println("Failed to load ratings, file not found");
    }
    return this.ratingList.size();
	}

	/**
	 * Implement loadStats method
	 * Add javadoc
   * @return number of entries in ratingStat, 0 if file doesn't exist
	 */
	public int loadStats(Path inStatPath) {


		//your code here

    try{
      File file = inStatPath.toFile();
      Scanner scnr = new Scanner(file); 
      String line;
      String id;
      long degree;
      Float productAvg;
      Float reviewerAvg; 



      while (scnr.hasNextLine()){
        line = scnr.nextLine(); 
        String[] lineArray = line.split(",");

        id = lineArray[0]; //product/reviewer id
        degree = Long.parseLong(lineArray[1]);
        productAvg = Float.parseFloat(lineArray[2]);
        reviewerAvg = Float.parseFloat(lineArray[3]);

        RatingSummary tempSummary = new RatingSummary(id, degree, productAvg, reviewerAvg);

        this.ratingStat.add(tempSummary);
      }
      scnr.close();
    }
    
    catch (FileNotFoundException e){
      System.out.println("Failed to load stats, file not found");
    }
    return this.ratingStat.size();
	}

	/**
	 * Implement computeStats method
	 * Add javadoc
   * @return boolean on success
	 */
	public boolean computeStats() {

    //for each rating in ratinglist
      //if reviewer doesn't exist, make him exist
      //instantiate a ratingsummary object 
      //add ratingSummary object to ratingStat

    for (Rating temp: this.ratingList){
      if (temp.getProductID()){


      }
      RatingSummary rSummary = new RatingSummary(getDataId(), this.ratingList); 
      this.ratingStat.add(rSummary);
    }


		// your code here 
		        /*
        (instantiate new lists of users/products:
        id
        amount
        rating                                //also called reviews
        stats
        
        if(id[0] == A)
            is a reviewer
            we need to compute:
                degree                 - the number of reviews from the ID
                product average     - all possible scores (from all the reviewers) of the products that this reviewer rated
                reviewer average    - all review scores that this reviewer provided
        if(id[0] == B)
            is a product
            we need to compute:
                degree                - the number of reviews from ID
                product average        - all possible reviews for this product from all reviewers
                reviewer average    - ratings from all reviews (from all the reviewers) that reviewed this product 
        */


    return true; 
	}

	/**
	 * Compute Statistics for object ratings
	 * @return if computing statistics from raw file was a success
	 */
	public String saveStats() {

		StringBuilder statString = new StringBuilder();
		// writing a rating summary in each line
		for (AbstractRatingSummary rs : this.getRatingStat()) {
			statString.append(rs.toString());
		}
		return statString.toString();
	}

	/**
	 * Data ID getter
	 * @return  unique dataset identifier
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * Data ID setter
	 * @param  dataId set unique dataset identifier
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * Get ratings filename
	 * @return Path to ratings file
	 */
	public Path getRawFile() {
		return this.rawFile;
	}

	/**
	 * rating list getter
	 * @return list of ratings
	 */
	public List<Rating> getRatingList() {
		return this.ratingList;
	}

	/**
	 * stat list getter
	 * @return list of rating stats
	 */
	public List<AbstractRatingSummary> getRatingStat() {
		return this.ratingStat;
	}

	/**
	 * @return if the object has stats
	 */
	public boolean hasStats(){
		return !(this.ratingStat.isEmpty());
	}

	/**
	 * Rating stat list setter
	 * @param ratingSummary list of rating stats
	 */
	public void setRatingSummary(List<AbstractRatingSummary> ratingSummary) {
		this.ratingStat = ratingSummary;
	}

	/** Print out format is dataID,RAW_FILE,RATINGS_NO,STAT_FILE 
	 * @return formatted output 
	*/
	@Override
	public String toString() {
		return (this.dataId + "," + this.ratingList.size() + "," + this.rawFile.getFileName());
	}

	private String dataId;
	private Path rawFile;
	private List<Rating> ratingList;
	private List<AbstractRatingSummary> ratingStat;
}
