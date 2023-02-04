package com.example.instruments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //Объявим переменные, которые будут связаны с элементами на странице
    private EditText codeEditText; // объект типа поле ввода текста
    private TextView textInfo; //объект типа поле отображения текста

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Основной метод, запускающийся при открытии окна
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Задали по открытию отображать именно наш описанный слой activity_main

        //Настало время для магии! Свяжем наши переменные и объекты на экране по id
        codeEditText = findViewById(R.id.codeEditText); //поле ввода текста
        Button searchButton = findViewById(R.id.searchButton); //кнопка
        textInfo =  findViewById(R.id.textResult); //поле отображения текста

        //Слушатель (listener) - это объект, уведомляющий о событии. Он ожидает выполнения условий. В данном случае - нажатия на кнопку
        searchButton.setOnClickListener(view -> {
            String code = codeEditText.getText().toString(); //После нажатия кнопки захватываем текст в поле ввода и конвертируем в строку
            String result = searchCodeInCSV(code); //Здесь мы сослались на функцию поиска в таблице, но саму функцию пока не описали!

            //Оформим уведомление об успешности поиска
            if (result == null) { //Если в таблице ничего не нашлось,
                textInfo.setText(null); //очистим блок отображения текста и
                //в виде всплывающего уведомления внизу экрана (toast) уведомим пользователя.
                //Тост должен всплывать на том же экране с текстом "В базе нет данных"
                Toast.makeText(MainActivity.this, "В базе нет данных", Toast.LENGTH_SHORT).show();
            } else { //Если что-то нашлось,
                textInfo.setText(result); // выведем результат в поле отображения текста
                //и сообщим в тосте, что поиск завершился успешно.
                Toast.makeText(MainActivity.this, "Поиск завершён успешно!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String searchCodeInCSV(String code) { //В качестве аргумента мы использовали считанную из поля ввода строку
        InputStream inputStream = getResources().openRawResource(R.raw.data);  //Получим и откроем таблицу data из ресурсов
        //Инициализируем поиск как работу с потоком
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Scanner scanner = new Scanner(reader); //Создали сканер, который будет проверять данные

        while (scanner.hasNextLine()) { //Пока на вход поступают данные, т.е. пока не кончились строки в таблице,
            String line = scanner.nextLine(); //переходим к следующей строке,
            String[] values = line.split(","); //проверяем значения с разбивкой на столбцы с помощью разделителя-запятой.

            if (values[0].equals(code)) { //Если значение в первом столбце (индексы считаем с 0) совпало с введённым,
                return values[1]; //возвращаем значение описания из второго столбца.
            }
        }
        return null; //Если ничего не получилось, ничего не возвращаем.
    }
}