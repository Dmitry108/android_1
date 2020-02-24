package ru.geekbrains.fragmentmanager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private CheckBox isBackStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создадим фрагменты
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        //обработка кнопок
        Button add1 = findViewById(R.id.add1);
        add1.setOnClickListener(new ListenerOnAdd(fragment1));

        Button add2 = findViewById(R.id.add2);
        add2.setOnClickListener(new ListenerOnAdd(fragment2));

        Button add3 = findViewById(R.id.add3);
        add3.setOnClickListener(new ListenerOnAdd(fragment3));

        Button remove1 = findViewById(R.id.remove1);
        remove1.setOnClickListener(new ListenerOnRemove(fragment1));

        Button remove2 = findViewById(R.id.remove2);
        remove2.setOnClickListener(new ListenerOnRemove(fragment2));

        Button remove3 = findViewById(R.id.remove3);
        remove3.setOnClickListener(new ListenerOnRemove(fragment3));

        Button replace1 = findViewById(R.id.replace1);
        replace1.setOnClickListener(new ListenerOnReplace(fragment1));

        Button replace2 = findViewById(R.id.replace2);
        replace2.setOnClickListener(new ListenerOnReplace(fragment2));

        Button replace3 = findViewById(R.id.replace3);
        replace3.setOnClickListener(new ListenerOnReplace(fragment3));

        isBackStack = findViewById(R.id.checkBox);

        // Обработка нашей кнопки "Назад"
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    // При написании анонимного класса слушателя кнопки было замечено, что при этом возникает
    // много дублирующего кода. Поэтому было решено вынести этот код в отдельный класс.
    // Сравните с:
    // add1.setOnClickListener(new View.OnClickListener(){
    //    @Override
    //    public void onClick(View view) {
    //        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    //        fragmentTransaction.add(R.id.fragment_container, fragment);
    //        fragmentTransaction.commit();
    //    }
    //  });
    private class ListenerOnAdd implements View.OnClickListener{

        private final Fragment fragment;

        ListenerOnAdd(Fragment fragment){
            this.fragment = fragment;
        }

        @Override
        public void onClick(View v) {
            addFragment();
        }

        // Добавить фрагмент
        private void addFragment(){
            //Добавлено условие: если фрагмент уже существует, то, вызывается событие замены
            if (!fragment.isAdded()){
                // Открыть транзакцию
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                // Добавить фрагмент
                fragmentTransaction.add(R.id.fragment_container, fragment);
                // Добавить фрагмент в стек обратного вызова
                if (isBackStack.isChecked())
                    fragmentTransaction.addToBackStack("");
                // Закрыть транзакцию
                fragmentTransaction.commit();
            } else {
                (new ListenerOnReplace(fragment)).onClick(fragment.getView());
            }
        }
    }

    private class ListenerOnRemove implements View.OnClickListener{

        private final Fragment fragment;

        ListenerOnRemove(Fragment fragment){
            this.fragment = fragment;
        }

        @Override
        public void onClick(View v) {
            removeFragment();
        }

        // Удалить фрагмент
        private void removeFragment(){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    private class ListenerOnReplace implements View.OnClickListener{

        private final Fragment fragment;

        ListenerOnReplace(Fragment fragment){
            this.fragment = fragment;
        }

        @Override
        public void onClick(View v) {
            replaceFragment();
        }

        // Заменить фрагмент
        private void replaceFragment(){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}
