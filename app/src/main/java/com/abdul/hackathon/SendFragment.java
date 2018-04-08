package com.abdul.hackathon;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment  implements PlaceSelectionListener {

    private static final String KEY ="taecP4HPL6NMiUdoXFQyVQc8gLp2" ;
    PlaceAutocompleteFragment autocompleteFragment1 , autocompleteFragment2 ;


    private DatabaseReference mDatabase;

    private TextView mPlaceDetailsText;

    private TextView mPlaceAttribution;

    Button btn_esitmate;

    String pickup , dropoff;

    LatLng  pick_co , drop_co;

    public SendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   View x = inflater.inflate(R.layout.fragment_send, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("deliveries");
   btn_esitmate = (Button)x.findViewById(R.id.btn_pricing);
   autocompleteFragment1 = (PlaceAutocompleteFragment) getActivity().
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment1);


        autocompleteFragment2 = (PlaceAutocompleteFragment) getActivity().
               getFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

//        mPlaceDetailsText = x.findViewById(R.id.place_details);
//        mPlaceAttribution = x.findViewById(R.id.place_attribution);

        autocompleteFragment1.setHint("Pickup Point");
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                autocompleteFragment1.setText(place.getName());
                pickup =String.valueOf( place.getName());
                pick_co = place.getLatLng();
                pickup = String.valueOf(place.getName());
            }

            @Override
            public void onError(Status status) {

            }
        });

        autocompleteFragment2.setHint( "Drop off Point");
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                autocompleteFragment2.setText(place.getName());
                dropoff = String.valueOf(place.getName());
                drop_co = place.getLatLng();
                dropoff = String.valueOf(place.getName());


            }

            @Override
            public void onError(Status status) {

            }
        });
        btn_esitmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ! pick_co.equals(null)  &&  !drop_co.equals(null)){

                    mDatabase.push().setValue(
                            new Deliveries(pick_co ,drop_co , pickup ,dropoff));
                  //  mDatabase.child(KEY).setValue( new Deliveries(pick_co , drop_co) );
                    Log.d("Key " , mDatabase.getKey());
                    Intent in = new Intent( getActivity() , Main2Activity.class);
                    in.putExtra("key" , mDatabase.getKey());
                    startActivity(in);

                }else {
                    Toast.makeText(getActivity() , " Choose Pickup and Drop off point "
                            , Toast.LENGTH_LONG).show();

                }
            }
        });




   return x ;

    }


    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
      //  Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
        //        websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }
}
