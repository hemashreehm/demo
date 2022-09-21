package jsonCompare;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.octomix.josson.Josson;
import com.octomix.josson.Jossons;
import com.octomix.josson.exception.UnresolvedDatasetException;

public class compareJsonObjects {
	public static void main(String[] args) throws IOException, UnresolvedDatasetException, ParseException {
		// TODO Auto-generated method stub
		
		String baselineFolderPath = "input//jsonBaseline"; 
		String regressionfolderPath = "input//jsonRegression";
		
		String fileToCompare = "sampleJson1_simple.json";
		
		String baselineFile = baselineFolderPath + "//" + fileToCompare;
		String regressionFile = regressionfolderPath + "//" + fileToCompare;
		
		InputStream getBaselineJsonFile = new FileInputStream(baselineFile);
		InputStream getRegressionJsonFile = new FileInputStream(regressionFile);
		
		System.out.println("getBaselineJsonFile "+baselineFile);
		System.out.println("getRegressionJsonFile "+regressionFile);
				
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};

		Map<String, Object> baselineMap = mapper.readValue(getBaselineJsonFile, type);
		Map<String, Object> regressionMap = mapper.readValue(getRegressionJsonFile, type);
		
//		MapDifference<String, Object> differenceNormal = Maps.difference(baselineMap, regressionMap);
//		System.out.println(differenceNormal);
//		
		
		Map<String, Object> baselineFlatMap = FlatMapUtil.flatten(baselineMap);
		Map<String, Object> regressionFlatMap = FlatMapUtil.flatten(regressionMap);
		
				
		System.out.println("baselineFlatMap "+baselineFlatMap);
		System.out.println("regressionFlatMap "+regressionFlatMap);
		
		MapDifference<String, Object> difference = Maps.difference(baselineFlatMap, regressionFlatMap);

		System.out.println("The diss "+difference);
		
		System.out.println("Entries only on the baseline json\n--------------------------");
		difference.entriesOnlyOnLeft()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries only on the regression json\n--------------------------");
		difference.entriesOnlyOnRight()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries differing (baseline, regression)\n--------------------------");
		difference.entriesDiffering()
		          .forEach((key, value) -> System.out.println(key + ": " + value));
		
		JsonNode response1=mapper.readTree("{\r\n"
				+ "    \"member\": {\r\n"
				+ "        \"mbrUid\": \"600546940\",\r\n"
				+ "        \"firstNm\": \"SHANNON\",\r\n"
				+ "        \"lastNm\": \"CALLOWAY\",\r\n"
				+ "        \"dob\": \"1975-03-05\",\r\n"
				+ "        \"tobaccoInd\": \"N\",\r\n"
				+ "        \"genderCd\": [\r\n"
				+ "		{\r\n"
				+ "            \"code\": \"F1\",\r\n"
				+ "            \"name\": \"Female\",\r\n"
				+ "            \"description\": \"Female\"\r\n"
				+ "        },\r\n"
				+ "		{\r\n"
				+ "            \"code\": \"F2\",\r\n"
				+ "            \"name\": \"FemaleLS\",\r\n"
				+ "            \"description\": \"Female low age\"\r\n"
				+ "        }\r\n"
				+ "		]\r\n"
				+ "    }    \r\n"
				+ "}");
		JsonNode response2=mapper.readTree("{\r\n"
				+ "    \"member\": {\r\n"
				+ "        \"mbrUid\": \"600546940\",\r\n"
				+ "        \"firstNm\": \"SHANNON\",\r\n"
				+ "        \"lastNm\": \"CALLOWAY\",\r\n"
				+ "        \"dob\": \"1975-03-05\",\r\n"
				+ "        \"tobaccoInd\": \"N\",\r\n"
				+ "		\"extrafiled\": \"no value\",\r\n"
				+ "        \"genderCd\": [\r\n"
				+ "		{\r\n"
				+ "            \"code\": \"F2\",\r\n"
				+ "            \"name\": \"FemaleLS\",\r\n"
				+ "            \"description\": \"Female low age\"\r\n"
				+ "        },\r\n"
				+ "		{\r\n"
				+ "			\"code\": \"F1\",\r\n"
				+ "            \"name\": \"Male\",\r\n"
				+ "            \"description\": \"Female\"   \r\n"
				+ "        }\r\n"
				+ "		]\r\n"
				+ "    }    \r\n"
				+ "}");
		
		
		System.out.println(response1.equals(response2)); // -> false
		Jossons jossons = new Jossons();
		jossons.putDataset("response1", Josson.create(response1));
		jossons.putDataset("response2", Josson.create(response2));
		
		System.out.println(jossons.evaluateQuery("response1 = response2")); //-true
		JsonNode comparedResponse=jossons.evaluateQuery("response1 = response2");
		System.out.println(comparedResponse);
		
		if(comparedResponse.asBoolean()) 
		{
			System.out.println("Both the json responses are same");
		}else {
			System.out.println("Both the json responses are not same");
			
		}
	
		System.out.println("Both the json comparision done");
	}

	

}
