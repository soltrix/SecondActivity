package ru.geekbrains.soltrix.secondactivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class StartSecondActivity implements View.OnClickListener {

    public final static String TEXT = "Text";
    private final static int rCode = 1;

    private Activity sourceActivity;

    public StartSecondActivity(Activity sourceActivity){
        this.sourceActivity = sourceActivity;
    }

    @Override
    public void onClick(View v) {
        // Формируем посылку
        EditText txt = sourceActivity.findViewById(R.id.editText);
        CheckBox mCheckBoxDetails = sourceActivity.findViewById(R.id.checkBoxDetails);
        CheckBox mCheckBoxSky = sourceActivity.findViewById(R.id.checkBoxSky);
        //EditText num = sourceActivity.findViewById(R.id.editText2);

        Parcel parcel = new Parcel();
        parcel.text = txt.getText().toString();
        parcel.checkDetails = mCheckBoxDetails.isChecked();
        parcel.checkSky = mCheckBoxSky.isChecked();

        //parcel.number = Integer.parseInt(num.getText().toString());

        // Посылка сформирована, отправляем
        Intent intent = new Intent(sourceActivity, SecondActivity.class);
        intent.putExtra(TEXT, parcel);    // Отправляем посылку

        // Возвращаем SecondActivity результат работы MainActivity методом onActivityResult
        sourceActivity.startActivityForResult(intent, rCode);
    }
}