package com.example.user.async_task_jsonreader_test02;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/*

Klasse mit verschiedenen GET-Methoden welche den durch InternetTask übergebenen String, JSON-Objekten übgergeben
und anschließend relevante Suchergebnisse ausfiltern und zurückgeben.

 */

public class JsonHelper {

    public  ArrayList<String> getJsonArtists (String s)throws JSONException {

        //Diese Methode gibt alle relevanten Interpreten der Suchanfrage zurück

        JSONObject jsonObj = new JSONObject(s);
        ArrayList<String> result = new ArrayList<String>();

        if (jsonObj.has("results")) {

            String[] artistsArray = new String[jsonObj
                    .getJSONObject("results").getJSONObject("artistmatches")
                    .getJSONArray("artist")
                    .length()];

            for (int i = 0; i < jsonObj.getJSONObject("results")
                    .getJSONObject("artistmatches")
                    .getJSONArray("artist")
                    .length();
                 i++) {
                artistsArray[i] = jsonObj
                        .getJSONObject("results")
                        .getJSONObject("artistmatches").getJSONArray("artist")
                        .getJSONObject(i)
                        .getString("name");
            }

            result.addAll(Arrays.asList(artistsArray));

//            ArrayList<String> artistList = new ArrayList<>();
//            artistList.addAll(Arrays.asList(artistsArray));
//            ListAdapter artistListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,artistList);;
//            artRslt.artistsListView.setAdapter(artistListAdapter);
//            setArtistViewListener();
        }
        return result;
    }

    public  ArrayList<String> getJsonAlbums (String s)throws JSONException {

        //Diese Methode gibt alle relevanten Topalben eines gesuchten Interpreten zurück

        JSONObject jsonObj = new JSONObject(s);
        ArrayList<String> result = new ArrayList<String>();


        if (jsonObj.has("topalbums")) {

            String[] albumArray = new String[jsonObj
                    .getJSONObject("topalbums")
                    .getJSONArray("album")
                    .length()];

            for (int i = 0; i < jsonObj
                    .getJSONObject("topalbums")
                    .getJSONArray("album")
                    .length();
                 i++) {

                albumArray[i] = jsonObj
                        .getJSONObject("topalbums")
                        .getJSONArray("album")
                        .getJSONObject(i)
                        .getString("name");
            }

            result.addAll(Arrays.asList(albumArray));
//            ArrayList<String> albumsList = new ArrayList<>();
//            albumsList.addAll(Arrays.asList(albumArray));
//            ArrayAdapter<String> albumsListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,albumsList);
//            albmRslt.albumsListView.setAdapter(albumsListAdapter);
//            setAblumsViewListener();
        }
        return result;
    }

        public  String[] getJsonAlbumsTracks (String s)throws JSONException {

        //Diese Methode gibt die Namen aller Tracks eines gesuchten Albums zurück

        JSONObject jsonObj = new JSONObject(s);
        String[] albumTracksArray = null;


        if(jsonObj.has("album")){

            albumTracksArray = new String[jsonObj
                    .getJSONObject("album")
                    .getJSONObject("tracks")
                    .getJSONArray("track")
                    .length()];

            for(int i = 0; i < jsonObj
                    .getJSONObject("album")
                    .getJSONObject("tracks")
                    .getJSONArray("track")
                    .length();
                i++) {

                albumTracksArray[i] = jsonObj
                        .getJSONObject("album")
                        .getJSONObject("tracks")
                        .getJSONArray("track")
                        .getJSONObject(i)
                        .getString("name");
            }


//            result.addAll(Arrays.asList(albumTracksArray));

//            AlbumDataOpenHandler dbHelper = new AlbumDataOpenHandler(this);
//
//            dbHelper.insertData(albumName,artist,albumTracksArray);
//
//            MainActivity.getIconTask iconTask = new MainActivity.getIconTask();
//            iconTask.execute(albumImageUrlString);

        }

        return albumTracksArray ;
    }

    public String getArtistName (String s) throws  JSONException{

        //Gibt den Namen eines gesuchten Interpreten zurück

        JSONObject jsonObj = new JSONObject(s);
        String artist = jsonObj.getJSONObject("album").getString("artist");

        return artist;
    }

    public String getAlbumName (String s) throws  JSONException{

        //Gibt den Namen eines gesuchten Albums zurück

        JSONObject jsonObj = new JSONObject(s);
        String albumName = jsonObj.getJSONObject("album").getString("name");

        return albumName;
    }

    public String getAlbumCoverImageURL (String s) throws  JSONException{

        //Gibt die URL eines gesuchten Albumcovers zurück

        JSONObject jsonObj = new JSONObject(s);
        String albumImageUrlString = jsonObj.getJSONObject("album").getJSONArray("image")
                .getJSONObject(1).getString("#text");

        return albumImageUrlString;
    }
}
