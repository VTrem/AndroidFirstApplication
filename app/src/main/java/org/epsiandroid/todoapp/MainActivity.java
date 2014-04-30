package org.epsiandroid.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.epsiandroid.todoapp.model.StorageHelper;
import org.epsiandroid.todoapp.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        /*SharedPreferences prefs = getSharedPreferences("com.epsi4android.prefs", 0);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt("value", 42);
        ed.commit();
        Log.d("main", "value: " + prefs.getInt("value", 0));*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        StorageHelper helper;
        ArrayList<String> dataList;
        List<Todo> todoList;
        //ArrayAdapter<String> adapter;
        TodoAdapter adapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            helper = new StorageHelper(this.getActivity());

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView txt = (TextView) rootView.findViewById(R.id.text);
            final EditText ed = (EditText) rootView.findViewById(R.id.editText);
            final EditText contentEd = (EditText) rootView.findViewById(R.id.editContent);
            Button btn = (Button) rootView.findViewById(R.id.button);
            ListView lst = (ListView) rootView.findViewById(R.id.listView);
            final Activity act = this.getActivity();

            todoList = helper.getAll();
            dataList = new ArrayList<String>();

            for(Todo t: todoList) {
                dataList.add(t.title);
            }

            final ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this.getActivity(),
                            android.R.layout.simple_list_item_1,
                            dataList);
            lst.setAdapter(adapter);
            btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ed.getText().toString();
                    String content = contentEd.getText().toString();
                    /*SharedPreferences prefs = act.getSharedPreferences("todoapp", 0);
                    String todo = prefs.getString("todolist", "");
                    todo += new String(",") + title;
                    SharedPreferences.Editor prefsEdit = prefs.edit();
                    prefsEdit.putString("todolist", todo);
                    prefsEdit.commit();*/
                    ArrayList<String> data = new ArrayList<String>();
                    data.add(title);
                    adapter.insert(title, 0);
                    helper.addTodo(title, content);
                    reloadData();
                }
            });

            /*adapter = new TodoAdapter(this.getActivity(), todoList);
            lst.setAdapter(adapter);
            btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ed.getText().toString();
                    String content = contentEd.getText().toString();
                    helper.addTodo(title, content);
                    reloadData();
                }
            });*/

            lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Todo todo = (Todo) parent.getItemAtPosition(position);
                    Log.v("todo", "got todo: " + todo.title);
                    helper.deleteTodo(todo);
                    reloadData();
                    return true;
                }
            });
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Todo todo = (Todo) parent.getItemAtPosition(position);
                    Intent intent = new Intent(act, TodoActivity.class);
                    intent.putExtra("id", todo.todo_id);
                    act.startActivity(intent);
                }
            });
            /*lst.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(act, TodoActivity.class);
                    act.startActivity(intent);
                }
            });*/
            return rootView;
        }

        public void reloadData() {
            /*
            todoList.clear();
            todoList = helper.getAll();
            dataList.clear();
            for(Todo t: todoList) {
                dataList.add(t.title);
            }*/
            todoList.clear();
            todoList.addAll(helper.getAll());
            adapter.notifyDataSetChanged();
        }
    }

}
