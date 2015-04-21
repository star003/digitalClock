package com.example.digitalclock;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class gisFromSite {
	/*
	TOWN       информация о пункте прогнозирования:
	Index      уникальный пятизначный код города
	Sname    закодированное название города
	Latitude   широта в целых градусах
	Longitude   долгота в целых градусах
	FORECAST     информация о сроке прогнозирования:
	day, month, year   дата, на которую составлен прогноз в данном блоке
	hour        местное время, на которое составлен прогноз
	tod         время суток, для которого составлен прогноз: 0 - ночь 1 - утро, 2 - день, 3 - вечер
	weekday   день недели, 1 - воскресенье, 2 - понедельник, и т.д.
	predict      заблаговременность прогноза в часах
	PHENOMENA    атмосферные явления:
	cloudiness       облачность по градациям:  0 - ясно, 1- малооблачно, 2 - облачно, 3 - пасмурно
	
	precipitation    тип осадков: 4 - дождь, 5 - ливень, 6,7 – снег, 8 - гроза, 9 - нет данных, 10 - без осадков
	rpower            интенсивность осадков, если они есть. 0 - возможен дождь/снег, 1 - дождь/снег
	spower            вероятность грозы, если прогнозируется: 0 - возможна гроза, 1 - гроза
	PRESSURE        атмосферное давление, в мм.рт.ст.
	TEMPERATURE     температура воздуха, в градусах Цельсия
	WIND     приземный ветер
	min, max          минимальное и максимальное значения средней скорости ветра, без порывов
	direction          направление ветра в румбах, 0 - северный, 1 - северо-восточный,  и т.д.
	RELWET          относительная влажность воздуха, в %
	HEAT            комфорт - температура воздуха по ощущению одетого по сезону человека, выходящего на улицу
	*/
	
	static ArrayList<String> otvet =new ArrayList<String>();
	static String rez = "";
	static String rz = "\n";
	static List<String> weekday  		= Arrays.asList("","воскресенье","понедельник","вторник","среда","четверг","пятница","суббота");
	static List<String> weekdaySh  		= Arrays.asList("","вс","пн","вт","ср","чт","пт","сб");
	static List<String> mont  			= Arrays.asList("Январь","Февраль","Март","Апрель","Май","Июнь"
														,"Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь");
	static List<String> tod  			= Arrays.asList("ночь","утро","день","вечер");
	static List<String> cloudiness 		= Arrays.asList("ясно","малооблачно","облачно","пасмурно");
	static List<String> precipitation  	= Arrays.asList("","","","","дождь","ливень","снег","снег","гроза","нет данных","без осадков");
	static List<String> direction  		= Arrays.asList("Северный","Северо-Восточный","Восточный","Юго-Восточный","Южный","Юго-Западный","Западный","Северо-Западный");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		прогноз погоды на следующее 1/4 дня (для вариант 1)
	//	пример:
	//		getPrognoz()
	static String getPrognoz() throws IOException {
		
		Document doc  = Jsoup.connect("http://informer.gismeteo.ru/xml/27225_1.xml").get();
		Elements FORECAST = doc.select("FORECAST");
		//System.out.println(FORECAST.id());
			rez = rez.concat((FORECAST.attr("day"))+"-");
			rez = rez.concat(FORECAST.attr("month")+"-");
			rez = rez.concat(FORECAST.attr("year")+" ");
			rez = rez.concat(FORECAST.attr("hour")+" ч. ");
			rez = rez.concat(tod.get(Integer.valueOf(FORECAST.attr("tod")))+" ");
			rez = rez.concat(weekday.get(Integer.valueOf(FORECAST.attr("weekday")))+rz);
		Elements PHENOMENA = doc.select("PHENOMENA");
			rez = rez.concat("Облачность: ");
			rez = FORECAST.attr("cloudiness").equals("") ? rez.concat(cloudiness.get(0))+rz : rez.concat(cloudiness.get(Integer.valueOf(FORECAST.attr("cloudiness")))+rz);
			rez = rez.concat("Осадки: ");
			rez = rez.concat(precipitation.get(Integer.valueOf(PHENOMENA.attr("precipitation")))+rz);
			if (PHENOMENA.attr("precipitation").equals("10")==false) {
				rez = rez.concat(PHENOMENA.attr("rpower")+rz);
				rez = rez.concat(PHENOMENA.attr("spower")+rz);
			}
			rez = rez.concat("атм. давл. от : ");
		
		Elements PRESSURE  = doc.select("PRESSURE");
			rez = rez.concat(PRESSURE .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(PRESSURE .attr("max")+rz);
			rez = rez.concat("температура : ");
		Elements TEMPERATURE   = doc.select("TEMPERATURE");
			rez = rez.concat(TEMPERATURE .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(TEMPERATURE .attr("max")+rz);
			rez = rez.concat("Ветер от: ");
		Elements WIND   = doc.select("WIND");
			rez = rez.concat(WIND .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(WIND .attr("max"));
			rez = rez.concat("м/с ,");
			rez = rez.concat(direction.get(Integer.valueOf(WIND.attr("direction")))+rz);
			rez = rez.concat("Ощущения: ");
		Elements HEAT   = doc.select("HEAT");
			rez = rez.concat(HEAT .attr("min"));
			rez = rez.concat("...");
			rez = rez.concat(HEAT .attr("max")+rz);
		return rez;
	}//static String getPrognoz()
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		вернет строку с распознанным тэгом, для второго варианта
	//	параметры:
	//		String ish	-входящая строка к которой прицепляем
	//		String tag	-имя тега который распознаем , например "PHENOMENA"
	//		String val	-атрибут который обрабатываем day="08"
	static String processingData(String ish, String tag,String val) {
		String atpName 	= val.split("=")[0];
		String atrVal 	= val.split("=")[1].replaceAll("\"", "");
		if (tag.equals("FORECAST")) {
			ish = ish.concat(atpName.equals("day") 		? atrVal+"-"	:	"");
			ish = ish.concat(atpName.equals("month") 	? atrVal+"-"	:	"");
			ish = ish.concat(atpName.equals("year") 	? atrVal+" "	:	"");
			ish = ish.concat(atpName.equals("hour") 	? atrVal+" ч. "	:	"");
			ish = ish.concat(atpName.equals("tod") 		? tod.get(Integer.valueOf(atrVal))+" "	:	"");
			ish = ish.concat(atpName.equals("weekday") 	? weekday.get(Integer.valueOf(atrVal))+rz	:	"");
		}
		if (tag.equals("PHENOMENA")) {
			if (atpName.equals("cloudiness")){
				ish = ish.concat(atrVal.equals("") 
						? "Облачность: "+cloudiness.get(0)+rz 
								: "Облачность: "+cloudiness.get(Integer.valueOf(atrVal))+rz);
			}
			if (atpName.equals("precipitation")){
				ish = ish.concat("Осадки: "+precipitation.get(Integer.valueOf(atrVal))+rz);
			}
		}
		if (tag.equals("PRESSURE")) {
			ish = ish.concat(atpName.equals("min") ? "..."+atrVal +rz: "");
			ish = ish.concat(atpName.equals("max") ? "атм. давл.: "+atrVal : "");
		}
		if (tag.equals("TEMPERATURE")) {
			ish = ish.concat(atpName.equals("min") ? "..."+atrVal+rz : "");
			ish = ish.concat(atpName.equals("max") ? "темп.: "+atrVal : "");
		}
		if (tag.equals("WIND")) {
			ish = ish.concat(atpName.equals("min") ? "Ветер: "+atrVal : "");
			ish = ish.concat(atpName.equals("max") ? "..."+atrVal : "");
			ish = ish.concat(atpName.equals("direction") ? " , "+direction.get(Integer.valueOf(atrVal))+rz : "");
		}
		if (tag.equals("HEAT")) {
			ish = ish.concat(atpName.equals("min") ? "ощущения: "+atrVal : "");
			ish = ish.concat(atpName.equals("max") ? "..."+atrVal : "");
		}	
		return ish;
	}//static String processingData(String ish, String tag,String val)
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		перебор атрибутов и значений атрибутов внутри текущего прогноза
	//	параметры:
	//	Document doc- документ
	//	String tag	- имя секции
	//	int lev		- номер секции по порядку
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
	//	описание:
	//		другой (второй) вариант прогноза
	//	параметры:
	//		прогнозы сложены в список 
	//	пример:
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		данные с моего датчика
	//	пример:
	//		readMy()
	static String[] readMy() throws IOException {
		String itg = "";
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
			itg = itg.concat("мин: "+x[2] +" тек:");
			itg = itg.concat(x[0] +" макс: ");
			itg = itg.concat(x[4] +" ");
			itg = itg.concat(x[6] );
		}
		else {
			itg ="нет данных";
		}
		
		return x;
	}//static String readMy() throws IOExceptio
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		вернет параметры захода - восхода солнца и 
	//		продолжительности дня
	//	результат:
	//		0	-Восход
	//		1	-05:27
	//		2	-Заход
	//		3	-19:25 
	//		4	-Долгота 
	//		5	-13:58
	public static String[] getAstronomy() throws IOException{
		String[] x = null;
		Document doc  = Jsoup.connect("http://www.gismeteo.ru/city/legacy/4298/").get();
		Elements sun = doc.select("ul.sun");
		x = sun.text().replaceAll("", "").split(" ");
		return x;
	}//public static String[] getAstronomy()
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		вернет список с параметрами текущей даты
	//	результат:
	//		0-2015 год
	//		1-04	месяц
	//		2-09	день
	//		3-10	час
	//		4-40	минута
	//		5-55	секунда
	//		6-Апрель назв.мес
	//		7-чт день недели
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
	    			};
	    return x;		
	}//public String[] getCurrData()
	
	///////////////////////////////////////////////////////////////////////////////////
	
	static ArrayList<String> getPokaz(Document doc,String prm){
		int i=0;
		ArrayList<String> x = new ArrayList<String>();
		Elements tm = doc.select(prm);
		for(Element v:tm) {
			if (i==13) {
				break;
			}
			x.add(v.text());
			i++;
		}
		return x;
	}//static ArrayList<String> getPokaz(Document doc,String prm)

	///////////////////////////////////////////////////////////////////////////////////

	static ArrayList<String> getImg(Document doc,String prm){
		ArrayList<String> x = new ArrayList<String>();
		Elements cloudness = doc.select(prm);
		Elements tm = cloudness.select("img");
		for(Element v:tm) {
			x.add((v.attr("src")));
		}
		return x;
	}//static ArrayList<String> getImg(Document doc,String prm)

	///////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		граб прогноза погоды с гисметео
	//	пример:
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
		x.add(getPokaz(doc,"span.value.m_temp.c"));
		x.add(getPokaz(doc,"span.value.m_press.torr"));
		x.add(getPokaz(doc,"dt.wicon"));
		x.add(getPokaz(doc,"span.value.m_wind.ms"));
		x.add(getPokaz(doc,"span.value.m_temp.c"));
		return x;
	}//static ArrayList<ArrayList<String>> grabGismeteo() throws IOException

	///////////////////////////////////////////////////////////////////////////////////
	//	описание:
	//		строит календарь на текущий месяц
	//	результат:
	//		ArrayList<String> 
	//	пример:
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
	public static void main(String[] args) throws IOException {
		System.out.println(readMy());
	}//public static void main(String[] args) throws IOException

}//public class gisFromSite
