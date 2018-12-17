package com.example.user.async_task_jsonreader_test02;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsResultFragment extends Fragment {


    ListView artistsListView;
    View artistsFragmentView;
    public ArtistsResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        artistsFragmentView = inflater.inflate(R.layout.fragment_artists_result, container, false);

        artistsListView = (ListView)artistsFragmentView.findViewById(R.id.my_list_view);



        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return artistsFragmentView;
    }

}
