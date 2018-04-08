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

import java.util.List;

import transportapisdk.JourneyBodyOptions;
import transportapisdk.TransportApiClient;
import transportapisdk.TransportApiClientSettings;
import transportapisdk.TransportApiResult;
import transportapisdk.models.Journey;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment  implements PlaceSelectionListener {

    private static final String KEY ="taecP4HPL6NMiUdoXFQyVQc8gLp2" ;
    PlaceAutocompleteFragment autocompleteFragment1 , autocompleteFragment2 ;
    String clientId = "c7b0138f-c9b2-4998-9e71-b053f2601684";
    String clientSecret = "wI81vUJNPoQMzz00kO9vt3t2lN+T05QRnxZsvu50+/w=";

    private DatabaseReference mDatabase;

    private TextView mPlaceDetailsText;

    TransportApiClient defaultClient;

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

        defaultClient = new TransportApiClient(new TransportApiClientSettings(clientId, clientSecret));

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


                    TransportApiResult<Journey> journey = defaultClient.postJourney(JourneyBodyOptions.defaultQueryOptions(), pick_co.latitude, pick_co.longitude, drop_co.latitude, drop_co.longitude, "");

                    List<List<Double>> response = journey.data.getItineraries().get(0).getLegs().get(0).getGeometry().getCoordinates();

                    mDatabase.push().setValue(
                            new Deliveries(pick_co ,drop_co , pickup ,dropoff));
                  //  mDatabase.child(KEY).setValue( new Deliveries(pick_co , drop_co) );
                    Log.d("Key " , mDatabase.getKey());
                    Log.d("Coord: ",String.valueOf(response));

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
