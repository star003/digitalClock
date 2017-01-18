package com.example.digitalclock;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class gisFromSite {
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	/*
	TOWN       ���������� � ������ ���������������:
	Index      ���������� ����������� ��� ������
	Sname    �������������� �������� ������
	Latitude   ������ � ����� ��������
	Longitude   ������� � ����� ��������
	FORECAST     ���������� � ����� ���������������:
	day, month, year   ����, �� ������� ��������� ������� � ������ �����
	hour        ������� �����, �� ������� ��������� �������
	tod         ����� �����, ��� �������� ��������� �������: 0 - ���� 1 - ����, 2 - ����, 3 - �����
	weekday   ���� ������, 1 - �����������, 2 - �����������, � �.�.
	predict      ������������������ �������� � �����
	PHENOMENA    ����������� �������:
	cloudiness       ���������� �� ���������:  0 - ����, 1- �����������, 2 - �������, 3 - ��������
	
	precipitation    ��� �������: 4 - �����, 5 - ������, 6,7 � ����, 8 - �����, 9 - ��� ������, 10 - ��� �������
	rpower            ������������� �������, ���� ��� ����. 0 - �������� �����/����, 1 - �����/����
	spower            ����������� �����, ���� ��������������: 0 - �������� �����, 1 - �����
	PRESSURE        ����������� ��������, � ��.��.��.
	TEMPERATURE     ����������� �������, � �������� �������
	WIND     ��������� �����
	min, max          ����������� � ������������ �������� ������� �������� �����, ��� �������
	direction          ����������� ����� � ������, 0 - ��������, 1 - ������-���������,  � �.�.
	RELWET          ������������� ��������� �������, � %
	HEAT            ������� - ����������� ������� �� �������� ������� �� ������ ��������, ���������� �� �����
	*/
	
	static ArrayList<String> otvet =new ArrayList<String>();
	static String rez = "";
	static String rz = "\n";
	static List<String> weekday  		= Arrays.asList("","�����������","�����������","�������","�����","�������","�������","�������");
	static List<String> weekdaySh  		= Arrays.asList("","��","��","��","��","��","��","��");
	static List<String> mont  			= Arrays.asList("������","�������","����","������","���","����"
														,"����","������","��������","�������","������","�������");
	static List<String> tod  			= Arrays.asList("����","����","����","�����");
	static List<String> cloudiness 		= Arrays.asList("����","�����������","�������","��������");
	static List<String> precipitation  	= Arrays.asList("","","","","�����","������","����","����","�����","��� ������","��� �������");
	static List<String> direction  		= Arrays.asList("��������","������-���������","���������","���-���������","�����","���-��������","��������","������-��������");
	static String this_marker 	= "DashBoard";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������� ������ �� ��������� 1/4 ��� (��� ������� 1)
	//	������:
	//		getPrognoz()
	static String getPrognoz() throws IOException {
		
		Document doc  = Jsoup.connect("http://informer.gismeteo.ru/xml/27225_1.xml").get();
		Elements FORECAST = doc.select("FORECAST");
		//System.out.println(FORECAST.id());
			rez = rez.concat((FORECAST.attr("day"))+"-");
			rez = rez.concat(FORECAST.attr("month")+"-");
			rez = rez.concat(FORECAST.attr("year")+" ");
			rez = rez.concat(FORECAST.attr("hour")+" �. ");
			rez = rez.concat(tod.get(Integer.valueOf(FORECAST.attr("tod")))+" ");
			rez = rez.concat(weekday.get(Integer.valueOf(FORECAST.attr("weekday")))+rz);
		Elements PHENOMENA = doc.select("PHENOMENA");
			rez = rez.concat("����������: ");
			rez = FORECAST.attr("cloudiness").equals("") ? rez.concat(cloudiness.get(0))+rz : rez.concat(cloudiness.get(Integer.valueOf(FORECAST.attr("cloudiness")))+rz);
			rez = rez.concat("������: ");
			rez = rez.concat(precipitation.get(Integer.valueOf(PHENOMENA.attr("precipitation")))+rz);
			if (PHENOMENA.attr("precipitation").equals("10")==false) {
				rez = rez.concat(PHENOMENA.attr("rpower")+rz);
				rez = rez.concat(PHENOMENA.attr("spower")+rz);
			}
			rez = rez.concat("���. ����. �� : ");
		
		Elements PRESSURE  = doc.select("PRESSURE");
			rez = rez.concat(PRESSURE .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(PRESSURE .attr("max")+rz);
			rez = rez.concat("����������� : ");
		Elements TEMPERATURE   = doc.select("TEMPERATURE");
			rez = rez.concat(TEMPERATURE .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(TEMPERATURE .attr("max")+rz);
			rez = rez.concat("����� ��: ");
		Elements WIND   = doc.select("WIND");
			rez = rez.concat(WIND .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(WIND .attr("max"));
			rez = rez.concat("�/� ,");
			rez = rez.concat(direction.get(Integer.valueOf(WIND.attr("direction")))+rz);
			rez = rez.concat("��������: ");
		Elements HEAT   = doc.select("HEAT");
			rez = rez.concat(HEAT .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(HEAT .attr("max")+rz);
		return rez;
	}//static String getPrognoz()
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ ������ � ������������ �����, ��� ������� ��������
	//	���������:
	//		String ish	-�������� ������ � ������� ����������
	//		String tag	-��� ���� ������� ���������� , �������� "PHENOMENA"
	//		String val	-������� ������� ������������ day="08"
	static String processingData(String ish, String tag,String val) {
		String atpName 	= val.split("=")[0];
		String atrVal 	= val.split("=")[1].replaceAll("\"", "");
		if (tag.equals("FORECAST")) {
			ish = ish.concat(atpName.equals("day") 		? atrVal+"-"	:	"");
			ish = ish.concat(atpName.equals("month") 	? atrVal+"-"	:	"");
			ish = ish.concat(atpName.equals("year") 	? atrVal+" "	:	"");
			ish = ish.concat(atpName.equals("hour") 	? atrVal+" �. "	:	"");
			ish = ish.concat(atpName.equals("tod") 		? tod.get(Integer.valueOf(atrVal))+" "	:	"");
			ish = ish.concat(atpName.equals("weekday") 	? weekday.get(Integer.valueOf(atrVal))+rz	:	"");
		}
		if (tag.equals("PHENOMENA")) {
			if (atpName.equals("cloudiness")){
				ish = ish.concat(atrVal.equals("") 
						? "����������: "+cloudiness.get(0)+rz 
								: "����������: "+cloudiness.get(Integer.valueOf(atrVal))+rz);
			}
			if (atpName.equals("precipitation")){
				ish = ish.concat("������: "+precipitation.get(Integer.valueOf(atrVal))+rz);
			}
		}
		if (tag.equals("PRESSURE")) {
			ish = ish.concat(atpName.equals("min") ? "..."+atrVal +rz: "");
			ish = ish.concat(atpName.equals("max") ? "���. ����.: "+atrVal : "");
		}
		if (tag.equals("TEMPERATURE")) {
			ish = ish.concat(atpName.equals("min") ? "..."+atrVal+rz : "");
			ish = ish.concat(atpName.equals("max") ? "����.: "+atrVal : "");
		}
		if (tag.equals("WIND")) {
			ish = ish.concat(atpName.equals("min") ? "�����: "+atrVal : "");
			ish = ish.concat(atpName.equals("max") ? "..."+atrVal : "");
			ish = ish.concat(atpName.equals("direction") ? " , "+direction.get(Integer.valueOf(atrVal))+rz : "");
		}
		if (tag.equals("HEAT")) {
			ish = ish.concat(atpName.equals("min") ? "��������: "+atrVal : "");
			ish = ish.concat(atpName.equals("max") ? "..."+atrVal : "");
		}	
		return ish;
	}//static String processingData(String ish, String tag,String val)
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������� ��������� � �������� ��������� ������ �������� ��������
	//	���������:
	//	Document doc- ��������
	//	String tag	- ��� ������
	//	int lev		- ����� ������ �� �������
	static String r1(Document doc,String tag,int lev){
		rez = "";
		Element root1 = doc.getElementsByTag(tag).get(lev);
		for (int i = 0 ;i<root1.attributes().asList().size();i++) {
			Attribute first1 = root1.attributes().asList().get(i);
			rez = processingData(rez, tag,first1.toString());
		}
		return  rez;
		//otvet.add(rez);
	}//static void r1(Document doc,String tag,int lev)

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ (������) ������� ��������
	//	���������:
	//		�������� ������� � ������ 
	//	������:
	//		getPrognozV2()
	static ArrayList<String> getPrognozV2() throws IOException {
		String rez = "";
		List<String> tagObl	= Arrays.asList("FORECAST","PHENOMENA","PRESSURE","TEMPERATURE","WIND","HEAT");
		Document doc  = Jsoup.connect("http://informer.gismeteo.ru/xml/27225_1.xml").get();
		for (int a1 = 0 ; a1 < doc.getElementsByTag("FORECAST").size() ; a1++) {
			for (String f1:tagObl){
				rez = rez.concat(r1(doc,f1,a1));

			}
			otvet.add(rez);
			rez ="";
		}

		return otvet;
	}//static String getPrognozV2() throws IOException
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String[] readThingSpeakTest() {
		//**�������� ��������� - ��������
	
		return "0;1;2;3;4;5;u".split(";");
		
	}	//readThingSpeakTest
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * @idChanell	- ID ������ �������� 180657
	 * @numCh		- ����� ���� = ������ ���� �������� ��� field2 =  2
	 * @fieldName	- ��� ���� �������� field2
	 * @numPok		- ���������� ��������� (1440 �� 1 ������ ��� 80 �� 15 ����� �������� = �����)
	 * @numForTrend - ������ ����� �� ������� ���������� ��������� ���������
	 */
	public static String[] readThingSpeak(String idChanell,String numCh,String fieldName,String numPok,int numForTrend) {
		
		String itg = "";
		String tek,mn,mx ;
		Log.i(this_marker,"readMy()");		
		
		String url = "https://api.thingspeak.com/channels/"+idChanell+"/fields/"+numCh+".xml?results="+numPok;
		Log.i(this_marker,"URL "+url);
		List<Double> testList=new ArrayList<Double>();
		
		org.jsoup.nodes.Document doc;
		try {
			
			doc = Jsoup.connect(url).get();
			
			
			
		} catch (IOException e1) {
			
			Log.e(this_marker,"������ ������������ � api.thingspeak.com , �������� ��� ����� ? ");
			return "-;-;-;-;-;-;u".split(";");
			
		}
		
		//**�������� ����� ���������� ���������� � �������� ������� � ������� �������� � ��������
		Elements createdAt = doc.select("updated-at");
		Date curDate = new Date();
		Date date =new Date();
		long rr =0; //**������� � �������� ����� ��������� ���������� � ������� ��������
		
		for (Element x:createdAt) {
			
			System.out.println(x.text());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
			try {
				
				date = format.parse(x.text());
				
			} catch (ParseException e) {
				
				Log.e(this_marker,"����� ��������� ���� ����. ��������� � thingspeak.com , �������� ��� ����� ? ");
				return "-;-;-;-;-;-;u".split(";");
				
			}
			
			rr = (curDate.getTime() - (date.getTime()+10800000))/1000;			
									
		}
		
		if (rr>3600) { //**��������� ��� ������ ����. ������ ������
			
			Log.i(this_marker,"cur date "+curDate);
			Log.i(this_marker,"updated-at "+date);
			
			Log.i(this_marker,"rr "+rr);
			
			return "-;-;-;-;-;-;u".split(";");
			
		}
		
		//** ��������� ��������� ��� - ���� ������
		
		Elements metaElements = doc.select(fieldName);
		
		for (Element x:metaElements) {
			
			try{
			
				if (!x.text().equals("") & x.text()!=null & x.text().length()<15) {
				
					testList.add(round(Double.valueOf(x.text()),1));
				
				}
				
			} 
			catch(NumberFormatException e){
				
				Log.e(this_marker,"������ ��������� ������ � thingspeak.com ��������: "+x.text()+ " �� ������������� � �����");
				
			}
						
		}
				
		tek = testList.get(testList.size()-1).toString();
		Log.i(this_marker,"tek "+tek);
		//**������ ������ ���������
		
		int trendInterval = numForTrend; //**�� 10 ������
		List<Double>  tr = testList.subList(
							testList.size()-(testList.size()<trendInterval ?testList.size(): trendInterval)
								, testList.size()-1); //**������ ��������� 10 ���������
		
		Double av  = 0.0; //**������� ����������� �� 10 ����������
		
		for(int i = 0; i < tr.size(); i++) {
			
			av += tr.get(i); //**��������� ���������
			
	    }
				
		av = av/tr.size(); //**�������� ������� ����������� 
		int trendK = 0;
		
		for(int i = 0; i < tr.size(); i++) {
			
			if (av - tr.get(i) < 0 ) { //**���� ����� ������ �� �� ���������
				
				trendK--;
				
			}
			else {
				
				trendK++; //**�� ���������
				
			}
			
	    }
		
		Collections.sort(testList);
		mx = testList.get(testList.size()-1).toString();
		mn = testList.get(0).toString();
		
		if (trendK >0) {
		
			itg = tek+"; ;"+mn+"; ;"+mx+"; ;u";
			
		}	
		
		else if (trendK <0) {
				
				itg = tek+"; ;"+mn+"; ;"+mx+"; ;d";	
			
		}	
		
		else {
			
			itg = tek+"; ;"+mn+"; ;"+mx+"; ;n";
			
		}
		
		Log.i(this_marker,"mn "+mn);
		Log.i(this_marker,"mx "+mx);
		Log.i(this_marker,"itg.split "+itg);
		
		return itg.split(";");
		
	}//readThingSpeak
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static String[] readMyOld() throws UnsupportedEncodingException, IOException {
		
		String itg = "";
		Log.i(this_marker,"readMy()");		
		
		URL url 				= new URL("http://star003.dlinkddns.com/03.php");
        URLConnection conn 		= url.openConnection();
        InputStreamReader rd 	= new InputStreamReader(conn.getInputStream(),"UTF-8");
        StringBuilder allpage 	= new StringBuilder();
        int n 					= 0;
        char[] buffer 			= new char[40000];
        while (n >= 0) {
            n = rd.read(buffer, 0, buffer.length);
            if (n > 0) {
                allpage.append(buffer, 0, n).append("\n");
            }
        }
		String[] x = allpage.toString().split("<br>");
		if (x.length>6) {
			itg = itg.concat("���: "+x[2] +" ���:");
			itg = itg.concat(x[0] +" ����: ");
			itg = itg.concat(x[4] +" ");
			itg = itg.concat(x[6] );
		}
		else {
			itg ="��� ������";
		}
		
		return x;
		
	}//readMyOld
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ � ����� �������
	//	������:
	//		readMy()
	static String[] readMy() throws IOException, ParseException {
	/*
	 * 180657
	 * 1
	 * 80
	 * field1	
	 */
		return readThingSpeak("180657","1","field1","80",5);
		
		
	}//static String readMy() throws IOExceptio
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ ��������� ������ - ������� ������ � 
	//		����������������� ���
	//	���������:
	//		0	-������
	//		1	-05:27
	//		2	-�����
	//		3	-19:25 
	//		4	-������� 
	//		5	-13:58
	public static String[] getAstronomy() throws IOException{
		String[] x = null;
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/legacy/4298/").get();
		Elements sun = doc.select("ul.sun");
		x = sun.text().replaceAll("", "").split(" ");
		return x;
	}//public static String[] getAstronomy()
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ ������ � ����������� ������� ����
	//	���������:
	//		0-2015 ���
	//		1-04	�����
	//		2-09	����
	//		3-10	���
	//		4-40	������
	//		5-55	�������
	//		6-������ ����.���
	//		7-�� ���� ������
	//		8-�� ���� ������ ���������
	public static String[] getCurrData(){
		Calendar currentTime = Calendar.getInstance();
	    String[] x = {String.valueOf(currentTime.get(1))
	    			,(currentTime.get(2) + 1 >= 10 ? String.valueOf(currentTime.get(2) + 1) : "0"+String.valueOf(currentTime.get(2) + 1))
	    			,(currentTime.get(5) >=10 ? String.valueOf(currentTime.get(5))  : "0"+String.valueOf(currentTime.get(5)))
	    			,((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11)))
	    			,((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12)))
	    			,((currentTime.get(13)) >=10 ? String.valueOf(currentTime.get(13))  : "0"+String.valueOf(currentTime.get(13)))
	    			,mont.get(currentTime.get(2))
	    			,String.valueOf(weekdaySh.get(currentTime.get(Calendar.DAY_OF_WEEK)))
	    			,String.valueOf(weekday.get(currentTime.get(Calendar.DAY_OF_WEEK)))
	    			};
	    return x;		
	}//public String[] getCurrData()
	
	///////////////////////////////////////////////////////////////////////////////////
	
	static ArrayList<String> getPokaz(Document doc,String prm){
		//int i=0;
		ArrayList<String> x = new ArrayList<String>();
		Elements tm = doc.select(prm);
		for(Element v:tm) {
			
			x.add(v.text());
			//i++;
		}
		return x;
	}//static ArrayList<String> getPokaz(Document doc,String prm)

	///////////////////////////////////////////////////////////////////////////////////

	static ArrayList<String> getImg(Document doc,String prm){
		ArrayList<String> x = new ArrayList<String>();
		Elements cloudness = doc.select(prm);
		Elements tm = cloudness.select("img");
		for(Element v:tm) {
			x.add("http:"+v.attr("src"));
		}
		return x;
	}//static ArrayList<String> getImg(Document doc,String prm)

	///////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		���� �������� ������ � ��������
	//	������:
	//public static void main(String[] args) throws IOException {
	//	ArrayList<ArrayList<String>> x =grabGismeteo();
	//	for(ArrayList<String> a:x){
	//		for(String h:a){
	//			System.out.print(h);
	//			System.out.print("	");
	//		}
	//		System.out.println("");
	//	}
	//System.out.println(m_tempO.get(0));
	//} //public static void main(String[] args) throws IOException
	//
	static ArrayList<ArrayList<String>> grabGismeteo() throws IOException{
		ArrayList<ArrayList<String>> x = new ArrayList<ArrayList<String>>();
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/legacy/4298/").get();
		x.add(getPokaz(doc,"th.df,th.current"));
		x.add(getImg(doc,"tr.cloudness"));
		x.add(getImg(doc,"tr.persp"));
		
		ArrayList<String> m1 = new ArrayList<String>();
		ArrayList<String> m2 = new ArrayList<String>();
		ArrayList<String> m = getPokaz(doc,"span.value.m_temp.c");
		for(int i = 0 ; i<m.size();i++) {
			if (i<13) {
				m1.add(m.get(i));
			}
			else if(i>=13 & i<=25) {
				m2.add(m.get(i));
			}
		}
		x.add(m1);
		x.add(getPokaz(doc,"span.value.m_press.torr"));
		x.add(getPokaz(doc,"dt.wicon"));
		x.add(getPokaz(doc,"span.value.m_wind.ms"));
		x.add(m2);
		return x;
	}//static ArrayList<ArrayList<String>> grabGismeteo() throws IOException

	///////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ ��������� �� ������� �����
	//	���������:
	//		ArrayList<String> 
	//	������:
	//		ArrayList<String> sp = buildCalendar();
	//		for (int i = 0 ; i<sp.size() ; i++){
	//	
	//		if(i%7 ==0) {
	//			System.out.println("");
	//		}
	//	
	//		System.out.print(sp.get(i));
	//		System.out.print("	|");
	//	
	//		}
	static public ArrayList<String> buildCalendar(){
		ArrayList<String> sp = new ArrayList<String>();
		Calendar currentTime = Calendar.getInstance();
		int y = currentTime.get(1);
		int m = currentTime.get(2);
		currentTime.clear();
		currentTime.set(y,m,1);
		int startDay = currentTime.get(Calendar.DAY_OF_WEEK)==1 ? 7 : currentTime.get(Calendar.DAY_OF_WEEK)-1;

		for (int i = 1 ; i <= currentTime.getActualMaximum(Calendar.DATE)+startDay-1 ; i++){
			if (i >=startDay){
				sp.add(String.valueOf(i-startDay+1));
			}	
			else {
				sp.add("");
			}
		}
		return sp;
	}//static public ArrayList<String> buildCalendar()

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������� ��������� �������
	//	��������� ������:
	//		0-�����
	//		1-����������,������
	//		2-������ �� ��������
	//		3-��� �����������	
	//		4-��������	
	//		5-����. �����	
	//		6-�������� �����	
	//		7-��������
	static public ArrayList<ArrayList<String>> getHourPrognoz() throws IOException{
		ArrayList<ArrayList<String>> x 	= new ArrayList<ArrayList<String>>();
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/hourly/4298/").get();
		Elements a0 = doc.select("tr.wrow.forecast");
		for (Element a1 : a0){
			ArrayList<String> x1 			= new ArrayList<String>();
			//System.out.println("-------");
			//**time forecast
			Elements a2 = a1.select("th");
			x1.add(a2.text());
			//**������
			Elements a3 = a1.select("img.png");
			x1.add(a3.attr("alt"));
			String ss = a3.attr("src");
			
			x1.add("http:"+ss);
			//**�����������
			Elements a4 = a1.select("span.value.m_temp.c");
			x1.add(a4.get(0).text());
			//**��������
			Elements a5 = a1.select("span.value.m_press.torr");
			x1.add(a5.first().text());
			//**�����
			try{
				Elements a6 = a1.select("dt.wicon.wind1,dt.wicon.wind2,dt.wicon.wind3,dt.wicon.wind4" +
										"dt.wicon.wind5,dt.wicon.wind6,dt.wicon.wind7,dt.wicon.wind8,dt.wicon.wind9");
				x1.add(a6.first().text());
			}
			catch(NullPointerException e) {
				x1.add("-");
			}
			//**���� �����
			Elements a7 = a1.select("span.value.m_wind.ms");
			x1.add(a7.first().text());
			//**��������
			x1.add(a4.get(1).text());
			x.add(x1);
		}	
		return x;
	}//static public void getHourPrognoz() throws IOException
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		������ ����� �������� ������� ������
	static String getCurGismeteoPic() throws IOException{
		Log.i("gisFromSite","getCurGismeteoPic()...����� ");
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/hourly/4298/").get();
		Elements a0 = doc.select("dt.png");
		String ur = a0.attr("style");
		Log.i("gisFromSite","getCurGismeteoPic() ur.substring(ur.indexOf(http),ur.length()-1) ... "+ur);
		Log.i("gisFromSite","return ... "+ur.substring(ur.indexOf("//"),ur.length()-1));
		return "http://"+ur.substring(ur.indexOf("//")+2,ur.length()-1);
	}//static String getCurGismeteoPic() throws IOException
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws IOException, ParseException {
		System.out.println(readMy());
	}//public static void main(String[] args) throws IOException

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	��������:
	//		��������� ���� /���� �� 8 ��������: 0,3,6,9,12,15,18,21 �/
	static public ArrayList<String> getMagnetic() throws IOException{
		ArrayList<String> x1 = new ArrayList<String>();
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/gm/4298/").get();
		Elements a = doc.select("div.gm1,div.gm2,div.gm3,div.gm4,div.gm5,div.gm6,div.gm7,div.gm8");
		for (Element a1 : a) {
			x1.add(a1.text());
		}
		return x1;
	}//static public ArrayList<ArrayList<String>> getMagnetic() throws IOException
	
}//public class gisFromSite
