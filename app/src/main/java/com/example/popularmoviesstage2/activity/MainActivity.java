package com.example.popularmoviesstage2.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.adapter.FavMovieAdapter;
import com.example.popularmoviesstage2.adapter.MovieAdapter;
import com.example.popularmoviesstage2.data.MainViewModel;
import com.example.popularmoviesstage2.data.PopularMoviesViewModel;
import com.example.popularmoviesstage2.data.TopRatedMoviesViewModel;
import com.example.popularmoviesstage2.model.Movie;
import com.example.popularmoviesstage2.model.MovieResponse;
import com.example.popularmoviesstage2.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String MENU_SELECTED = "selected";
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView mRvMovies;
    private String API_KEY, mSortType;
    private MainViewModel mainViewModel;
    private PopularMoviesViewModel popularMoviesViewModel;
    private TopRatedMoviesViewModel topRatedMoviesViewModel;
    private int selected = -1;
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        API_KEY = this.getResources().getString(R.string.api_key);


        if (savedInstanceState != null) {
            mSortType = savedInstanceState.getString("sort_type");
            selected = savedInstanceState.getInt(MENU_SELECTED);

        } else {
            mSortType = "popular";
        }


        if (TextUtils.isEmpty(mSortType)) {
            mSortType = "popular";
        }


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        popularMoviesViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);

        topRatedMoviesViewModel = ViewModelProviders.of(this).get(TopRatedMoviesViewModel.class);


        if (mSortType.equals("popular"))
            getSupportActionBar().setTitle(R.string.most_popular_menu_title);
        else if (mSortType.equals("top_rated"))
            getSupportActionBar().setTitle(R.string.top_rated_menu_title);
        else if (mSortType.equals("favorite"))
            getSupportActionBar().setTitle(R.string.favorite_menu_title);

        if (NetworkUtils.isNetworkAvailable(this)) {

            switch (mSortType) {
                case "popular":
                    popularMoviesViewModel.getPopularMovies(API_KEY).observe(this, new Observer<List<MovieResponse>>() {
                        @Override
                        public void onChanged(@Nullable List<MovieResponse> movieResponses) {
                            if (mRvMovies != null) {
                                mRvMovies.setHasFixedSize(true);
                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                                mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                                mRvMovies.setAdapter(new MovieAdapter(movieResponses, MainActivity.this));

                            }
                        }
                    });
                    break;
                case "top_rated":

                    topRatedMoviesViewModel.getTopRatedMovies(API_KEY).observe(this, new Observer<List<MovieResponse>>() {
                        @Override
                        public void onChanged(@Nullable List<MovieResponse> movieResponses) {
                            if (mRvMovies != null) {
                                mRvMovies.setHasFixedSize(true);
                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                                mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                                mRvMovies.setAdapter(new MovieAdapter(movieResponses, MainActivity.this));
                            }
                        }
                    });
                    break;
                case "favorite":
                    getFavorites();
                    break;
            }
        } else {

            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

        }

    }

    private void initUI() {

        mRvMovies = findViewById(R.id.rvmovies);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void getFavorites() {

        mainViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                if (mRvMovies != null) {
                    mRvMovies.setHasFixedSize(true);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                    mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                    mRvMovies.setAdapter(new FavMovieAdapter(movies, MainActivity.this));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby_menu, menu);


        if (selected == -1) {
            return true;
        }

        switch (selected) {
            case R.id.most_popular:
                menuItem = menu.findItem(R.id.most_popular);
                menuItem.setChecked(true);
                break;

            case R.id.top_rated:
                menuItem = menu.findItem(R.id.top_rated);
                menuItem.setChecked(true);
                break;

            case R.id.favorite:
                menuItem = menu.findItem(R.id.favorite);
                menuItem.setChecked(true);
                break;
        }

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.most_popular:
                item.setChecked(!item.isChecked());
                if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
//                    editor=sharedPreferences.edit();
//                    editor.putString(Constant.SHARED_PREF_NAME,"popular");
//                    editor.apply();

                    getSupportActionBar().setTitle(R.string.most_popular_menu_title);

                    mSortType = "popular";

                    selected = id;

                    Toast.makeText(this, "Sorting movies by popularity", Toast.LENGTH_SHORT).show();

                    popularMoviesViewModel.getPopularMovies(API_KEY).observe(this, new Observer<List<MovieResponse>>() {
                        @Override
                        public void onChanged(@Nullable List<MovieResponse> movieResponses) {


                            if (mRvMovies != null) {
                                mRvMovies.setHasFixedSize(true);
                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                                mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                                mRvMovies.setAdapter(new MovieAdapter(movieResponses, MainActivity.this));
                            }
                        }
                    });

                } else
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.top_rated:
                item.setChecked(!item.isChecked());
                if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {

//                    editor=sharedPreferences.edit();
//                    editor.putString(Constant.SHARED_PREF_NAME,"top_rated");
//                    editor.apply();

                    getSupportActionBar().setTitle(R.string.top_rated_menu_title);

                    selected = id;

                    mSortType = "top_rated";

                    Toast.makeText(this, "Sorting movies by top ratings", Toast.LENGTH_SHORT).show();

                    topRatedMoviesViewModel.getTopRatedMovies(API_KEY).observe(this, new Observer<List<MovieResponse>>() {
                        @Override
                        public void onChanged(@Nullable List<MovieResponse> movieResponses) {


                            if (mRvMovies != null) {
                                mRvMovies.setHasFixedSize(true);
                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                                mRvMovies.setLayoutManager(staggeredGridLayoutManager);
                                mRvMovies.setAdapter(new MovieAdapter(movieResponses, MainActivity.this));
                            }
                        }
                    });

                } else
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favorite:
                item.setChecked(!item.isChecked());
                if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {

//                    editor=sharedPreferences.edit();
//                    editor.putString(Constant.SHARED_PREF_NAME,"favorite");
//                    editor.apply();

                    getSupportActionBar().setTitle(R.string.favorite_menu_title);

                    mSortType = "favorite";

                    selected = id;


                    Toast.makeText(this, "Sorting movies by favorites", Toast.LENGTH_SHORT).show();
                    getFavorites();
                } else
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sort_type", mSortType);
        outState.putInt(MENU_SELECTED, selected);
    }
}



