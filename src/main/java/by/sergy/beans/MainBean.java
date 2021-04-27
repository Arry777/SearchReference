package by.sergy.beans;

import by.sergy.constants.Constants;
import lombok.Data;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Класс является обработчиком одного из основных ивентов, а именно анализа введенной ссылки и выдача
* информации о содержащихся ссылках на странице, так же присутствует обработка невалидных данных, которая
* позволяет пользователю понять, что он делает не так
* */


@ManagedBean
@SessionScoped
@Data
public class MainBean implements Serializable {
    private String enteredLine;
    private List<String> urls = new ArrayList<>();

    public void analise() {

        urls.clear(); //очистка от предыдущих данных если такие имеются

        try {
            //Создаем объект url из введенной строки для дальнейшего получения html документа
            //если она выбрасывает exception, блок catch выдает нужный message
            URL url = new URL(enteredLine);

            URLConnection conn = url.openConnection();//коннект
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder html = new StringBuilder();

            while ((line = br.readLine()) != null) { //Заполняем переменную html
                html.append(line);
                line = br.readLine();
            }

            br.close();

            //Объекты паттерна и матчера в данном случае помогут найти
            //все совпадения '<a href="">...</a>', где мы сможем из
            //строки html достать нужную группу
            Pattern pattern = Pattern.compile(Constants.REGEX_A_TAG);
            Matcher matcher = pattern.matcher(html);

            while (matcher.find()) {//поиск совпадений
                try {
                    //здесь мы отсеиваем ссылки которые являются
                    //частью веток сайта проверяемого html документа
                    //повторный блок catch игнорирует ее проход и инициализирует лист дальше
                    new URL(matcher.group(1));

                    if (!urls.contains(matcher.group(1))) {//инициализия листа
                        urls.add(matcher.group(1));
                    }

                } catch (MalformedURLException ignored) {}//игнор невалидной ссылки
            }

        } catch (IOException e) {//введенная ссылка невалидна => push message
            FacesContext.getCurrentInstance().addMessage("inputform:input",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Error: no valid entered line"));
        }
    }

    public void clear() {
        enteredLine = null;
        urls.clear();
    }
}