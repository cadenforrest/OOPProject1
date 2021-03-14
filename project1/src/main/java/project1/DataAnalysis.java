package project1;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class, all static methods Inventory of the datasets in
 * DATA_FILE_FOLDER, kept in DATA_FILE_NAME
 * 
 * @author tesic
 */
public class DataAnalysis {

	/** Private Constructor */
	private DataAnalysis() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Implement method
	 * Add javadoc
   * @return sorted list by degree
	 */
	public static List<AbstractRatingSummary> sortByDegree(List<AbstractRatingSummary> inList) {


		inList.sort((AbstractRatingSummary r1, AbstractRatingSummary r2) ->
									Long.compare(r2.getDegree(), r1.getDegree()));

    return inList;
	}

	/**
	 * Implement method
	 * Add javadoc
	 */
	public static List<AbstractRatingSummary> sortByAvgDiff(List<AbstractRatingSummary> inList) {

		// your code here 
		inList.sort((AbstractRatingSummary r1, AbstractRatingSummary r2) ->
									Float.compare(r2.avgScore(), r1.avgScore()));

    return inList; 
	}

	
	/**
	 * Implement method
	 * Add javadoc
	 */
	public static String printReport(List<AbstractRatingSummary> inList, int k) {
		StringBuilder stringBuilder = new StringBuilder(); //Create StringBuilder
		int startOfProducts = 0; //Declare int variable to find index of the start of the products in inList
		String finalString = null;

		//Find startOfProducts: iterate through inList till first product
		for (AbstractRatingSummary tempRS : inList){
			if (tempRS.getNodeID().charAt(0) == 'B'){
				break;
			}
			startOfProducts++;
		}

		//Create reviewer and product sublists
		List<AbstractRatingSummary> reviewersList = inList.subList(0, startOfProducts - 1);
		List<AbstractRatingSummary> productsList = inList.subList(startOfProducts, inList.size() - 1);

		//Begin StringBuilder output
		stringBuilder.append("Id,degree,product avg,reviewer avg\n--------------------------------------------------\n");
		stringBuilder.append("Top "+ k + " REVIEWER ANALYSIS\n--------------------------------------------------\n");
		
		//Reviewer output
		reviewersList = sortByDegree(reviewersList);
		stringBuilder.append("Reviewers with highest number of reviews\n");
		stringBuilder.append(appendTopK(reviewersList, k));

		reviewersList = sortByAvgDiff(reviewersList);
		stringBuilder.append("Reviewers with highest discrepencies per reviewer\n");
		stringBuilder.append(appendTopK(reviewersList, k));
		
		stringBuilder.append("--------------------------------------------------\nTop " + k + " PRODUCT ANALYSIS\n");
		stringBuilder.append("--------------------------------------------------\n");

		//Product output
		productsList = sortByDegree(productsList);
		stringBuilder.append("Products with highest number of reviews\n");
		stringBuilder.append(appendTopK(productsList, k));

		productsList = sortByAvgDiff(productsList);
		stringBuilder.append("Products with highest rating discrepancies\n");
		stringBuilder.append(appendTopK(productsList, k));

		stringBuilder.append("--------------------------------------------------\n");

		//Conver stringBuilder to String type finalString and return
		finalString = stringBuilder.toString();
		return finalString;
	}

	public static String appendTopK(List<AbstractRatingSummary> inList, int k){
		StringBuilder stringBuilder = new StringBuilder();
		String finalString = null;
		int i = 0;
		while(i < k && i < inList.size()){
			stringBuilder.append(inList.get(i).getNodeID() + ",");
			stringBuilder.append(inList.get(i).getDegree() + ",");
			stringBuilder.append(String.format("%.3f,", inList.get(i).getList().get(0)));
			stringBuilder.append(String.format("%.3f%n", inList.get(i).getList().get(1)));
			i++;
		}
		
		//Catch for if there are less than k products or entries in the sublist
		if (i < k - 1){
			stringBuilder.append("There are less than k entries for this type\n");
		}

		finalString = stringBuilder.toString();
		return finalString;
	}

	/**
	 * The file name of where the database is going to be saved.
	 */
	public static final String LINE_SEP = System.lineSeparator();
	public static final String DELIMITER = ",";
	public static final String DB_FOLDER = "data";
	public static final String DB_FILENAME = "data.csv";
	public static final String DATA_ID_TEMPLATE = "<dataID>";
	public static final String STAT_FILE_TEMPLATE = "ratingSummary_<dataID>.csv";
	public static final String REPORT_FILE_TEMPLATE = "report_<dataID>.csv";
	public static final String RESULTS_FILE_TEMPLATE = "results_<dataID>.csv";
	public static final String SUMMARY_HEADER ="Id,degree,product avg,reviewer avg";
}