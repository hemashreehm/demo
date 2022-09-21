package jsonCompare;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//HashMap;
//import java.util.Map;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.octomix.josson.exception.UnresolvedDatasetException;


public class compareUtil {

	
	public static void main(String[] args) throws IOException, UnresolvedDatasetException, ParseException {
		// TODO Auto-generated method stub
		
		String baselineFolderPath = "input//jsonBaseline"; 
		String regressionfolderPath = "input//jsonRegression";
		
		String fileToCompare = "sampleJson1_simple.json";
		
		String baselineFile = baselineFolderPath + "//" + fileToCompare;
		String regressionFile = regressionfolderPath + "//" + fileToCompare;
		
		InputStream getBaselineJsonFile = new FileInputStream(baselineFile);
		InputStream getRegressionJsonFile = new FileInputStream(regressionFile);
						
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};

		Map<String, Object> baselineMap = mapper.readValue(getBaselineJsonFile, type);
		Map<String, Object> regressionMap = mapper.readValue(getRegressionJsonFile, type);
		
		MapDifference<String, Object> differenceNormal = Maps.difference(baselineMap, regressionMap);
		System.out.println(differenceNormal);
		
		Map<String, Object> baselineFlatMap = FlatMapUtil.flatten(baselineMap);
		Map<String, Object> regressionFlatMap = FlatMapUtil.flatten(regressionMap);
						
		System.out.println("baselineFlatMap "+baselineFlatMap);
		System.out.println("regressionFlatMap "+regressionFlatMap);
		
		MapDifference<String, Object> difference = Maps.difference(baselineFlatMap, regressionFlatMap);

		System.out.println("The diff "+difference);
		
		System.out.println("Entries only on the baseline json\n--------------------------");
		difference.entriesOnlyOnLeft()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries only on the regression json\n--------------------------");
		difference.entriesOnlyOnRight()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries differing (baseline, regression)\n--------------------------");
		difference.entriesDiffering()
		          .forEach((key, value) -> System.out.println(key + ": " + value));
		 
	}

	
}
