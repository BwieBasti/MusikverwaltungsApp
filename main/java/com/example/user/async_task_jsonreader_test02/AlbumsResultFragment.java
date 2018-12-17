package com.example.user.async_task_jsonreader_test02;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsResultFragment extends Fragment {


    ListView albumsListView;
    View albumsFragmentView;

    public AlbumsResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        albumsFragmentView = inflater.inflate(R.layout.fragment_albums_result, container, false);

        albumsListView = (ListView)albumsFragmentView.findViewById(R.id.my_list_view_albums);

        return albumsFragmentView;





    }

}
