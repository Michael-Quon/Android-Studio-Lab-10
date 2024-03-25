// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class Mi1chaelFragment extends Fragment {

    private ImageView imageView;
    private ProgressBar progressBar;

    private final String[] imageURLs = {
            "", // Placeholder
            "https://d.newsweek.com/en/full/1643309/pokemon-sword-shield-ash-pikachu-passwords.jpg?w=1600&h=1600&l=73&t=41&q=88&f=8771dba0ca0c7147d64d34d537123312",
            "https://assets.pokemon.com/assets/cms2/img/pokedex/full/001.png",
            "https://assets.pokemon.com/assets/cms2/img/pokedex/full/004.png",
            "https://assets.pokemon.com/assets/cms2/img/pokedex/full/007.png"
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

            /// Load image using Glide
            Glide.with(this)
                    .load(imageURL)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.wallpaper_anime_574x1024)) // Placeholder while loading
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Hide progress bar on error
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            // Display error message in a toast
                            Toast.makeText(requireContext(), getString(R.string.failed_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            // Show the error image
                            imageView.setImageResource(R.drawable.error);
                            // Return true to indicate that the failure has been handled
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Simulate the progress bar for 5 seconds
                            new Handler().postDelayed(() -> {
                                progressBar.setVisibility(View.GONE); // Hide the progress bar
                                imageView.setVisibility(View.VISIBLE); // Show the downloaded image
                            }, 5000); // 5000 milliseconds = 5 seconds
                            return false; // Return false to allow Glide to handle displaying the image
                        }
                    })
                    .into(imageView); // ImageView to load the image into


        });

        return view;
    }
}
