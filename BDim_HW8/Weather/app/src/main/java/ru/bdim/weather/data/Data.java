package ru.bdim.weather.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Data {
    private Coord coord;
    private Weather[] weathers;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private String name;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Weather[] getWeathers() {
        return weathers;
    }

    public void setWeathers(Weather[] weathers) {
        this.weathers = weathers;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//
//    public static void getDatas(String city){
//        HttpsURLConnection urlConnection = null;
//        URL url = new URL(uri); // Указать адрес URI
//        try {
//            urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
//            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//    //        OutputStream out = new BufferedOutputStream(urlConnection.getInputStream());
//            String result = getLines(in);
//            webView.loadData(result, "text/html; charset=utf-8", "utf-8");
//
//        } catch (IOException e){}

//        https://api.openweathermap.org/data/2.5/weather?q=Moscow,RU&appid=7de3f0ac9dcba20434e192866767930e
//        https://api.openweathermap.org/data/2.5/weather?id=524901&appid=
//
//
//        Опционально можно загрузить тело запроса. Если в тело запроса надо включить какие-то объекты, то соединение необходимо конфигурировать при помощи setDoOutput(true). При передаче потоковых данных, надо использовать getOutputStream.
//         // пишем
//        writeStream(out);
//
//        Читаем запрос, обычно заголовок запроса содержит метаданные, такие как, тип контента тела, сессионные куки. Тело запроса может содержать потоковые данные, возвращаемые методом getInputStream.
//
//
//        Закрываем соединение. Уже прочитанный запрос необходимо закрыть, чтобы освободить ресурсы.
//        urlConnection.disconnect();
//

}
