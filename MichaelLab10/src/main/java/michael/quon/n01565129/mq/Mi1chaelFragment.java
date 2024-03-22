package michael.quon.n01565129.mq;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Mi1chaelFragment extends Fragment {

    private ImageView imageView;
    private ProgressBar progressBar;

    private final String[] imageURLs = {
            getString(R.string.blank), // Placeholder
            getString(R.string.https_d_newsweek_com_en_full_1643309_pokemon_sword_shield_ash_pikachu_passwords_jpg_w_1600_h_1600_l_73_t_41_q_88_f_8771dba0ca0c7147d64d34d537123312),
            getString(R.string.https_assets_pokemon_com_assets_cms2_img_pokedex_full_001_png),
            getString(R.string.https_assets_pokemon_com_assets_cms2_img_pokedex_full_004_png),
            getString(R.string.https_assets_pokemon_com_assets_cms2_img_pokedex_full_007_png)
    };

    public Mi1chaelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mi1chael, container, false);

        Spinner spinner = view.findViewById(R.id.Mic_spinner);
        Button button = view.findViewById(R.id.Mic_button);
        imageView = view.findViewById(R.id.Mic_imageView);
        progressBar = view.findViewById(R.id.Mic_progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(v -> {
            int selectedPosition = spinner.getSelectedItemPosition();
            String imageURL = imageURLs[selectedPosition];

            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE); // Hide the default image

            // Load image using Glide
            Glide.with(this)
                    .load(imageURL)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.wallpaper_anime_574x1024) // Placeholder while loading
                            .error(R.drawable.error)) // Error image if loading fails
                    .into(imageView); // ImageView to load the image into

            // Simulate the progress bar for 5 seconds
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE); // Hide the progress bar
                imageView.setVisibility(View.VISIBLE); // Show the downloaded image
            }, 5000); // 5000 milliseconds = 5 seconds
        });

        return view;
    }
}
