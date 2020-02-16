package ru.bdim.weather;

// Это Presenter, делаем его на основе паттерна «одиночка».
// Этот паттерн обладает свойством хранить один экземпляр объекта на все приложение.
// Для реализации паттерна «одиночка» надо добавить статическое приватное поле (instance),
// сделать конструктор приватным,
// добавить статический метод, который проверяет, существует ли этот объект в нашем поле,
// если нет, то создает его и возвращает это поле в качестве результата.
// Таким образом, в приложении всегда существует только один объект.
// Класс, реализующий паттерн «одиночка», нельзя наследовать.
public final class MainPresenter {
    //Внутреннее поле, будет хранить единственный экземпляр
    private static MainPresenter instance;
    // Поле для синхронизации
    private static final Object syncObj = new Object();
    // Переменные
    private String city;
    private int cloudiness;
    private int temperature;
    private boolean isTemperature;
    private boolean isCloudiness;
    private boolean isWind;
    private boolean isPressure;
    private boolean isHumidity;

    //сеттеры
    public void setCity(String city) { this.city = city; }
    public void setCloudiness(int cloudiness) { this.cloudiness = cloudiness; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
    public void setIsTemperature(boolean temperature) { isTemperature = temperature; }
    public void setIsCloudiness(boolean cloudiness) { isCloudiness = cloudiness; }
    public void setIsWind(boolean wind) { isWind = wind; }
    public void setIsPressure(boolean pressure) { isPressure = pressure; }
    public void setIsHumidity(boolean humidity) { isHumidity = humidity; }

    //геттеры
    public String getCity() { return city; }
    public int getCloudiness() { return cloudiness; }
    public int getTemperature() { return temperature; }
    public boolean isTemperature() { return isTemperature; }
    public boolean isCloudiness() { return isCloudiness; }
    public boolean isWind() { return isWind; }
    public boolean isPressure() { return isPressure; }
    public boolean isHumidity() { return isHumidity; }

    // Конструктор
    private MainPresenter() {
        //тестовые значения
        temperature = -15;
        cloudiness = 1;
    }

    // Метод, который возвращает экземпляр объекта.
    // Если объекта нет, то создаем его.
    public static MainPresenter getInstance() {
        // Здесь реализована «ленивая» инициализация объекта,
        // то есть, пока объект не нужен, не создаем его.
        synchronized (syncObj) {
            if (instance == null) instance = new MainPresenter();
            return instance;
        }
    }
}