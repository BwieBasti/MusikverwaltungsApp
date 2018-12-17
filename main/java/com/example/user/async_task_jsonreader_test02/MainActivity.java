package com.example.user.async_task_jsonreader_test02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.security.NetworkSecurityPolicy;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import static java.lang.System.out;


/*
    Funktionalität:

    Mit dieser App kann der Nutzer nach Musikinterpreten innerhalb der LastFM API suchen.
    Nach der Suchanfrage wird eine Liste mit relevanten Künstlern angezeigt. Durch Klick auf ein Listenelement(Künstler)
    kann der Nutzer zu einer weiteren Liste navigieren welche dessen Topalben anzeigt. Durch Klick auf ein Listenelement aus den Topalben
    werden entsprechende Albeninformationen(Artist,Album,Tracks) in einer SQLite Datenbank gespeichert sowie das Albencover heruntergeladen.

    Noch zu erledigen:

    Speicherung des Albencovers mit Verweis in der Datenbank

    Weiterhin steht noch die Entwicklung einer Activity an über die der Nutzer die Lokal gespeicherten Informationen
    auch wieder abrufen kann.
 */


/*

    Die Klasse MainActivity zur Darstellung der Benutzeroberfläche

    Implementiert Nutzerfunktionen wie der Künstlersuche, Albensuche oder der Speicherung in einer SQLite Datenbank

    Anzeige der Suchergebnisse über Listenfragmente

    Über die jeweiligen ClickListener des Suchbuttons oder der Listen werden entsprechende Such-URL's über AsyncTasks
    an die LastFM API gesendet und bei Rückmeldung durch die Methoden der JsonHelper Klasse ausgewertet

 */


public class MainActivity extends AppCompatActivity {


    private TextView myTextView;
    private Button myButton;
    private String lastClickedArtist;

    private static final String URL = "http://ws.audioscrobbler.com/2.0/";
    private static final String ARTIST_SEARCH = "?method=artist.search&artist=";
    private static final String TOPALBUM_SEARCH ="?method=artist.gettopalbums&artist=";
    private static final String API_KEY = "&api_key=8171da4ea57eac6063995466238ddb8c";
    private static final String FORMAT = "&format=json";

    //?method=album.getinfo&api_key=YOUR_API_KEY&artist=Cher&album=Believe&format=json

    private static final String ALBUM_INFO = "?method=album.getinfo&";
    private static final String ALBUM_INFO_ARTIST_SEARCH = "&artist=";
    private static final String ALBUM_INFO_ALBUM_SEARCH = "&album=";

    private FragmentManager fragmentManager;

    AlbumsResultFragment albmRslt = new AlbumsResultFragment();
    ArtistsResultFragment artRslt = new ArtistsResultFragment();

    private DataHelper dataHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myTextView = (TextView)findViewById(R.id.editText);
        myButton = (Button)findViewById(R.id.button);
        fragmentManager = getSupportFragmentManager();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchArtistString = URL + ARTIST_SEARCH + myTextView.getText() + API_KEY + FORMAT;

                InternetTask myTask = new InternetTask(getApplicationContext(),new OnEventListener<String>(){

                    @Override
                    public void OnSuccess(String result) {
                        JsonHelper jsonHelper = new JsonHelper();
                        try {
                            setArtistViewListener(jsonHelper.getJsonArtists(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFailure(Exception e) {

                    }
                }
                );

                myTask.execute(searchArtistString);

                FragmentTransaction fT;

                fT = fragmentManager.beginTransaction();
                fT.replace(R.id.ll,artRslt);
                fT.addToBackStack(null);
                fT.commit();

            }
        });

    }

    private void setArtistViewListener(ArrayList<String> list){

        ListAdapter artistListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,list);;
        artRslt.artistsListView.setAdapter(artistListAdapter);

        artRslt.artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedArtist = artRslt.artistsListView.getAdapter().getItem(position).toString();
                String albumsOfArtistsString = URL + TOPALBUM_SEARCH + clickedArtist + API_KEY + FORMAT;

                lastClickedArtist = clickedArtist;

                InternetTask myTask = new InternetTask(getApplicationContext(),new OnEventListener<String>(){

                    @Override
                    public void OnSuccess(String result) {
                        JsonHelper jsonHelper = new JsonHelper();
                        try {
                            setAblumsViewListener(jsonHelper.getJsonAlbums(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFailure(Exception e) {



                    }
                }
                );

                myTask.execute(albumsOfArtistsString);

                FragmentTransaction fT;

                fT = fragmentManager.beginTransaction();
                fT.replace(R.id.ll,albmRslt);
                fT.addToBackStack(null);
                fT.commit();

            }
        });

    }


