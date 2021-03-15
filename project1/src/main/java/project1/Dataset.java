package project1;

/* Team needs to import relevant packages here */

import java.util.Scanner;
import java.nio.file.Path;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

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
	 * Load ratings data from this.getRawFile(), add Rating objects to this.ratingList
   * @return number of items in ratingList
	 */
	public int loadRatings() {
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
   * Loads statistics from file, adds to this.ratingStat
	 * @param inStatPath filepath to the calculated statistics
   * @return number of entries in ratingStat, 0 if file doesn't exist
	 */
	public int loadStats(Path inStatPath) {
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
      return this.ratingStat.size();
    }
    
    catch (FileNotFoundException e){
      return 0; 
    }
	}

	/**
	 * Computes statistics and creates lists of users and products from ratingList
   * @return boolean on success
	 */
	public boolean computeStats() {
		ArrayList<String> objectsCreated = new ArrayList<String>();
		for (Rating tempRating: this.ratingList){
			boolean prodExist = false;
			boolean reviewerExist = false;

			if (objectsCreated.contains(tempRating.getProductID())){
				prodExist = true;
			}
			if (objectsCreated.contains(tempRating.getReviewerID())){
				reviewerExist = true;
			}
			
			if (!prodExist){ 
				RatingSummary temp = new RatingSummary(tempRating.getProductID(), this.ratingList);
				this.ratingStat.add(temp); 
				objectsCreated.add(tempRating.getProductID());
			}

			if (!reviewerExist){
				RatingSummary temp1 = new RatingSummary(tempRating.getReviewerID(), this.ratingList);
				this.ratingStat.add(0, temp1);
				objectsCreated.add(tempRating.getReviewerID()); 
			}
		}
		
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
