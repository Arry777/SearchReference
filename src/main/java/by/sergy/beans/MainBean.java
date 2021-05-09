package by.sergy.beans;

import by.sergy.impl.MainInterface;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import java.io.*;

import java.util.ArrayList;
import java.util.List;


/*
* Класс является обработчиком одного из основных ивентов, а именно анализа введенной ссылки и выдача
* информации о содержащихся ссылках на странице, так же присутствует обработка невалидных данных, которая
* позволяет пользователю понять, что он делает не так
* */


@ManagedBean
@SessionScoped
@Data
public class MainBean implements Serializable, MainInterface {
    private String enteredLine;
    private List<String> urls = new ArrayList<>();

    @Override
    public void analise() {
        urls.clear(); //очистка от предыдущих данных если такие имеются

        try {
//          получаем html документ
            Document doc = Jsoup.connect(enteredLine).get();
            Elements elements = doc.select("a");//получаем все элементы <a>...</a>

            for (Element elem: elements) {
                //проверяем аттрибут href на пустоту и уникальность в листе
                if (!elem.absUrl("href").equals("") && !urls.contains(elem.absUrl("href"))) {
                    urls.add(elem.absUrl("href"));//добавляем в список
                }
            }

        } catch (IOException e) {//введенная ссылка невалидна => push message
            System.err.println(e.getMessage());
            FacesContext.getCurrentInstance().addMessage("inputform:input",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Error: no valid entered line"));
        }
    }

    @Override
    public void clear() {
        enteredLine = null;
        urls.clear();
    }
    
}