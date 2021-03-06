package ru.igsanov.refresher;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.igsanov.refresher.adapter.TabAdapter;
import ru.igsanov.refresher.datebase.DBHelper;
import ru.igsanov.refresher.dialog.AddingTaskDilogFragment;
import ru.igsanov.refresher.fragment.CurrentTaskFragment;
import ru.igsanov.refresher.fragment.DoneTaskFragment;
import ru.igsanov.refresher.fragment.SplashFragment;
import ru.igsanov.refresher.model.ModelTask;

public class MainActivity extends AppCompatActivity implements AddingTaskDilogFragment.AddingTaskListener {
    private static final String TAG = "myLogs";
    FragmentManager fragmentManager;
    PreferenceHelper preferenseHelper;
    TabAdapter tabAdapter;
    CurrentTaskFragment currentTaskFragment;
    DoneTaskFragment doneTaskFragment;

    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceHelper.getInstance().init(getApplication());
        preferenseHelper = PreferenceHelper.getInstance();

        dbHelper = new DBHelper(getApplicationContext());

        fragmentManager = getFragmentManager();
        Log.d(TAG, "main activituy");
        runSplash();

        setUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenseHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_splash) {
            //Log.d(TAG, "я здесь");
            item.setChecked(!item.isChecked()); //(item.isChecked());
            preferenseHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runSplash() {
        if (!preferenseHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentManager, 2);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {     //addOnTabSelectedListener   setOnTabSelectedListener
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);

        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDilogFragment();
                addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        Log.d(TAG, "Added");
        currentTaskFragment.addTask(newTask,true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "Task adding cancel.", Toast.LENGTH_LONG).show();
    }
    //@Override
    //public void onTaskDone(Model task) { doneTaskFragment.addTask(task)}

    //@Override
    //public void onTaskRestore(Model task) {
    // currentTaskFragment.addTask(task, false)  }
}
