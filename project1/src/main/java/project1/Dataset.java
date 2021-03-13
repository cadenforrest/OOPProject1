package project1;

/* Team needs to import relevant packages here */

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;

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
   * @return
	 */
	public int loadRatings() {
    //test

		File file = this.getRawFile().toFile();
		String line = null;
		String pID = null;
		String rID = null;
		float rating = null;

		while (line = file.readLine()){
			String[] lineArray = line.split(",");

			pID = lineArray[0];
			rID = lineArray[1];
			rating = Float.parseFloat(lineArray[2]);

			Rating tempRating = new Rating(pID, rID, rating);

			ratingList.add(tempRating);
		}

		return ratingList.size();
	}

	/**
	 * Implement loadStats method
	 * Add javadoc
	 */
	public int loadStats(Path inStatPath) {

		//your code here

	}

	/**
	 * Implement computeStats method
	 * Add javadoc
	 */
	public boolean computeStats() {
		
		// your code here 


		
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
