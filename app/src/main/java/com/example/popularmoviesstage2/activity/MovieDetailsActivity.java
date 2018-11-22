package com.example.popularmoviesstage2.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.adapter.ReviewAdapter;
import com.example.popularmoviesstage2.adapter.TrailersAdapter;
import com.example.popularmoviesstage2.data.MainViewModel;
import com.example.popularmoviesstage2.data.MovieDatabase;
import com.example.popularmoviesstage2.model.Movie;
import com.example.popularmoviesstage2.model.Review;
import com.example.popularmoviesstage2.model.ReviewResponse;
import com.example.popularmoviesstage2.model.TrailersBaseResponse;
import com.example.popularmoviesstage2.model.TrailersResponse;
import com.example.popularmoviesstage2.networking.PopularMoviesAPI;
import com.example.popularmoviesstage2.networking.RestClient;
import com.example.popularmoviesstage2.utils.Constant;
import com.example.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailsActivity extends AppCompatActivity {

    private String mMovieTitle, mReleaseDate, mMovieOverview, mMoviePosterPath, API_KEY;
    private TextView mTxtMovieTitle, mTxtReleaseDate, mTxtMovieRating, mTxtOverview, mTxtDateHeading;
    private ImageView mImgPoster;
    private int mId;
    private Double mMovieRating;
    private RecyclerView mRvTrailers, mRvReviews;
    private NestedScrollView mNsDetails;
    private Button btnAddtoFav;
    private MovieDatabase movieDatabase;
    private Boolean isFav = false;
    private MainViewModel mainViewModel;
    private android.support.v7.widget.Toolbar toolbar;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.button_favorite:
                    if (!isFav)
                        addFav();
                    else
                        removeFav();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initUI();
        getData();
        setData();
        API_KEY = this.getResources().getString(R.string.api_key);

        if (NetworkUtils.isNetworkAvailable(MovieDetailsActivity.this)) {
            getTrailers();
            getReviews();
        } else {
            Snackbar.make(mNsDetails, R.string.no_internet_text, Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void getTrailers() {
        try {
            Retrofit retrofit = RestClient.retrofitService();
            PopularMoviesAPI popularMoviesAPI = retrofit.create(PopularMoviesAPI.class);
            Call<TrailersBaseResponse> trailersBaseResponseCall = popularMoviesAPI.getTrailers(mId, API_KEY);
            trailersBaseResponseCall.enqueue(new Callback<TrailersBaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailersBaseResponse> call, @NonNull Response<TrailersBaseResponse> response) {
                    if (response.isSuccessful()) {
                        TrailersBaseResponse trailersBaseResponse = response.body();
                        List<TrailersResponse> trailersList = trailersBaseResponse.getResults();

                        if (mRvTrailers != null) {
                            mRvTrailers.setHasFixedSize(true);
                            LinearLayoutManager linearLayout = new LinearLayoutManager(MovieDetailsActivity.this);
                            mRvTrailers.setLayoutManager(linearLayout);
                            mRvTrailers.setAdapter(new TrailersAdapter(trailersList, MovieDetailsActivity.this));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TrailersBaseResponse> call, @NonNull Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getReviews() {
        try {


            Retrofit retrofit = RestClient.retrofitService();
            PopularMoviesAPI popularMoviesAPI = retrofit.create(PopularMoviesAPI.class);
            Call<ReviewResponse> reviewResponseCall = popularMoviesAPI.getReviews(mId, API_KEY);
            reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        ReviewResponse reviewResponse = response.body();
                        List<Review> reviewList = reviewResponse.getResults();
                        if (mRvReviews != null) {
                            mRvReviews.setHasFixedSize(true);
                            LinearLayoutManager linearLayout = new LinearLayoutManager(MovieDetailsActivity.this);
                            mRvReviews.setLayoutManager(linearLayout);
                            mRvReviews.setAdapter(new ReviewAdapter(reviewList, MovieDetailsActivity.this));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUI() {
        mTxtMovieTitle = findViewById(R.id.textview_original_title);
        mTxtMovieRating = findViewById(R.id.textview_vote_average);
        mTxtReleaseDate = findViewById(R.id.textview_release_date);
        mTxtOverview = findViewById(R.id.textview_overview);
        mTxtDateHeading = findViewById(R.id.textview_release_date_title);
        mImgPoster = findViewById(R.id.imageview_poster);
        mRvTrailers = findViewById(R.id.trailer_recycler_view);
        mRvReviews = findViewById(R.id.review_recycler_view);
        mNsDetails = findViewById(R.id.nestedscrollview_details);
        btnAddtoFav = findViewById(R.id.button_favorite);
        btnAddtoFav.setOnClickListener(onClickListener);
        toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);


    }

    private void getIsFav() {

        mainViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                for (int i = 0; i < movies.size(); i++) {
                    Movie movie = movies.get(i);
                    if (mId == movie.getId()) {
                        isFav = true;
                        break;
                    }
                }

                if (isFav)
                    btnAddtoFav.setText(R.string.remove_from_favorites_button_title);
                else
                    btnAddtoFav.setText(R.string.add_to_favorites_button_title);
            }
        });


    }

    private void removeFav() {
        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, Constant.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Movie movie = new Movie();
                movie.setId(mId);


                mainViewModel.delete(movie);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnAddtoFav.setText(R.string.add_to_favorites_button_title);
                        isFav = false;
                        Toast.makeText(MovieDetailsActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void addFav() {

        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, Constant.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Movie movie = new Movie();
                movie.setId(mId);
                movie.setOriginalTitle(mMovieTitle);
                movie.setPosterPath(mMoviePosterPath);
                movie.setOverview(mMovieOverview);
                movie.setReleaseDate(mReleaseDate);
                movie.setVoteAverage(mMovieRating);

                mainViewModel.insert(movie);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnAddtoFav.setText(R.string.remove_from_favorites_button_title);
                        isFav = true;
                        Toast.makeText(MovieDetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void setData() {
        mTxtMovieTitle.setText(mMovieTitle);
        if (!TextUtils.isEmpty(mReleaseDate))
            mTxtReleaseDate.setText(mReleaseDate);
        else {
            mTxtReleaseDate.setVisibility(View.GONE);
            mTxtDateHeading.setVisibility(View.GONE);
        }
        mTxtOverview.setText(mMovieOverview);


        mTxtMovieRating.setText(String.format(Locale.getDefault(), "%.1f", mMovieRating));
        Picasso.with(this)
                .load(Constant.POSTER_IMAGE_PATH + mMoviePosterPath)
                .resize(185, 278)
                .placeholder(R.drawable.movie_poster)
                .into(mImgPoster);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        getSupportActionBar().setTitle(mMovieTitle);

        getIsFav();


    }

    private void getData() {
        Intent intent = getIntent();
        mMovieTitle = intent.getStringExtra("title");
        mReleaseDate = intent.getStringExtra("date");
        mMovieRating = intent.getDoubleExtra("review", 1.1);
        mMovieOverview = intent.getStringExtra("overview");
        mMoviePosterPath = intent.getStringExtra("posterpath");
        mId = intent.getIntExtra("id", 0);
    }


}
