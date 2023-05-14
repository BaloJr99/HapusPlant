package com.example.hapusplant.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.download.picasso.PicassoDownloadRequestBuilderFactory;
import com.example.hapusplant.LoadingDialog;
import com.example.hapusplant.R;
import com.example.hapusplant.SucculentKindForm;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;
import com.example.hapusplant.network.RetrofitInstance;
import com.example.hapusplant.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SucculentAdapter  extends RecyclerView.Adapter<SucculentViewHolder> {

    private List<SearchSucculentType> dataList;
    Context c;
    HomeFragment home;
    ViewGroup parent;

    public SucculentAdapter(List<SearchSucculentType> dataList, Context c, HomeFragment home){
        this.dataList = dataList;
        this.c = c;
        this.home = home;
    }

    @NonNull
    @Override
    public SucculentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card, parent, false);
        this.parent = parent;
        return new SucculentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SucculentViewHolder holder, int position) {
        holder.swSucculentEndemic.setChecked(dataList.get(position).getEndemic());
        holder.swSucculentPappers.setChecked(dataList.get(position).getHasDocuments());
        holder.tvSucculentName.setText(dataList.get(position).getSucculentName());
        MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
        MediaManager.get().download(c).load(dataList.get(position).getPhotoLink()).into(holder.ivSucculentPhoto);

        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(c, SucculentKindForm.class);
            intent.putExtra("idSucculent", dataList.get(position).getIdSucculent());
            c.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.AlertDialogTheme);
            View viewDialog = LayoutInflater.from(c).inflate(
                    R.layout.delete_dialog, parent.findViewById(R.id.layoutDialogContainer));
            builder.setView(viewDialog);
            ((TextView) viewDialog.findViewById(R.id.textTitle)).setText("Delete Succulent");
            ((TextView) viewDialog.findViewById(R.id.textMessage)).setText("Are you sure to delete this succulent?");
            ((Button) viewDialog.findViewById(R.id.buttonYes)).setText("Yes");
            ((Button) viewDialog.findViewById(R.id.buttonNo)).setText("No");
            ((ImageView) viewDialog.findViewById(R.id.imageIcon)).setImageResource(R.drawable.delete);
            
            final AlertDialog alertDialog = builder.create();
            viewDialog.findViewById(R.id.buttonYes).setOnClickListener(view1 -> {
                SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstance().create(SucculentKindAPI.class);

                /* Get last known Token */
                HapusPlantLiteDb db = new HapusPlantLiteDb(c);
                String token = db.getJwtIfExists();
                Call<Void> call = kindAPI.deleteSucculent(dataList.get(position).getIdSucculent(), token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
                        home.getAllSuculents();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(c, "Error", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            });

            viewDialog.findViewById(R.id.buttonNo).setOnClickListener(view1 -> {
                alertDialog.dismiss();
            });

            if(alertDialog.getWindow() != null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        });

        holder.btnDownload.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
