import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather {
	String response, city, stateAbbr, dataType, feature, request ;
	String key = "f58a4e9128db7a73";
	String endPoint = "http://api.wunderground.com/api/";
	String[][] hourlyWeather = new String[36][4];
	
	public Weather(String a, String b, String c, String d) throws IOException {
		feature = a;
		city = b;
		stateAbbr = c;
		dataType = d;
		request = endPoint+key+"/"+feature+"/q/"+stateAbbr+"/"+city+"."+dataType;
		HTTP hourlyWeather = new HTTP(request,"GET");
		response=hourlyWeather.getResponse();
	}
	
	public void parseJSON() {

        JsonElement jelement = new JsonParser().parse(response);
        JsonObject  jobject = jelement.getAsJsonObject();
        JsonArray hourly_forecasts = jobject.get("hourly_forecast").getAsJsonArray();
        for (int i=0; i<hourly_forecasts.size(); i++) {
        	JsonObject hourly_forecast = hourly_forecasts.get(i).getAsJsonObject();
        	JsonObject FCTTIME = hourly_forecast.get("FCTTIME").getAsJsonObject();
        	
        	hourlyWeather[i][0] =  //Date
        				FCTTIME.get("weekday_name").getAsString()+", "+
        				FCTTIME.get("month_name").getAsString()+" "+
        				FCTTIME.get("mday_padded").getAsString()+" "+
        				FCTTIME.get("year").getAsString()
        				;
        	hourlyWeather[i][1]=FCTTIME.get("civil").getAsString(); //Hour
        	JsonObject temp = hourly_forecast.get("temp").getAsJsonObject();
        	hourlyWeather[i][2] = temp.get("english").getAsString(); //Temperature
        	hourlyWeather[i][3] = //Date YYYY-MM-DD
        			FCTTIME.get("year").getAsString()+"-"+
        			FCTTIME.get("mon_padded").getAsString()+"-"+
        			FCTTIME.get("mday_padded").getAsString()
        			;
        }        	
	}
               
	
	
	public void printHourlyWeatherHTML() {
		System.out.println("<div class=\"w3-col l4\">");
		for (int i =0;i<hourlyWeather.length;i++) {
			if (i==0||hourlyWeather[i][1].equals("12:00 AM")) {
				System.out.println("  <div class=\"w3-card-4 w3-margin w3-white\">");
				System.out.println("    Weather");
				System.out.println("    <div class=\"w3-container w3-white\">");
				System.out.println("      <h4><b><a href=\"https://www.wunderground.com/hourly/us/"
				+stateAbbr+"/"+city+"/date/"+hourlyWeather[i][3]+"\" target=\"_blank\">"+hourlyWeather[i][0]+"</a></b></h4>");
				System.out.println("      <ul>");
			}
			System.out.println("        <li>"+hourlyWeather[i][1]+": "+hourlyWeather[i][2]+"\u00b0"+"F"+"</li>");
			if (i==35||hourlyWeather[i][1].equals("11:00 PM")) {
				System.out.println("      </ul>");
				System.out.println("    </div>");
				System.out.println("  </div>");
			}
			if (i==35) {
				System.out.println("</div>");
			}
		}
	}

}
