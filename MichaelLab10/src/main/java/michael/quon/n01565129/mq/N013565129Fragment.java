// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class N013565129Fragment extends Fragment {

    private WebView webView;
    private RecyclerView recyclerView;
    private TextView textView;

    // Array of video descriptions and URLs
    private String[] videoDescriptions;
    private String[] videoUrls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_n013565129, container, false);

        // Initialize views
        webView = rootView.findViewById(R.id.Mic_webView);
        recyclerView = rootView.findViewById(R.id.Mic_recyclerView);
        textView = rootView.findViewById(R.id.Mic_Frag3);

        // Display your name and student ID
        textView.setText(getString(R.string.name_id));

        // Initialize array of video descriptions and URLs
        videoDescriptions = getResources().getStringArray(R.array.video_titles);
        videoUrls = getResources().getStringArray(R.array.video_urls);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                holder.textView.setText(videoDescriptions[position]);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getBindingAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            // Load corresponding video URL into WebView
                            loadVideoUrl(videoUrls[adapterPosition]);
                            // Do NOT update the textView here
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return videoDescriptions.length;
            }
        });

        return rootView;
    }

    // Method to load video URL into WebView
    private void loadVideoUrl(String videoUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videoUrl);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
