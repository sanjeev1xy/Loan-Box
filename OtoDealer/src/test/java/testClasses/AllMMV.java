package testClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import Utility.Constants;
import Utility.Utils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utilsApi.ApiCall;
import utilsApi.ApiPaths;
import utilsApi.ApiTestUtils;
import utilsApi.ReadExcelFile;

public class AllMMV {

	Map<String, Object[]> makedata = new TreeMap<String, Object[]>();
	Map<String, Object[]> modeldata = new TreeMap<String, Object[]>();
	Map<String, Object[]> versiondata = new TreeMap<String, Object[]>();

	@Test
	public void getAllMMV() {

		HashMap<String, String> requestBody= new HashMap<String, String>();
		requestBody.put("category_id", "1");


		String payload=new Gson().toJson(requestBody);
		Response response=ApiCall.getApiMethod(payload,ApiPaths.all_mmv);

		ApiTestUtils.getAllApiResponse(response);
		ApiTestUtils.checkStatusCode(response, 200);
		ApiTestUtils.getStatusLineAssertTrue(response, "OK");

		String res =response.getBody().asString();
		ApiTestUtils.checkResponse(response, "Success");


		Utils.logPrint(ApiTestUtils.getJsonPath(response).getString("data.make_count"));
		Utils.logPrint(ApiTestUtils.getJsonPath(response).getString("data.model_count"));
		Utils.logPrint(ApiTestUtils.getJsonPath(response).getString("data.version_count"));
		Utils.logPrint(ApiTestUtils.getAValueFromList(response, "data.make.make").toString());
		Utils.logPrint(ApiTestUtils.getAllListdata(response, "data.make").toString());
		//	ApiTestUtils.assertEquals(ApiTestUtils.getJsonPath(response).getString("data.make_count"),
		//			ApiTestUtils.getListSize(response, "data.make.make"));
		//	ApiTestUtils.assertEquals(ApiTestUtils.getJsonPath(response).getString("data.model_count"),
		//			ApiTestUtils.getListSize(response, "data.model.model"));
		//	ApiTestUtils.assertEquals(ApiTestUtils.getJsonPath(response).getString("data.version_count"),
		//			ApiTestUtils.getListSize(response, "data.version.version"));

		JSONObject resObject = new JSONObject(response.asString());

		//  JSONObject obj1 = new JSONObject(obj.get("data.make"));
		JSONObject dataObj = resObject.getJSONObject("data");
		JSONArray jsonArrayMake = dataObj.getJSONArray("make");
		System.out.println("make length::"+jsonArrayMake.length());

		JSONArray jsonArrayModel = dataObj.getJSONArray("model");
		System.out.println("model length::"+jsonArrayModel.length());
		JSONArray jsonArrayVersion = dataObj.getJSONArray("version");
		System.out.println("verion length::"+jsonArrayVersion.length());


		XSSFWorkbook workbook = new XSSFWorkbook(); 

		//Create a blank sheet
		XSSFSheet sheetMake = workbook.createSheet("Make");
		XSSFSheet sheetModel = workbook.createSheet("Model");
		XSSFSheet sheetVersion = workbook.createSheet("Version");
		int rowCount = 0;

		makedata.put(""+ 0, new Object[] {"make_id","make_Name","category_id"});

		for (int i = 0; i < jsonArrayMake.length(); ++i) {

			//  JSONObject objn = new JSONObject();

			int id=jsonArrayMake.getJSONObject(i).getInt("id");
			String MakeName=jsonArrayMake.getJSONObject(i).getString("make");
			int category_id=jsonArrayMake.getJSONObject(i).getInt("category_id");


			System.out.println(jsonArrayMake.getJSONObject(i).get("make"));
			makedata.put(""+1+ i, new Object[] {id,MakeName,category_id});

		}
		for (int i = 0; i < jsonArrayModel.length(); ++i) {

			int id=jsonArrayModel.getJSONObject(i).getInt("id");
			String modelName=jsonArrayModel.getJSONObject(i).getString("m");
			String modelrank=jsonArrayModel.getJSONObject(i).getString("rk");
			int makeid=jsonArrayModel.getJSONObject(i).getInt("mk_id");
			int pmid=jsonArrayModel.getJSONObject(i).getInt("p_m_id");
			int startyear=jsonArrayModel.getJSONObject(i).getInt("s_year");
			int endyear=jsonArrayModel.getJSONObject(i).getInt("e_year");
			int category_id=jsonArrayModel.getJSONObject(i).getInt("category_id");
			System.out.println(jsonArrayModel.getJSONObject(i).get("m"));

			modeldata.put(""+ i, new Object[] {modelName,id,makeid,modelrank,pmid,startyear,endyear,category_id});

		}
		for (int i = 0; i < jsonArrayVersion.length(); ++i) {

			int versionid=jsonArrayVersion.getJSONObject(i).getInt("vn_id");
			int makeid=jsonArrayVersion.getJSONObject(i).getInt("mk_id");
			int modelid=jsonArrayVersion.getJSONObject(i).getInt("md_id");
			int startyear=jsonArrayVersion.getJSONObject(i).getInt("s_year");
			int endyear=jsonArrayVersion.getJSONObject(i).getInt("e_year");
			Object fueltype=jsonArrayVersion.getJSONObject(i).get("f_type");
			Object cbtype=jsonArrayVersion.getJSONObject(i).get("c_b_type");
			Object transmissionType=jsonArrayVersion.getJSONObject(i).get("tms");
			String makeName=jsonArrayVersion.getJSONObject(i).getString("mk");
			String modelName=jsonArrayVersion.getJSONObject(i).getString("md");
			int parent_id=jsonArrayVersion.getJSONObject(i).getInt("p_m_id");
			Object parentname=jsonArrayVersion.getJSONObject(i).get("p_m_n");
			String engine_capacity=jsonArrayVersion.getJSONObject(i).getString("engine_capacity");
			String drivetype=jsonArrayVersion.getJSONObject(i).getString("drive_type");
			String  versionName=jsonArrayVersion.getJSONObject(i).getString("vn");
			int category_id=jsonArrayVersion.getJSONObject(i).getInt("category_id");
			Object yc=jsonArrayVersion.getJSONObject(i).get("yc");
			System.out.println(jsonArrayVersion.getJSONObject(i).get("vn"));
			versiondata.put(""+ i, new Object[] {versionName,versionid,makeid,modelid,startyear,endyear,fueltype,cbtype,transmissionType,makeName,modelName,parent_id,parentname,engine_capacity,drivetype,category_id,yc});

		}
		/////////////////// Writing in Excel Sheet ///////////////////
		ReadExcelFile.createData(makedata,sheetMake);
		ReadExcelFile.createData(modeldata,sheetModel);
		ReadExcelFile.createData(versiondata,sheetVersion);



		try
		{
			//Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(Constants.CreateMMVExcelFile));
			workbook.write(out);
			out.close();
			System.out.println(Constants.CreateMMVExcelFile+" written successfully on disk.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		//////////////////////   read sheet ////////////////////////



		/******************************* Get all the data from email Login api **************************/

		//String dealer_id=ApiTestUtils.getValueFromResponse(loginResponse, "data.user_data.user_id");

		//String Token = ApiTestUtils.getValueFromResponse(loginResponse, "data.token");


	}
	

}