    private  void setAblumsViewListener(ArrayList<String> list){

        ListAdapter albumsListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,list);
        albmRslt.albumsListView.setAdapter(albumsListAdapter);

        albmRslt.albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedAlbum = albmRslt.albumsListView.getAdapter().getItem(position).toString();
                String albumInfoSearchString = URL
                       + ALBUM_INFO
                        + API_KEY
                        + ALBUM_INFO_ARTIST_SEARCH
                        + lastClickedArtist
                        + ALBUM_INFO_ALBUM_SEARCH
                        + clickedAlbum
                        + FORMAT;

                InternetTask internetTask = new InternetTask(getApplicationContext(), new OnEventListener<String>() {
                    @Override
                    public void OnSuccess(String object) throws FileNotFoundException {



                    }

                    @Override
                    public void OnFailure(Exception e) {

                    }
                });
                internetTask.execute(albumInfoSearchString);


            }
        });

    }

//    private void setAblumsViewListener(ArrayList<String> list){
//
//        ListAdapter albumsListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,list);;
//        albmRslt.albumsListView.setAdapter(albumsListAdapter);
//
//        albmRslt.albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //?method=album.getinfo&api_key=YOUR_API_KEY&artist=Cher&album=Believe&format=json
//
//                String clickedAlbum = albmRslt.albumsListView.getAdapter().getItem(position).toString();
//                String albumInfoSearchString = URL
//                        + ALBUM_INFO
//                        + API_KEY
//                        + ALBUM_INFO_ARTIST_SEARCH
//                        + lastClickedArtist
//                        + ALBUM_INFO_ALBUM_SEARCH
//                        + clickedAlbum
//                        + FORMAT;
//
//                InternetTask myTask = new InternetTask(getApplicationContext(),new OnEventListener<String>(){
//
//                    @Override
//                    public void OnSuccess(final String result) {
//                        final JsonHelper jsonHelper = new JsonHelper();
//
//                        getIconTask iconTask = new getIconTask(getApplicationContext(), new OnEventListener<Bitmap>() {
//                            @Override
//                            public void OnSuccess(Bitmap bitmap) throws FileNotFoundException {
//
//
//                                String filePath = Environment.getExternalStorageDirectory().toString();
//                                File myDir = new File(filePath + "/Album_Covers/");
//                                myDir.mkdir();
//
//
//                                FileOutputStream out = null;
//                                try {
//                                    out = new FileOutputStream(jsonHelper.getAlbumName(result));
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                                try {
//                                    out.flush();
//                                    out.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//
//
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//
//                                try {
//                                    dBInsert(jsonHelper
//                                            .getAlbumName(result),jsonHelper
//                                            .getArtistName(result),jsonHelper
//                                            .getJsonAlbumsTracks(result));
//                                } catch (JSONException e) {
//
//
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void OnFailure(Exception e) {
//                                try {
//                                    dBInsert(jsonHelper
//                                            .getAlbumName(result),jsonHelper
//                                            .getArtistName(result),jsonHelper
//                                            .getJsonAlbumsTracks(result));
//                                } catch (JSONException e1) {
//                                    e1.printStackTrace();
//                                }
//                            }
//                        });
//                        try {
//                            iconTask.execute(jsonHelper.getAlbumCoverImageURL(result));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void OnFailure(Exception e) {
//
//                    }
//                }
//                );
//
//                myTask.execute(albumInfoSearchString);
//
//            }
//        });
//    }


    private String getCoverImageFileAndLocation(String url, final String albumName){

       final String imageFileLocation = null;
        getIconTask iconTask = new getIconTask(getApplicationContext(), new OnEventListener<Bitmap>() {


            @Override
            public void OnSuccess(Bitmap bitmap) throws FileNotFoundException {

                FileOutputStream out = new FileOutputStream(albumName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            }

            @Override
            public void OnFailure(Exception e) {

            }
        });
        iconTask.execute(url);



        return imageFileLocation;
    }

    void dBInsert( String album, String artist, String[] tracks){

        AlbumDataOpenHandler dbHelper = new AlbumDataOpenHandler(this);
        dbHelper.insertData(album, artist, tracks);
        Toast.makeText(this, "Album Info heruntergeladen", 2).show();

    }



}






